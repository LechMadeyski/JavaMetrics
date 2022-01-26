package pl.edu.pwr.report;

import pl.edu.pwr.core.MetricGeneratorBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvHeaders {
  protected static final String FILE = "file";
  protected static final String PACKAGE = "package";
  protected static final String OUTER_CLASS = "outer_class";
  protected static final String CLASS = "class";
  protected static final String METHOD = "method";
  protected static final String ACCESS_MODIFIER = "access_modifier";
  protected static final String IS_STATIC = "is_static";
  protected static final String IS_FINAL = "is_final";

  private CsvHeaders() {}

  private static List<String> getBaseHeaders() {
    List<String> out = new ArrayList<>();
    out.add(CsvHeaders.FILE);
    out.add(CsvHeaders.PACKAGE);
    out.add(CsvHeaders.OUTER_CLASS);
    out.add(CsvHeaders.CLASS);
    out.add(CsvHeaders.METHOD);
    out.add(CsvHeaders.ACCESS_MODIFIER);
    out.add(CsvHeaders.IS_STATIC);
    out.add(CsvHeaders.IS_FINAL);
    return out;
  }

  public static List<String> getNoDependencyHeaders() {
    return Stream.concat(
            getBaseHeaders().stream(),
            MetricGeneratorBuilder.getMetricWithoutDependenciesNames().stream())
        .collect(Collectors.toList());
  }

  public static List<String> getDependencyHeaders() {
    return Stream.concat(
            getBaseHeaders().stream(), MetricGeneratorBuilder.getAllMetricNames().stream())
        .collect(Collectors.toList());
  }
}
