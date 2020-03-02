package pl.edu.pwr.master.report.csv;

import pl.edu.pwr.master.core.model.Metric;
import pl.edu.pwr.master.core.model.MetricDetail;
import pl.edu.pwr.master.input.Input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class OutputReporter {

    public abstract void writeMetrics(String projectName, List<Metric> metrics, List<String> metricNames, Input selectInput);

    protected Map<MetricDetail, TreeMap<String, Metric<?>>> rollUpMetrics(List<Metric> metrics) {
        Map<MetricDetail, TreeMap<String, Metric<?>>> result = new HashMap<>();

        metrics.forEach(m -> {
            MetricDetail key = m.getDetails();
            if (!result.containsKey(key)) {
                result.put(key, new TreeMap<>());
            }

            result.get(key).put(m.getName(), m);
        });

        return result;
    }

    protected List<Metric> selectMetrics(List<Metric> metrics, Input input) {
        return metrics.stream()
                .filter(metric -> {
                    if (metric.getDetails().getMethodSignature().isPresent()) {
                        if (metric.getDetails().getMethodSignature().get().isBlank()) {
                            // logic for class
                            return input.containsClass(metric.getDetails());
                        } else {
                            // logic for method
                            return input.containsMethod(metric.getDetails());
                        }
                    } else {
                        // logic for class
                        return input.containsClass(metric.getDetails());
                    }
                })
                .collect(Collectors.toList());
    }

    protected Map<MetricDetail, TreeMap<String, Metric<?>>> replaceMissingMetrics(List<Metric> metrics, List<String> allMetricNames) {
        Map<MetricDetail, TreeMap<String, Metric<?>>> result = rollUpMetrics(metrics);

        result.forEach((k, v) ->
                allMetricNames.stream()
                        .filter(metricName -> !result.get(k).containsKey(metricName))
                        .forEach(metricName -> result.get(k).put(metricName, null))
        );

        return result;
    }
}
