package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.metrics.visitors.CycloVisitor;

import java.util.List;

/**
 * Class for computing WMCNAMM metric. WMCNAMM - weighted methods per class without accessor and
 * mutator methods (sum of cyclomatic complexities of its methods without accessor and mutator
 * methods) Methods of inner classes are counted as a methods of enclosing class!
 */
public class WmcnammMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "WMCNAMM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> wmcnamm =
        c -> {
          List<MethodDeclaration> methods = c.findAll(MethodDeclaration.class);
          List<MethodDeclaration> mutators = getClassMutatorMethods(c);
          List<MethodDeclaration> accessors = getClassAccessorMethods(c);
          methods.removeAll(mutators);
          methods.removeAll(accessors);

          return methods.stream()
              .mapToInt(
                  m -> {
                    CycloVisitor cycloVisitor = new CycloVisitor();
                    cycloVisitor.visit(m, null);
                    return cycloVisitor.getCount();
                  })
              .sum();
        };

    return getMetricForClass(wmcnamm, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
