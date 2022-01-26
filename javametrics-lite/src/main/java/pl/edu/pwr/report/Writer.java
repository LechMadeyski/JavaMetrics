package pl.edu.pwr.report;

import de.siegmar.fastcsv.writer.CsvWriter;
import pl.edu.pwr.core.model.Metric;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

public class Writer {

  private static volatile Writer instance;
  private final CsvWriter csvWriter;
  private final List<String> headers;

  private Writer(String output, List<String> headers) throws IOException {
    this.csvWriter = CsvWriter.builder().build(Paths.get(output), Charset.defaultCharset());
    this.headers = headers;
    this.csvWriter.writeRow(this.headers);
  }

  public static Writer getInstance(String output, List<String> headers) throws IOException {
    if (instance == null) {
      synchronized (Writer.class) {
        if (instance == null) {
          instance = new Writer(output, headers);
        }
      }
    }
    return instance;
  }

  public void write(List<Metric> metrics) {
    var rows = CsvOutput.rows(this.headers, metrics);
    rows.forEach(this.csvWriter::writeRow);
  }

  public void close() throws IOException {
    this.csvWriter.close();
  }
}
