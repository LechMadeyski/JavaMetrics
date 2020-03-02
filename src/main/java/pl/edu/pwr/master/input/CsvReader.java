package pl.edu.pwr.master.input;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for reading input CSV to the metrics tool.
 */
public class CsvReader {

    private static final Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

    private CsvReader() {
    }

    /**
     * Gets input from the CSV file to be parsed. Looks for an input in columns 'type' and 'code_name'.
     *
     * @param inputPath path to the CSV file
     * @return Methods and classes to be parsed
     */
    public static Input getInputToParse(String inputPath) {
        Set<ClassInput> classes = new LinkedHashSet<>();
        Set<MethodInput> methods = new LinkedHashSet<>();

        try (Reader in = new FileReader(inputPath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(CsvHeaders.class).withSkipHeaderRecord().parse(in);

            records.forEach(r -> {
                String type = r.get(CsvHeaders.TYPE);
                String codeName = r.get(CsvHeaders.CODE_NAME);

                if (LOGGER.isLoggable(Level.INFO))
                    LOGGER.info("Parsing " + r);

                if (type.equals(ClassInput.CLASS_TYPE)) {

                    String packageName = "";
                    String className = "";
                    if (codeName.contains(".")) {
                        packageName = extractPackageName(codeName);
                        className = extractClassName(codeName);
                    } else {
                        className = codeName;
                    }
                    classes.add(new ClassInput(packageName, className));
                } else if (type.equals(MethodInput.METHOD_TYPE)) {
                    if (codeName.contains("#")) { // logic for methods
                        String[] codeNameSplit = codeName.split("#"); // # is the method delimiter
                        String packageNameWithClass = codeNameSplit[0];

                        String packageName = "";
                        String className = "";

                        if (existsPackageInMethod(packageNameWithClass)) {
                            packageName = extractPackageName(packageNameWithClass);
                            className = extractClassName(packageNameWithClass);
                        } else {
                            className = packageNameWithClass;
                        }

                        String[] methodWithArgsSplit = codeNameSplit[1].split(" "); // ' ' is the delimiter between method name and args

                        String methodName = methodWithArgsSplit[0];

                        List<String> arguments = new ArrayList<>();
                        if (codeNameSplit[1].contains(" "))
                            arguments = Arrays.asList(methodWithArgsSplit[1].split("\\|"));

                        methods.add(new MethodInput(packageName, className, methodName, arguments));
                    } else { // logic for constructors
                        String[] constructorSplit = codeName.split(" ");
                        String packageName = "";
                        String className = "";
                        String constructorName = "";

                        if (existsPackageInConstructor(constructorSplit[0])) {
                            String[] codeNameSplit = constructorSplit[0].split("\\."); // . is the delimiter between elements in a package
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < codeNameSplit.length - 2; i++) {
                                sb.append(codeNameSplit[i]);
                                sb.append('.');
                            }

                            packageName = sb.toString();
                            packageName = packageName.substring(0, packageName.length() - 1);

                            className = codeNameSplit[codeNameSplit.length - 2];
                            constructorName = codeNameSplit[codeNameSplit.length - 1];
                        } else {
                            String[] codeNameSplit = constructorSplit[0].split("\\.");
                            className = codeNameSplit[0];
                            constructorName = codeNameSplit[1];
                        }
                        List<String> arguments = new ArrayList<>();
                        if (codeName.contains(" "))
                            arguments = Arrays.asList(constructorSplit[1].split("\\|"));

                        methods.add(new MethodInput(packageName, className, constructorName, arguments));
                    }
                } else {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Found unknown value " + type + " in column 'type'!");
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Input(new ArrayList<>(classes), new ArrayList<>(methods));
    }

    private static String extractPackageName(String codeName) {
        String[] codeNameSplit = codeName.split("\\."); // . is the delimiter between elements in a package

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeNameSplit.length - 1; i++) {
            sb.append(codeNameSplit[i]);
            sb.append('.');
        }

        String packageName = sb.toString();
        packageName = packageName.substring(0, packageName.length() - 1);

        return packageName;
    }

    private static String extractClassName(String codeName) {
        String[] codeNameSplit = codeName.split("\\."); // . is the delimiter between elements in a package
        return codeNameSplit[codeNameSplit.length - 1];
    }

    private static boolean existsPackageInMethod(String codeName) {
        return codeName.contains(".");
    }

    private static boolean existsPackageInConstructor(String codeName) {
        return (codeName.length() - codeName.replace(".", "").length()) > 1;
    }

    /**
     * Gets repositories from the CSV file with hashes. Hashes are deduplicated.
     *
     * @param inputPath path to the CSV file
     * @return Map of repositories and set of hashes related to the repository
     */
    public static Map<String, Set<String>> getRepositories(String inputPath) {
        Map<String, Set<String>> output = new HashMap<>();
        try (Reader in = new FileReader(inputPath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(CsvHeaders.class).parse(in);
            for (CSVRecord record : records) {
                String repository = record.get(CsvHeaders.REPOSITORY);
                String hash = record.get(CsvHeaders.COMMIT_HASH);

                if (output.containsKey(repository))
                    output.get(repository).add(hash);
                else
                    output.put(repository, new HashSet<>());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return output;
    }
}
