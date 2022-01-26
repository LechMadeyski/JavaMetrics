package pl.edu.pwr.metrics.cognitive;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

/**
 * Class for computing NDCWC metric. NDCWC - Non-documentaion comments word count. This is the count
 * of words in non-documentation comments in the class.
 */
public class NdcwcMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "NDCWC";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> ndcwc =
        c -> {
          Integer lineCommentsWordCount =
              c.findAll(LineComment.class).stream()
                  .map(Comment::getContent)
                  .mapToInt(lc -> lc.split("\\s+").length)
                  .sum();

          Integer blockCommentWordCount =
              c.findAll(BlockComment.class).stream()
                  .map(Comment::getContent)
                  .mapToInt(bc -> bc.split("\\s+").length)
                  .sum();

          return blockCommentWordCount + lineCommentsWordCount;
        };

    return getMetricForClass(ndcwc, compilationUnit);
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
