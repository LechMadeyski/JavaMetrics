package pl.edu.pwr.master.input;

import pl.edu.pwr.master.core.model.MetricDetail;

import java.util.List;
import java.util.Optional;

public class Input {

    private List<ClassInput> classes;
    private List<MethodInput> methods;

    public Input(List<ClassInput> classes, List<MethodInput> methods) {
        this.classes = classes;
        this.methods = methods;
    }

    public boolean isEmpty() {
        return methods.isEmpty() && classes.isEmpty();
    }

    public boolean containsClass(String packageName, String outerClassName, String className) {
        for (ClassInput classInput : classes) {
            String inputOuterClassName = classInput.getLastPackageElement();
            if (inputOuterClassName.equals(outerClassName)) {
                if (classInput.getPackageWithoutLastElement().equals(packageName) && classInput.getClassName().equals(className))
                    return true;
            } else {
                if (classInput.getPackagePath().equals(packageName) && classInput.getClassName().equals(className))
                    return true;
            }
        }
        return false;
    }

    public boolean containsClass(MetricDetail metricDetail) {
        if (metricDetail.getPackageName().isEmpty() || metricDetail.getClassName().isEmpty())
            return false;

        String packageName = metricDetail.getPackageName().get();
        String outerClass = "";
        if (metricDetail.getOuterClassName().isPresent()) {
            outerClass = metricDetail.getOuterClassName().get();
        }
        String className = metricDetail.getClassName().get();

        return containsClass(packageName, outerClass, className);
    }

    public boolean containsMethod(String packageName, String outerClassName, String className, String methodName, List<String> arguments) {
        for (MethodInput methodInput : methods) {
            String inputOuterClassName = methodInput.getLastPackageElement();
            if (inputOuterClassName.equals(outerClassName)) {
                if (methodInput.getPackageWithoutLastElement().equals(packageName) && methodInput.getClassName().equals(className) && methodInput.getMethodName().equals(methodName)) {
                    return hasTheSameArguments(arguments, methodInput.getArguments());
                }
            } else {
                if (methodInput.getPackagePath().equals(packageName) && methodInput.getClassName().equals(className) && methodInput.getMethodName().equals(methodName)) {
                    return hasTheSameArguments(arguments, methodInput.getArguments());
                }
            }
        }
        return false;
    }

    public boolean containsMethod(String packageName, String outerClassName, String className, String methodName) {
        for (MethodInput methodInput : methods) {
            String inputOuterClassName = methodInput.getLastPackageElement();
            if (inputOuterClassName.equals(outerClassName)) {
                if (methodInput.getPackageWithoutLastElement().equals(packageName) && methodInput.getClassName().equals(className) && methodInput.getMethodName().equals(methodName)) {
                    return true;
                }
            } else {
                if (methodInput.getPackagePath().equals(packageName) && methodInput.getClassName().equals(className) && methodInput.getMethodName().equals(methodName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsMethod(MetricDetail metricDetail) {
        if (metricDetail.getPackageName().isEmpty() || metricDetail.getClassName().isEmpty() || metricDetail.getMethodSignature().isEmpty())
            return false;

        String packageName = metricDetail.getPackageName().get();
        String outerClass = "";
        if (metricDetail.getOuterClassName().isPresent()) {
            outerClass = metricDetail.getOuterClassName().get();
        }
        String className = metricDetail.getClassName().get();
        Optional<String> methodNameOpt = metricDetail.getMethodName();

        if (methodNameOpt.isEmpty())
            return false;

        String methodName = methodNameOpt.get();

        return containsMethod(packageName, outerClass, className, methodName);
    }

    private boolean hasTheSameArguments(List<String> expectedArguments, List<String> actualArguments) {
        if (expectedArguments.size() == actualArguments.size()) {
            for (int i = 0; i < expectedArguments.size(); i++)
                if (!expectedArguments.get(i).equals(actualArguments.get(i)))
                    return false;

            return true;
        }
        return false;
    }

    public List<ClassInput> getClasses() {
        return classes;
    }

    public List<MethodInput> getMethods() {
        return methods;
    }
}
