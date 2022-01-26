package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.metrics.visitors.LambdaVisitor;

import java.util.List;

/** NOL_M - Number Of Lambda expressions (Java 8 feature) in a method or a constructor */
public class NolmMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOL_M";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> lambdaMethod =
        m -> {
          LambdaVisitor lambdaVisitor = new LambdaVisitor();
          lambdaVisitor.visit(m, null);
          return lambdaVisitor.getCount();
        };

    ConstructorMetric<Integer> lambdaConstructor =
        c -> {
          LambdaVisitor lambdaVisitor = new LambdaVisitor();
          lambdaVisitor.visit(c, null);
          return lambdaVisitor.getCount();
        };

    return getMetricForCallable(lambdaMethod, lambdaConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
