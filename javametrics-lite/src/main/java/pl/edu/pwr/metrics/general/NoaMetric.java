package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** NOA (Number of Annotations) - number of annotations inside a class computed for the fields */
public class NoaMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NOA_C";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> noa =
        c ->
            c.findAll(FieldDeclaration.class).stream()
                .mapToInt(fd -> fd.getAnnotations().size())
                .sum();

    return getMetricForClass(noa, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
