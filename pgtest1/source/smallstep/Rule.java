package smallstep;

/**
 * This class represents a small step rule as
 * used by the interpreter when evaluating an
 * expression.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class Rule {
  /**
   * The <b>(OP)</b> axiom to evaluate binary operations.
   */
  public static final Rule OP = new Rule(true, "OP");
  
  /**
   * The <b>(BETA-V)</b> axiom to perform beta-reduction.
   */
  public static final Rule BETA_VALUE = new Rule(true, "BETA-V");
  
  /**
   * The <b>(APP-LEFT)</b> meta-rule to evaluate the left
   * part of an application.
   */
  public static final Rule APP_LEFT = new Rule(false, "APP-LEFT");
  
  /**
   * The <b>(APP-LEFT-EXN)</b> meta-rule which forwards an
   * exception that occurred while evaluating the left part
   * of an application.
   */
  public static final Rule APP_LEFT_EXN = new Rule(false, "APP-LEFT-EXN");
  
  /**
   * The <b>(APP-RIGHT)</b> meta-rule to evaluate the right
   * part of an application.
   */
  public static final Rule APP_RIGHT = new Rule(false, "APP-RIGHT");
  
  /**
   * The <b>(APP-RIGHT-EXN)</b> meta-rule which forwards an
   * exception that occurred while evaluating the left part
   * of an application.
   */
  public static final Rule APP_RIGHT_EXN = new Rule(false, "APP-RIGHT-EXN");
  
  /**
   * The <b>(COND-EVAL)</b> meta-rule to evaluate the conditional
   * expression of an <code>if then else</code> expression.
   */
  public static final Rule COND_EVAL = new Rule(false, "COND-EVAL");
  
  /**
   * The <b>(COND-EVAL-EXN)</b> meta-rule to forward exceptions
   * that occur while evaluating the conditional part of an
   * <code>if then else</code> expression.
   */
  public static final Rule COND_EVAL_EXN = new Rule(false, "COND-EVAL-EXN");
  
  /**
   * The <b>(COND-TRUE)</b> axiom to evaluate an <code>if then else</code>
   * expression whose conditional part was evaluated to <code>true</code>.
   */
  public static final Rule COND_TRUE = new Rule(true, "COND-TRUE");
  
  /**
   * The <b>(COND-FALSE)</b> axiom to evaluate an <code>if then else</code>
   * expression whose conditional part was evaluated to <code>false</code>.
   */
  public static final Rule COND_FALSE = new Rule(true, "COND-FALSE");
  
  /**
   * The <b>(LET-EVAL)</b> meta-rule evaluates the first expression
   * of a <code>let in</code> block.
   */
  public static final Rule LET_EVAL = new Rule(false, "LET-EVAL");
  
  /**
   * The <b>(LET-EVAL-EXN)</b> meta-rule forwards an exception that
   * occurred in the evaluation of the first expression of a <code>let
   * in</code> block.
   */
  public static final Rule LET_EVAL_EXN = new Rule(false, "LET-EVAL-EXN");
  
  /**
   * The <b>(LET-EXEC)</b> axiom executes a <code>let in</code> block
   * once the first expression is evaluated.
   */
  public static final Rule LET_EXEC = new Rule(true, "LET-EXEC");
  
  /**
   * Checks whether this rule is an axiom.
   * @return <code>true</code> if this rule is an axiom.
   */
  public boolean isAxiom() {
    return this.axiom;
  }
  
  /**
   * Returns the name of this rule.
   * @return the name of this rule.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Returns the string representation of the rule.
   *
   * @return the string representation of the rule.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getName();
  }

  private Rule(boolean axiom, String name) {
    this.axiom = axiom;
    this.name = name;
  }
  
  private boolean axiom;
  private String name;
}
