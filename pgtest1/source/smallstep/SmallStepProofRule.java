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
  
  static final SmallStepProofRule APP_LEFT = new SmallStepProofRule("APP-LEFT", 1);
  
  static final SmallStepProofRule APP_LEFT_EXN = new SmallStepProofRule("APP-LEFT-EXN", 1);
  
  static final SmallStepProofRule APP_RIGHT = new SmallStepProofRule("APP-RIGHT", 1);
  
  static final SmallStepProofRule APP_RIGHT_EXN = new SmallStepProofRule("APP-RIGHT-EXN", 1);
  
  static final SmallStepProofRule BETA_V = new SmallStepProofRule("BETA-V", 0);
  
  static final SmallStepProofRule OP = new SmallStepProofRule("OP", 0);
  
  
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
