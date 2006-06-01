package smallstep;

import common.ProofRule;

/**
 * Represents a small step proof rule.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class SmallStepProofRule extends ProofRule {
  //
  // Small step rules
  //
  
  static final SmallStepProofRule AND_EVAL = new SmallStepProofRule(false, "AND-EVAL");
  
  static final SmallStepProofRule AND_EVAL_EXN = new SmallStepProofRule(false, "AND-EVAL-EXN");
  
  static final SmallStepProofRule AND_FALSE = new SmallStepProofRule(true, "AND-FALSE");
  
  static final SmallStepProofRule AND_TRUE = new SmallStepProofRule(true, "AND-TRUE");
  
  static final SmallStepProofRule APP_LEFT = new SmallStepProofRule(false, "APP-LEFT");
  
  static final SmallStepProofRule APP_LEFT_EXN = new SmallStepProofRule(false, "APP-LEFT-EXN");
  
  static final SmallStepProofRule APP_RIGHT = new SmallStepProofRule(false, "APP-RIGHT");
  
  static final SmallStepProofRule APP_RIGHT_EXN = new SmallStepProofRule(false, "APP-RIGHT-EXN");
  
  static final SmallStepProofRule ASSIGN = new SmallStepProofRule(true, "ASSIGN");
  
  static final SmallStepProofRule BETA_V = new SmallStepProofRule(true, "BETA-V");
  
  static final SmallStepProofRule BOP = new SmallStepProofRule(true, "BOP");
  
  static final SmallStepProofRule COND_EVAL = new SmallStepProofRule(false, "COND-EVAL");
  
  static final SmallStepProofRule COND_EVAL_EXN = new SmallStepProofRule(false, "COND-EVAL-EXN");
  
  static final SmallStepProofRule COND_FALSE = new SmallStepProofRule(true, "COND-FALSE");
  
  static final SmallStepProofRule COND_TRUE = new SmallStepProofRule(true, "COND-TRUE");
  
  static final SmallStepProofRule COND_1_EVAL = new SmallStepProofRule(false, "COND-1-EVAL");
  
  static final SmallStepProofRule COND_1_EVAL_EXN = new SmallStepProofRule(false, "COND-1-EVAL-EXN");
  
  static final SmallStepProofRule COND_1_FALSE = new SmallStepProofRule(true, "COND-1-FALSE");
  
  static final SmallStepProofRule COND_1_TRUE = new SmallStepProofRule(true, "COND-1-TRUE");
  
  static final SmallStepProofRule DEREF = new SmallStepProofRule(true, "DEREF");
  
  static final SmallStepProofRule FST = new SmallStepProofRule(true, "FST");
  
  static final SmallStepProofRule LET_EVAL = new SmallStepProofRule(false, "LET-EVAL");
  
  static final SmallStepProofRule LET_EVAL_EXN = new SmallStepProofRule(false, "LET-EVAL-EXN");
  
  static final SmallStepProofRule LET_EXEC = new SmallStepProofRule(true, "LET-EXEC");
  
  static final SmallStepProofRule OR_EVAL = new SmallStepProofRule(false, "OR-EVAL");
  
  static final SmallStepProofRule OR_EVAL_EXN = new SmallStepProofRule(false, "OR-EVAL-EXN");
  
  static final SmallStepProofRule OR_FALSE = new SmallStepProofRule(true, "OR-FALSE");
  
  static final SmallStepProofRule OR_TRUE = new SmallStepProofRule(true, "OR-TRUE");
  
  static final SmallStepProofRule REF = new SmallStepProofRule(true, "REF");
  
  static final SmallStepProofRule SEQ_EVAL = new SmallStepProofRule(false, "SEQ-EVAL");

  static final SmallStepProofRule SEQ_EVAL_EXN = new SmallStepProofRule(false, "SEQ-EVAL-EXN");
  
  static final SmallStepProofRule SEQ_EXEC = new SmallStepProofRule(true, "SEQ-EXEC");
  
  static final SmallStepProofRule SND = new SmallStepProofRule(true, "SND");
  
  static final SmallStepProofRule PROJ = new SmallStepProofRule(true, "PROJ");
  
  static final SmallStepProofRule TUPLE = new SmallStepProofRule(false, "TUPLE");
  
  static final SmallStepProofRule TUPLE_EXN = new SmallStepProofRule(false, "TUPLE-EXN");
  
  static final SmallStepProofRule UNFOLD = new SmallStepProofRule(true, "UNFOLD");
  
  static final SmallStepProofRule UOP = new SmallStepProofRule(true, "UOP");
  
  static final SmallStepProofRule WHILE = new SmallStepProofRule(true, "WHILE");
  

  static final SmallStepProofRule CONS = new SmallStepProofRule(true, "CONS");
  static final SmallStepProofRule LIST = new SmallStepProofRule(false, "LIST");
  static final SmallStepProofRule LIST_EXN = new SmallStepProofRule(false, "LIST-EXN");
  static final SmallStepProofRule HD = new SmallStepProofRule(true, "HD");
  static final SmallStepProofRule TL = new SmallStepProofRule(true, "TL");
  static final SmallStepProofRule HD_EMPTY = new SmallStepProofRule(true, "HD-EMPTY");
  static final SmallStepProofRule TL_EMPTY = new SmallStepProofRule(true, "TL-EMPTY");
  static final SmallStepProofRule IS_EMPTY_TRUE = new SmallStepProofRule(true, "IS-EMPTY-TRUE");
  static final SmallStepProofRule IS_EMPTY_FALSE = new SmallStepProofRule(true, "IS-EMPTY-FALSE");
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof rule with the given
   * <code>name</code>. If <code>axiom</code> is <code>true</code>
   * the rule has no premises.
   *
   * @param axiom whether the rule has no premises.
   * @param name the name of the small step rule.
   */
  private SmallStepProofRule(boolean axiom, String name) {
    super(axiom, name);
  }
}
