package pl.edu.pwr.filter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReaderTest {

  private static final String CSV_INPUT_TEST_FILE = "pl.edu.pwr/filter/ReaderTestInputFile.csv";
  private static List<ClassInput> classes;
  private static List<MethodInput> methods;
  private static List<ConstructorInput> constructors;

  @BeforeAll
  static void setUp() {
    String filePath =
        Objects.requireNonNull(ReaderTest.class.getClassLoader().getResource(CSV_INPUT_TEST_FILE))
            .getPath()
            .replace("%20", " ");
    Filter input = Reader.getFilter(Path.of(filePath));

    classes = new ArrayList<>(input.getClasses());
    methods = new ArrayList<>(input.getMethods());
    constructors = new ArrayList<>(input.getConstructors());
  }

  @Test
  void testGetInputToParseSize() {
    assertEquals(1, classes.size());
    assertEquals(1, methods.size());
    assertEquals(1, constructors.size());
  }

  @Test
  void testClassInput() {
    ClassInput c = classes.get(0);

    assertEquals("ClassA", c.getClassName());
    assertEquals("org.test", c.getPackage());
  }

  @Test
  void testMethodInput() {
    MethodInput m = methods.get(0);

    assertEquals("ClassA", m.getClassName());
    assertEquals("org.test", m.getPackage());
    assertEquals("method1", m.getMethodName());
  }

  @Test
  void testConstructorInput() {
    ConstructorInput m = constructors.get(0);

    assertEquals("ClassA", m.getClassName());
    assertEquals("org.test", m.getPackage());
  }
}
