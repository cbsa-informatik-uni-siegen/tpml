package expressions;

import java.util.Set;

import common.prettyprinter.PrettyStringBuilder;

import expressions.annotation.SyntacticSugar;

/**
 * Represents a <code>while</code> loop.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
public final class While extends Expression {
  //
  // Attributes
  //
  
  /**
   * The conditional part.
   */
  private Expression e1;
  
  /**
   * The statement part.
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>While</code> instance.
   * 
   * @param e1 the conditional part.
   * @param e2 the repeated statement.
   */
  public While(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    // determine a new unique identifier
    String id = "w";
    Set<String> freeE1 = this.e1.free();
    Set<String> freeE2 = this.e2.free();
    while (freeE1.contains(id) || freeE2.contains(id)) {
      id = id + "'";
    }

    // translate to recursion: rec w.if e1 then e2;w else ()
    return new Recursion(id, new Condition(this.e1, new Sequence(this.e2, new Identifier(id)), UnitConstant.UNIT));
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new While(this.e1.substitute(id, e), this.e2.substitute(id, e));
  }

  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("while");
    builder.appendText(" ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("do");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
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
}
