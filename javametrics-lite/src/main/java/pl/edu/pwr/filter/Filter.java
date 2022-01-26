package pl.edu.pwr.filter;

import de.siegmar.fastcsv.reader.NamedCsvRow;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.core.model.MetricDetail;

import java.util.Set;

public class Filter {

  protected enum Type {
    CLASS,
    METHOD,
    CONSTRUCTOR,
    UNKNOWN
  }

  private Set<String> filePaths;
  private Set<ClassInput> classes;
  private Set<MethodInput> methods;
  private Set<ConstructorInput> constructors;

  public Filter(
      Set<String> filePaths,
      Set<ClassInput> classes,
      Set<MethodInput> methods,
      Set<ConstructorInput> constructors) {
    this.filePaths = filePaths;
    this.classes = classes;
    this.methods = methods;
    this.constructors = constructors;
  }

  public boolean contains(Metric m) {
    var md = m.getDetails();
    return classesContain(md) || methodsContain(md) || constructorsContain(md);
  }

  private boolean classesContain(MetricDetail md) {
    return this.classes.contains(
        new ClassInput(md.getPackageName().orElse(""), md.getClassName().orElse("")));
  }

  private boolean methodsContain(MetricDetail md) {
    return this.methods.contains(
        new MethodInput(
            md.getPackageName().orElse(""),
            md.getClassName().orElse(""),
            md.getMethodName().orElse("")));
  }

  private boolean constructorsContain(MetricDetail md) {
    return this.constructors.contains(
        new ConstructorInput(md.getPackageName().orElse(""), md.getClassName().orElse("")));
  }

  public static Type getTypeOf(NamedCsvRow csvRow) {
    String pkg = csvRow.getField(CsvHeaders.PACKAGE.getValue());
    String clazz = csvRow.getField(CsvHeaders.CLASS.getValue());
    String method = csvRow.getField(CsvHeaders.METHOD.getValue());

    if (isClassFilter(pkg, clazz, method)) {
      return Type.CLASS;
    } else if (isMethodFilter(pkg, clazz, method)) {
      return Type.METHOD;
    } else if (isConstructorFilter(pkg, clazz, method)) {
      return Type.CONSTRUCTOR;
    } else {
      return Type.UNKNOWN;
    }
  }

  private static boolean isClassFilter(String pkg, String clazz, String method) {
    return !pkg.isBlank() && !clazz.isBlank() && method.isBlank();
  }

  private static boolean isMethodFilter(String pkg, String clazz, String method) {
    return !pkg.isBlank() && !method.isBlank() && !clazz.equals(method);
  }

  private static boolean isConstructorFilter(String pkg, String clazz, String method) {
    return !pkg.isBlank() && !method.isBlank() && clazz.equals(method);
  }

  public Set<String> getFilePaths() {
    return filePaths;
  }

  public Set<ClassInput> getClasses() {
    return classes;
  }

  public Set<MethodInput> getMethods() {
    return methods;
  }

  public Set<ConstructorInput> getConstructors() {
    return constructors;
  }
}
