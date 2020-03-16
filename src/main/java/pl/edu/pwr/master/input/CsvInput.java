package pl.edu.pwr.master.input;

import java.util.Objects;

public class CsvInput {

    private final String packagePath;
    private final String outerClass;

    public CsvInput(String packagePath, String outerClass) {
        this.packagePath = packagePath;
        this.outerClass = outerClass;
    }

    public String getClosestOuterClass() {
        String[] split = this.getOuterClass().split("\\.");
        return split[split.length - 1];
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String getOuterClass() {
        return outerClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvInput csvInput = (CsvInput) o;
        return Objects.equals(packagePath, csvInput.packagePath) &&
                Objects.equals(outerClass, csvInput.outerClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packagePath, outerClass);
    }
}
