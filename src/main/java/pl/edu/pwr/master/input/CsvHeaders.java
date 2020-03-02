package pl.edu.pwr.master.input;

/**
 * CSV Headers for the input file.
 */
public enum CsvHeaders {
    ID("id"), TYPE("type"), CODE_NAME("code_name"), REPOSITORY("repository"), COMMIT_HASH("commit_hash");

    private String header;

    CsvHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
