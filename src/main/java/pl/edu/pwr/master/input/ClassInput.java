package pl.edu.pwr.master.input;

import java.util.Objects;

public class ClassInput extends CsvInput {

    public static final String CLASS_TYPE = "class";

    private final String className;

    public ClassInput(String packagePath, String outerClass, String className) {
        super(packagePath, outerClass);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClassInput that = (ClassInput) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), className);
    }
}
