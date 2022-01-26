package pl.edu.pwr.filter;

import de.siegmar.fastcsv.reader.NamedCsvRow;

import java.util.Objects;

public class ConstructorInput extends CsvInput {

  private final String className;

  public ConstructorInput(NamedCsvRow csvRow) {
    super(csvRow.getField(CsvHeaders.PACKAGE.getValue()));
    this.className = csvRow.getField(CsvHeaders.CLASS.getValue());
  }

  public ConstructorInput(String pkg, String className) {
    super(pkg);
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ConstructorInput that = (ConstructorInput) o;
    return Objects.equals(className, that.className);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), className);
  }
}
