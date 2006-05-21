package expressions;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class represents a chain of small step
 * rules, that were executed during the execution
 * of a single small step.
 * 
 * This means that every rule chain contains no
 * more than one axiom and if it contains an
 * axiom, the axiom is the last rule in the
 * chain.
 *
 * @author bmeurer
 * @version $Id:RuleChain.java 121 2006-04-28 16:45:27Z benny $
 */
@Deprecated
public class RuleChain {
  /**
   * Generates a new empty rule chain object.
   */
  public RuleChain() {
    // nothing to do here
  }
  
  /**
   * Checks whether the rule chain currently contains no rules.
   * @return @c true if the rule chain is empty.
   */
  public final boolean isEmpty() {
    return this.rules.isEmpty();
  }
  
  /**
   * Prepends <code>rule</code> to the chain.
   * 
   * This method first checks several constraints
   * prior to prepending the <code>rule</code>.
   * 
   * @param rule the rule to append.
   */
  public void prepend(Rule rule) {
    // cannot prepend axiom to non-empty list and
    // cannot prepend meta-rule to an empty list
    assert (isEmpty() || !rule.isAxiom());
    assert (!isEmpty () || rule.isAxiom());
    
    this.rules.addFirst(rule);
  }
  
  /**
   * Returns the rules contained within this chain.
   * @return the rules contained within this chain.
   */
  public LinkedList<Rule> getRules() {
    return this.rules;
  }
  
  /**
   * Returns a list iterator on the rules, starting with
   * the first rule.
   * @return a list iterator on the rules.
   */
  public ListIterator<Rule> listIterator() {
    return this.rules.listIterator();
  }
  
  private LinkedList<Rule> rules = new LinkedList<Rule>();
}
