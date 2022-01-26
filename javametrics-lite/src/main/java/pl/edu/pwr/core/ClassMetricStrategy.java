package pl.edu.pwr.core;

import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import pl.edu.pwr.config.Constants;
import pl.edu.pwr.core.exception.JavaParserNotConfiguredException;
import pl.edu.pwr.core.model.Metric;
import pl.edu.pwr.core.model.MetricDetail;
import pl.edu.pwr.metrics.visitors.AfferentCouplingVisitor;
import pl.edu.pwr.metrics.visitors.EfferentCouplingVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract class that should be extended to compute metrics for a class.
 *
 * @param <T> Type of the metric value
 */
public abstract class ClassMetricStrategy<T> implements MetricStrategy<T> {

  private static final Logger LOGGER = Logger.getLogger(ClassMetricStrategy.class.getName());
  /**
   * Get list of metrics for a class
   *
   * @param cm class metric
   * @param compilationUnit compilation unit
   * @return list of metrics for a class
   */
  protected List<Metric<T>> getMetricForClass(ClassMetric<T> cm, CompilationUnit compilationUnit) {
    String packageName = getPackageName(compilationUnit).orElse("");

    return compilationUnit.findAll(ClassOrInterfaceDeclaration.class).stream()
        .filter(c -> Constants.TAKE_INNER_TYPE || !c.isNestedType())
        .map(
            c -> {
              MetricDetail metricDetail =
                  new MetricDetail.MetricDetailBuilder()
                      .setPackageName(packageName)
                      .setClassName(c.getNameAsString())
                      .setOuterClassName(getOuterClassName(c).orElse(""))
                      .setAccessModifier(c.getAccessSpecifier().asString())
                      .setStatic(c.isStatic())
                      .setFinal(c.isFinal())
                      .build();

              return new Metric<>(this.getName(), cm.apply(c), metricDetail);
            })
        .collect(Collectors.toList());
  }

  /**
   * Get accessor methods of a class
   *
   * @param declaration class declaration
   * @return list of accessor method declarations
   */
  protected List<MethodDeclaration> getClassAccessorMethods(
      ClassOrInterfaceDeclaration declaration) {
    List<String> fieldNames = getClassFieldNames(declaration);

    return declaration.findAll(MethodDeclaration.class).stream()
        .filter(
            m -> {
              List<Statement> statements =
                  m.getBody().map(BlockStmt::getStatements).orElse(new NodeList<>());
              return statements.size() == 1
                  && statements.get(0).isReturnStmt()
                  && statements.get(0).findAll(NameExpr.class).stream()
                      .allMatch(n -> fieldNames.contains(n.getNameAsString()));
            })
        .collect(Collectors.toList());
  }

  /**
   * Find class files containing the regex
   *
   * @param regex regular expression
   * @return list of absolute file paths containing the regex
   */
  protected List<String> getClassFilesContaining(String regex) {
    String projectPath = MetricParserUtil.getProjectPath();
    List<String> result = new ArrayList<>();

    try (Stream<Path> walk = Files.walk(Paths.get(projectPath))) {
      walk.map(Path::toFile)
          .filter(f -> f.getName().endsWith(".java"))
          .forEach(
              f -> {
                try (Stream<String> stream = Files.lines(Paths.get(f.getAbsolutePath()))) {
                  Optional<String> lineHavingTarget =
                      stream.filter(l -> l.matches(regex)).findFirst();

                  if (lineHavingTarget.isPresent()) {
                    result.add(f.getAbsolutePath());
                  }
                } catch (IOException e) {
                  LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
              });
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }

    return result;
  }

  /**
   * Parse a file using the same parser configuration that was specified for the whole project. It
   * is useful for metrics needing additional information about other classes (i. e. subclasses)
   *
   * @param fileName
   * @return Javaparser AST CompilationUnit with the parsed file
   * @throws IOException
   * @throws ParseException
   * @throws JavaParserNotConfiguredException
   */
  protected CompilationUnit parseFile(String fileName)
      throws IOException, ParseException, JavaParserNotConfiguredException {
    ParseResult<CompilationUnit> parseResult =
        MetricParserUtil.getJavaParserInstance().parse(Paths.get(fileName));
    if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
      return parseResult.getResult().get();
    } else throw new ParseException(parseResult.getProblems().toString());
  }

  /**
   * Get classes that depend upon a specified class (afferent coupling relationship)
   *
   * @param declaration class declaration that other classes should depend upon
   * @return list of classes that depend upon the declaration
   */
  protected Set<ClassOrInterfaceDeclaration> getAfferentCouplingClasses(
      ClassOrInterfaceDeclaration declaration) {
    Set<ClassOrInterfaceDeclaration> result = new HashSet<>();
    List<String> classFilesUsingClassName =
        getClassFilesContaining(".*" + declaration.getNameAsString() + ".*");

    Optional<String> declarationQualifiedName = declaration.getFullyQualifiedName();
    if (declarationQualifiedName.isPresent()) {
      classFilesUsingClassName.forEach(
          cf -> {
            try {
              CompilationUnit cu = parseFile(cf);

              AfferentCouplingVisitor visitor = new AfferentCouplingVisitor(declaration);
              visitor.visit(cu, null);

              result.addAll(visitor.getResult());

            } catch (IOException | ParseException | JavaParserNotConfiguredException e) {
              LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
          });
    }

    return result;
  }

  protected Set<String> getEfferentCouplingClasses(ClassOrInterfaceDeclaration declaration) {
    EfferentCouplingVisitor visitor = new EfferentCouplingVisitor();
    visitor.visit(declaration, null);
    return visitor.getResult();
  }

  /**
   * Get list of field names in a class
   *
   * @param declaration class declaration
   * @return list of field names
   */
  private List<String> getClassFieldNames(ClassOrInterfaceDeclaration declaration) {
    return declaration.findAll(FieldDeclaration.class).stream()
        .map(f -> f.getVariables().stream())
        .flatMap(Function.identity())
        .map(NodeWithSimpleName::getNameAsString)
        .collect(Collectors.toList());
  }

  /**
   * Get list of field names, including inherited fields
   *
   * @param declaration class declaration
   * @return list of field names (from a given class and those inherited)
   */
  protected List<String> getAllClassFieldNames(ClassOrInterfaceDeclaration declaration) {
    return declaration.resolve().getVisibleFields().stream()
        .map(ResolvedDeclaration::getName)
        .collect(Collectors.toList());
  }

  /**
   * Get a list of all method names, including inherited ones
   *
   * @param declaration class declaration
   * @return list of method names from a given class, including inherited methods
   */
  protected List<String> getAllClassMethodNames(ClassOrInterfaceDeclaration declaration) {
    ResolvedReferenceTypeDeclaration resolved = declaration.resolve();

    return resolved.getAllMethods().stream()
        .filter(
            m ->
                (m.getDeclaration().accessSpecifier().equals(AccessSpecifier.PRIVATE)
                        && m.declaringType().equals(resolved.asType()))
                    || !m.getDeclaration().accessSpecifier().equals(AccessSpecifier.PRIVATE))
        .map(MethodUsage::getName)
        .collect(Collectors.toList());
  }

  /**
   * Get name of outer class name containing a given class
   *
   * @param c class declaration
   * @return name of containing outer class
   */
  private Optional<String> getOuterClassName(ClassOrInterfaceDeclaration c) {
    return getParentClass(c).map(NodeWithSimpleName::getNameAsString);
  }

  /**
   * Check if the method is a foreign accessor.
   *
   * @param me method to be checked
   * @param c class which uses the method call
   * @param classMethodNames class' method names
   * @return true if the method is a foreign accessor, false otherwise
   */
  protected boolean isForeignAccessorCalled(
      MethodCallExpr me, ClassOrInterfaceDeclaration c, List<String> classMethodNames) {
    return !classMethodNames.contains(me.getNameAsString())
        && me.getScope().isPresent()
        && me.getScope().get().isNameExpr()
        && !c.getNameAsString().equals(me.resolve().declaringType().getClassName());
  }

  /**
   * Check if the field accessed is a foreign field.
   *
   * @param fe field to be tested
   * @param c class which uses the field access
   * @param classFieldNames class' field names
   * @return true if the method is a foreign field, false otherwise
   */
  protected boolean isForeignFieldAccessed(
      FieldAccessExpr fe, ClassOrInterfaceDeclaration c, List<String> classFieldNames) {
    if (!classFieldNames.contains(fe.getNameAsString()) && fe.getScope().isNameExpr()) {
      ResolvedValueDeclaration resolved = fe.resolve();
      return resolved.isField()
          && !c.getNameAsString().equals(resolved.asField().declaringType().getClassName());
    }
    return false;
  }

  /**
   * Get declarations of class mutator methods (getters).
   *
   * @param declaration class declaration from which the mutators will be extracted
   * @return list of mutator method declarations
   */
  protected List<MethodDeclaration> getClassMutatorMethods(
      ClassOrInterfaceDeclaration declaration) {
    List<String> fieldNames = getClassFieldNames(declaration);

    return declaration.findAll(MethodDeclaration.class).stream()
        .filter(m -> m.getParameters().size() == 1)
        .filter(m -> m.getBody().isPresent())
        .filter(
            m ->
                m.getBody().get().findAll(AssignExpr.class).stream()
                        .filter(
                            e -> e.asAssignExpr().getOperator().equals(AssignExpr.Operator.ASSIGN))
                        .filter(
                            e ->
                                e.asAssignExpr().getValue().isNameExpr()
                                    && e.asAssignExpr().getValue().toNameExpr().isPresent())
                        .filter(
                            e ->
                                m.getParameterByName(
                                        e.asAssignExpr()
                                            .getValue()
                                            .toNameExpr()
                                            .get()
                                            .getNameAsString())
                                    .isPresent())
                        .filter(
                            e ->
                                fieldNames.contains(
                                        e.asAssignExpr()
                                            .getTarget()
                                            .findFirst(FieldAccessExpr.class)
                                            .map(NodeWithSimpleName::getNameAsString)
                                            .orElse(null))
                                    || fieldNames.contains(
                                        e.asAssignExpr()
                                            .getTarget()
                                            .findFirst(NameExpr.class)
                                            .map(NodeWithSimpleName::getNameAsString)
                                            .orElse(null)))
                        .mapToInt(i -> 1)
                        .sum()
                    == 1)
        .collect(Collectors.toList());
  }
}
