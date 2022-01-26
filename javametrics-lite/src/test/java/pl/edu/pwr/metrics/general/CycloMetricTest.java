package pl.edu.pwr.metrics.general;

import com.github.javaparser.StaticJavaParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.core.model.Metric;

import java.util.List;


class CycloMetricTest {

  private static final CycloMetric cyclo = new CycloMetric();

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
        var got = cyclo.compute(cu);
        var want = List.of(new Metric<>("CYCLO", 1, null));

        Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
    }

  @Test
  void methodTest() {
      var fixture = """
      class Fixture {
          public void one() {
              if (true) {
                  return;
              }
          }
      }
      """;

      var cu = StaticJavaParser.parse(fixture);
      var got = cyclo.compute(cu);
      var want = List.of(new Metric<>("CYCLO", 2, null));

      Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

    @Test
    void cnstructorTest() {
        var fixture = """
      class Fixture {
          public Fixture() {
              if (true) {
                  return;
              }
          }
      }
      """;

        var cu = StaticJavaParser.parse(fixture);
        var got = cyclo.compute(cu);
        var want = List.of(new Metric<>("CYCLO", 2, null));

        Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
    }

    @Test
    void switchTest() {
        var fixture = """
      class Fixture {
        void foo(int a) {
         switch (a)
           {
             case 1:
             case 2:
              return;
           }
         }
      }
      """;

        var cu = StaticJavaParser.parse(fixture);
        var got = cyclo.compute(cu);
        var want = List.of(new Metric<>("CYCLO", 3, null));

        Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
    }

    @Test
    void doWhileTest() {
        var fixture = """
      class Fixture {
        void foo(int a) {
          do
             a--;
          while(a);
        }
      }
      """;

        var cu = StaticJavaParser.parse(fixture);
        var got = cyclo.compute(cu);
        var want = List.of(new Metric<>("CYCLO", 2, null));

        Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
    }

  @Test
  void whileTest() {
    var fixture = """
      class Fixture {
        void foo(int a) {
           while (a)
              a--;
         }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = cyclo.compute(cu);
    var want = List.of(new Metric<>("CYCLO", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void forTest() {
    var fixture = """
      class Fixture {
        void foo(int a) {
          for (int x = 0; x < a; x++)
             foo();
        }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = cyclo.compute(cu);
    var want = List.of(new Metric<>("CYCLO", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void tryCatchTest() {
    var fixture = """
      class Fixture {
        void foo(int a) {
          try {
           x();
          }
          catch (Exception ex) {
           y();
          }
        }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = cyclo.compute(cu);
    var want = List.of(new Metric<>("CYCLO", 2, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }

  @Test
  void nestedTest() {
    var fixture = """
      class Fixture {
        void foo(int a) {
          while(true) {
            if (1 == 1) {
             x();
            } else if (2 == 2) {
             y();
            } else {
             z();
            }
          }
        }
      }
      """;

    var cu = StaticJavaParser.parse(fixture);
    var got = cyclo.compute(cu);
    var want = List.of(new Metric<>("CYCLO", 4, null));

    Assertions.assertThat(got).usingRecursiveComparison().ignoringFields("details").isEqualTo(want);
  }
}
