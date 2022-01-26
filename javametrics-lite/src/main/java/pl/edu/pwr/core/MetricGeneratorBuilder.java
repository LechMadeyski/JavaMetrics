package pl.edu.pwr.core;

import pl.edu.pwr.metrics.ck.*;
import pl.edu.pwr.metrics.cognitive.*;
import pl.edu.pwr.metrics.general.*;
import pl.edu.pwr.metrics.martin.CaMetric;
import pl.edu.pwr.metrics.martin.CeMetric;
import pl.edu.pwr.metrics.qmood.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetricGeneratorBuilder {

  private static final List<MetricStrategy> STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION =
      new ArrayList<>(
          List.of(
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
              new WocMetric(),
              new NpmMetric(),
              new NovarMetric(),
              new NosecMetric(),
              new NosemMetric(),
              new NotbmMetric(),
              new NotbcMetric(),
              new LdvcmMetric(),
              new LdvccMetric(),
              new NdcwcMetric(),
              new LmncMetric()));

  private static final List<MetricStrategy> STRATEGIES_WITH_DEPENDENCY_RESOLUTION =
      new ArrayList<>(
          List.of(
              new AtfdMetric(),
              new NocMetric(),
              new DitMetric(),
              new RfcMetric(),
              new CboMetric(),
              new LcomMetric(),
              new CbomzMetric(),
              new DamMetric(),
              new MoaMetric(),
              new MfaMetric(),
              new CamMetric(),
              new CeMetric(),
              new CaMetric()));

  private List<MetricStrategy> strategies;

  public MetricGeneratorBuilder() {
    strategies = new ArrayList<>();
  }

  public MetricGeneratorBuilder addMetric(MetricStrategy strategy) {
    if (!containsMetric(strategy)) strategies.add(strategy);
    return this;
  }

  public MetricGeneratorBuilder addMetricsWithoutDependencyResolution() {
    STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.forEach(
        s -> {
          if (!containsMetric(s)) strategies.add(s);
        });
    return this;
  }

  public MetricGeneratorBuilder addMetricsWithDependencyResolution() {
    STRATEGIES_WITH_DEPENDENCY_RESOLUTION.forEach(
        s -> {
          if (!containsMetric(s)) strategies.add(s);
        });
    return this;
  }

  public MetricGenerator build() {
    return new MetricGenerator(strategies);
  }

  private boolean containsMetric(MetricStrategy metricStrategy) {
    return strategies.stream().anyMatch(s -> s.getName().equals(metricStrategy.getName()));
  }

  public static List<String> getMetricWithoutDependenciesNames() {
    return STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.stream()
        .map(MetricStrategy::getName)
        .collect(Collectors.toList());
  }

  public static List<String> getAllMetricNames() {
    List<String> list = new ArrayList<>();

    STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.forEach(s -> list.add(s.getName()));
    STRATEGIES_WITH_DEPENDENCY_RESOLUTION.forEach(s -> list.add(s.getName()));

    return list;
  }
}
