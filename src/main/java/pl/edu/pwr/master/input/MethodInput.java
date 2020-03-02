package pl.edu.pwr.master.input;

import java.util.List;
import java.util.Objects;

public class MethodInput extends CsvInput {

    public static final String METHOD_TYPE = "function";

    private final String className;
    private final String methodName;
    private List<String> arguments;

    public MethodInput(String packagePath, String className, String methodName, List<String> arguments) {
        super(packagePath);
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MethodInput that = (MethodInput) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), className, methodName, arguments);
    }
}
