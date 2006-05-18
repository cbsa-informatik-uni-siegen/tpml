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
  
  static final SmallStepProofRule AND_EVAL = new SmallStepProofRule("AND-EVAL", 1);
  
  static final SmallStepProofRule AND_EVAL_EXN = new SmallStepProofRule("AND-EVAL-EXN", 1);
  
  static final SmallStepProofRule AND_FALSE = new SmallStepProofRule("AND-FALSE", 0);
  
  static final SmallStepProofRule AND_TRUE = new SmallStepProofRule("AND-TRUE", 0);
  
  static final SmallStepProofRule APP_LEFT = new SmallStepProofRule("APP-LEFT", 1);
  
  static final SmallStepProofRule APP_LEFT_EXN = new SmallStepProofRule("APP-LEFT-EXN", 1);
  
  static final SmallStepProofRule APP_RIGHT = new SmallStepProofRule("APP-RIGHT", 1);
  
  static final SmallStepProofRule APP_RIGHT_EXN = new SmallStepProofRule("APP-RIGHT-EXN", 1);
  
  static final SmallStepProofRule BETA_V = new SmallStepProofRule("BETA-V", 0);
  
  static final SmallStepProofRule COND_EVAL = new SmallStepProofRule("COND-EVAL", 1);
  
  static final SmallStepProofRule COND_EVAL_EXN = new SmallStepProofRule("COND-EVAL-EXN", 1);
  
  static final SmallStepProofRule COND_FALSE = new SmallStepProofRule("COND-FALSE", 0);
  
  static final SmallStepProofRule COND_TRUE = new SmallStepProofRule("COND-TRUE", 0);
  
  static final SmallStepProofRule DEREF = new SmallStepProofRule("DEREF", 0);
  
  static final SmallStepProofRule FST = new SmallStepProofRule("FST", 0);
  
  static final SmallStepProofRule LET_EVAL = new SmallStepProofRule("LET-EVAL", 1);
  
  static final SmallStepProofRule LET_EVAL_EXN = new SmallStepProofRule("LET-EVAL-EXN", 1);
  
  static final SmallStepProofRule LET_EXEC = new SmallStepProofRule("LET-EXEC", 0);
  
  static final SmallStepProofRule OP = new SmallStepProofRule("OP", 0);
  
  static final SmallStepProofRule OR_EVAL = new SmallStepProofRule("OR-EVAL", 1);
  
  static final SmallStepProofRule OR_EVAL_EXN = new SmallStepProofRule("OR-EVAL-EXN", 1);
  
  static final SmallStepProofRule OR_FALSE = new SmallStepProofRule("OR-FALSE", 0);
  
  static final SmallStepProofRule OR_TRUE = new SmallStepProofRule("OR-TRUE", 0);
  
  static final SmallStepProofRule REF = new SmallStepProofRule("REF", 0);
  
  static final SmallStepProofRule SND = new SmallStepProofRule("SND", 0);
  
  static final SmallStepProofRule PROJ = new SmallStepProofRule("PROJ", 0);
  
  static final SmallStepProofRule TUPLE = new SmallStepProofRule("TUPLE", 1);
  
  static final SmallStepProofRule TUPLE_EXN = new SmallStepProofRule("TUPLE-EXN", 1);
  
  static final SmallStepProofRule UNFOLD = new SmallStepProofRule("UNFOLD", 0);
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof rule with the given
   * <code>name</code> and number of <code>premises</code>.
   * 
   * @param name the name of the small step rule.
   * @param premises the number of premises.
   */
  private SmallStepProofRule(String name, int premises) {
    super(name, premises);
  }
}
