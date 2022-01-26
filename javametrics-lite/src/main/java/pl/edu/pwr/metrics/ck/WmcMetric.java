package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.metrics.visitors.CycloVisitor;

import java.util.List;

/**
 * Class for computing WMC metric. WMC - weighted methods per class (sum of cyclomatic complexities
 * of all of its methods) Methods of inner classes are counted as a methods of enclosing class!
 */
public class WmcMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "WMC";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> wmc =
        c ->
            c.findAll(MethodDeclaration.class).stream()
                .mapToInt(
                    m -> {
                      CycloVisitor cycloVisitor = new CycloVisitor();
                      cycloVisitor.visit(m, null);
                      return cycloVisitor.getCount();
                    })
                .sum();
    return getMetricForClass(wmc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
