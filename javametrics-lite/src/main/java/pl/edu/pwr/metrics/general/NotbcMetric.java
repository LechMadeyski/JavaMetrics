package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOTB_C metric. NOTB_C - Number of text block literal expressions in a class.
 */
public class NotbcMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOTB_C";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> notbcConstructor = c -> c.findAll(TextBlockLiteralExpr.class).size();

    return getMetricForClass(notbcConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
