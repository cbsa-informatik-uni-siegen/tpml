package smallstep;

import java.util.Set;
import java.util.TreeSet;

public abstract class Expression {
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
