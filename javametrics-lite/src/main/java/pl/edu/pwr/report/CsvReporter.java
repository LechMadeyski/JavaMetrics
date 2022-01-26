package pl.edu.pwr.report;

import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.filter.Filter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvReporter extends OutputReporter {

  private static final Logger LOGGER = Logger.getLogger(CsvReporter.class.getName());
  private static final String DEFAULT_CSV_NAME = "smells.csv";

  private final String outputFilename;

  public CsvReporter() {
    this.outputFilename = DEFAULT_CSV_NAME;
  }

  public CsvReporter(String outputFilename) {
    this.outputFilename = outputFilename;
  }

  @Override
  public void writeMetrics(
      String projectName, List<Metric> metrics, List<String> metricNames, Filter selectInput) {
    try {
      // List<Metric> metricsToWrite = metrics;
      // if (!selectInput.isEmpty()) metricsToWrite = selectMetrics(metrics, selectInput);

      CsvProcessor.write(
          outputFilename, projectName, replaceMissingMetrics(metrics, metricNames), metricNames);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Workbook exception for: " + this.getClass().getSimpleName(), e);
    }
  }
}
