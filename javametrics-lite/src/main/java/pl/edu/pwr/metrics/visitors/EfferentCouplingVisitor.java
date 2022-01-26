package pl.edu.pwr.metrics.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EfferentCouplingVisitor extends VoidVisitorAdapter<Void> {

  private Set<String> result = new HashSet<>();

  @Override
  public void visit(CompilationUnit n, Void arg) {
    super.visit(n, arg);
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration n, Void arg) {
    super.visit(n, arg);
  }

  @Override
  public void visit(FieldAccessExpr n, Void arg) {
    super.visit(n, arg);

    result.add(n.resolve().getType().describe());
  }

  @Override
  public void visit(FieldDeclaration n, Void arg) {
    super.visit(n, arg);

    result.add(n.resolve().getType().describe());
  }

  @Override
  public void visit(MethodDeclaration n, Void arg) {
    super.visit(n, arg);

    result.addAll(
        n.getParameters().stream()
            .map(p -> p.getType().resolve().describe())
            .collect(Collectors.toList()));
  }

  @Override
  public void visit(NameExpr n, Void arg) {
    super.visit(n, arg);

    result.add(n.resolve().getType().describe());
  }

  @Override
  public void visit(ReturnStmt n, Void arg) {
    super.visit(n, arg);

    Optional<Expression> expr = n.getExpression();
    expr.ifPresent(expression -> result.add(expression.calculateResolvedType().describe()));
  }

  @Override
  public void visit(ThrowStmt n, Void arg) {
    super.visit(n, arg);

    result.add(n.getExpression().calculateResolvedType().describe());
  }

  @Override
  public void visit(CatchClause n, Void arg) {
    super.visit(n, arg);

    result.add(n.getParameter().getType().resolve().describe());
  }

  @Override
  public void visit(VariableDeclarator n, Void arg) {
    super.visit(n, arg);

    result.add(n.getType().resolve().describe());
  }

  public Set<String> getResult() {
    return result;
  }
}
