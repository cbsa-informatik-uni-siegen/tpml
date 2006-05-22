package expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a curried <code>let rec</code> expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class CurriedLetRec extends CurriedLet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>CurriedLetRec</code> expression.
   * 
   * @param identifiers an array with atleast two identifiers,
   *                    where the first identifier is the name
   *                    to use for the function and the remaining
   *                    identifiers specify the parameters for
   *                    the function.
   * @param e1 the function body.
   * @param e2 the second expression.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *                                  contains less than two identifiers.
   */
  public CurriedLetRec(String[] identifiers, Expression e1, Expression e2) {
    super(identifiers, e1, e2);
  }
  
  
  
  //
  // Primitives
  //

  /**
   * Performs the substitution for <b>CurriedLetRec</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * 
   * @return the new expression.
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // determine the expressions
    Expression e1 = this.e1;
    Expression e2 = this.e2;
    
    // bound rename for substituting e in e1
    Set<String> freeE = e.free();
    for (int n = 0; n < this.identifiers.length; ++n) {
      // generate a new unique identifier
      String newId = this.identifiers[n];
      while (freeE.contains(newId))
        newId = newId + "'";

      // perform the bound renaming
      e1 = e1.substitute(this.identifiers[n], new Identifier(newId));
    }
    
    // substitute e2 if id is not bound in e2
    if (!this.identifiers[0].equals(id))
      e2 = e2.substitute(id, e);
    
    // generate the new expression
    return new CurriedLetRec(this.identifiers, e1, e2);
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
  public final Set<String> free() {
    Set<String> freeE2 = new TreeSet<String>();
    freeE2.addAll(this.e2.free());
    freeE2.remove(this.identifiers[0]);
    
    Set<String> freeE1 = new TreeSet<String>();
    freeE1.addAll(this.e1.free());
    for (int n = 0; n < this.identifiers.length; ++n)
      freeE1.remove(this.identifiers[n]);

    Set<String> free = new TreeSet<String>();
    free.addAll(freeE1);
    free.addAll(freeE2);
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    // translate to: let id1 = rec id1.lambda id2...lambda idn.e1 in e2
    Expression e1 = this.e1;
    for (int n = this.identifiers.length - 1; n > 0; --n) {
      e1 = new Abstraction(this.identifiers[n], e1);
    }
    return new Let(this.identifiers[0], new Recursion(this.identifiers[0], e1), this.e2);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("let");
    builder.appendText(" ");
    builder.appendKeyword("rec");
    for (String id : this.identifiers)
      builder.appendText(" " + id);
    builder.appendText(" = ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("in");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }
}
