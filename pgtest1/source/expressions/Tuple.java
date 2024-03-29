package expressions;

import common.prettyprinter.PrettyStringBuilder;

/**
 * Represents a tuple expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Tuple extends Expression {
  /**
   * Allocates a new tuple with the given <code>expressions</code>.
   * 
   * @param expressions a non-empty array of {@link Expression}s.
   */
  public Tuple(Expression[] expressions) {
    // validate the expressions
    assert (expressions.length > 0);
    for (Expression expression : expressions)
      assert (expression instanceof Expression);
    
    // apply the list of expressions
    this.expressions = expressions;
  }
  
  /**
   * Checks if all sub expressions of the tuple are
   * values and returns <code>true</code>. <code>false</code>
   * is returned if atleast one expression is not a value.
   * 
   * @return <code>true</code> if all sub expressions are
   *         values.
   *         
   * @see expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    for (Expression expression : this.expressions)
      if (!expression.isValue())
        return false;
    return true;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // substitute all subexpressions
    Expression[] expressions = new Expression[this.expressions.length];
    for (int n = 0; n < expressions.length; ++n)
      expressions[n] = this.expressions[n].substitute(id, e);
    return new Tuple(expressions);
  }

  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendText("(");
    for (int n = 0; n < this.expressions.length; ++n) {
      if (n > 0) {
        builder.appendText(", ");
        builder.appendBreak();
      }
      builder.appendBuilder(this.expressions[n].toPrettyStringBuilder(), 0);
    }
    builder.appendText(")");
    return builder;
  }

  
  
  //
  // Accessors
  //
  
  /**
   * Returns the arity of the tuple.
   * 
   * @return the arity of the tuple.
   */
  public int getArity() {
    return this.expressions.length;
  }
  
  /**
   * Returns the sub expressions of the tuple.
   * 
   * @return the sub expressions of the tuple.
   */
  public Expression[] getExpressions() {
    return this.expressions;
  }
  
  /**
   * Returns the sub expression at the given <code>index</code>.
   * 
   * @param index the index of the sub expression to return.
   *              
   * @return the sub expression at the given <code>index</code>.
   *         
   * @throws ArrayIndexOutOfBoundsException if the <code>index</code>
   *                                        is out of bounds.
   *                                        
   * @see #getExpressions()
   */
  public Expression getExpressions(int index) {
    return this.expressions[index];
  }
  
  // member attributes
  private Expression[] expressions;
}
