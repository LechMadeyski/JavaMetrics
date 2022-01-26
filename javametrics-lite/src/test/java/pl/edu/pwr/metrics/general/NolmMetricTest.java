package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class NolmMetricTest {
  private static final NolmMetric nolm = new NolmMetric();

  @Test
  void baseTest() {
    var fixture = """
      class Fixture {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nolm.compute(cu);
    var want = List.of();

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
    var got = nolm.compute(cu);
    var want = List.of();

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void methodTest() {
    var fixture = """
      class Fixture {
          private void two() {
            var x = y -> 1;
            var z = j -> 1;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nolm.compute(cu);
    var want = List.of(new Metric<>("NOL_M", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void constructorTest() {
    var fixture = """
      class Fixture {
          public Fixture() {
            var x = y -> 1;
            var z = j -> 1;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nolm.compute(cu);
    var want = List.of(new Metric<>("NOL_M", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void combinedTest() {
    var fixture = """
      class Fixture {
          private var a = x -> 1;
          
          public Fixture() {
            var x = y -> 1;
            var z = j -> 1;
          }
          
          private void two() {
            var x = y -> 1;
            var z = j -> 1;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nolm.compute(cu);
    var want = List.of(new Metric<>("NOL_M", 2, null), new Metric<>("NOL_M", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }
}