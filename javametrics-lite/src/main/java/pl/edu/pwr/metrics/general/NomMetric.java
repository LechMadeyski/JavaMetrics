package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NOM metric. NOM - number of methods in a class Methods of inner classes are
 * counted as a methods of enclosing class!
 */
public class NomMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> nom = c -> c.findAll(MethodDeclaration.class).size();
    return getMetricForClass(nom, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
