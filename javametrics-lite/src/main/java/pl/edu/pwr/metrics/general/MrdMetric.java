package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import pl.edu.pwr.config.Constants;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * MRD (Method Reference Density) - density of method references (Java 8 feature) in class in
 * percents MRD = NOMR_C * 100 / LOC_C = Number Of Method Reference * 100 / Lines of Code in Class
 */
public class MrdMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "MRD";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> mrd =
        c -> {
          double methodReferenceCount =
              c.findAll(MethodReferenceExpr.class).stream().mapToDouble(md -> 1).sum();
          double loc = c.toString().split(Constants.NEW_LINE_REGEX).length;
          return methodReferenceCount * 100 / loc;
        };

    return getMetricForClass(mrd, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
