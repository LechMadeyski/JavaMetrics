package pl.edu.pwr.master.metrics.ck;

import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import pl.edu.pwr.master.core.ClassMetric;
import pl.edu.pwr.master.core.ClassMetricStrategy;
import pl.edu.pwr.master.core.MetricParserUtil;
import pl.edu.pwr.master.core.exception.JavaParserNotConfiguredException;
import pl.edu.pwr.master.core.model.Metric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class for computing NOC metric.
 * NOC - number of children (number of immediate subclasses of a class)
 */
public class NocMetric extends ClassMetricStrategy<Integer> {

    private static final Logger LOGGER = Logger.getLogger(NocMetric.class.getName());
    private static final String METRIC_NAME = "NOC";

    @Override
    public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
        // TODO: Add pre-parsing for all classes to count child classes. Keep the list in universally accessible map.
        ClassMetric<Integer> noc = c -> {
            Optional<String> baseClassQualifiedNameOptional = c.getFullyQualifiedName();
            if (baseClassQualifiedNameOptional.isPresent()) {
                List<String> possibleSubclassesFiles = getImmediateSubclassFiles(c.getNameAsString());
                return possibleSubclassesFiles.stream()
                        .mapToInt(f -> {
                            try {
                                return getNumberOfAncestors(f, baseClassQualifiedNameOptional.get());
                            } catch (Exception e) {
                                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                            }
                            return 0;
                        })
                        .sum();
            } else
                return 0;
        };

        return getMetricForClass(noc, compilationUnit);
    }

    private Integer getNumberOfAncestors(String fileName, String baseClassQualifiedName) throws IOException, ParseException, JavaParserNotConfiguredException {
        CompilationUnit cu = parseFile(fileName);
        return cu.findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .flatMap(c -> c.resolve().getAncestors().stream())
                .filter(resolvedReferenceType -> resolvedReferenceType.getQualifiedName().equals(baseClassQualifiedName))
                .mapToInt(resolvedReferenceType -> 1)
                .sum();
    }

    private List<String> getImmediateSubclassFiles(String baseClassName) {
        return getClassFilesContaining(".*extends\\s+" + baseClassName + ".*");
    }

    @Override
    public String getName() {
        return METRIC_NAME;
    }
}
