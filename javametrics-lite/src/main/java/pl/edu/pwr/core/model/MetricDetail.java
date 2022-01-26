package pl.edu.pwr.core.model;

import java.util.*;

public class MetricDetail {
  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final String outerClassName;
  private final String accessModifier;
  private final Boolean isStatic;
  private final Boolean isFinal;

  private MetricDetail(
      String packageName,
      String className,
      String methodSignature,
      String outerClassName,
      String accessModifier,
      Boolean isStatic,
      Boolean isFinal) {
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.outerClassName = outerClassName;
    this.accessModifier = accessModifier;
    this.isStatic = isStatic;
    this.isFinal = isFinal;
  }

  public Optional<String> getPackageName() {
    return Optional.ofNullable(packageName);
  }

  public Optional<String> getClassName() {
    return Optional.ofNullable(className);
  }

  public Optional<String> getMethodSignature() {
    return Optional.ofNullable(methodSignature);
  }

  public Optional<String> getOuterClassName() {
    return Optional.ofNullable(outerClassName);
  }

  public Optional<String> getAccessModifier() {
    return Optional.ofNullable(accessModifier);
  }

  public Optional<String> getMethodName() {
    Optional<String> methodSignatureOpt = getMethodSignature();
    if (methodSignatureOpt.isEmpty()) return Optional.empty();

    String methodSignatureStr = methodSignatureOpt.get();
    return Optional.of(methodSignatureStr.split("\\(")[0]);
  }

  public List<String> getMethodArguments() {
    List<String> output = new ArrayList<>();
    Optional<String> methodSignatureOpt = getMethodSignature();
    if (methodSignatureOpt.isEmpty()) return output;

    String methodSignatureStr = methodSignatureOpt.get();
    String methodArguments = methodSignatureStr.split("\\(")[1];
    methodArguments = methodArguments.replace(")", "");
    methodArguments = methodArguments.replace(" ", "");

    return Arrays.asList(methodArguments.split(","));
  }

  public Boolean getStatic() {
    return isStatic;
  }

  public Boolean getFinal() {
    return isFinal;
  }

  public static class MetricDetailBuilder {
    private String packageName;
    private String className;
    private String methodSignature;
    private String outerClassName;
    private String accessModifier;
    private Boolean isStatic = false;
    private Boolean isFinal = false;

    public MetricDetail build() {
      return new MetricDetail(
          packageName,
          className,
          methodSignature,
          outerClassName,
          accessModifier,
          isStatic,
          isFinal);
    }

    public MetricDetailBuilder setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public MetricDetailBuilder setClassName(String className) {
      this.className = className;
      return this;
    }

    public MetricDetailBuilder setMethodSignature(String methodSignature) {
      this.methodSignature = methodSignature;
      return this;
    }

    public MetricDetailBuilder setOuterClassName(String outerClassName) {
      this.outerClassName = outerClassName;
      return this;
    }

    public MetricDetailBuilder setAccessModifier(String accessModifier) {
      this.accessModifier = accessModifier;
      return this;
    }

    public MetricDetailBuilder setStatic(Boolean aStatic) {
      isStatic = aStatic;
      return this;
    }

    public MetricDetailBuilder setFinal(Boolean aFinal) {
      isFinal = aFinal;
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MetricDetail that = (MetricDetail) o;
    return Objects.equals(packageName, that.packageName)
        && Objects.equals(className, that.className)
        && Objects.equals(methodSignature, that.methodSignature)
        && Objects.equals(outerClassName, that.outerClassName)
        && Objects.equals(accessModifier, that.accessModifier)
        && Objects.equals(isStatic, that.isStatic)
        && Objects.equals(isFinal, that.isFinal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        packageName, className, methodSignature, outerClassName, accessModifier, isStatic, isFinal);
  }
}
