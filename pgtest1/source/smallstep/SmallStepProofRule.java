package smallstep;

import java.util.HashMap;

import common.ProofRule;

/**
 * Represents a small step proof rule.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class SmallStepProofRule extends ProofRule {
  //
  // Constants
  //
  
  /**
   * The definition of the small step rules as pairs of
   * strings and booleans, where the string is the name
   * of the rule and the boolean value indicates whether
   * the rule is an axiom or a meta rule.
   * 
   * The EXN rules are generated automatically for each
   * meta rule.
   */
  private static final Object[] RULES = {
    // application rules
    "APP-LEFT", false,
    "APP-RIGHT", false,
    "BETA-V", true,
    
    // condition rules
    "COND-EVAL", false,
    "COND-FALSE", true,
    "COND-TRUE", true,
    
    // let rules
    "LET-EVAL", false,
    "LET-EXEC", true,
    
    // operator rules
    "BOP", true,
    "UOP", true,
    
    // recursion rules
    "UNFOLD", true,
    
    // imperative rules
    "ASSIGN", true,
    "DEREF", true,
    "REF", true,
    "SEQ-EVAL", false,
    "SEQ-EXEC", true,
    "WHILE", true,
    "COND-1-EVAL", false,
    "COND-1-FALSE", true,
    "COND-1-TRUE", true,
    
    // tuple rules
    "FST", true,
    "SND", true,
    "PROJ", true,
    "TUPLE", false,
    
    // list rules
    "CONS", true,
    "LIST", false,
    "HD", true,
    "TL", true,
    "HD-EMPTY", true,
    "TL-EMPTY", true,
    "IS-EMPTY-TRUE", true,
    "IS-EMPTY-FALSE", true,
    
    // && rules
    "AND-EVAL", false,
    "AND-FALSE", true,
    "AND-TRUE", true,
    
    // || rules
    "OR-EVAL", false,
    "OR-FALSE", true,
    "OR-TRUE", true,
  };
  
  
  
  //
  // Class attributes
  //
  
  /**
   * A mapping of rule names to rules, generated automatically
   * from the {@link #RULES} when the class is loaded.
   * 
   * @see #RULES
   */
  private static HashMap<String, SmallStepProofRule> rules;
  
  
  
  //
  // Class loading
  //
  
  static {
    // generate the rules mapping from the RULES array
    rules = new HashMap<String, SmallStepProofRule>();
    for (int n = 0; n < RULES.length; n += 2) {
      // generate the rule
      boolean axiom = ((Boolean)RULES[n + 1]).booleanValue();
      String name = (String)RULES[n + 0];
      
      // determine the associated EXN rule
      SmallStepProofRule exnRule = null;
      if (!axiom) {
        // generate the associated EXN rule
        exnRule = new SmallStepProofRule(axiom, name + "-EXN", null);
        rules.put(name + "-EXN", exnRule);
      }
      
      // generate the proof rule
      rules.put(name, new SmallStepProofRule(axiom, name, exnRule));
    }
  }
  
  
  
  //
  // Class methods
  //
  
  /**
   * Returns the <code>SmallStepProofRule</code> with
   * the specified <code>name</code>.
   * 
   * @return the <code>SmallStepProofRule</code> with
   *         the specified <code>name</code>.
   *         
   * @throws IllegalArgumentException if the <code>name</code> does not
   *                                  refer to a valid small step rule.
   */
  static SmallStepProofRule getRule(String name) {
    SmallStepProofRule rule = rules.get(name);
    if (rule == null) {
      throw new IllegalArgumentException("name is invalid");
    }
    else {
      return rule;
    }
  }
  
  /**
   * Returns all available <code>SmallStepProofRule</code>s,
   * except the <b>EXN</b> rules.
   * 
   * @return all available <code>SmallStepProofRule</code>s.
   */
  static SmallStepProofRule[] getRules() {
    SmallStepProofRule[] rules = new SmallStepProofRule[RULES.length / 2];
    for (int n = 0; n < rules.length; ++n) {
      rules[n] = getRule((String)RULES[n * 2]);
    }
    return rules;
  }
  
  
  
  //
  // Attributes
  //
  
  /**
   * The <b>EXN</b> rule associated with this meta-rule. This
   * is <code>null</code> for axioms and exception rules.
   * 
   * @see #getExnRule()
   */
  private SmallStepProofRule exnRule;
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new small step proof rule with the given
   * <code>name</code>. If <code>axiom</code> is <code>true</code>
   * the rule has no premises.
   *
   * @param axiom whether the rule has no premises.
   * @param name the name of the small step rule.
   * @param exnRule the <b>EXN</b> rule for this meta rule.
   */
  private SmallStepProofRule(boolean axiom, String name, SmallStepProofRule exnRule) {
    super(axiom, name);
    this.exnRule = exnRule;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the <b>EXN</b> rule for this meta rule. If this
   * rule is an axiom or an <b>EXN</b> rule itself, this
   * method will return a reference to this rule (this does
   * not really make sense, but its convenient to use).
   * 
   * @return the <b>EXN</b> rule for this meta rule.
   */
  public SmallStepProofRule getExnRule() {
    if (this.exnRule != null) {
      return this.exnRule;
    }
    else {
      return this;
    }
  }
}
