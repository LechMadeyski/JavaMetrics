package pl.edu.pwr.master.input;

import java.util.Objects;

public class CsvInput {

    private final String packagePath;

    public CsvInput(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getLastPackageElement() {
        String[] split = this.getPackagePath().split("\\.");
        return split[split.length - 1];
    }

    public String getPackageWithoutLastElement() {
        String[] split = this.getPackagePath().split("\\.");
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < split.length - 1; i++) {
            sb.append(split[i]);
            sb.append(".");
        }

        String out = sb.toString();
        return out.substring(0, out.length() - 1);
    }

    public String getPackagePath() {
        return packagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvInput csvInput = (CsvInput) o;
        return Objects.equals(packagePath, csvInput.packagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packagePath);
    }
}
