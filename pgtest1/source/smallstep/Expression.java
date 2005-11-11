package smallstep;

import java.util.Set;

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
}
