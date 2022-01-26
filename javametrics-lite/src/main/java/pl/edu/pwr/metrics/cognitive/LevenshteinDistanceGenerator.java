package pl.edu.pwr.metrics.cognitive;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.utils.Pair;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevenshteinDistanceGenerator {

  private LevenshteinDistance ld;

  public LevenshteinDistanceGenerator() {
    this.ld = new LevenshteinDistance();
  }

  public Integer countLevenshteinPairs(List<VariableDeclarator> vars) {
    Set<Pair<String, String>> pairs = createPairs(vars);

    return (int) pairs.stream().filter(p -> ld.apply(p.a, p.b) <= 2).count();
  }

  private Set<Pair<String, String>> createPairs(List<VariableDeclarator> vars) {
    Set<Pair<String, String>> varPairs = new HashSet<>((vars.size() * (vars.size() - 1)) / 2);
    for (int i = 0; i < vars.size(); i++) {
      for (int j = i + 1; j < vars.size(); j++) {
        varPairs.add(new Pair<>(vars.get(i).getNameAsString(), vars.get(j).getNameAsString()));
      }
    }

    return varPairs;
  }
}
