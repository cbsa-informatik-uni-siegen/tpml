package expressions;

import java.util.Set;
import java.util.TreeSet;

import common.prettyprinter.PrettyStringBuilder;

import expressions.annotation.SyntacticSugar;

/**
 * Represents the <code>let rec</code> expression, which is
 * syntactic sugar for <b>(LET)</b> and <b>(REC)</b>.
 * 
 * The expression
 * <pre>let rec id = e1 in e2</pre>
 * is equal to
 * <pre>let id = rec id.e1 in e2</pre>
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
public final class LetRec extends Let {
  //
  // Constructor
  //
  
  /**
   * Generates a new let rec expression.
   * 
   * @param id the name of the identifier.
   * @param e1 the first expression.
   * @param e2 the second expression. 
   */
  public LetRec(String id, Expression e1, Expression e2) {
    super(id, e1, e2);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Performs the substitution on let rec expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * 
   * @return the new expression.
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers for e
      Set<String> free = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (free.contains(newId))
        newId = newId + "'";
      
      // perform the bound renaming
      Expression newE1 = this.e1.substitute(this.id, new Identifier(newId));
      
      // perform the substitution
      return new LetRec(newId, newE1.substitute(id, e), this.e2.substitute(id, e));
    }
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
    set.addAll(this.e1.free());
    set.remove(this.id);
    return set;
  }
  
  /**
   * Translates the syntactic sugar for the <b>(LET-REC)</b>
   * and its subexpressions to the core syntax.
   * 
   * @return the new expression in the core syntax.
   * 
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Let(this.id, new Recursion(this.id, this.e1), this.e2);
  }

  /**
   * Returns the pretty string builder for let rec expressions.
   * @return the pretty string builder for let rec expressions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("let");
    builder.appendText(" ");
    builder.appendKeyword("rec");
    builder.appendText(" " + this.id + " = ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("in");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }
}
