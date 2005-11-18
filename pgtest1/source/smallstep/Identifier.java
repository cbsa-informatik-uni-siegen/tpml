package smallstep;

import java.util.Set;
import java.util.TreeSet;

import smallstep.printer.AtomicItem;
import smallstep.printer.Item;

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
   * @see smallstep.Expression#getPrettyPrintItem()
   */
  @Override
  public Item getPrettyPrintItem() {
    return new AtomicItem(this.name);
  }
  
  private String name;
}
