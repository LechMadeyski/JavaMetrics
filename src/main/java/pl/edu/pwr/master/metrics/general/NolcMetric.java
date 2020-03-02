package pl.edu.pwr.master.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.LambdaExpr;
import pl.edu.pwr.master.core.ClassMetric;
import pl.edu.pwr.master.core.ClassMetricStrategy;
import pl.edu.pwr.master.core.model.Metric;

import java.util.List;

/**
 * NOL_C - Number Of Lambda expressions (Java 8 feature) in Class
 */
public class NolcMetric extends ClassMetricStrategy<Integer> {

    private static final String METRIC_NAME = "NOL_C";

    @Override
    public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
        ClassMetric<Integer> nol = c -> c.findAll(LambdaExpr.class)
                .stream()
                .mapToInt(md -> 1)
                .sum();

        return getMetricForClass(nol, compilationUnit);
    }

    @Override
    public String getName() {
        return METRIC_NAME;
    }
}
