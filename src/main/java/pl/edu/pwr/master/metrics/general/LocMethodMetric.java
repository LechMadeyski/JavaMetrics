package pl.edu.pwr.master.metrics.general;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.master.config.Constants;
import pl.edu.pwr.master.core.ConstructorMetric;
import pl.edu.pwr.master.core.MethodMetric;
import pl.edu.pwr.master.core.MethodMetricStrategy;
import pl.edu.pwr.master.core.model.Metric;

import java.util.List;

/**
 * Class for computing LOC metric for a method or as constructor.
 * LOC - lines of code
 */
public class LocMethodMetric extends MethodMetricStrategy<Integer> {

    private static final String METRIC_NAME = "LOC_M";

    @Override
    public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
        MethodMetric<Integer> locMethod = m ->
                m.getBody().map(body -> body.toString().split(Constants.NEW_LINE_REGEX).length).orElse(0);

        ConstructorMetric<Integer> locConstructor = c ->
                c.getBody().toString().split(Constants.NEW_LINE_REGEX).length;

        return getMetricForCallable(locMethod, locConstructor, compilationUnit);
    }

    @Override
    public String getName() {
        return METRIC_NAME;
    }
}
