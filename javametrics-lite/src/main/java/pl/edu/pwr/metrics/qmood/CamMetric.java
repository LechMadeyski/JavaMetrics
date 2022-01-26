package pl.edu.pwr.metrics.qmood;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for computing CAM metric. CAM (Cohesion Among Methods of Class) - computes the relatedness
 * among methods of a class based upon the parameter list of the methods. The metric is computed
 * using the summation of number of different types of method parameters in every method divided by
 * a multiplication of number of different method parameter types in whole class and number of
 * methods
 */
public class CamMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "CAM";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> cam =
        c -> {
          int numberOfMethods = c.getMethods().size();
          Set<String> distinctParameters = new HashSet<>();
          c.getMethods()
              .forEach(
                  m -> {
                    for (Parameter p : m.getParameters()) {
                      distinctParameters.add(p.getType().resolve().describe());
                    }
                  });

          return distinctParameters.size() / ((double) numberOfMethods * distinctParameters.size());
        };
    return getMetricForClass(cam, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
