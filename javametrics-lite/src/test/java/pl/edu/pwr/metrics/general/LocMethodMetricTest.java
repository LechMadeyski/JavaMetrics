package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class LocMethodMetricTest {
  private static final LocMethodMetric loc = new LocMethodMetric();

  @Test
  void noMethodsTest() {
    var fixture = """
      class BaseTwoLines {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of();

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void emptyTest() {
    var fixture = """
      class BaseTwoLines {
          // No methods
          public void two() {
            // no lines
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_M", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void twoPlusTwoContentTest() {
    var fixture = """
      class BaseTwoLines {
          // No methods
          public void two() {
            x();
            y();
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_M", 4, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void twoPlusTwoContentWithMultiLineCommentTest() {
    var fixture = """
      class BaseTwoLines {
          // No methods
          public void two() {
            x();
            /* multi
               line
               comment
            */
            y();
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_M", 4, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void twoMethodsTest() {
    var fixture = """
      class BaseTwoLines {
          public int a = 1;
          // No methods
          public void two() {
            x();
            y();
          }
          
          public void three() {
            x();
            y();
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_M", 4, null), new Metric<>("LOC_M", 4, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }
}