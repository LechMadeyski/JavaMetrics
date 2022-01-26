package pl.edu.pwr.filter;

/** CSV Headers for the input file. */
public enum CsvHeaders {
  FILE("file"),
  PACKAGE("package"),
  CLASS("class"),
  METHOD("method");

  private String value;

  CsvHeaders(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
