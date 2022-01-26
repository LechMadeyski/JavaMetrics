package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.LambdaExpr;
import pl.edu.pwr.config.Constants;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * LD (Lambda Density) - density of lambda expressions (Java 8 feature) in class in percents LD =
 * NOL * 100 / LOC_C = Number of Lambdas * 100 / Lines of Code in Class
 */
public class LdMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "LD";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> ld =
        c -> {
          double numberOfLambdas = c.findAll(LambdaExpr.class).stream().mapToDouble(md -> 1).sum();
          double loc = c.toString().split(Constants.NEW_LINE_REGEX).length;
          return numberOfLambdas * 100 / loc;
        };

    return getMetricForClass(ld, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
