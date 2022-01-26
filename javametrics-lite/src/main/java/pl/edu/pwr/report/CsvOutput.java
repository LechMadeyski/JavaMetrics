package pl.edu.pwr.report;

import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.core.model.MetricDetail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvOutput {

  public static List<List<String>> rows(List<String> headers, List<Metric> metrics) {
    var ms = gatherMetrics(metrics);
    return ms.entrySet().stream().map(e -> row(headers, e)).collect(Collectors.toList());
  }

  private static List<String> row(List<String> headers, Map.Entry<MetricDetail, List<Metric>> e) {
    LinkedHashMap<String, String> output = new LinkedHashMap<>(headers.size());
    headers.forEach(h -> output.put(h, null));
    // TODO: implement
    output.put(CsvHeaders.FILE, "unimplemented");
    output.put(CsvHeaders.PACKAGE, e.getKey().getPackageName().orElse(""));
    output.put(CsvHeaders.OUTER_CLASS, e.getKey().getOuterClassName().orElse(""));
    output.put(CsvHeaders.CLASS, e.getKey().getClassName().orElse(""));
    output.put(CsvHeaders.METHOD, e.getKey().getMethodName().orElse(""));
    output.put(CsvHeaders.ACCESS_MODIFIER, e.getKey().getAccessModifier().orElse(""));
    output.put(CsvHeaders.IS_STATIC, e.getKey().getStatic().toString());
    output.put(CsvHeaders.IS_FINAL, e.getKey().getFinal().toString());

    e.getValue().forEach(m -> output.put(m.getName(), m.getValue().toString()));
    return new ArrayList<>(output.values());
  }

  private static Map<MetricDetail, List<Metric>> gatherMetrics(List<Metric> metrics) {
    return metrics.stream().collect(Collectors.groupingBy(Metric::getDetails));
  }
}
