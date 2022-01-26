package pl.edu.pwr.core;

import com.github.javaparser.ParseException;
import pl.edu.pwr.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class MetricsRunner {

  private static final Logger LOGGER = Logger.getLogger(MetricsRunner.class.getName());
  private boolean parseDependencies;

  public MetricsRunner(String input, String output, Mode mode) throws IOException, ParseException {
    this.parseDependencies = mode == Mode.WITH_DEPENDENCIES;
    removeExistingOutputFile(output);
    this.metricSuite(input, output);
  }

  public MetricsRunner(String inputPath, String output, Filter inputCsv, Mode mode)
      throws IOException, ParseException {
    this.parseDependencies = mode == Mode.WITH_DEPENDENCIES;
    removeExistingOutputFile(output);
    this.metricSuite(inputPath, output, inputCsv);
  }

  public void metricSuite(String path, String outputFilename) throws IOException, ParseException {
    List<String> projectPaths = new ArrayList<>();

    LOGGER.info("Creating metric suite for path: " + path + " and output: " + outputFilename);

    if (containsOnlyJavaFiles(path)) {
      projectPaths.add(path);
    } else {
      projectPaths = getProjectPaths(path);
    }

    for (String p : projectPaths) {

      LOGGER.info("Parsing project: " + p);

      // TODO: assert whether dependency path is inside the given root directory
      parseProject(p, outputFilename);
    }
  }

  public void metricSuite(String path, String outputFilename, Filter input)
      throws IOException, ParseException {
    List<String> projectPaths = getProjectPaths(path);

    for (String p : projectPaths) {

      LOGGER.info("Parsing project: " + p);

      // TODO: assert whether dependency path is inside the given root directory

      parseProject(p, outputFilename, input);

      LOGGER.info("Finished parsing project: " + p);
    }
  }

  private void parseProject(String projectPath, String outputFilename)
      throws ParseException, IOException {
    MetricGenerator metricGenerator = prepareMetricGeneratorNoDeps();
    MetricParser metricParser = new MetricParser(metricGenerator, projectPath, outputFilename);
    metricParser.parse(projectPath);
  }

  private void parseProject(String projectPath, String outputFilename, Filter input)
      throws ParseException, IOException {
    MetricGenerator metricGenerator = prepareMetricGeneratorNoDeps();
    MetricParser metricParser =
        new MetricParser(metricGenerator, projectPath, outputFilename, input);
    metricParser.parse(projectPath);
  }

  private void removeExistingOutputFile(String fileName) {
    try {
      Files.delete(Paths.get(fileName));
      LOGGER.info("Removed output file " + fileName);
    } catch (IOException ex) {
      LOGGER.log(Level.WARNING, ex.getMessage(), ex);
    }
  }

  private String basename(String path) {
    if (path.endsWith(File.separator)) {
      path = path.substring(0, path.length() - 1);
    }

    String[] split = path.split("/");
    if (System.getProperty("os.name").startsWith("Windows")) split = path.split("\\\\");
    return split[split.length - 1];
  }

  protected boolean containsOnlyJavaFiles(String path) throws IOException {
    long count = 0;
    try (Stream<Path> files = Files.list(Paths.get(path))) {
      count = files.count();
    }

    long javaFilesCount = 0;
    try (Stream<Path> walk = Files.walk(Paths.get(path))) {
      javaFilesCount = walk.map(Path::toFile).filter(f -> f.getName().endsWith(".java")).count();
    }

    return count == javaFilesCount;
  }

  protected ArrayList<String> getProjectPaths(String path) {
    File f = new File(path);

    if (f.isFile()) {
      LOGGER.warning(path + " is a file, not a directory!");
      return new ArrayList<>();
    }

    ArrayList<String> mvn = new ArrayList<>();
    traverseDirectoryTree(path, "pom.xml", mvn);

    ArrayList<String> gradle = new ArrayList<>();
    traverseDirectoryTree(path, "build.gradle", gradle);

    ArrayList<String> src = new ArrayList<>();
    traverseDirectoryTree(path, "src", src);

    ArrayList<String> dirs = new ArrayList<>();
    dirs.addAll(mvn);
    dirs.addAll(gradle);
    dirs.addAll(src);

    return dedupSubdirectories(dirs);
  }

  private ArrayList<String> dedupSubdirectories(ArrayList<String> directories) {
    ArrayList<String> result = (ArrayList<String>) directories.clone();
    Collections.sort(result);

    if (result.size() > 1) {
      String dir = result.get(0);

      for (int i = 1; i < result.size(); i++) {
        if (result.get(i).startsWith(dir)) {
          result.remove(i);
          i -= 1;
        } else dir = result.get(i);
      }
    }
    return result;
  }

  private void traverseDirectoryTree(String path, String searchToken, ArrayList<String> accum) {
    File f = new File(path);

    if (f.isFile()) {
      return;
    }

    if (new File(path, searchToken).exists()) {
      accum.add(path);
    } else {
      File[] dirs = f.listFiles(File::isDirectory);
      if (dirs != null) {
        ArrayList<File> lFiles = new ArrayList<>(Arrays.asList(dirs));
        for (File file : lFiles) {
          if (new File(file.getPath(), searchToken).exists()) {
            accum.add(file.getPath());
          } else {
            File[] dirsTemp = file.listFiles(File::isDirectory);
            if (dirsTemp != null)
              for (File dir : dirsTemp) traverseDirectoryTree(dir.getPath(), searchToken, accum);
          }
        }
      }
    }
  }

  private MetricGenerator prepareMetricGeneratorNoDeps() {
    return new MetricGeneratorBuilder()
        .addMetricsWithoutDependencyResolution()
        // .addMetric(new AtfdMetric())
        .build();
  }

  private MetricGenerator prepareMetricGenerator() {
    return new MetricGeneratorBuilder()
        .addMetricsWithoutDependencyResolution()
        .addMetricsWithDependencyResolution()
        .build();
  }
}
