package pl.edu.pwr.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.config.Constants;
import pl.edu.pwr.core.ConstructorMetric;
import pl.edu.pwr.core.MethodMetric;
import pl.edu.pwr.core.MethodMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/** Class for computing LOC metric for a method or as constructor. LOC - lines of code */
public class LocMethodMetric extends MethodMetricStrategy<Integer> {

  private static final String METRIC_NAME = "LOC_M";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    MethodMetric<Integer> locMethod =
        m -> {
          int methodLen =
              m.getBody()
                  .map(body -> body.toString().split(Constants.NEW_LINE_REGEX).length)
                  .orElse(0);
          int commentsLen =
              m.getAllContainedComments().stream()
                  .mapToInt(comment -> comment.getContent().split(Constants.NEW_LINE_REGEX).length)
                  .sum();

          return methodLen - commentsLen;
        };

    ConstructorMetric<Integer> locConstructor =
        c -> {
          int constructorLen = c.getBody().toString().split(Constants.NEW_LINE_REGEX).length;
          int commentsLen =
              c.getAllContainedComments().stream()
                  .mapToInt(comment -> comment.getContent().split(Constants.NEW_LINE_REGEX).length)
                  .sum();

          return constructorLen - commentsLen;
        };

    return getMetricForCallable(locMethod, locConstructor, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
