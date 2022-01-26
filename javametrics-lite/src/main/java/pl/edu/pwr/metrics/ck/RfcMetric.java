package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for computing RFC metric. RFC - response for a class (the value of RFC is the sum of number
 * of methods called within the class method bodies and the number of class methods)
 */
public class RfcMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "RFC";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> rfc =
        c -> {
          List<String> methodNames =
              c.findAll(MethodDeclaration.class).stream()
                  .map(NodeWithSimpleName::getNameAsString)
                  .collect(Collectors.toList());

          List<String> methodsCalled =
              c.findAll(MethodCallExpr.class).stream()
                  .filter(m -> !methodNames.contains(m.getNameAsString()))
                  .map(NodeWithSimpleName::getNameAsString)
                  .collect(Collectors.toList());

          return methodNames.size() + methodsCalled.size();
        };

    return getMetricForClass(rfc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
