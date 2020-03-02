package pl.edu.pwr.master.core;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.master.core.model.Metric;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetricGenerator {

    private List<MetricStrategy> strategies;

    public MetricGenerator(List<MetricStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<Metric> compute(CompilationUnit compilationUnit) {
        List<Metric> metrics = new ArrayList<>();
        for (MetricStrategy strategy : strategies) {
            metrics.addAll(strategy.compute(compilationUnit));
        }

        return metrics;
    }

    public List<Metric> compute(List<CompilationUnit> compilationUnits) {
        List<Metric> metrics = new ArrayList<>();

        compilationUnits.forEach(cu -> {
            for (MetricStrategy strategy : strategies) {
                metrics.addAll(strategy.compute(cu));
            }
        });

        return metrics;
    }

    public List<String> getMetricStrategyNames() {
        return strategies.stream().map(MetricStrategy::getName).collect(Collectors.toList());
    }
}
