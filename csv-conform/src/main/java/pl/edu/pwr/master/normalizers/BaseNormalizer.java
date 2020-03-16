package pl.edu.pwr.master.normalizers;

public class BaseNormalizer {

    protected static String extractPackageName(String codeName, int startIndexOffset) {
        String[] codeNameSplit = codeName.split("\\."); // . is the delimiter between elements in a package

        StringBuilder sb = new StringBuilder();
        boolean isInsideOuterClassChain = true;
        for (int i = codeNameSplit.length - startIndexOffset; i >= 0; i--) {
            if (! Character.isUpperCase(codeNameSplit[i].codePointAt(0))) {
                isInsideOuterClassChain = false;
                sb.insert(0, '.');
                sb.insert(0, codeNameSplit[i]);

            }
            else if (! isInsideOuterClassChain) {
                sb.insert(0, '.');
                sb.insert(0, codeNameSplit[i]);
            }
        }

        String packageName = sb.toString();
        if (! packageName.isBlank()) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }

        return packageName;
    }

    protected static String extractOuterClass(String codeName, int startIndexOffset) {
        String[] codeNameSplit = codeName.split("\\."); // . is the delimiter between elements in a package

        StringBuilder sb = new StringBuilder();
        for (int i = codeNameSplit.length - startIndexOffset; i >= 0; i--) {
            if (Character.isUpperCase(codeNameSplit[i].codePointAt(0))) {
                sb.insert(0, '.');
                sb.insert(0, codeNameSplit[i]);
            }
            else {
                break;
            }
        }

        String outerClassChain = sb.toString();
        if (! outerClassChain.isBlank()) {
            outerClassChain = outerClassChain.substring(0, outerClassChain.length() - 1);
        }

        return outerClassChain;
    }

    protected static String extractClassName(String codeName) {
        String[] codeNameSplit = codeName.split("\\."); // . is the delimiter between elements in a package
        return codeNameSplit[codeNameSplit.length - 1];
    }
}
