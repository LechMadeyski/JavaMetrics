package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class NoaMetricTest {
  private static final NoaMetric noa = new NoaMetric();

  @Test
  void baseTest() {
    var fixture = """
      class Fixture {
          public void one() {
              return;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noa.compute(cu);
    var want = List.of(new Metric<>("NOA_C", 0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void oneTest() {
    var fixture = """
      class Fixture {
          @AnnotationField
          private int field = 1;
          @AnnotationMethod
          public void one() {
              return;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noa.compute(cu);
    var want = List.of(new Metric<>("NOA_C", 1, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void twoTest() {
    var fixture = """
      @ClassAnnotation
      class Fixture {
          @AnnotationField
          private int field1 = 1;
          @AnnotationField
          private int field2 = 1;
          @Annotation
          public void one() {
              return;
          }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = noa.compute(cu);
    var want = List.of(new Metric<>("NOA_C", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }
}
