package pl.edu.pwr.filter;

import de.siegmar.fastcsv.reader.NamedCsvRow;

import java.util.Objects;

public class MethodInput extends CsvInput {

  private final String className;
  private final String methodName;

  public MethodInput(NamedCsvRow csvRow) {
    super(csvRow.getField(CsvHeaders.PACKAGE.getValue()));
    this.className = csvRow.getField(CsvHeaders.CLASS.getValue());
    this.methodName = csvRow.getField(CsvHeaders.METHOD.getValue());
  }

  public MethodInput(String pkg, String className, String methodName) {
    super(pkg);
    this.className = className;
    this.methodName = methodName;
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MethodInput that = (MethodInput) o;
    return Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), className, methodName);
  }
}
