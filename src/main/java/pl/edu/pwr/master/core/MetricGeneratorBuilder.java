package pl.edu.pwr.master.core;

import java.util.ArrayList;
import java.util.List;

public class MetricGeneratorBuilder {

    private List<MetricStrategy> strategies;

    public MetricGeneratorBuilder() {
        strategies = new ArrayList<>();
    }

    public MetricGeneratorBuilder addMetric(MetricStrategy strategy) {
        if (!containsMetric(strategy))
            strategies.add(strategy);
        return this;
    }

    public MetricGenerator build() {
        return new MetricGenerator(strategies);
    }

    private boolean containsMetric(MetricStrategy metricStrategy) {
        return strategies.stream().anyMatch(s -> s.getName().equals(metricStrategy.getName()));
    }
}
