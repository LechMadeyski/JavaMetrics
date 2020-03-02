package pl.edu.pwr.master.core;

import com.github.javaparser.ParseException;
import pl.edu.pwr.master.input.Input;
import pl.edu.pwr.master.metrics.ck.WmcMetric;
import pl.edu.pwr.master.metrics.ck.WmcnammMetric;
import pl.edu.pwr.master.metrics.general.*;

import java.io.IOException;

public class MetricsRunner {

    public MetricsRunner(String input, String output) throws IOException, ParseException {
        this.metricSuite(input, output);
    }

    public MetricsRunner(String inputPath, String output, Input inputCsv) throws IOException, ParseException {
        this.metricSuite(inputPath, output, inputCsv);
    }

    public void metricSuite(String path, String outputFilename) throws IOException, ParseException {
        MetricGenerator metricGenerator = prepareMetricGenerator();

        MetricParser metricParser = new MetricParser(metricGenerator, outputFilename);
        metricParser.parse(path);
    }

    public void metricSuite(String path, String outputFilename, Input input) throws IOException, ParseException {
        MetricGenerator metricGenerator = prepareMetricGenerator();

        MetricParser metricParser = new MetricParser(metricGenerator, outputFilename, input);
        metricParser.parse(path);
    }

    private MetricGenerator prepareMetricGenerator() {
        return new MetricGeneratorBuilder()
                .addMetric(new CycloMetric())
                .addMetric(new LdMetric())
                .addMetric(new LocMethodMetric())
                .addMetric(new LocClassMetric())
                .addMetric(new MrdMetric())
                .addMetric(new NoamMetric())
                .addMetric(new NolcMetric())
                .addMetric(new NolmMetric())
                .addMetric(new NomrcMetric())
                .addMetric(new NomrmMetric())
                .addMetric(new NomMetric())
                .addMetric(new NommMetric())
                .addMetric(new NopaMetric())
                .addMetric(new NopvMetric())
                .addMetric(new WmcMetric())
                .addMetric(new WmcnammMetric())
                .addMetric(new WocMetric())
                .build();
    }
}
