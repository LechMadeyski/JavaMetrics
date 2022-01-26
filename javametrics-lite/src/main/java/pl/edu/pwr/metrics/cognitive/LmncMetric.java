package pl.edu.pwr.metrics.cognitive;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing LMNC metric. LMNC - Long Method Name Count - number of methods in a class
 * which length surpasses 24 characters.
 */
public class LmncMetric extends ClassMetricStrategy<Integer> {

  private static final Integer LMNC_LENGTH = 24;
  private static final String METRIC_NAME = "LMNC";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> lmnc =
        c ->
            (int)
                c.findAll(MethodDeclaration.class).stream()
                    .map(NodeWithSimpleName::getNameAsString)
                    .filter(mn -> mn.length() > LMNC_LENGTH)
                    .count();

    return getMetricForClass(lmnc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
