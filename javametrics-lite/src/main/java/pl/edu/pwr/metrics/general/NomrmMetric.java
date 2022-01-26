package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** NOMR_M - Number Of Method References (Java 8 feature) per method or constructor */
public class NomrmMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOMR_M";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> nomrMethod = m -> m.findAll(MethodReferenceExpr.class).size();
    ConstructorMetric<Integer> nomrConstructor = c -> c.findAll(MethodReferenceExpr.class).size();

    return getMetricForCallable(nomrMethod, nomrConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
