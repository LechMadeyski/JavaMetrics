package pl.edu.pwr.metrics.cognitive;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing LDVC metric. LDVC - Levenshtein Distance Variable Count. This metric finds
 * number of pairs of variables declared in a method/constructor for names of which the Levenshtein
 * Distance is <= 2.
 */
public class LdvcmMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "LDVC_M";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    LevenshteinDistanceGenerator ldg = new LevenshteinDistanceGenerator();
    MethodMetric<Integer> ldvcMethod =
        m -> ldg.countLevenshteinPairs(m.findAll(VariableDeclarator.class));
    ConstructorMetric<Integer> ldvcConstructor =
        c -> ldg.countLevenshteinPairs(c.findAll(VariableDeclarator.class));

    return getMetricForCallable(ldvcMethod, ldvcConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
