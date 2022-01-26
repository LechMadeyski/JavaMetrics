package pl.edu.pwr.master;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.master.core.MetricParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class MetricTestInputParser {
    private static MetricParser metricParser = new MetricParser();

    private MetricTestInputParser() {

    }

    static CompilationUnit parse(String inputPath) throws ParseException, IOException, URISyntaxException {
        ClassLoader classLoader = MetricTestInputParser.class.getClassLoader();
        URL url = classLoader.getResource(inputPath);
        File file = new File(url.toURI());
        return metricParser.parseFile(file);
    }

    static List<String> splitArgumentValues(String commaSeparatedValues) {
        return Arrays.stream(commaSeparatedValues.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
