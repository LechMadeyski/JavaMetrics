package pl.edu.pwr.core;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.function.Function;

@FunctionalInterface
public interface ClassMetric<T> extends Function<ClassOrInterfaceDeclaration, T> {
  T apply(ClassOrInterfaceDeclaration c);
}
