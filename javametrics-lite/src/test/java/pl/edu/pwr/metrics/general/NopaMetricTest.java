package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class NopaMetricTest {

  private static final NopaMetric nopa = new NopaMetric();

  @Test
  void baseTest() {
    var fixture = """
      class Fixture {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nopa.compute(cu);
    var want = List.of(new Metric<>("NOPA", 0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void emptyTest() {
    var fixture = """
      class Fixture {
          // No methods
          private int a;
          protected int b;
          int c;
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nopa.compute(cu);
    var want = List.of(new Metric<>("NOPA", 0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void oneTest() {
    var fixture = """
      class Fixture {
          // No methods
          private int a;
          protected int b;
          int c;
          public int d;
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nopa.compute(cu);
    var want = List.of(new Metric<>("NOPA", 1, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void twoTest() {
    var fixture = """
      class Fixture {
          // No methods
          private int a;
          protected int b;
          int c;
          public int d;
          public static int e = 1;
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = nopa.compute(cu);
    var want = List.of(new Metric<>("NOPA", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

}