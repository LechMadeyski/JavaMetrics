package pl.edu.pwr.master;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<Row> readCsv(String filename) {
        List<Row> rows = new ArrayList<>();
        try(Reader in = new FileReader(filename)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(RowHeaders.class).withSkipHeaderRecord().withDelimiter(';').parse(in);
            for (CSVRecord record : records) {
                Integer id = Integer.parseInt(record.get(RowHeaders.ID));
                String type = record.get(RowHeaders.TYPE);
                String codeName = record.get(RowHeaders.CODE_NAME);
                String repository = record.get(RowHeaders.REPOSITORY);
                String commitHash = record.get(RowHeaders.COMMIT_HASH);
                String link = record.get(RowHeaders.LINK);

                rows.add(new Row(id, type, codeName, repository, commitHash, link));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return rows;
    }
}
