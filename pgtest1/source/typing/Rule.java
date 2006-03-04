package typing;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a type rule.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Rule {
  /**
   * Returns the name of the type rule.
   * 
   * @return the name of the type rule.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code>
   * is the same type rule as this object.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code>
   *         is the same type rule as this object.
   *         
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    // we have only a private constructor,
    // so the comparison is simple here
    return (this == obj);
  }
  
  /**
   * Returns the string representation of the type rule.
   * 
   * @return the string representation of the rule.
   * 
   * @see #getName()
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(" + getName() + ")";
  }
  
  /**
   * The <b>(CONST)</b> type rule.
   */
  public static final Rule CONST = new Rule("CONST");
  
  /**
   * The <b>(ID)</b> type rule.
   */
  public static final Rule ID = new Rule("ID");
  
  /**
   * The <b>(APP)</b> type rule.
   */
  public static final Rule APP = new Rule("APP");
  
  /**
   * The <b>(COND)</b> type rule.
   */
  public static final Rule COND = new Rule("COND");
  
  /**
   * The <b>(ABSTR)</b> type rule. 
   */
  public static final Rule ABSTR = new Rule("ABSTR");
  
  /**
   * The <b>(LET)</b> type rule.
   */
  public static final Rule LET = new Rule("LET");
  
  /**
   * The <b>(LET-REC)</b> type rule.
   */
  public static final Rule LET_REC = new Rule("LET-REC");
  
  /**
   * The <b>(REC)</b> type rule.
   */
  public static final Rule REC = new Rule("REC");
  
  /**
   * The <b>(INFIX)</b> type rule.
   */
  public static final Rule INFIX = new Rule("INFIX");
  
  /**
   * The <b>(AND)</b> type rule.
   */
  public static final Rule AND = new Rule("AND");
  
  /**
   * The <b>(OR)</b> type rule.
   */
  public static final Rule OR = new Rule("OR");
  
  /**
   * Returns the list of all available rules.
   * 
   * @return the list of all available rules.
   */
  public static Collection<Rule> getAllRules() {
    LinkedList<Rule> rules = new LinkedList<Rule>();
    rules.add(CONST);
    rules.add(ID);
    rules.add(APP);
    rules.add(COND);
    rules.add(ABSTR);
    rules.add(LET);
    rules.add(LET_REC);
    rules.add(REC);
    rules.add(INFIX);
    rules.add(AND);
    rules.add(OR);
    return rules;
  }
  
  private Rule(String name) {
    this.name = name;
  }
  
  // member attributes
  private String name;
}
