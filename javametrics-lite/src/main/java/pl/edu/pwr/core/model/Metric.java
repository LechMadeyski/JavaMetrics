package pl.edu.pwr.core.model;

public class Metric<T> {
  private final String name;
  private final T value;
  private final MetricDetail details;

  public Metric(String name, T value, MetricDetail details) {
    this.name = name;
    this.value = value;
    this.details = details;
  }

  public String getName() {
    return name;
  }

  public T getValue() {
    return value;
  }

  public MetricDetail getDetails() {
    return details;
  }
}
