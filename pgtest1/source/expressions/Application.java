package expressions;

import java.util.Set;
import java.util.TreeSet;

public class Application extends Expression {
  //
  // Attributes
  //
  
  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Generates a new application.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Application(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * An <code>Application</code> can be a value if it consists
   * of a binary operator and a value.
   * 
   * @return <code>true</code> if the application consists of
   *                           a binary operator and a value.
   *
   * @see expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    return (this.e1 instanceof BinaryOperator && this.e2.isValue());
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
}
