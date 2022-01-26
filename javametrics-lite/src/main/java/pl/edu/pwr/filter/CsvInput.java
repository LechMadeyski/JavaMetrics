package pl.edu.pwr.filter;

import java.util.Objects;

public abstract class CsvInput {

  private final String pkg;

  protected CsvInput(String pkg) {
    this.pkg = pkg;
  }

  public String getPackage() {
    return pkg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CsvInput csvInput = (CsvInput) o;
    return Objects.equals(pkg, csvInput.pkg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pkg);
  }
}
