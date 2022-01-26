package pl.edu.pwr.filter;

import de.siegmar.fastcsv.reader.NamedCsvReader;
import pl.edu.pwr.filter.exception.InvalidInputException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/** Class for reading input CSV to the metrics tool. */
public class Reader {

  private Reader() {}

  /**
   * Gets input from the CSV file to be parsed.
   *
   * @param path path to the CSV file
   * @return Methods, constructors and classes to be parsed
   */
  public static Filter getFilter(Path path) throws InvalidInputException {
    Set<String> filePaths = new HashSet<>();
    Set<ClassInput> classes = new HashSet<>();
    Set<MethodInput> methods = new HashSet<>();
    Set<ConstructorInput> constructors = new HashSet<>();

    var builder = NamedCsvReader.builder();

    try (NamedCsvReader csv = builder.build(path, Charset.defaultCharset())) {
      csv.forEach(
          r -> {
              filePaths.add(r.getField(CsvHeaders.FILE.getValue()));
              switch (Filter.getTypeOf(r)) {
                  case CLASS -> classes.add(new ClassInput(r));
                  case METHOD -> methods.add(new MethodInput(r));
                  case CONSTRUCTOR -> constructors.add(new ConstructorInput(r));
                  default -> throw new InvalidInputException(String.format("Invalid row: %s", r));
              }
          });
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return new Filter(filePaths, classes, methods, constructors);
  }
}
