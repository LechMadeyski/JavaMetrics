package pl.edu.pwr.master;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvNormalizerTest {

    private static Row classWithGenericUUT;
    private static Row methodArgsGenericUUT;

    private static List<Row> csvMock;

    @BeforeAll
    static void setUp() {
        classWithGenericUUT = new Row(1, "class", "org.test.package.OuterClass1.OuterClass2.ClassName<T>", "test@github.com", "1EC4", "https://test.com");
        methodArgsGenericUUT = new Row(2, "function", "org.test.package.ClassName#methodName arg1<Generic>|arg2|arg3<String, Integer>", "test@github.com", "1EC2", "https://test.com");

        csvMock = new ArrayList<>();
        csvMock.add(classWithGenericUUT);
        csvMock.add(methodArgsGenericUUT);
    }

    @Test
    void cleanseCsvTest() {
        List<Row> csvCleansed = CsvNormalizer.cleanseCsv(csvMock);

        assertEquals(2, csvCleansed.size());
        assertEquals("org.test.package.OuterClass1.OuterClass2.ClassName", csvCleansed.get(0).getCodeName());
        assertEquals("org.test.package.ClassName#methodName arg1|arg2|arg3", csvCleansed.get(1).getCodeName());
    }
}
