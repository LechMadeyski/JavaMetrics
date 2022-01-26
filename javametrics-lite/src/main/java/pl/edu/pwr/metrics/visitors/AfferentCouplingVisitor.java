package pl.edu.pwr.metrics.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AfferentCouplingVisitor extends VoidVisitorAdapter<Void> {

  private Set<ClassOrInterfaceDeclaration> result;
  private ClassOrInterfaceDeclaration clazz;

  public AfferentCouplingVisitor(ClassOrInterfaceDeclaration clazz) {
    this.clazz = clazz;
    this.result = new HashSet<>();
  }

  private Optional<ClassOrInterfaceDeclaration> getParentClass(Node node) {
    Optional<Node> parent = node.getParentNode();
    Optional<ClassOrInterfaceDeclaration> parentClass =
        parent
            .filter(p -> p instanceof ClassOrInterfaceDeclaration)
            .flatMap(p -> Optional.of((ClassOrInterfaceDeclaration) p));

    while (parentClass.isEmpty() && parent.isPresent()) {
      parent = parent.get().getParentNode();
      parentClass =
          parent
              .filter(p -> p instanceof ClassOrInterfaceDeclaration)
              .flatMap(p -> Optional.of((ClassOrInterfaceDeclaration) p));
    }

    return parentClass;
  }

  private void addParentClass(Node n) {
    Optional<Node> parent = n.getParentNode();
    if (parent.isPresent()) {
      Optional<ClassOrInterfaceDeclaration> parentClass = getParentClass(parent.get());
      parentClass.ifPresent(declaration -> this.result.add(declaration));
    }
  }

  @Override
  public void visit(CompilationUnit n, Void arg) {
    super.visit(n, arg);
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration n, Void arg) {
    super.visit(n, arg);
    List<ClassOrInterfaceDeclaration> found =
        n.findAll(ClassOrInterfaceDeclaration.class).stream()
            .filter(c -> c.getExtendedTypes().contains(clazz))
            .collect(Collectors.toList());

    this.result.addAll(found);
  }

  @Override
  public void visit(ConstructorDeclaration n, Void arg) {
    super.visit(n, arg);
    boolean found =
        n.getParameters().stream()
            .anyMatch(
                p ->
                    p.getType()
                        .resolve()
                        .describe()
                        .equals(clazz.getFullyQualifiedName().orElse(null)));

    if (found) {
      Optional<ClassOrInterfaceDeclaration> parentClass =
          n.getParentNode()
              .filter(p -> p instanceof ClassOrInterfaceDeclaration)
              .flatMap(p -> Optional.of((ClassOrInterfaceDeclaration) p));

      parentClass.ifPresent(declaration -> this.result.add(declaration));
    }
  }

  @Override
  public void visit(FieldAccessExpr n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.resolve().getType().describe().equals(clazz.getFullyQualifiedName().orElse(null));

    if (found) addParentClass(n);
  }

  @Override
  public void visit(FieldDeclaration n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.resolve().getType().describe().equals(clazz.getFullyQualifiedName().orElse(null));

    if (found) addParentClass(n);
  }

  @Override
  public void visit(MethodDeclaration n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.getParameters().stream()
            .anyMatch(
                p ->
                    p.getType()
                        .resolve()
                        .describe()
                        .equals(clazz.getFullyQualifiedName().orElse(null)));

    if (found) addParentClass(n);
  }

  @Override
  public void visit(NameExpr n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.resolve().getType().describe().equals(clazz.getFullyQualifiedName().orElse(null));

    if (found) addParentClass(n);
  }

  @Override
  public void visit(ReturnStmt n, Void arg) {
    super.visit(n, arg);

    Optional<Expression> expr = n.getExpression();
    if (expr.isPresent()) {
      boolean found =
          expr.get()
              .calculateResolvedType()
              .describe()
              .equals(clazz.getFullyQualifiedName().orElse(null));

      if (found) addParentClass(n);
    }
  }

  @Override
  public void visit(ThrowStmt n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.getExpression()
            .calculateResolvedType()
            .describe()
            .equals(clazz.getFullyQualifiedName().orElse(null));

    if (found) addParentClass(n);
  }

  @Override
  public void visit(CatchClause n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.getParameter()
            .getType()
            .resolve()
            .describe()
            .equals(clazz.getFullyQualifiedName().orElse(null));

    if (found) addParentClass(n);
  }

  @Override
  public void visit(VariableDeclarator n, Void arg) {
    super.visit(n, arg);

    boolean found =
        n.getType().resolve().describe().equals(clazz.getFullyQualifiedName().orElse(null));

    if (found) addParentClass(n);
  }

  public Set<ClassOrInterfaceDeclaration> getResult() {
    return result;
  }
}
