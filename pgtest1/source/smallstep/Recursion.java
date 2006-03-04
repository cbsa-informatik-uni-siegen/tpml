/**
 * 
 */
package smallstep;

import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of the <b>(REC)</b> rule.
 *
 * @author bmeurer
 * @version $Id$
 */
public class Recursion extends Expression {
  /**
   * Allocates a new <b>(REC)</b> expression.
   * @param id the identifier of the recursive expression.
   * @param e the expression.
   */
  public Recursion(String id, Expression e) {
    this.id = id;
    this.e = e;
  }
  
  /**
   * Performs the substitution for recursive expressions.
   * 
   * @param id the identifier.
   * @param e the expression to substitute.
   * @return the resulting expression.
   * 
   * @see smallstep.Expression#substitute(java.lang.String, smallstep.Expression)
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
      Expression newE = this.e.substitute(this.id, new Identifier(newId));
      
      // perform the substitution
      return new Recursion(newId, newE.substitute(id, e));
    }
  }

  /**
   * Evaluates a <b>(REC)</b> expression using the <b>(UNFOLD)</b>
   * rule.
   * 
   * @param ruleChain the rule chain.
   * @return the resulting expression.
   * 
   * @see smallstep.Expression#evaluate(smallstep.RuleChain)
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    
    // prepend the (UNFOLD) axiom
    ruleChain.prepend(new Rule(this, Rule.UNFOLD));
    
    // perform the substitution
    return this.e.substitute(this.id, this);
  }

  /**
   * Returns the free identifiers of the
   * subexpression minus the identifier of
   * the <b>(REC)</b> expression.
   *
   * @return the free identifiers.
   * 
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e.free());
    set.remove(this.id);
    return set;
  }

  /**
   * @return Returns the id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * @return Returns the e.
   */
  public Expression getE() {
    return this.e;
  }
  
  /**
   * Returns <code>true</code> if the subexpression
   * contains syntactic sugar.
   * 
   * @return whether the subexpression contains syntactic sugar.
   * 
   * @see smallstep.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return this.e.containsSyntacticSugar();
  }
  
  /**
   * Returns the new expression in the core syntax.
   * 
   * @return the new expression in the core syntax.
   * 
   * @see smallstep.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    Expression e = this.e.translateSyntacticSugar();
    
    if (e != this.e)
      return new Recursion(this.id, e);
    else
      return this;
  }

  /**
   * Returns the pretty string builder for rec expressions.
   * @return the pretty string builder for rec expressions.
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("rec");
    builder.appendText(" " + this.id + ".");
    builder.appendBuilder(this.e.toPrettyStringBuilder(), 0);
    return builder;
  }

  // the internal structure
  private String id;
  private Expression e;
}
