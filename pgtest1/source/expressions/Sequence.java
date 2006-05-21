package expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an expression for sequential execution
 * of commands, <code>e1;e2</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Sequence extends Expression {
  //
  // Attributes
  //
  
  /**
   * The first expression.
   */
  private Expression e1;
  
  /**
   * The second expression.
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new sequence with the given expressions
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Sequence(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * @return Returns the e1.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * @return Returns the e2.
   */
  public Expression getE2() {
    return this.e2;
  }
  
  
  
  //
  // Primitives
  //
  
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
   * {@inheritDoc}
   * 
   * @see expressions.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    // determine a new unique identifier
    String id = "u";
    Set<String> freeE1 = this.e1.free();
    Set<String> freeE2 = this.e2.free();
    while (freeE1.contains(id) || freeE2.contains(id)) {
      id = id + "'";
    }
    
    // translate to "let id = e1 in e2"
    return new Let(id, this.e1, this.e2);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new Sequence(this.e1.substitute(id, e), this.e2.substitute(id, e));
  }
  
  /**
   * {@inheritDoc}
   * 
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
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendText("; ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }
}
