package pl.edu.pwr.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.filter.Filter;
import pl.edu.pwr.report.CsvReporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricParser {

  private static final Logger LOGGER = Logger.getLogger(MetricParser.class.getName());

  private final JavaParser javaParser;
  private final MetricGenerator metricGenerator;
  private final CsvReporter csvReporter;
  private Filter input;

  public MetricParser() {
    this.javaParser = prepareParser();
    this.metricGenerator = null;
    this.csvReporter = new CsvReporter();
    this.input = getEmptyInput();

    MetricParserUtil.setup(javaParser.getParserConfiguration(), "");
  }

  public MetricParser(MetricGenerator metricGenerator, String projectPath, String outputFilename) {
    this.metricGenerator = metricGenerator;
    this.csvReporter = new CsvReporter(outputFilename);
    this.javaParser = prepareParser(projectPath);
    this.input = getEmptyInput();

    MetricParserUtil.setup(javaParser.getParserConfiguration(), projectPath);
  }

  public MetricParser(
      MetricGenerator metricGenerator,
      String projectPath,
      String dependencyPath,
      String outputFilename) {
    this.metricGenerator = metricGenerator;
    this.csvReporter = new CsvReporter(outputFilename);
    this.javaParser = prepareParser(projectPath, dependencyPath);
    this.input = getEmptyInput();

    MetricParserUtil.setup(javaParser.getParserConfiguration(), projectPath);
  }

  public MetricParser(
      MetricGenerator metricGenerator, String projectPath, String outputFilename, Filter input) {
    this.metricGenerator = metricGenerator;
    this.csvReporter = new CsvReporter(outputFilename);
    this.javaParser = prepareParser(projectPath);
    this.input = input;

    MetricParserUtil.setup(javaParser.getParserConfiguration(), projectPath);
  }

  public MetricParser(
      MetricGenerator metricGenerator,
      String projectPath,
      String dependencyPath,
      String outputFilename,
      Filter input) {
    this.metricGenerator = metricGenerator;
    this.csvReporter = new CsvReporter(outputFilename);
    this.javaParser = prepareParser(projectPath, dependencyPath);
    this.input = input;

    MetricParserUtil.setup(javaParser.getParserConfiguration(), projectPath);
  }

  private JavaParser prepareParser() {
    CombinedTypeSolver typeSolver = prepareBaseCombinedTypeSolver();
    JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);

    ParserConfiguration parserConfiguration =
        new ParserConfiguration().setAttributeComments(true).setSymbolResolver(javaSymbolSolver);

    return new JavaParser(parserConfiguration);
  }

  private JavaParser prepareParser(String projectPath) {
    CombinedTypeSolver typeSolver = prepareBaseCombinedTypeSolver(projectPath);
    JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);

    ParserConfiguration parserConfiguration =
        new ParserConfiguration().setAttributeComments(true).setSymbolResolver(javaSymbolSolver);

    return new JavaParser(parserConfiguration);
  }

  private JavaParser prepareParser(String projectPath, String dependencyPath) {
    CombinedTypeSolver typeSolver = prepareBaseCombinedTypeSolver(projectPath);

    registerJarTypeSolvers(dependencyPath, typeSolver);
    JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);

    ParserConfiguration parserConfiguration =
        new ParserConfiguration().setAttributeComments(true).setSymbolResolver(javaSymbolSolver);

    return new JavaParser(parserConfiguration);
  }

  private void registerJavaParserTypeSolvers(String projectPath, CombinedTypeSolver typeSolver) {
    try (Stream<Path> walk = Files.walk(Paths.get(projectPath))) {

      List<String> result =
          walk.filter(Files::isDirectory)
              .map(Path::toString)
              .filter(
                  f ->
                      f.endsWith("src" + File.separator + "main" + File.separator + "java")
                          || f.endsWith("src" + File.separator + "test" + File.separator + "java"))
              .collect(Collectors.toList());

      result.forEach(
          r -> {
            LOGGER.info("Registering project package: " + r);
            typeSolver.add(new JavaParserTypeSolver(r));
          });

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  private void registerJarTypeSolvers(String dependencyPath, CombinedTypeSolver typeSolver) {
    try (Stream<Path> walk = Files.walk(Paths.get(dependencyPath))) {

      List<String> result =
          walk.map(Path::toString).filter(f -> f.endsWith(".jar")).collect(Collectors.toList());

      result.forEach(
          path -> {
            try {
              LOGGER.info("Registering " + path);
              typeSolver.add(new JarTypeSolver(path));
            } catch (IOException e) {
              LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
          });

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  private CombinedTypeSolver prepareBaseCombinedTypeSolver() {
    CombinedTypeSolver typeSolver = new CombinedTypeSolver();

    TypeSolver reflectionTypeSolver = new ReflectionTypeSolver(false);
    typeSolver.add(reflectionTypeSolver);

    return typeSolver;
  }

  private CombinedTypeSolver prepareBaseCombinedTypeSolver(String projectPath) {
    CombinedTypeSolver typeSolver = new CombinedTypeSolver();

    TypeSolver reflectionTypeSolver = new ReflectionTypeSolver(false);
    typeSolver.add(reflectionTypeSolver);

    registerJavaParserTypeSolvers(projectPath, typeSolver);

    return typeSolver;
  }

  private Filter getEmptyInput() {
    return new Filter(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
  }

  public void parse(String path) throws IOException, ParseException {
    File file = new File(path);
    if (!file.exists()) {
      throw new FileNotFoundException("File does not exists!" + file.getAbsolutePath());
    } else if (file.isDirectory()) {
      this.parseDirectory(path);
    } else {
      this.parseFile(file);
    }
  }

  private void parseDirectory(String directory) {
    try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
      walk.map(Path::toFile)
          .filter(f -> f.getName().endsWith(".java"))
          .forEach(
              f -> {
                try {
                  tryToParse(f);
                } catch (Exception e) {
                  LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
              });
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  private void tryToParse(File file) {
    LOGGER.info("Trying to parse " + file.getPath());

    try {
      CompilationUnit compilationUnit = parseFile(file);

      List<Metric> metrics = this.metricGenerator.compute(compilationUnit);
      String projectName = Optional.of(file.getParent()).orElse("");
      this.csvReporter.writeMetrics(
          projectName, metrics, metricGenerator.getMetricStrategyNames(), this.input);
    } catch (FileNotFoundException
        | ParseException e) { // Use catch Exception to filter out all faulty records
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public CompilationUnit parseFile(File file) throws FileNotFoundException, ParseException {
    ParseResult<CompilationUnit> parseResult = this.javaParser.parse(file);
    if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
      return parseResult.getResult().get();
    } else throw new ParseException(parseResult.getProblems().toString());
  }
}
