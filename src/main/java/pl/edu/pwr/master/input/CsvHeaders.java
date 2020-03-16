package pl.edu.pwr.master.input;

/**
 * CSV Headers for the input file.
 */
public enum CsvHeaders {
    TYPE("TYPE"), PACKAGE("PACKAGE"), OUTER_CLASS("OUTER_CLASS"), CLASS("CLASS"), METHOD("METHOD"), PARAMETERS("PARAMETERS"),
    REPOSITORY("REPOSITORY"), COMMIT_HASH("COMMIT_HASH"), GIT_SOURCE_FILE_URL("GIT_SOURCE_FILE_URL");

    private String header;

    CsvHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
