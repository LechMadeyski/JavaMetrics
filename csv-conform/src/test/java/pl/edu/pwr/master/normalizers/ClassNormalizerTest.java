package pl.edu.pwr.master.normalizers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.pwr.master.CsvNormalizer;
import pl.edu.pwr.master.Row;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassNormalizerTest {

    private static Row classUUT;
    private static Row classWithOuterClassUUT;
    private static Row classWithOuterClassChainUUT;
    private static Row classWithGenericUUT;
    private static Row noPackageClassUUT;
    private static Row classWithStaticInnerClassUUT;

    private static List<Row> rowsUUT;

    @BeforeAll
    static void setUp() {
        classUUT = new Row(1, "class", "org.test.package.ClassName", "test@github.com", "1EC2", "https://test.com");
        classWithOuterClassUUT = new Row(2, "class", "org.test.package.OuterClass1.ClassName", "test@github.com", "1EC3", "https://test.com");
        classWithOuterClassChainUUT = new Row(3, "class", "org.test.package.OuterClass1.OuterClass2.ClassName", "test@github.com", "1EC4", "https://test.com");
        classWithGenericUUT = new Row(4, "class", "org.test.package.OuterClass1.OuterClass2.ClassName<T>", "test@github.com", "1EC4", "https://test.com");
        noPackageClassUUT = new Row(9, "class", "ClassName", "test@github.com", "1EC2", "https://test.com");
        classWithStaticInnerClassUUT = new Row(10, "class", "Foo.Mumble", "test@github.com", "1EC2", "https://test.com");

        rowsUUT = Arrays.asList(classUUT, classWithOuterClassUUT, classWithOuterClassChainUUT, classWithGenericUUT);
        rowsUUT = CsvNormalizer.cleanseCsv(rowsUUT);
    }

    @Test
    void mapPackageForClassTest() {
        for (Row r : rowsUUT) {
            String codeName = r.getCodeName();
            assertEquals("org.test.package", ClassNormalizer.mapPackageForClass(codeName));
        }
    }

    @Test
    void mapClassNameForClassTest() {
        for (Row r : rowsUUT) {
            String codeName = r.getCodeName();
            assertEquals("ClassName", ClassNormalizer.mapClassNameForClass(codeName));
        }
    }

    @Test
    void mapPackageForClassNoPackageTest() {
        String codeName = noPackageClassUUT.getCodeName();
        assertEquals("", ClassNormalizer.mapPackageForClass(codeName));
    }

    @Test
    void mapPackageForClassWithStaticInnerClassTest() {
        String codeName = classWithStaticInnerClassUUT.getCodeName();
        assertEquals("", ClassNormalizer.mapPackageForClass(codeName));
    }

    @Test
    void mapClassNameForClassNoPackageTest() {
        String codeName = noPackageClassUUT.getCodeName();
        assertEquals("ClassName", ClassNormalizer.mapClassNameForClass(codeName));
    }

    @Test
    void maClassNameForClassWithStaticInnerClassTest() {
        String codeName = classWithStaticInnerClassUUT.getCodeName();
        assertEquals("Mumble", ClassNormalizer.mapClassNameForClass(codeName));
    }

    @Test
    void mapOuterClassForClassSingleOuterClassTest() {
        String codeName = classWithOuterClassUUT.getCodeName();
        assertEquals("OuterClass1", ClassNormalizer.mapOuterClassForClass(codeName));
    }

    @Test
    void mapOuterClassForClassChainOuterClassTest() {
        String codeName = classWithOuterClassChainUUT.getCodeName();
        assertEquals("OuterClass1.OuterClass2", ClassNormalizer.mapOuterClassForClass(codeName));
    }

    @Test
    void mapOuterClassForClassNoOuterClassTest() {
        String codeName = classUUT.getCodeName();
        assertEquals("", ClassNormalizer.mapOuterClassForClass(codeName));
    }

    @Test
    void maOuterClassForClassWithStaticInnerClassTest() {
        String codeName = classWithStaticInnerClassUUT.getCodeName();
        assertEquals("Foo", ClassNormalizer.mapOuterClassForClass(codeName));
    }

}
