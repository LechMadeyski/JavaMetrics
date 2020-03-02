package pl.edu.pwr.master.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {

    private static final String CSV_INPUT_TEST_FILE = "CsvReaderTestInputFile.csv";
    private static List<ClassInput> classes;
    private static List<MethodInput> methods;

    @BeforeAll
    static void setUp() {
        String filePath = CsvReaderTest.class.getClassLoader().getResource(CSV_INPUT_TEST_FILE).getPath();
        Input input = CsvReader.getInputToParse(filePath);

        classes = input.getClasses();
        methods = input.getMethods();
    }

    @Test
    void getInputToParseSize() {
        assertEquals(1, classes.size());
        assertEquals(2, methods.size());
    }

    @Test
    void getInputToParseClass() {
        ClassInput c = classes.get(0);

        assertEquals("Class", c.getClassName());
        assertEquals("org.test.package", c.getPackagePath());
    }

    @Test
    void getInputToParseMethod() {
        MethodInput m = methods.get(0);

        assertEquals("Class", m.getClassName());
        assertEquals("org.test.package", m.getPackagePath());
        assertEquals("method", m.getMethodName());

        assertEquals(3, m.getArguments().size());

        for (int i = 0; i < m.getArguments().size(); i++) {
            assertEquals("arg" + (i + 1), m.getArguments().get(i));
        }
    }
}
