package expressions;

import java.util.Set;
import java.util.TreeSet;

public abstract class Expression {
  /**
   * Returns <code>true</code> if the expression is an
   * exception that cannot be evaluated any further.
   * 
   * @return <code>true</code> if the expression is an
   *         exception.
   */
  public boolean isException() {
    return (this instanceof Exn);
  }
  
  /**
   * Returns <code>true</code> if the expression is a value
   * that cannot be evaluated any further.
   * 
   * @return <code>true</code> if the expression is a value
   *         and no further evaluation is possible.
   */
  public boolean isValue() {
    return false;
  }
  
  /**
   * Normalizes the expression, i.e. replaces {@link Application}s
   * of {@link Operator}s to {@link Constant}s with {@link
   * AppliedOperator}s and such.  
   * 
   * @return the normalized expression.
   */
  public Expression normalize() {
    return this;
  }
  
  /**
   * Substitutes the value <code>v</code> for the identifier <code>>id</code>
   * and returns the resulting expression.
   * 
   * @param id the name of the identifier.
   * @param e the expression to substitute.
   * @return the resulting expression.
   */
  public abstract Expression substitute(String id, Expression e);

  /**
   * Evaluates the expression and returns the resulting expression.
   * The rules that were taken into account will be appended to
   * the given <code>ruleChain</code>.
   * 
   * @param ruleChain the <code>RuleChain</code> to which the
   *        rules required for the evaluation are appended.
   * @return the resulting expression.        
   */
  public abstract Expression evaluate(RuleChain ruleChain);
  
  /**
   * Returns the free identifiers within this expression.
   * @return the free identifiers within this expression.
   */
  public abstract Set<String> free();
  
  /**
   * Returns <code>true</code> if the expression contains
   * memory references or locations.
   * 
   * @return <code>true</code> if the expression contains
   *                           memory references or locations.
   */
  public boolean containsReferences() {
    return false;
  }
  
  /**
   * Returns <code>true</code> if the expression contains
   * syntactic sugar, else <code>false</code>.
   * 
   * You can call translateSyntacticSugar() to translate
   * all expressions to the core language.
   * 
   * @return <code>true</code> if the expression contains
   *         syntactic sugar.
   *         
   * @see #translateSyntacticSugar()
   */
  public boolean containsSyntacticSugar() {
    return false;
  }
  
  /**
   * If this {@link Expression} is syntactic, it is translated
   * to core syntax.
   *
   * @return the new expression without syntactic sugar.
   * 
   * @see #containsSyntacticSugar()
   */
  public Expression translateSyntacticSugar() {
    return this;
  }

  /**
   * Returns the <code>PrettyString</code> for this expression,
   * which can be used to present the expression to the user.
   * @return the <code>PrettyString</code> for this expression.
   */
  public final PrettyString toPrettyString() {
    return toPrettyStringBuilder().toPrettyString();
  }
  
  /**
   * Default string converter for expressions, will be overridden
   * by special constructs like constants.
   * @return the string representation for the whole expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return toPrettyString().toString();
  }
  
  /**
   * Returns the <code>PrettyStringBuilder</code> for this
   * expression, which can be used to generate a pretty
   * string for the expression.
   * 
   * @return the pretty string builder for the expression.
   */
  protected abstract PrettyStringBuilder toPrettyStringBuilder();  
  
  /**
   * An empty string set, used solely to save memory in
   * derived classes, that don't provide any free
   * identifiers.
   */
  public static final TreeSet<String> EMPTY_SET = new TreeSet<String>();
}
