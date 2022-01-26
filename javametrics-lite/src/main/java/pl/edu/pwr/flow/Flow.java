package pl.edu.pwr.flow;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.filter.Filter;
import pl.edu.pwr.report.exception.OutputWriteFailedException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Flow {
  private static final Logger LOGGER = Logger.getLogger(Flow.class.getName());

  private final String root;
  private final String output;
  private final Filter filter;

  public Flow(String root, String output, Filter filter) {
    this.root = root;
    this.output = output;
    this.filter = filter;
  }

  protected abstract void postProcess() throws IOException;

  protected abstract void save(List<Metric> metrics) throws IOException;

  protected abstract List<Metric> computeMetrics(CompilationUnit cu);

  protected abstract List<ParseResult<CompilationUnit>> parseFiltered();

  protected abstract List<SourceRoot> gatherSources();

  // TODO: add abstract get filtered sources

  public final void process() throws IOException {
    List<ParseResult<CompilationUnit>> parseResults;
    if (filter == null) {
      List<SourceRoot> sources = gatherSources();
      parseResults = parse(sources);
    } else {
      parseResults = parseFiltered();
    }

    // TODO: benchmark parallelStream
    parseResults.stream()
        .forEach(
            pr -> {
              if (pr.isSuccessful() && pr.getResult().isPresent()) {
                var metrics = computeMetrics(pr.getResult().get());
                if (filter != null) {
                  metrics.removeIf(m -> !this.filter.contains(m));
                }
                concurrentSave(metrics);
              } else {
                LOGGER.warning(pr.getProblems().toString());
              }
            });

    postProcess();
  }

  private List<ParseResult<CompilationUnit>> parse(List<SourceRoot> sources) {
    return sources.stream()
        // .map(SourceRoot::tryToParseParallelized)
        .map(
            sr -> {
              try {
                return sr.tryToParse();
              } catch (IOException e) {
                e.printStackTrace();
                return null;
              }
            })
        .filter(Objects::nonNull)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private synchronized void concurrentSave(List<Metric> metrics) {
    try {
      save(metrics);
    } catch (IOException ex) {
      throw new OutputWriteFailedException(ex.getMessage());
    }
  }

  public String getRoot() {
    return root;
  }

  public String getOutput() {
    return output;
  }

  public Filter getFilter() {
    return filter;
  }
}
