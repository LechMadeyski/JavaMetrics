package pl.edu.pwr.metrics.cognitive;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing LDVC metric. LDVC - Levenshtein Distance Variable Count. This metric finds
 * summed value of number of pairs of variables declared in a method/constructor for names of which
 * the Levenshtein Distance is <= 2. It is computed for a class.
 */
public class LdvccMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "LDVC_C";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    LevenshteinDistanceGenerator ldg = new LevenshteinDistanceGenerator();
    ClassMetric<Integer> ldvc = c -> ldg.countLevenshteinPairs(c.findAll(VariableDeclarator.class));

    return getMetricForClass(ldvc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
