package pl.edu.pwr.master;

import pl.edu.pwr.master.normalizers.ClassNormalizer;
import pl.edu.pwr.master.normalizers.ConstructorNormalizer;
import pl.edu.pwr.master.normalizers.MethodNormalizer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CsvNormalizer {

    private static final Logger LOGGER = Logger.getLogger(CsvNormalizer.class.getName());


    public static List<Row> cleanseCsv(List<Row> rows) {
        return rows.stream()
                .filter(row -> {
                    if (isEmptyField(row.getType())) {
                        LOGGER.warning("Found empty type in " + row.toString());
                        return false;
                    }
                    else if (isEmptyField(row.getCodeName())) {
                        LOGGER.warning("Found empty code_name in " + row.toString());
                        return false;
                    }
                    return true;
                })
                .map(row -> {
                    if (row.getCodeName().contains("<") && row.getCodeName().contains(">")) {
                        LOGGER.warning("Found generic type usage (<...>): " + row.getCodeName() + " in " + row.toString() + ". Cleansing...");
                    }
                    String codeName = row.getCodeName().replaceAll("<.*?>", "");
                    row.setCodeName(codeName);
                    return row;
                })
                .collect(Collectors.toList());
    }

    private static boolean isEmptyField(String field) {
        return field.isBlank() || field.isEmpty();
    }

    public static List<ConformedRow> conformCsv(List<Row> rows) {
        return rows.stream()
                .map(row -> {
                    ConformedRow c = new ConformedRow();

                    if (row.getType().equals(Row.CLASS_TYPE)) {
                        c.setType(ConformedRow.CLASS_TYPE);
                        c.setPackageName(ClassNormalizer.mapPackageForClass(row.getCodeName()));
                        c.setOuterClassName(ClassNormalizer.mapOuterClassForClass(row.getCodeName()));
                        c.setClassName(ClassNormalizer.mapClassNameForClass(row.getCodeName()));
                    }
                    else if (row.getType().equals(Row.METHOD_TYPE)) {
                        if (row.getCodeName().contains("#")) { // logic for methods
                            c.setType(ConformedRow.METHOD_TYPE);
                            c.setPackageName(MethodNormalizer.mapPackageForMethod(row.getCodeName()));
                            c.setOuterClassName(MethodNormalizer.mapOuterClassForMethod(row.getCodeName()));
                            c.setClassName(MethodNormalizer.mapClassNameForMethod(row.getCodeName()));

                            c.setMethodName(MethodNormalizer.mapMethodNameForMethod(row.getCodeName()));
                            c.setParameters(MethodNormalizer.mapParametersForMethod(row.getCodeName()));
                        }
                        else { // logic for constructors
                            c.setType(ConformedRow.CONSTRUCTOR_TYPE);
                            c.setPackageName(ConstructorNormalizer.mapPackageForConstructor(row.getCodeName()));
                            c.setOuterClassName(ConstructorNormalizer.mapOuterClassForConstructor(row.getCodeName()));
                            c.setClassName(ConstructorNormalizer.mapClassNameForConstructor(row.getCodeName()));

                            c.setMethodName(ConstructorNormalizer.mapMethodNameForConstructor(row.getCodeName()));
                            c.setParameters(ConstructorNormalizer.mapParametersForConstructor(row.getCodeName()));
                        }
                    }
                    else {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning("Unknown type " + c.getType() + " for row: " + row.toString());
                        }
                    }

                    c.setRepository(row.getRepository());
                    c.setCommitHash(row.getCommitHash());
                    c.setGitSourceFileUrl(row.getLink());

                    return c;
                })
                .collect(Collectors.toList());
    }
}
