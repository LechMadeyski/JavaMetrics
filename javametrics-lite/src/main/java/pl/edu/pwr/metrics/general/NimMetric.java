package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NIM metric. NIM - Number of Inherited Methods Number of inherited methods of
 * a given class.
 */
public class NimMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NIM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> nim =
        c -> {
          ResolvedReferenceTypeDeclaration resolved = c.resolve();

          return resolved.getAllMethods().stream()
              .filter(
                  m ->
                      !m.declaringType().equals(resolved.asType())
                          && !m.getDeclaration().accessSpecifier().equals(AccessSpecifier.PRIVATE))
              .mapToInt(i -> 1)
              .sum();
        };

    return getMetricForClass(nim, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
