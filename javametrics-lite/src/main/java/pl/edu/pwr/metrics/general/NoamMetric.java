package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOAM metric. NOAM - number of accessor methods in a class Here accessor is a
 * method that has only one expression, which is a return expression. Methods of inner classes are
 * counted as a methods of enclosing class!
 */
public class NoamMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOAM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> noam = c -> getClassAccessorMethods(c).stream().mapToInt(m -> 1).sum();

    return getMetricForClass(noam, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
