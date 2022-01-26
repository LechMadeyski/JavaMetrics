package pl.edu.pwr.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import pl.edu.pwr.core.model.Metric;

import java.util.List;
import java.util.Optional;

public interface MetricStrategy<T> {
  List<Metric<T>> compute(CompilationUnit compilationUnit);

  String getName();

  default Optional<String> getPackageName(CompilationUnit compilationUnit) {
    return compilationUnit.getPackageDeclaration().map(PackageDeclaration::getNameAsString);
  }

  default Optional<ClassOrInterfaceDeclaration> getParentClass(Node node) {
    return node.getParentNode()
        .filter(p -> p instanceof ClassOrInterfaceDeclaration)
        .flatMap(p -> Optional.of((ClassOrInterfaceDeclaration) p));
  }
}
