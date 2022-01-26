package pl.edu.pwr.metrics.martin;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing Ce metric. Ce (Efferent coupling) - number of classes that the measured class
 * is depended upon.
 */
public class CeMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "CE";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> ce = c -> getEfferentCouplingClasses(c).size();
    return getMetricForClass(ce, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
