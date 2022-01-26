package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class LocClassMetricTest {

  private static final LocClassMetric loc = new LocClassMetric();

  @Test
  void emptyMultiLineTest() {
    var fixture = """
      class BaseTwoLines {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_C", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void emptySingleLineTest() {
    var fixture = """
      class BaseTwoLines {}
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_C", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void fiveTest() {
    var fixture = """
      class BaseTwoLines {
        public void plusThree() {}
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_C", 5, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void eightTest() {
    var fixture = """
      class Fixture {
        public void plusThree() {}
        public void plusThree() {}
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_C", 8, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void nineWithFieldTest() {
    var fixture = """
      class Fixture {
        private int plusTwo = 1;
        
        // whitespace above is ignored
        public void plusThree() {}
        public void plusThree() {}
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_C", 10, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void eightWithCommentTest() {
    var fixture = """
      class Fixture {
        public void plusThree() {
          /* some
              multiline
                comment */
        }
        public void plusThree() {}
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = loc.compute(cu);
    var want = List.of(new Metric<>("LOC_C", 8, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

}