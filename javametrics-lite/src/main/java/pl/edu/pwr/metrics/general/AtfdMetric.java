package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for computing ATFD metric. ATFD - Access to Foreign Data. Number of fields from unrelated
 * classes that are accessed in this class (accessor methods or directly). If the same attribute is
 * used in multiple methods, it is counted multiple times (one per method even if used several times
 * inside one).
 */
public class AtfdMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "ATFD";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    Set<String> foreignAttributeUsages = new HashSet<>();

    ClassMetric<Integer> atfd =
        c -> {
          List<String> classFieldNames = getAllClassFieldNames(c);

          c.findAll(FieldAccessExpr.class).stream()
              .filter(fe -> isForeignFieldAccessed(fe, c, classFieldNames))
              .forEach(
                  fe ->
                      foreignAttributeUsages.add(
                          fe.resolve().asField().declaringType().getQualifiedName()
                              + "#"
                              + fe.getNameAsString()));

          List<String> classMethodNames = getAllClassMethodNames(c);

          c.findAll(MethodCallExpr.class).stream()
              .filter(
                  me ->
                      me.isMethodCallExpr()
                          && me.getNameAsString().startsWith("get")
                          && isForeignAccessorCalled(me, c, classMethodNames))
              .forEach(me -> foreignAttributeUsages.add(me.resolve().getQualifiedSignature()));

          return foreignAttributeUsages.size();
        };

    return getMetricForClass(atfd, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
