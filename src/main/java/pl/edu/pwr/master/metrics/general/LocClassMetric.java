package pl.edu.pwr.master.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.master.config.Constants;
import pl.edu.pwr.master.core.ClassMetric;
import pl.edu.pwr.master.core.ClassMetricStrategy;
import pl.edu.pwr.master.core.model.Metric;

import java.util.List;

/**
 * Class for computing LOC metric for a class.
 * LOC - lines of code
 * Code of inner classes are counted as a code of enclosing class! No distinct entity for an inner class.
 */
public class LocClassMetric extends ClassMetricStrategy<Integer> {

    private static final String METRIC_NAME = "LOC_C";

    @Override
    public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
        ClassMetric<Integer> loc = c -> c.toString().split(Constants.NEW_LINE_REGEX).length;

        return getMetricForClass(loc, compilationUnit);
    }

    @Override
    public String getName() {
        return METRIC_NAME;
    }
}
