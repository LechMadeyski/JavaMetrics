package pl.edu.pwr.core;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.utils.Utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

public class FilteredSourceRoot extends SourceRoot {

  private static final Pattern JAVA_IDENTIFIER =
      Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

  private Set<String> filePaths;

  public FilteredSourceRoot(Path root, Set<String> filePaths) {
    super(root);
    this.filePaths = filePaths;
  }

  public FilteredSourceRoot(
      Path root, ParserConfiguration parserConfiguration, Set<String> filePaths) {
    super(root, parserConfiguration);
    this.filePaths = filePaths;
  }

  @Override
  public List<ParseResult<CompilationUnit>> tryToParse(String startPackage) throws IOException {
    Utils.assertNotNull(startPackage);
    Path path = CodeGenerationUtils.packageAbsolutePath(this.getRoot(), startPackage);
    Files.walkFileTree(
        path,
        new SimpleFileVisitor<Path>() {
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Path relative = FilteredSourceRoot.this.getRoot().relativize(file.getParent());
            if (!attrs.isDirectory()
                && file.toString().endsWith(".java")
                && isFilepathMatching(relative.toString())) {

              FilteredSourceRoot.this.tryToParse(
                  relative.toString(), file.getFileName().toString());
            }

            return FileVisitResult.CONTINUE;
          }

          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
              throws IOException {
            return FilteredSourceRoot.isSensibleDirectoryToEnter(dir)
                ? FileVisitResult.CONTINUE
                : FileVisitResult.SKIP_SUBTREE;
          }
        });
    return this.getCache();
  }

  private boolean isFilepathMatching(String path) {
    return this.filePaths.stream().anyMatch(fp -> path.endsWith(fp) || fp.endsWith(path));
  }

  private static boolean isSensibleDirectoryToEnter(Path dir) throws IOException {
    String dirToEnter = dir.getFileName().toString();
    boolean directoryIsAValidJavaIdentifier = JAVA_IDENTIFIER.matcher(dirToEnter).matches();
    if (!Files.isHidden(dir) && directoryIsAValidJavaIdentifier) {
      return true;
    } else {
      Log.trace("Not processing directory \"%s\"", () -> dirToEnter);
      return false;
    }
  }

  @Override
  public List<ParseResult<CompilationUnit>> tryToParseParallelized(String startPackage) {
    Utils.assertNotNull(startPackage);
    Path path = CodeGenerationUtils.packageAbsolutePath(this.getRoot(), startPackage);
    FilteredSourceRoot.ParallelParse parse =
        new FilteredSourceRoot.ParallelParse(
            path,
            (file, attrs) -> {
              Path relative = this.getRoot().relativize(file.getParent());
              if (!attrs.isDirectory()
                  && file.toString().endsWith(".java")
                  && isFilepathMatching(relative.toString())) {

                try {
                  this.tryToParse(
                      relative.toString(),
                      file.getFileName().toString(),
                      this.getParserConfiguration());
                } catch (IOException var5) {
                  Log.error(var5);
                }
              }

              return FileVisitResult.CONTINUE;
            });
    ForkJoinPool pool = new ForkJoinPool();
    pool.invoke(parse);
    return this.getCache();
  }

  private static class ParallelParse extends RecursiveAction {
    private static final long serialVersionUID = 1L;
    private final Path path;
    private final FilteredSourceRoot.ParallelParse.VisitFileCallback callback;

    ParallelParse(Path path, FilteredSourceRoot.ParallelParse.VisitFileCallback callback) {
      this.path = path;
      this.callback = callback;
    }

    protected void compute() {
      final ArrayList walks = new ArrayList();

      try {
        Files.walkFileTree(
            this.path,
            new SimpleFileVisitor<Path>() {
              public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                  throws IOException {
                if (!FilteredSourceRoot.isSensibleDirectoryToEnter(dir)) {
                  return FileVisitResult.SKIP_SUBTREE;
                } else if (!dir.equals(FilteredSourceRoot.ParallelParse.this.path)) {
                  FilteredSourceRoot.ParallelParse w =
                      new FilteredSourceRoot.ParallelParse(
                          dir, FilteredSourceRoot.ParallelParse.this.callback);
                  w.fork();
                  walks.add(w);
                  return FileVisitResult.SKIP_SUBTREE;
                } else {
                  return FileVisitResult.CONTINUE;
                }
              }

              public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                return FilteredSourceRoot.ParallelParse.this.callback.process(file, attrs);
              }
            });
      } catch (IOException var4) {
        Log.error(var4);
      }

      Iterator var2 = walks.iterator();

      while (var2.hasNext()) {
        FilteredSourceRoot.ParallelParse w = (FilteredSourceRoot.ParallelParse) var2.next();
        w.join();
      }
    }

    interface VisitFileCallback {
      FileVisitResult process(Path file, BasicFileAttributes attrs);
    }
  }

  @FunctionalInterface
  public interface Callback {
    SourceRoot.Callback.Result process(
        Path localPath, Path absolutePath, ParseResult<CompilationUnit> result);

    public static enum Result {
      SAVE,
      DONT_SAVE,
      TERMINATE;

      private Result() {}
    }
  }
}
