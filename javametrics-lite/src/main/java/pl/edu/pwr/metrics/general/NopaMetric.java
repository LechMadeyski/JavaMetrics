package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** Class for computing NOPA metric. NOPA - number of public fields in a class */
public class NopaMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOPA";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> nopa =
        c ->
            c.findAll(FieldDeclaration.class).stream()
                .filter(f -> f.getModifiers().contains(Modifier.publicModifier()))
                .mapToInt(i -> 1)
                .sum();

    return getMetricForClass(nopa, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
