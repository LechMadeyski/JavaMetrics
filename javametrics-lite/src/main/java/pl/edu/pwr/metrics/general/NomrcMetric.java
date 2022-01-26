package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** NOMR_C - Number Of Method References (Java 8 feature) per Class */
public class NomrcMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOMR_C";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> nomr =
        c -> c.findAll(MethodReferenceExpr.class).stream().mapToInt(md -> 1).sum();

    return getMetricForClass(nomr, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
