package pl.edu.pwr.master.normalizers;

public class ClassNormalizer extends BaseNormalizer {

    public static String mapPackageForClass(String codeName) {
        if (codeName.contains(".")) {
            return extractPackageName(codeName, 2);
        }
        else {
            return "";
        }
    }

    public static String mapOuterClassForClass(String codeName) {
        if (codeName.contains(".")) {
            return extractOuterClass(codeName, 2);
        }
        else {
            return "";
        }
    }

    public static String mapClassNameForClass(String codeName) {
        if (codeName.contains(".")) {
            return extractClassName(codeName);
        }
        else {
            return codeName;
        }
    }
}
