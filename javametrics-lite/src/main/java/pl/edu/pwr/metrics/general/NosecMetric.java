package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.SwitchExpr;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** Class for computing NOSE_C metric. NOSE_C - Number of switch expressions in a class. */
public class NosecMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOSE_C";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> nosec = c -> c.findAll(SwitchExpr.class).size();

    return getMetricForClass(nosec, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
