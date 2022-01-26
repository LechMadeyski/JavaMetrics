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
 * Class for computing FDP metric. FDP - Foreign Data Providers. Number of unrelated classes, which
 * fields are accessed in this class (accessor methods or directly). Java standard library classes
 * are counted in as well, e. g. System.out is taken as a foreign data access.
 */
public class FdpMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "FDP";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    Set<String> foreignProviders = new HashSet<>();

    ClassMetric<Integer> fdp =
        c -> {
          List<String> classFieldNames = getAllClassFieldNames(c);

          c.findAll(FieldAccessExpr.class).stream()
              .filter(fe -> isForeignFieldAccessed(fe, c, classFieldNames))
              .forEach(
                  fe ->
                      foreignProviders.add(
                          fe.resolve().asField().declaringType().getQualifiedName()));

          List<String> classMethodNames = getAllClassMethodNames(c);

          c.findAll(MethodCallExpr.class).stream()
              .filter(
                  me ->
                      me.isMethodCallExpr()
                          && me.getNameAsString().startsWith("get")
                          && isForeignAccessorCalled(me, c, classMethodNames))
              .forEach(me -> foreignProviders.add(me.resolve().declaringType().getQualifiedName()));

          return foreignProviders.size();
        };

    return getMetricForClass(fdp, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
