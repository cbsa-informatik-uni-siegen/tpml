package smallstep;

import java.util.Set;
import java.util.TreeSet;

import smallstep.printer.AtomicItem;
import smallstep.printer.Item;

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
   * @see smallstep.Expression#getPrettyPrintItem()
   */
  @Override
  public Item getPrettyPrintItem() {
    return new AtomicItem(this.name);
  }

  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn DIVIDE_BY_ZERO = new Exn("divide_by_zero");
  
  private Exn(final String name) {
    this.name = name;
  }
  
  private String name;
}
