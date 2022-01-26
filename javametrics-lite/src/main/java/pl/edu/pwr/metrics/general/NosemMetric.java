package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.SwitchExpr;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOSE_M metric. NOSE_M - Number of switch expressions in a method/constructor.
 */
public class NosemMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOSE_M";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> nosemMethod = m -> m.findAll(SwitchExpr.class).size();
    ConstructorMetric<Integer> nosemConstructor = c -> c.findAll(SwitchExpr.class).size();

    return getMetricForCallable(nosemMethod, nosemConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
