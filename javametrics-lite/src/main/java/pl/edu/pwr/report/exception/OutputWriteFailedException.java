package pl.edu.pwr.report.exception;

public class OutputWriteFailedException extends RuntimeException {
  public OutputWriteFailedException(String message) {
    super(message);
  }
}
