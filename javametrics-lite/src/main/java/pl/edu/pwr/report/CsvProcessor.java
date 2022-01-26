package pl.edu.pwr.report;

import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.core.model.MetricDetail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvProcessor {

  private static final Logger LOGGER = Logger.getLogger(CsvProcessor.class.getName());
  private static final String[] headerValuesRolledUp = {
    "Project",
    "Package",
    "Class",
    "MethodSignature",
    "OuterClass",
    "AccessModifier",
    "IsStatic",
    "IsFinal"
  };
  private static final String DEFAULT_CSV_NAME = "smells.csv";

  private CsvProcessor() {}

  public static void write(
      String fileLocation,
      String projectName,
      Map<MetricDetail, TreeMap<String, Metric<?>>> rolledUpMetrics,
      List<String> metricNames)
      throws IOException {
    fileLocation = prepareFileLocation(fileLocation);

    if (LOGGER.isLoggable(Level.INFO))
      LOGGER.info(
          String.format("Writing output from project (%s) to: %s", projectName, fileLocation));

    Collections.sort(metricNames);

    File f = new File(fileLocation);
    if (f.exists() && f.canRead()) {
      try (FileWriter fileWriter = new FileWriter(f, true)) {
        writeRow(projectName, fileWriter, rolledUpMetrics);
      }
    } else {
      try (FileWriter fileWriter = new FileWriter(f)) {
        String header =
            String.join(",", headerValuesRolledUp) + "," + String.join(",", metricNames);
        fileWriter.append(header);
        fileWriter.append("\n");
        fileWriter.flush();
        writeRow(projectName, fileWriter, rolledUpMetrics);
      }
    }
  }

  private static void writeRow(
      String projectName,
      FileWriter fileWriter,
      Map<MetricDetail, TreeMap<String, Metric<?>>> rolledUpMetrics)
      throws IOException {
    rolledUpMetrics
        .keySet()
        .forEach(
            k -> {
              List<String> details =
                  Arrays.asList(
                      projectName,
                      k.getPackageName().orElse(""),
                      k.getClassName().orElse(""),
                      "\"" + k.getMethodSignature().orElse("") + "\"",
                      k.getOuterClassName().orElse(""),
                      k.getAccessModifier().orElse(""),
                      String.valueOf(k.getStatic()),
                      String.valueOf(k.getFinal()));

              StringJoiner stringJoiner = new StringJoiner(",", "", "\n");
              details.forEach(stringJoiner::add);
              rolledUpMetrics
                  .get(k)
                  .forEach(
                      (ki, v) -> {
                        if (v == null) stringJoiner.add("");
                        else stringJoiner.add(v.getValue().toString());
                      });
              try {
                fileWriter.append(stringJoiner.toString());
              } catch (IOException ex) {
                LOGGER.severe("Error while writing row..." + ex);
              }
            });

    fileWriter.flush();
  }

  private static String prepareFileLocation(String fileLocation) {
    if (fileLocation == null || fileLocation.isEmpty()) {
      File currDir = new File(".");
      fileLocation = currDir.getAbsolutePath();
      fileLocation = fileLocation.substring(0, fileLocation.length() - 1) + DEFAULT_CSV_NAME;
    }
    return fileLocation;
  }
}
