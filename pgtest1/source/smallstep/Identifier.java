package smallstep;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

public class Identifier extends Expression {
  /**
   * Generates a new Identifier.
   * @param name the name of the identifier.
   */
  public Identifier(String name) {
    this.name = name;
  }

  /**
   * Performs the substitution for <b>(ID)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Expression e) {
    if (this.name.equals(id))
      return e;
    else
      return this;
  }

  /**
   * Just returns the identifier expression.
   *
   * @param ruleChain the chain of rules.
   * @return this identifier expression.
   */
  public Expression evaluate(RuleChain ruleChain) {
    return this;
  }
  
  /**
   * Returns the set that contains this identifier.
   * @return the set that contains this identifier.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.add(this.name);
    return set;
  }

  /**
   * Returns the name of the identifier.
   * @return the name of the identifier.
   */
  public final String getName() {
    return this.name;
  }

  /**
   * Returns the name of the identifier as MessageFormat,
   * since an identifier has no subexpressions.
   * @return the name of the identifier.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return new MessageFormat(this.name);
  }

  /**
   * Returns the pretty print priority of this identifier.
   * @return the pretty print priority of this identifier.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }

  /**
   * Returns an empty array, since an identifier has no
   * subexpressions.
   * @return an empty array.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns an empty array, since an identifier has no subexpressions.
   * @return an empty array.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return Expression.EMPTY_ARRAY;
  }

  // the name of the identifier
  private String name;
  
  // pretty print support
  private static final int PRETTY_PRINT_PRIORITIES[] = new int[0];
  private static final int PRETTY_PRINT_PRIORITY = 2;
}
