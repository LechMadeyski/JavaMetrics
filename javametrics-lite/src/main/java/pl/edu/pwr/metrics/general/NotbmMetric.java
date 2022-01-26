package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOTB_M metric. NOTB_M - Number of text block literal expressions in a
 * method/constructor.
 */
public class NotbmMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOTB_M";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> notbmMethod = m -> m.findAll(TextBlockLiteralExpr.class).size();
    ConstructorMetric<Integer> notbmConstructor = c -> c.findAll(TextBlockLiteralExpr.class).size();

    return getMetricForCallable(notbmMethod, notbmConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
