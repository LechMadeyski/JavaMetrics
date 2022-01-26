package pl.edu.pwr.flow;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import pl.edu.pwr.config.DefaultParserConfiguration;
import pl.edu.pwr.core.MetricGenerator;
import pl.edu.pwr.core.MetricGeneratorBuilder;
import pl.edu.pwr.core.MetricParserUtil;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.filter.Filter;
import pl.edu.pwr.report.CsvHeaders;
import pl.edu.pwr.report.Writer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class PlainFlow extends Flow {

  private static final Logger LOGGER = Logger.getLogger(PlainFlow.class.getName());

  private final ParserCollectionStrategy parserCollectionStrategy;
  private final MetricGenerator generator;
  private final Writer writer;

  public PlainFlow(String root, String output, Filter filter) throws IOException {
    super(root, output, filter);
    this.parserCollectionStrategy =
        new ParserCollectionStrategy(DefaultParserConfiguration.getInstance());
    this.generator = new MetricGeneratorBuilder().addMetricsWithoutDependencyResolution().build();
    MetricParserUtil.setup(this.parserCollectionStrategy.getParserConfiguration(), root);
    this.writer = Writer.getInstance(this.getOutput(), CsvHeaders.getNoDependencyHeaders());
  }

  @Override
  protected void postProcess() throws IOException {
    this.writer.close();
  }

  @Override
  protected void save(List<Metric> metrics) throws IOException {
    this.writer.write(metrics);
  }

  @Override
  protected List<Metric> computeMetrics(CompilationUnit cu) {
    return this.generator.compute(cu);
  }

  @Override
  protected List<ParseResult<CompilationUnit>> parseFiltered() {
    List<ParseResult<CompilationUnit>> out = new ArrayList<>();
    var jp = new JavaParser(this.parserCollectionStrategy.getParserConfiguration());
    getFilter().getFilePaths().stream()
        .filter(
            fp -> {
              var p = Path.of(fp).isAbsolute();
              if (!p) {
                LOGGER.warning("path " + fp + " is not absolute");
              }
              return p;
            })
        .map(Path::of)
        .map(
            p -> {
              try {
                return jp.parse(p);
              } catch (IOException e) {
                e.printStackTrace();
                return null;
              }
            })
        .filter(Objects::nonNull)
        .forEach(out::add);
    return out;
  }

  @Override
  protected List<SourceRoot> gatherSources() {
    final ProjectRoot projectRoot =
        this.parserCollectionStrategy.collect(Paths.get(this.getRoot()));
    return projectRoot.getSourceRoots();
  }
}
