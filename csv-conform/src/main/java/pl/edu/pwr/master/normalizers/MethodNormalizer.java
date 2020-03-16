package pl.edu.pwr.master.normalizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodNormalizer extends BaseNormalizer {

    public static String mapPackageForMethod(String codeName) {
        String[] codeNameSplit = codeName.split("#"); // # is the method delimiter
        String packageNameWithClass = codeNameSplit[0];

        if (packageNameWithClass.contains(".")) {
            return extractPackageName(packageNameWithClass, 2);
        }
        else {
            return "";
        }
    }

    public static String mapOuterClassForMethod(String codeName) {
        String[] codeNameSplit = codeName.split("#"); // # is the method delimiter
        String packageNameWithClass = codeNameSplit[0];

        if (packageNameWithClass.contains(".")) {
            return extractOuterClass(packageNameWithClass, 2);
        }
        else {
            return "";
        }
    }

    public static String mapClassNameForMethod(String codeName) {
        String[] codeNameSplit = codeName.split("#"); // # is the method delimiter
        String packageNameWithClass = codeNameSplit[0];

        if (packageNameWithClass.contains(".")) {
            return extractClassName(packageNameWithClass);
        }
        else {
            return packageNameWithClass;
        }
    }

    public static String mapMethodNameForMethod(String codeName) {
        String[] codeNameSplit = codeName.split("#"); // # is the method delimiter
        String[] methodWithArgsSplit = codeNameSplit[1].split(" "); // ' ' is the delimiter between method name and args

        return methodWithArgsSplit[0];
    }

    public static List<String> mapParametersForMethod(String codeName) {
        String[] codeNameSplit = codeName.split("#"); // # is the method delimiter
        String[] methodWithArgsSplit = codeNameSplit[1].split(" "); // ' ' is the delimiter between method name and args

        List<String> arguments = new ArrayList<>();
        if (codeNameSplit[1].contains(" "))
            arguments = Arrays.asList(methodWithArgsSplit[1].split("\\|"));

        return arguments;
    }
}
