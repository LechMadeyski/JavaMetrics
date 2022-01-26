package pl.edu.pwr.metrics.qmood;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.exception.JavaParserNotConfiguredException;
import pl.edu.pwr.core.model.Metric;

import java.io.IOException;
import java.util.List;

/**
 * Class for computing MOA metric. MOA (Measure of Aggregation) - metric is a count of the number of
 * class fields whose types are user defined classes
 */
public class MoaMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "MOA";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> moa =
        c -> {
          List<FieldDeclaration> fields = c.getFields();
          return (int)
              fields.stream()
                  .filter(
                      f -> {
                        try {
                          return isUserDefined(
                              f.getElementType().toString(), f.resolve().getType().describe());
                        } catch (ParseException
                            | IOException
                            | JavaParserNotConfiguredException ex) {
                          return false;
                        }
                      })
                  .count();
        };
    return getMetricForClass(moa, compilationUnit);
  }

  private boolean isUserDefined(String fieldTypeName, String fieldDescribe)
      throws IOException, ParseException, JavaParserNotConfiguredException {
    List<String> classFilesWithFieldClass = getClassFilesContaining(fieldTypeName);

    for (String s : classFilesWithFieldClass) {
      CompilationUnit cu = parseFile(s);
      boolean found =
          cu.findAll(ClassOrInterfaceDeclaration.class).stream()
              .anyMatch(c -> fieldDescribe.equals(c.getFullyQualifiedName().orElse(null)));

      if (found) return true;
    }
    return false;
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
