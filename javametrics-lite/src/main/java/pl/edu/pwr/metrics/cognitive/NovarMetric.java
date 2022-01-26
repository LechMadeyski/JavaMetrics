package pl.edu.pwr.metrics.cognitive;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOVAR metric. NOVAR - Number of variables. Number of variables declared
 * inside a method.
 */
public class NovarMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOVAR";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> novarMethod = m -> m.findAll(VariableDeclarator.class).size();
    ConstructorMetric<Integer> novarConstructor = c -> c.findAll(VariableDeclarator.class).size();

    return getMetricForCallable(novarMethod, novarConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
