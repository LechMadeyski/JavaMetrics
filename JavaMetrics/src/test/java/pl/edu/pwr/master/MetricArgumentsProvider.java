package pl.edu.pwr.master;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MetricArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<MetricSource> {
    private String file;
    private String[] values;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(MetricTestInputParser.parse(file), values)
        );
    }

    @Override
    public void accept(MetricSource metricSource) {
        this.file = metricSource.file();
        this.values = metricSource.values();
    }

}
