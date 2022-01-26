package pl.edu.pwr.core;

import com.github.javaparser.ast.body.ConstructorDeclaration;

import java.util.function.Function;

public interface ConstructorMetric<T> extends Function<ConstructorDeclaration, T> {
  T apply(ConstructorDeclaration c);
}
