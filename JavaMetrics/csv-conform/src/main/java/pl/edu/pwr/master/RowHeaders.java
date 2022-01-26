package pl.edu.pwr.master;

public enum RowHeaders {
    ID("id"), REVIEWER_ID("reviewer_id"), SAMPLE_ID("sample_id"),
        SMELL("smell"), SEVERITY("severity"), REVIEW_TIMESTAMP("review_timestamp"), TYPE("type"),
        CODE_NAME("code_name"), REPOSITORY("repository"), COMMIT_HASH("commit_hash"), PATH("path"),
        START_LINE("start_line"), END_LINE("end_line"), LINK("link"), IS_FROM_INDUSTRY_RELEVANT_PROJECT("is_from_industry_relevant_project");

    private String header;

    RowHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
