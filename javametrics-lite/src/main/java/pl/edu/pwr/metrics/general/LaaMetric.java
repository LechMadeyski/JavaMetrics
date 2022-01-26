package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Class for computing LAA metric. LAA - Locality of Attribute Accesses. Number of own attributes
 * used divided by number of all attributes used.
 */
public class LaaMetric extends ClassMetricStrategy<Double> {

  private static final String METRIC_NAME = "LAA";

  @Override
  public List<Metric<Double>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Double> laa =
        c -> {
          List<String> classAccessorMethods =
              getAllClassMethodNames(c).stream()
                  .filter(m -> m.startsWith("get"))
                  .collect(Collectors.toList());
          List<String> classFieldNames = getAllClassFieldNames(c);

          AtomicInteger ownAttributeUsages = new AtomicInteger(0);
          AtomicInteger allAttributeUsages = new AtomicInteger(0);

          c.findAll(FieldAccessExpr.class)
              .forEach(
                  fe -> {
                    allAttributeUsages.incrementAndGet();
                    if (!isForeignFieldAccessed(fe, c, classFieldNames)) {
                      ownAttributeUsages.incrementAndGet();
                    }
                  });

          c.findAll(MethodCallExpr.class).stream()
              .filter(me -> me.isMethodCallExpr() && me.getNameAsString().startsWith("get"))
              .forEach(
                  me -> {
                    allAttributeUsages.incrementAndGet();
                    if (!isForeignAccessorCalled(me, c, classAccessorMethods)) {
                      ownAttributeUsages.incrementAndGet();
                    }
                  });

          return (double) ownAttributeUsages.get() / (double) allAttributeUsages.get();
        };

    return getMetricForClass(laa, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
