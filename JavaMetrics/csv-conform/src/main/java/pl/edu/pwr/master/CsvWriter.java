package pl.edu.pwr.master;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {

    public static void writeCsv(String filename, List<ConformedRow> rows) {
        try(FileWriter out = new FileWriter(filename)) {
            CSVPrinter printer = CSVFormat.DEFAULT
                    .withHeader(ConformedRowHeaders.class).print(out);

            for (ConformedRow r : rows) {
                String parameters = "";
                if (r.getType().equals(ConformedRow.METHOD_TYPE) || r.getType().equals(ConformedRow.CONSTRUCTOR_TYPE)) {
                    parameters = String.join("|", r.getParameters());
                }

                printer.printRecord(
                        r.getType(),
                        r.getPackageName(),
                        r.getOuterClassName(),
                        r.getClassName(),
                        r.getMethodName(),
                        parameters,
                        r.getRepository(),
                        r.getCommitHash(),
                        r.getGitSourceFileUrl()
                );
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
