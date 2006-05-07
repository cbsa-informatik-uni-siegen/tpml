package expressions;

import java.util.Set;
import java.util.TreeSet;

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
public final class LetRec extends Expression {
  /**
   * Generates a new let rec expression.
   * 
   * @param id the name of the identifier.
   * @param e1 the first expression.
   * @param e2 the second expression. 
   */
  public LetRec(String id, Expression e1, Expression e2) {
    this.id = id;
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
    
    // check if we need to generate a new letrec
    if (e1 != this.e1 || e2 != this.e2) {
      return new LetRec(this.id, e1, e2);
    }
    else {
      return this;
    }
  }
  
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
      return new LetRec(this.id, this.e1.substitute(id, e), this.e2.substitute(id, e));
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
   * Evaluates the let rec expression.
   * 
   * @param ruleChain the chain of rules.
   * 
   * @return the resulting expression.
   * 
   * @see expressions.Expression#evaluate(expressions.RuleChain)
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    assert (this.e1 instanceof Expression);
    assert (this.e2 instanceof Expression);
    
    // perform the (UNFOLD), which includes a (LET-EVAL) of course, operation on e1
    ruleChain.prepend(new Rule(this, Rule.UNFOLD));
    ruleChain.prepend(new Rule(this, Rule.LET_EVAL));
    Expression e1 = this.e1.substitute(this.id, new Recursion(this.id, this.e1));
    
    // and generate a (LET) expression
    return new Let(this.id, e1, this.e2);
  }

  /**
   * Determines the set of free (unbound) identifiers for
   * the let rec expression.
   * 
   * @return the free identifiers.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e1.free());
    set.remove(this.id);
    set.addAll(this.e2.free());
    return set;
  }
  
  /**
   * @return Returns the id.
   */
  public String getId() {
    return this.id;
  }
  
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
  
  /**
   * Returns <code>true</code> as <b>(LET-REC)</b> is
   * syntactic sugar.
   * 
   * @return <code>true</code>.
   * 
   * @see expressions.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
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

  private String id;
  private Expression e1;
  private Expression e2;
}
