package expressions;

import java.util.Set;
import java.util.TreeSet;

public class Application extends Expression {
  /**
   * Generates a new application.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Application(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }

  /**
   * {@inheritDoc}
   * @see expressions.Expression#normalize()
   */
  @Override
  public Expression normalize() {
    // normalize the sub expression
    Expression e1 = this.e1.normalize();
    Expression e2 = this.e2.normalize();
    
    // check if e1 is an operator and e2 is a constant
    if (e1 instanceof Operator && e2 instanceof Constant) {
      // replace with applied operator
      return new AppliedOperator((Operator)e1, (Constant)e2);
    }
    else if (this.e1 != e1 || this.e2 != e2) {
      // generate new application
      return new Application(e1, e2);
    }
    else {
      // application is the same
      return this;
    }
  }
  
  /**
   * Performs the substitution on <b>(APP)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Expression e) {
    return new Application(this.e1.substitute(id, e), this.e2.substitute(id, e));
  }

  /**
   * Returns the free identifiers of the two expressions
   * of the application.
   * @return the free identifiers.
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e1.free());
    set.addAll(this.e2.free());
    return set;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return (this.e1.containsReferences() || this.e2.containsReferences());
  }
  
  /**
   * Returns the first sub expression.
   * 
   * @return the e1.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second sub expression.
   * 
   * @return the e2.
   */
  public Expression getE2() {
    return this.e2;
  }
  
  /**
   * Returns the pretty string builder for applications.
   * @return the pretty string builder for applications.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 5);
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 5);
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 6);
    return builder;
  }
  
  // sub expressions
  private Expression e1;
  private Expression e2;
}
