package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.metrics.visitors.CycloVisitor;

import java.util.List;

/**
 * Class for computing CYCLO metric in a method or a constructor. CYCLO - McCabe cyclomatic
 * complexity (number of possible disjunctive paths of program execution) Computed as number of:
 * ifs, cases, catches, fors, for-eaches and whiles.
 */
public class CycloMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "CYCLO";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> cycloMethod =
        m -> {
          CycloVisitor cycloVisitor = new CycloVisitor();
          cycloVisitor.visit(m, null);
          return cycloVisitor.getCount();
        };

    ConstructorMetric<Integer> cycloConstructor =
        c -> {
          CycloVisitor cycloVisitor = new CycloVisitor();
          cycloVisitor.visit(c, null);
          return cycloVisitor.getCount();
        };

    return getMetricForCallable(cycloMethod, cycloConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
