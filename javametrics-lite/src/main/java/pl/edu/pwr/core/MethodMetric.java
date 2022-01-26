package pl.edu.pwr.core;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.function.Function;

public interface MethodMetric<T> extends Function<MethodDeclaration, T> {
  T apply(MethodDeclaration m);
}
