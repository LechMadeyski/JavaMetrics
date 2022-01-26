package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing DIT metric. DIT - depth of inheritance tree (number of inheritance levels
 * from object hierarchy top)/
 */
public class DitMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "DIT";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> dit =
        c -> {
          List<ResolvedReferenceType> resolvedReferenceTypes = c.resolve().getAncestors();
          return resolvedReferenceTypes.stream()
              .mapToInt(r -> getInheritanceDepth(r, 1))
              .max()
              .orElse(0);
        };

    return getMetricForClass(dit, compilationUnit);
  }

  public Integer getInheritanceDepth(ResolvedReferenceType r, Integer depth) {
    List<ResolvedReferenceType> ancestors = r.getDirectAncestors();
    if (ancestors.isEmpty()) return depth;
    else {
      return ancestors.stream()
          .mapToInt(a -> getInheritanceDepth(a, depth + 1))
          .max()
          .orElse(depth);
    }
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
