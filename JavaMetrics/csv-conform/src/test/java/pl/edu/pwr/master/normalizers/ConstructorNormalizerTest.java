package pl.edu.pwr.master.normalizers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.master.CsvNormalizer;
import pl.edu.pwr.master.Row;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorNormalizerTest {

    private static Row constructorNoArgsUUT;
    private static Row constructorArgsUUT;
    private static Row noPackageConstructorUUT;

    private static List<Row> rowsUUT;

    @BeforeAll
    static void setUp() {
        constructorNoArgsUUT = new Row(1, "function", "org.test.package.ClassName.ClassName", "test@github.com", "1EC2", "https://test.com");
        constructorArgsUUT = new Row(2, "function", "org.test.package.OuterClass.ClassName.ClassName arg1", "test@github.com", "1EC2", "https://test.com");
        noPackageConstructorUUT = new Row(3, "function", "ClassName.ClassName", "test@github.com", "1EC2", "https://test.com");

        rowsUUT = Arrays.asList(constructorArgsUUT, constructorNoArgsUUT);
        rowsUUT = CsvNormalizer.cleanseCsv(rowsUUT);
    }

    @Test
    void mapPackageForConstructorTest() {
        for (Row r : rowsUUT) {
            String codeName = r.getCodeName();
            assertEquals("org.test.package", ConstructorNormalizer.mapPackageForConstructor(codeName));
        }
    }

    @Test
    void mapClassNameForConstructorTest() {
        for (Row r : rowsUUT) {
            String codeName = r.getCodeName();
            assertEquals("ClassName", ConstructorNormalizer.mapClassNameForConstructor(codeName));
        }
    }

    @Test
    void mapPackageForConstructorNoPackageTest() {
        String codeName = noPackageConstructorUUT.getCodeName();
        assertEquals("", ConstructorNormalizer.mapPackageForConstructor(codeName));
    }

    @Test
    void mapClassNameForConstructorNoPackageTest() {
        String codeName = noPackageConstructorUUT.getCodeName();
        assertEquals("ClassName", ConstructorNormalizer.mapClassNameForConstructor(codeName));
    }

    @Test
    void mapOuterClassForConstructorNoPackageTest() {
        String codeName = noPackageConstructorUUT.getCodeName();
        assertEquals("", ConstructorNormalizer.mapOuterClassForConstructor(codeName));
    }

    @Test
    void mapOuterClassForConstructorNoOuterClassTest() {
        String codeName = constructorNoArgsUUT.getCodeName();
        assertEquals("", ConstructorNormalizer.mapOuterClassForConstructor(codeName));
    }

    @Test
    void mapOuterClassForConstructorSingleOuterClassTest() {
        String codeName = constructorArgsUUT.getCodeName();
        assertEquals("OuterClass", ConstructorNormalizer.mapOuterClassForConstructor(codeName));
    }

    @Test
    void mapMethodNameForConstructorTest() {
        for (Row r : rowsUUT) {
            String codeName = r.getCodeName();
            assertEquals("ClassName", ConstructorNormalizer.mapMethodNameForConstructor(codeName));
        }
    }

    @Test
    void mapParametersForConstructorNoArgsTest() {
        String codeName = constructorNoArgsUUT.getCodeName();
        assertEquals(0, ConstructorNormalizer.mapParametersForConstructor(codeName).size());
    }

    @Test
    void mapParametersForConstructorArgsTest() {
        String codeName = constructorArgsUUT.getCodeName();
        List<String> params = ConstructorNormalizer.mapParametersForConstructor(codeName);

        assertEquals(1, params.size());

        assertEquals("arg1", params.get(0));
    }
}
