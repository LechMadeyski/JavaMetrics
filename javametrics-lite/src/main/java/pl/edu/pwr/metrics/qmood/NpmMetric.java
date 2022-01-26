package pl.edu.pwr.metrics.qmood;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** Class for computing NPM metric. NPM - all the methods in a class that are declared as public. */
public class NpmMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NPM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> npm =
        c ->
            (int)
                c.findAll(MethodDeclaration.class).stream()
                    .filter(m -> m.getAccessSpecifier().equals(AccessSpecifier.PUBLIC))
                    .count();
    return getMetricForClass(npm, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
