package pl.edu.pwr.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import pl.edu.pwr.core.model.Metric;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetricGenerator {

  private static final Logger LOGGER = Logger.getLogger(MetricGenerator.class.getName());
  private List<MetricStrategy> strategies;

  public MetricGenerator(List<MetricStrategy> strategies) {
    this.strategies = strategies;
  }

  public List<Metric> compute(CompilationUnit compilationUnit) {
    List<Metric> metrics = new ArrayList<>();
    for (MetricStrategy strategy : strategies) {
      LOGGER.info("Computing strategy " + strategy.getName());
      try {
        metrics.addAll(strategy.compute(compilationUnit));
      } catch (UnsolvedSymbolException e) {
        LOGGER.log(Level.WARNING, e.getMessage(), e);
      }
    }

    return metrics;
  }

  public List<Metric> compute(List<CompilationUnit> compilationUnits) {
    List<Metric> metrics = new ArrayList<>();

    compilationUnits.forEach(
        cu -> {
          for (MetricStrategy strategy : strategies) {
            metrics.addAll(strategy.compute(cu));
          }
        });

    return metrics;
  }

  public List<String> getMetricStrategyNames() {
    // return strategies.stream().map(MetricStrategy::getName).collect(Collectors.toList());
    return MetricGeneratorBuilder.getAllMetricNames();
  }
}
