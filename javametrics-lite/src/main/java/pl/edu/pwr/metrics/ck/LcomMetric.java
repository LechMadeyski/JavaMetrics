package pl.edu.pwr.metrics.ck;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.utils.Pair;
import pl.edu.pwr.core.ClassMetric;
import pl.edu.pwr.core.ClassMetricStrategy;
import pl.edu.pwr.core.model.Metric;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class for computing LCOM metric. LCOM - lack of cohesion in methods (the lack of cohesion in
 * methods is calculated by subtracting from the number of method pairs that do not share a field
 * access the number of method pairs that do)
 */
public class LcomMetric extends ClassMetricStrategy<Integer> {

  private static final String METRIC_NAME = "LCOM";

  @Override
  public List<Metric<Integer>> compute(CompilationUnit compilationUnit) {
    ClassMetric<Integer> lcom =
        c -> {
          List<String> classFieldNames = getAllClassFieldNames(c);
          Map<String, List<String>> fieldUsageMap = new HashMap<>();

          c.findAll(MethodDeclaration.class)
              .forEach(
                  m -> {
                    List<String> fieldAccessed =
                        m.findAll(FieldAccessExpr.class).stream()
                            .filter(fae -> classFieldNames.contains(fae.getNameAsString()))
                            .map(NodeWithSimpleName::getNameAsString)
                            .collect(Collectors.toList());

                    String methodName = m.getNameAsString();

                    if (fieldUsageMap.containsKey(methodName)) { // Inner classes with same names?
                      fieldUsageMap.get(methodName).addAll(fieldAccessed);
                    } else {
                      fieldUsageMap.put(methodName, fieldAccessed);
                    }
                  });

          Set<Pair<String, String>> pairs = new HashSet<>();
          Set<Pair<String, String>> nonPairs = new HashSet<>();

          for (String method : fieldUsageMap.keySet()) {
            fieldUsageMap.forEach(
                (k, v) -> {
                  if (isPair(fieldUsageMap.get(method), v)) {
                    Pair<String, String> p = new Pair<>(method, k);
                    if (!containsPair(pairs, p)) {
                      pairs.add(p);
                    }
                  } else {
                    Pair<String, String> p = new Pair<>(method, k);
                    if (!containsPair(nonPairs, p)) nonPairs.add(p);
                  }
                });
          }

          return nonPairs.size() - pairs.size();
        };

    return getMetricForClass(lcom, compilationUnit);
  }

  private boolean isPair(List<String> a, List<String> b) {
    for (String s : a) {
      if (b.contains(s)) return true;
    }
    return false;
  }

  private boolean containsPair(Set<Pair<String, String>> set, Pair<String, String> pair) {
    return set.stream()
        .anyMatch(
            p ->
                (p.a.equals(pair.a) && p.b.equals(pair.b))
                    || (p.a.equals(pair.b) && p.b.equals(pair.a)));
  }

  @Override
  public String getName() {
    return METRIC_NAME;
  }
}
