package pl.edu.pwr.master.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import pl.edu.pwr.master.core.model.Metric;
import pl.edu.pwr.master.input.Input;
import pl.edu.pwr.master.report.csv.CsvReporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class MetricParser {

    private static final Logger LOGGER = Logger.getLogger(MetricParser.class.getName());

    private final JavaParser javaParser;
    private final MetricGenerator metricGenerator;
    private final CsvReporter csvReporter;
    private Input input;

    public MetricParser() {
        this.javaParser = prepareParser();
        this.metricGenerator = null;
        this.csvReporter = new CsvReporter();
        this.input = getEmptyInput();
    }

    public MetricParser(MetricGenerator metricGenerator, String outputFilename) {
        this.metricGenerator = metricGenerator;
        this.csvReporter = new CsvReporter(outputFilename);
        this.javaParser = prepareParser();
        this.input = getEmptyInput();
    }

    public MetricParser(MetricGenerator metricGenerator, String outputFilename, Input input) {
        this.metricGenerator = metricGenerator;
        this.csvReporter = new CsvReporter(outputFilename);
        this.javaParser = prepareParser();
        this.input = input;
    }

    private JavaParser prepareParser() {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();

        TypeSolver reflectionTypeSolver = new ReflectionTypeSolver();
        reflectionTypeSolver.setParent(reflectionTypeSolver);
        typeSolver.add(reflectionTypeSolver);

        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        ParserConfiguration parserConfiguration =
                new ParserConfiguration()
                        .setAttributeComments(false)
                        .setSymbolResolver(javaSymbolSolver);

        return new JavaParser(parserConfiguration);
    }

    private Input getEmptyInput() {
        return new Input(new ArrayList<>(), new ArrayList<>());
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
                    .forEach(this::tryToParse);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void tryToParse(File file) {
        try {
            CompilationUnit compilationUnit = parseFile(file);

            List<Metric> metrics = this.metricGenerator.compute(compilationUnit);
            String projectName = Optional.of(file.getParent()).orElse("");
            this.csvReporter.writeMetrics(projectName, metrics, metricGenerator.getMetricStrategyNames(), this.input);

        } catch (FileNotFoundException | ParseException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public CompilationUnit parseFile(File file) throws FileNotFoundException, ParseException {
        ParseResult<CompilationUnit> parseResult = this.javaParser.parse(file);
        if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
            return parseResult.getResult().get();
        } else throw new ParseException(parseResult.getProblems().toString());
    }
}
