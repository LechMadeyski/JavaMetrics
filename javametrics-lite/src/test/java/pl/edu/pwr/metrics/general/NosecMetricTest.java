package pl.edu.pwr.metrics.general;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;

class NosecMetricTest {

  private static String metricName = "NOSE_C";
  private static final NosecMetric metric = new NosecMetric();

  @BeforeEach
  void setUp() {
    StaticJavaParser.setConfiguration(new ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_15));
  }

  @Test
  void baseTest() {
    var fixture = """
      class Fixture {
          // No methods
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = metric.compute(cu);
    var want = List.of(new Metric<>(metricName, 0, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void methodTest() {
    var fixture = """
      class Fixture {
         public void one() {
          String x = "TEST";
          String y = switch (x) {
              case "a" -> "1";
              case "b" -> "2";
              case "c" -> "3";
              case "d" -> "4";
              default -> "5";
          };
         }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = metric.compute(cu);
    var want = List.of(new Metric<>(metricName,1, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void combinedTest() {
    var fixture = """
      class Fixture {
         var z = switch("TEST") {
          case "TEST":
            yield "1";
          default:
            yield "2";
         };
         public void one() {
          String x = "TEST";
          String y = switch (x) {
              case "a" -> "1";
              case "b" -> "2";
              case "c" -> "3";
              case "d" -> "4";
              default -> "5";
          };
         }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = metric.compute(cu);
    var want = List.of(new Metric<>(metricName,2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }
}