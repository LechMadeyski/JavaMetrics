package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOMM metric. NOMM - number of mutator methods in a class Here mutator is a
 * method that has only one expression, which is an assignment expression. It has to have one
 * parameter that should be a source and assign expression should take any class' field as a target.
 * Methods of inner classes are counted as a methods of enclosing class!
 */
public class NommMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOMM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> nomm = c -> getClassMutatorMethods(c).stream().mapToInt(m -> 1).sum();

    return getMetricForClass(nomm, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
