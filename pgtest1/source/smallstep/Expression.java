package smallstep;

import java.text.MessageFormat;
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

  /**
   * Returns an array of subexpressions for this expression. For
   * example, an application has two subexpressions, while a
   * condition has three subexpressions and a value has no
   * subexpressions.
   * @return the array of subexpressions.
   */
  public abstract Expression[] getSubExpressions();
  
  /**
   * Returns the required priorities for the subexpression
   * fields. For example, an application will returned [1,2]
   * here, while a condition will return [0,0,0].
   * @return the required priorities for the subexpression fields.
   */
  public abstract int[] getSubExpressionPriorities();
  
  /**
   * Returns the pretty print message fornat of this expression.
   * You can use the returned message format to generate a pretty
   * print expression while inserting the subexpressions.
   * @return the pretty print message format of this expression.
   */
  public abstract MessageFormat getPrettyPrintFormat();
  
  /**
   * Returns the pretty print priority of this expression.
   * This is the pretty print priority returned for this
   * expression, not of one of the sub expressions.
   * @return the pretty print priority of this expression.
   */
  public abstract int getPrettyPrintPriority();
  
  /**
   * Default string converter for expressions, will be overridden
   * by special constructs like constants.
   * @return the string representation for the whole expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    Expression[] subExpressions = getSubExpressions();
    int[] subExpressionPriorities = getSubExpressionPriorities();
    
    // verify that the lengths for the arrays match
    assert (subExpressions.length == subExpressionPriorities.length);
    
    // determine strings for the subexpressions, which appropriate
    // parenthesis
    String[] subStrings = new String[subExpressions.length];
    for (int n = 0; n < subExpressions.length; ++n) {
      String s = subExpressions[n].toString();
      if (subExpressions[n].getPrettyPrintPriority() < subExpressionPriorities[n])
        s = "(" + s + ")";
      subStrings[n] = s;
    }
    
    // use the specified message format to generate
    // the string using the substring array
    StringBuffer buffer = new StringBuffer();
    buffer = getPrettyPrintFormat().format(subStrings, buffer, null);
    return buffer.toString();
  }
  
  /**
   * An empty array of expressions, used solely to save
   * memory in derived classes.
   */
  public static final Expression[] EMPTY_ARRAY = new Expression[0];
}
