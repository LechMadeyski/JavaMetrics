package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class LdMetricTest {

  private static final LdMetric ld = new LdMetric();

  @Test
  void baseTest() {
    var fixture = """
      class Fixture {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = ld.compute(cu);
    var want = List.of(new Metric<>("LD", 0.0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void fieldTest() {
    var fixture = """
      class Fixture {
          private var a = x -> 1;
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = ld.compute(cu);
    var want = List.of(new Metric<>("LD", toPercent(1, 4), null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void methodTest() {
    var fixture = """
      class Fixture { // loc of class is 2
          private void two() { // loc of method is 6
            var x = y -> 1;
            var z = j -> 1;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = ld.compute(cu);
    var want = List.of(new Metric<>("LD", toPercent(2, 8), null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void combinedTest() {
    var fixture = """
      class Fixture {
          private var a = x -> 1;
          private void two() {
            var x = y -> 1;
            var z = j -> 1;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = ld.compute(cu);
    var want = List.of(new Metric<>("LD", toPercent(3, 9), null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  private static double toPercent(int numberOfLambdas, int numberOfLines) {
    return (double)(numberOfLambdas) * 100 / numberOfLines;
  }
}
