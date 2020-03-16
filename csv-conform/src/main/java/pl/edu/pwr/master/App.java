package pl.edu.pwr.master;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {

    public static void main(String[] args) {
        setLoggingLevel();
        if (args.length == 1) {
            List<Row> rows = CsvReader.readCsv(args[0]);
            rows = CsvNormalizer.cleanseCsv(rows);

            List<ConformedRow> conformedRows = CsvNormalizer.conformCsv(rows);
            CsvWriter.writeCsv(args[0], conformedRows);
        }
        else if (args.length == 2) {
            List<Row> rows = CsvReader.readCsv(args[0]);
            rows = CsvNormalizer.cleanseCsv(rows);

            List<ConformedRow> conformedRows = CsvNormalizer.conformCsv(rows);
            CsvWriter.writeCsv(args[1], conformedRows);
        }
        else {
            System.err.println("Usage: java -jar CsvConform <input_csv> [<output_csv>]");
            System.exit(1);
        }

        System.exit(0);
    }

    private static void setLoggingLevel() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.WARNING);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.WARNING);
        }
    }
}
