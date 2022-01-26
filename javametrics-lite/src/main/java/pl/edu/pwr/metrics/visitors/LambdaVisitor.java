package pl.edu.pwr.metrics.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class LambdaVisitor extends VoidVisitorAdapter<Void> {

  private int count = 0;

  @Override
  public void visit(MethodDeclaration n, Void arg) {
    super.visit(n, arg);
  }

  @Override
  public void visit(LambdaExpr n, Void arg) {
    super.visit(n, arg);
    count++;
  }

  public int getCount() {
    return count;
  }
}
