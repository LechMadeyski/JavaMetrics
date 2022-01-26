package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class for computing CBOMZ metric. CBOMZ - bidirectional coupling between objects (These couplings
 * can occur through method calls, variable accesses, inheritance, method arguments, return types,
 * and exceptions). Coupled classes are not deduplicated and therefore the detection of sensitivity
 * of change is increased.
 */
public class CbomzMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "CBOMZ";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> cbomz =
        c -> {
          Set<ClassOrInterfaceDeclaration> afferentClasses = getAfferentCouplingClasses(c);
          Set<String> afferent =
              afferentClasses.stream()
                  .map(clazz -> clazz.getFullyQualifiedName().orElse(""))
                  .collect(Collectors.toSet());
          Set<String> efferent = getEfferentCouplingClasses(c);

          return afferent.size() + efferent.size();
        };

    return getMetricForClass(cbomz, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
