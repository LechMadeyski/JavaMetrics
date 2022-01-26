package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class NoamMetricTest {

  private static final NoamMetric noam = new NoamMetric();

  @Test
  void baseTest() {
    var fixture = """
      class Fixture {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noam.compute(cu);
    var want = List.of(new Metric<>("NOAM", 0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void emptyAccessorTest() {
    var fixture = """
      class Fixture {
          public void one() {
              // accessor, because it consists only 1 statement with return value
              return;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noam.compute(cu);
    var want = List.of(new Metric<>("NOAM", 1, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void accessorTest() {
    var fixture = """
      class Fixture {
          private int field = 1;
          public int one() {
              // accessor, because it consists only 1 statement, which is return and it return the field from class.
              return field;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noam.compute(cu);
    var want = List.of(new Metric<>("NOAM", 1, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void multipleAccessorTest() {
    var fixture = """
      class Fixture {
          private int field = 1;
          public int one() {
              // accessor, because it consists only 1 statement, which is return and it return the field from class.
              return field;
          }
          
          public int two() {
              // accessor, because it consists only 1 statement, which is return and it return the field from class.
              return field;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noam.compute(cu);
    var want = List.of(new Metric<>("NOAM", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void unknownFieldAccessorTest() {
    var fixture = """
      class Fixture {
          private int field = 1;
          public int one() {
              // not accessor, because it returns field not from the class.
              return TEST_FIELD;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noam.compute(cu);
    var want = List.of(new Metric<>("NOAM", 0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }
}
