package smallstep;

import java.util.LinkedList;

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
 * @version $Id$
 */
public class RuleChain {
  /**
   * Generates a new empty rule chain object.
   */
  public RuleChain() {
    // nothing to do here
  }
  
  /**
   * Appends <code>rule</code> to the chain.
   * 
   * This method first checks several contraints
   * prior to appending the <code>rule</code>.
   * 
   * @param rule the rule to append.
   */
  public void append(Rule rule) {
    // verify the rule list first if its not empty
    if (!this.rules.isEmpty()) {
      Rule last = this.rules.getLast();
      if (last.isAxiom())
        throw new IllegalStateException("Cannot append a rule to a chain that already contains an axiom");
    }
    
    // append the rule to the chain of rules
    this.rules.add(rule);
  }
  
  /**
   * Returns the rules within this chain as a linked list.
   * @return the rules within this chain as a linked list.
   */
  public LinkedList<Rule> getRules() {
    return this.rules;
  }
  
  private LinkedList<Rule> rules = new LinkedList<Rule>();
}
