package pl.edu.pwr.master.normalizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstructorNormalizer extends BaseNormalizer {

    public static String mapPackageForConstructor(String codeName) {
        String[] constructorSplit = codeName.split(" "); // ' ' is the constructor delimiter

        if (existsPackageInConstructor(constructorSplit[0])) {
            return extractPackageName(constructorSplit[0], 3);
        }
        else {
            return "";
        }
    }

    public static String mapOuterClassForConstructor(String codeName) {
        String[] constructorSplit = codeName.split(" "); // ' ' is the constructor delimiter

        if (existsPackageInConstructor(constructorSplit[0])) {
            return extractOuterClass(constructorSplit[0], 3);
        }
        else {
            return "";
        }
    }

    public static String mapClassNameForConstructor(String codeName) {
        String[] constructorSplit = codeName.split(" ");

        if (existsPackageInConstructor(constructorSplit[0])) {
            String[] codeNameSplit = constructorSplit[0].split("\\."); // . is the delimiter between elements in a package
            return codeNameSplit[codeNameSplit.length - 2];
        }
        else {
            String[] codeNameSplit = constructorSplit[0].split("\\."); // . is the delimiter between elements in a package
            return codeNameSplit[0];
        }
    }

    public static String mapMethodNameForConstructor(String codeName) {
        String[] constructorSplit = codeName.split(" ");
        if (existsPackageInConstructor(constructorSplit[0])) {
            String[] codeNameSplit = constructorSplit[0].split("\\."); // . is the delimiter between elements in a package

            return codeNameSplit[codeNameSplit.length - 1];
        }
        else {
            String[] codeNameSplit = constructorSplit[0].split("\\.");
            return codeNameSplit[1];
        }
    }

    public static List<String> mapParametersForConstructor(String codeName) {
        String[] constructorSplit = codeName.split(" ");

        List<String> arguments = new ArrayList<>();
        if (codeName.contains(" "))
            arguments = Arrays.asList(constructorSplit[1].split("\\|"));

        return arguments;
    }

    private static boolean existsPackageInConstructor(String codeName) {
        return (codeName.length() - codeName.replace(".", "").length()) > 1;
    }
}
