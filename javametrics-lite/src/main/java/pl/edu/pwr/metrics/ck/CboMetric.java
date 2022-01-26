package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for computing CBO metric. CBO - coupling between objects (These couplings can occur through
 * method calls, variable accesses, inheritance, method arguments, return types, and exceptions)
 */
public class CboMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "CBO";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> cbo =
        c -> {
          Set<ClassOrInterfaceDeclaration> afferentClasses = getAfferentCouplingClasses(c);
          Set<String> afferent =
              afferentClasses.stream()
                  .map(clazz -> clazz.getFullyQualifiedName().orElse(""))
                  .collect(Collectors.toSet());
          Set<String> efferent = getEfferentCouplingClasses(c);

          Set<String> result = new HashSet<>(afferent);
          result.addAll(efferent);
          return result.size();
        };

    return getMetricForClass(cbo, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
