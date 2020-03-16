package pl.edu.pwr.master;

public enum ConformedRowHeaders {
    TYPE("type"), PACKAGE("package"), OUTER_CLASS("outer_class"), CLASS("class"), METHOD("method"),
        PARAMETERS("parameters"), REPOSITORY("repository"), COMMIT_HASH("commit_hash"), GIT_SOURCE_FILE_URL("git_source_file_url");

    private String header;

    ConformedRowHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
