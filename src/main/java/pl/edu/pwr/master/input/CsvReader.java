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
                String packageName = r.get(CsvHeaders.PACKAGE);
                String outerClass = r.get(CsvHeaders.OUTER_CLASS);
                String className = r.get(CsvHeaders.CLASS);
                String methodName = r.get(CsvHeaders.METHOD);
                List<String> parameters = Arrays.asList(r.get(CsvHeaders.PARAMETERS).split("\\|"));

                if (type.equals(ClassInput.CLASS_TYPE)) {
                    classes.add(new ClassInput(packageName, outerClass, className));
                } else if (type.equals(MethodInput.METHOD_TYPE) || type.equals(MethodInput.CONSTRUCTOR_TYPE)) {
                    methods.add(new MethodInput(packageName, outerClass, className, methodName, parameters));
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
