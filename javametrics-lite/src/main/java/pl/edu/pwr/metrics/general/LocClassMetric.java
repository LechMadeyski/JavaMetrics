package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.config.Constants;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing LOC metric for a class. LOC - lines of code Code of inner classes are counted
 * as a code of enclosing class! No distinct entity for an inner class.
 */
public class LocClassMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "LOC_C";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> loc =
        c -> {
          int classLen = c.toString().split(Constants.NEW_LINE_REGEX).length;
          int commentsLen =
              c.getAllContainedComments().stream()
                  .mapToInt(comment -> comment.getContent().split(Constants.NEW_LINE_REGEX).length)
                  .sum();

          return classLen - commentsLen;
        };

    return getMetricForClass(loc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
