package smallstep;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a runtime exception for the
 * small step interpreter.
 * 
 * @author bmeurer
 * @version $Id$
 */
public class Exn extends Expression {
  /**
   * Just returns the expression itself, since no
   * substitution is possible on exceptions.
   * 
   * @param id the identifier.
   * @param e the expression to substitute.
   * @return the exception itself.
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // cannot substitute anything here
    return this;
  }

  /**
   * Just returns the expression itself, since no
   * evaluation is possible on exceptions. Nothing
   * is added to the <code>ruleChain</code>.
   * 
   * @param ruleChain the chain of rules.
   * @return the exception itself.
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    // cannot evaluate an exception
    return this;
  }
  
  /**
   * Returns the empty set, since exceptions
   * cannot contain any free identifiers.
   * @return the empty set.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return new TreeSet<String>();
  }

  /**
   * Returns the pretty print format for this exception,
   * which is simply a string that contains no format items,
   * but just the name of the exception.
   * @return the pretty print string for the exception.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return new MessageFormat(this.name);
  }

  /**
   * Returns the pretty print priority for the exception.
   * @return the pretty print priority for the exception.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }

  /**
   * Returns an empty array, as an exception has no subexpressions.
   * @return an empty array, as an exception has no subexpressions.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns an empty array, as an exception has no subexpressions.
   * @return an empty array, as an exception has no subexpressions.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return Expression.EMPTY_ARRAY;
  }

  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn DIVIDE_BY_ZERO = new Exn("divide_by_zero");
  
  private Exn(final String name) {
    this.name = name;
  }
  
  // the name of the exception
  private String name;
  
  // pretty print support
  private static final int PRETTY_PRINT_PRIORITIES[] = new int[0];
  private static final int PRETTY_PRINT_PRIORITY = 2;
}
