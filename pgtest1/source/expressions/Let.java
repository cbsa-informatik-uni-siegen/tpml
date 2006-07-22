package expressions;

import java.util.Set;
import java.util.TreeSet;

import common.prettyprinter.PrettyStringBuilder;

/**
 * Represents a <code>let</code> expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class Let extends Expression {
  //
  // Attributes
  //
  
  /**
   * The identifier of the <code>Let</code> expression.
   * 
   * @see #getId()
   */
  protected String id;
  
  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  protected Expression e1;
  
  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  protected Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Generates a new let expression.
   * @param id the name of the identifier.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Let(String id, Expression e1, Expression e2) {
    this.id = id;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Primitives
  //

  /**
   * Performs the substitution for <b>Let</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * 
   * @return the new expression.
   */
  @Override
  public Expression substitute(String id, Expression e) {
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.id.equals(id) ? this.e2 : this.e2.substitute(id, e);
    return new Let(this.id, e1, e2);
  }

  /**
   * Returns the free identifiers of
   * the subexpressions.
   * 
   * @return the free identifiers.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e2.free());
    set.remove(this.id);
    set.addAll(this.e1.free());
    return set;
  }
  
  /**
   * Returns the pretty string builder for let expressions.
   * @return the pretty string builder for let expressions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("let");
    builder.appendText(" " + this.id + " = ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("in");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }

  
  
  //
  // Accessors
  //
  
  /**
   * Returns the identifier of the <code>Let</code> expression.
   * 
   * @return the identifier of the <code>Let</code> expression.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2() {
    return this.e2;
  }
}
