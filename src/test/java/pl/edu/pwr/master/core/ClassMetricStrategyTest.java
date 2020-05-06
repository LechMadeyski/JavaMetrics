package pl.edu.pwr.master.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import pl.edu.pwr.master.MetricSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ClassMetricStrategyTest {

    private static ClassMetricStrategy classMetricStrategy;

    @BeforeAll
    static void setUp() {
        classMetricStrategy = Mockito.mock(
                ClassMetricStrategy.class,
                Mockito.CALLS_REAL_METHODS
        );
    }

    @ParameterizedTest
    @MetricSource(
            file = "CommentTestInput.java",
            values =
                    {
                            "public int getSampleField() {\n" +
                                    "    return sampleField;\n" +
                                    "}",
                            "public String getAnotherSampleField() {\n" +
                                    "    return this.anotherSampleField;\n" +
                                    "}"
                    })
    void getClassAccessorMethods(CompilationUnit cu, String[] expected) {
        AtomicInteger index = new AtomicInteger(0);
        for (int i = 0; i < expected.length; i++) {
            expected[i] = expected[i].replace("\n", System.getProperty("line.separator"));
        }
        cu.findAll(ClassOrInterfaceDeclaration.class)
                .forEach(c -> {
                    index.set(0);
                    List<MethodDeclaration> methodDeclarations = classMetricStrategy.getClassAccessorMethods(c);
                    for (MethodDeclaration m : methodDeclarations) {
                        assertEquals(expected[index.get()], m.toString());
                        index.incrementAndGet();
                    }
                });
    }

    @ParameterizedTest
    @MetricSource(file = "CommentTestInput.java", values = {"sampleField", "anotherSampleField"})
    void getAllClassFieldNames(CompilationUnit cu, String[] expected) {
        List<String> fieldNames =
                classMetricStrategy.getAllClassFieldNames(
                        cu.findFirst(ClassOrInterfaceDeclaration.class)
                                .orElseThrow(() -> new IllegalArgumentException("Input file to this test does not contain a class!"))
                );
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], fieldNames.get(i));
        }
    }
}