package pl.edu.pwr.master.metrics.martin;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.master.core.ClassMetric;
import pl.edu.pwr.master.core.ClassMetricStrategy;
import pl.edu.pwr.master.core.model.Metric;

import java.util.List;

/**
 * Class for computing Ca metric.
 * Ca (Afferent coupling) - number of classes that depend upon the measured class
 */
public class CaMetric extends ClassMetricStrategy<Integer> {

    private static final String METRIC_NAME = "CA";

    @Override
    public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
        ClassMetric<Integer> ce = c -> getAfferentCouplingClasses(c).size();
        return getMetricForClass(ce, compilationUnit);
    }

    @Override
    public String getName() {
        return METRIC_NAME;
    }
}
