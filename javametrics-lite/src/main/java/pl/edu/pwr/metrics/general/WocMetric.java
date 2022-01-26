package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing WOC metric. WOC - weight of the class (number of functional (non-accessor and
 * non-mutator) methods divided by number of all methods) Methods of inner classes are counted as a
 * methods of enclosing class!
 */
public class WocMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "WOC";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> woc =
        c -> {
          List<MethodDeclaration> methods = c.findAll(MethodDeclaration.class);
          List<MethodDeclaration> mutators = getClassMutatorMethods(c);
          List<MethodDeclaration> accessors = getClassAccessorMethods(c);

          return (double) (methods.size() - mutators.size() - accessors.size())
              / (double) methods.size();
        };

    return getMetricForClass(woc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
