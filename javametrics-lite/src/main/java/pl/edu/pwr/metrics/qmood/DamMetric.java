package pl.edu.pwr.metrics.qmood;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing DAM metric. DAM (Data Access Metric) - ratio of the number of private
 * (protected) attributes to the total number of attributes declared in the class
 */
public class DamMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "DAM";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> dam =
        c -> {
          long privateAttributes =
              compilationUnit.findAll(FieldDeclaration.class).stream()
                  .filter(
                      f ->
                          f.getAccessSpecifier().equals(AccessSpecifier.PRIVATE)
                              || f.getAccessSpecifier().equals(AccessSpecifier.PROTECTED))
                  .count();

          long allAttributes = getAllClassFieldNames(c).size();

          return privateAttributes / (double) allAttributes;
        };

    return getMetricForClass(dam, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
