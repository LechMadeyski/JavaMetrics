package pl.edu.pwr.metrics.qmood;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing MFA metric. MFA (Measure of Functional Abstraction) - ratio of the number of
 * methods inherited by a class to the total number of methods of the class. Constructors are
 * omitted.
 */
public class MfaMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "MFA";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> mfa =
        c -> {
          int allMethodsCount = getAllClassMethodNames(c).size();
          int classMethodsCount = c.getMethods().size();

          return (allMethodsCount - classMethodsCount) / (double) allMethodsCount;
        };
    return getMetricForClass(mfa, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
