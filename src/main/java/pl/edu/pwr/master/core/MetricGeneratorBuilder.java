package pl.edu.pwr.master.core;

import pl.edu.pwr.master.metrics.ck.WmcMetric;
import pl.edu.pwr.master.metrics.ck.WmcnammMetric;
import pl.edu.pwr.master.metrics.general.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricGeneratorBuilder {

    private static final List<MetricStrategy> STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION =
            new ArrayList<>(List.of(
                    new CycloMetric(),
                    new LdMetric(),
                    new LocMethodMetric(),
                    new LocClassMetric(),
                    new MrdMetric(),
                    new NoamMetric(),
                    new NolcMetric(),
                    new NolmMetric(),
                    new NomrcMetric(),
                    new NomrmMetric(),
                    new NomMetric(),
                    new NommMetric(),
                    new NopaMetric(),
                    new NopvMetric(),
                    new WmcMetric(),
                    new WmcnammMetric(),
                    new WocMetric()
                )
            );

    private static final List<MetricStrategy> STRATEGIES_WITH_DEPENDENCY_RESOLUTION =
            new ArrayList<>(List.of(
                    new AtfdMetric()
                )
            );

    private List<MetricStrategy> strategies;

    public MetricGeneratorBuilder() {
        strategies = new ArrayList<>();
    }

    public MetricGeneratorBuilder addMetric(MetricStrategy strategy) {
        if (!containsMetric(strategy))
            strategies.add(strategy);
        return this;
    }

    public MetricGeneratorBuilder addMetricsWithoutDependencyResolution() {
        STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.forEach(s -> {
            if (!containsMetric(s))
                strategies.add(s);
        });
        return this;
    }

    public MetricGeneratorBuilder addMetricsWithDependencyResolution() {
        STRATEGIES_WITH_DEPENDENCY_RESOLUTION.forEach(s -> {
            if (!containsMetric(s))
                strategies.add(s);
        });
        return this;
    }

    public MetricGenerator build() {
        return new MetricGenerator(strategies);
    }

    private boolean containsMetric(MetricStrategy metricStrategy) {
        return strategies.stream().anyMatch(s -> s.getName().equals(metricStrategy.getName()));
    }

    public static List<String> getAllMetricNames() {
        List<String> list = new ArrayList<>();

        STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.forEach(s -> list.add(s.getName()));
        STRATEGIES_WITH_DEPENDENCY_RESOLUTION.forEach(s -> list.add(s.getName()));

        return list;
    }
}
