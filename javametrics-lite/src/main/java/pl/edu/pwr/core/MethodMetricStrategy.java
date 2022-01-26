package pl.edu.pwr.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.core.model.MetricDetail;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract class that should be extended to compute metrics for a method or a constructor.
 *
 * @param <T> generic type of class metric return type
 */
public abstract class MethodMetricStrategy<T> implements MetricStrategy<T> {

  /**
   * Get list of metrics for a method
   *
   * @param mm method metric
   * @param compilationUnit compilation unit
   * @return list of metrics for a method
   */
  protected List<Metric<T>> getMetricForMethod(
      MethodMetric<T> mm, CompilationUnit compilationUnit) {
    return deriveMethodMetrics(mm, compilationUnit);
  }

  /**
   * Get list of metrics for a constructor
   *
   * @param cc constructor metric
   * @param compilationUnit compilation unit
   * @return list of metrics for a constructor
   */
  protected List<Metric<T>> getMetricForConstructor(
      ConstructorMetric<T> cc, CompilationUnit compilationUnit) {
    return deriveConstructorMetrics(cc, compilationUnit);
  }

  /**
   * Get list of metrics for a callable (method or constructor)
   *
   * @param mm method metric
   * @param cc constructor metric
   * @param compilationUnit compilation unit
   * @return list of metrics for a callable
   */
  protected List<Metric<T>> getMetricForCallable(
      MethodMetric<T> mm, ConstructorMetric<T> cc, CompilationUnit compilationUnit) {
    List<Metric<T>> output = deriveMethodMetrics(mm, compilationUnit);
    output.addAll(deriveConstructorMetrics(cc, compilationUnit));
    return output;
  }

  private List<Metric<T>> deriveConstructorMetrics(
      ConstructorMetric<T> cc, CompilationUnit compilationUnit) {
    String packageName = getPackageName(compilationUnit).orElse("");

    return compilationUnit.findAll(ConstructorDeclaration.class).stream()
        .filter(c -> getClassName(c).isPresent())
        .map(
            c -> {
              MetricDetail metricDetail = prepareMetricDetail(packageName, c);

              return new Metric<>(this.getName(), cc.apply(c), metricDetail);
            })
        .collect(Collectors.toList());
  }

  private List<Metric<T>> deriveMethodMetrics(MethodMetric<T> mm, CompilationUnit compilationUnit) {
    String packageName = getPackageName(compilationUnit).orElse("");

    return compilationUnit.findAll(MethodDeclaration.class).stream()
        .filter(m -> getClassName(m).isPresent())
        .map(
            m -> {
              MetricDetail metricDetail = prepareMetricDetail(packageName, m);

              return new Metric<>(this.getName(), mm.apply(m), metricDetail);
            })
        .collect(Collectors.toList());
  }

  private MetricDetail prepareMetricDetail(String packageName, CallableDeclaration c) {
    return new MetricDetail.MetricDetailBuilder()
        .setPackageName(packageName)
        .setClassName(getClassName(c).orElse(""))
        .setOuterClassName(getOuterClassName(c).orElse(""))
        .setAccessModifier(c.getAccessSpecifier().asString())
        .setMethodSignature(c.getSignature().asString())
        .setStatic(c.isStatic())
        .setFinal(c.isFinal())
        .build();
  }

  /**
   * Get name of a class that contains a method/constructor
   *
   * @param n callable declaration
   * @return name of containing class
   */
  protected Optional<String> getClassName(CallableDeclaration n) {
    return getParentClass(n).map(NodeWithSimpleName::getNameAsString);
  }

  /**
   * Get name of outer class name containing a given method/constructor
   *
   * @param m callable declaration
   * @return name of containing outer class
   */
  private Optional<String> getOuterClassName(CallableDeclaration m) {
    return getParentClass(m).flatMap(this::getParentClass).map(NodeWithSimpleName::getNameAsString);
  }
}
