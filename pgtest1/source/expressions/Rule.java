package expressions;

/**
 * This class represents a small step rule as
 * used by the interpreter when evaluating an
 * expression.
 *
 * @author bmeurer
 * @version $Id:Rule.java 121 2006-04-28 16:45:27Z benny $
 */
@Deprecated
public final class Rule {
  /**
   * Represents the various types of supported rules for
   * the small step interpreter.
   */
  static class Type {
    /**
     * Returns <code>true</code> if the type represents an axiom rule.
     * @return <code>true</code> if the type represents an axiom rule.
     */
    public final boolean isAxiom() {
      return this.axiom;
    }
    
    /**
     * Returns the name of the rule type.
     * @return the name of the rule type.
     */
    public final String getName() {
      return this.name;
    }
    
    private Type(boolean axiom, String name) {
      this.axiom = axiom;
      this.name = name;
    }
    
    private boolean axiom;
    private String name;
  }
  
  /**
   * The <b>(OP)</b> axiom to evaluate binary operations.
   */
  public static final Type OP = new Type(true, "OP");
  
  /**
   * The <b>(BETA-V)</b> axiom to perform beta-reduction.
   */
  public static final Type BETA_VALUE = new Type(true, "BETA-V");
  
  /**
   * The <b>(APP-LEFT)</b> meta-rule to evaluate the left
   * part of an application.
   */
  public static final Type APP_LEFT = new Type(false, "APP-LEFT");
  
  /**
   * The <b>(APP-LEFT-EXN)</b> meta-rule which forwards an
   * exception that occurred while evaluating the left part
   * of an application.
   */
  public static final Type APP_LEFT_EXN = new Type(false, "APP-LEFT-EXN");
  
  /**
   * The <b>(APP-RIGHT)</b> meta-rule to evaluate the right
   * part of an application.
   */
  public static final Type APP_RIGHT = new Type(false, "APP-RIGHT");
  
  /**
   * The <b>(APP-RIGHT-EXN)</b> meta-rule which forwards an
   * exception that occurred while evaluating the left part
   * of an application.
   */
  public static final Type APP_RIGHT_EXN = new Type(false, "APP-RIGHT-EXN");
  
  /**
   * The <b>(COND-EVAL)</b> meta-rule to evaluate the conditional
   * expression of an <code>if then else</code> expression.
   */
  public static final Type COND_EVAL = new Type(false, "COND-EVAL");
  
  /**
   * The <b>(COND-EVAL-EXN)</b> meta-rule to forward exceptions
   * that occur while evaluating the conditional part of an
   * <code>if then else</code> expression.
   */
  public static final Type COND_EVAL_EXN = new Type(false, "COND-EVAL-EXN");
  
  /**
   * The <b>(COND-TRUE)</b> axiom to evaluate an <code>if then else</code>
   * expression whose conditional part was evaluated to <code>true</code>.
   */
  public static final Type COND_TRUE = new Type(true, "COND-TRUE");
  
  /**
   * The <b>(COND-FALSE)</b> axiom to evaluate an <code>if then else</code>
   * expression whose conditional part was evaluated to <code>false</code>.
   */
  public static final Type COND_FALSE = new Type(true, "COND-FALSE");
  
  /**
   * The <b>(LET-EVAL)</b> meta-rule evaluates the first expression
   * of a <code>let in</code> block.
   */
  public static final Type LET_EVAL = new Type(false, "LET-EVAL");
  
  /**
   * The <b>(LET-EVAL-EXN)</b> meta-rule forwards an exception that
   * occurred in the evaluation of the first expression of a <code>let
   * in</code> block.
   */
  public static final Type LET_EVAL_EXN = new Type(false, "LET-EVAL-EXN");
  
  /**
   * The <b>(LET-EXEC)</b> axiom executes a <code>let in</code> block
   * once the first expression is evaluated.
   */
  public static final Type LET_EXEC = new Type(true, "LET-EXEC");
  
  /**
   * The <b>(UNFOLD)</b> axiom executes a <code>rec</code> expression.
   */
  public static final Type UNFOLD = new Type(true, "UNFOLD");
  
  /**
   * The <b>(AND-EVAL)</b> meta-rule evaluates the first expression
   * of a <code>&&</code> block.
   */
  public static final Type AND_EVAL = new Type(false, "AND-EVAL");
  
  /**
   * The <b>(AND-EVAL-EXN)</b> meta-rule forwards an exception that
   * occurred in the evaluation of the first operand of a
   * <code>&&</code> expression.
   */
  public static final Type AND_EVAL_EXN = new Type(false, "AND-EVAL-EXN");
  
  /**
   * The <b>(AND-TRUE)</b> axiom executes the <code>&&</code> for
   * the case that the first operand evaluated to <code>true</code>.
   */
  public static final Type AND_TRUE = new Type(true, "AND-TRUE");
  
  /**
   * The <b>(AND-FALSE)</b> axiom executes the <code>&&</code> for
   * the case that the first operand evaluated to <code>false</code>.
   */
  public static final Type AND_FALSE = new Type(true, "AND-FALSE");
  
  /**
   * The <b>(OR-EVAL)</b> meta-rule evaluates the first expression
   * of a <code>||</code> block.
   */
  public static final Type OR_EVAL = new Type(false, "OR-EVAL");
  
  /**
   * The <b>(OR-EVAL-EXN)</b> meta-rule forwards an exception
   * that occurred in the evaluation of the first operand of
   * a <code>||</code> expression.
   */
  public static final Type OR_EVAL_EXN = new Type(false, "OR-EVAL-EXN");
  
  /**
   * The <b>(OR-TRUE)</b> axiom executes the <code>||</code> expression
   * for the case that the first operand evaluated to <code>true</code>.
   */
  public static final Type OR_TRUE = new Type(true, "OR-TRUE");
  
  /**
   * The <b>(OR-FALSE)</b> axiom executes the <code>||</code> expression
   * for the case that the first operand evaluated to <code>false</code>.
   */
  public static final Type OR_FALSE = new Type(true, "OR-FALSE");
  
  /**
   * The <b>(TUPLE)</b> meta-rule evaluates a tuples.
   */
  public static final Type TUPLE = new Type(false, "TUPLE");
  
  /**
   * The <b>(TUPLE-EXN)</b> meta-rule forwards an exception
   * that occurred in the evaluation of a pair.
   */
  public static final Type TUPLE_EXN = new Type(false, "TUPLE-EXN");
  
  /**
   * The <b>(PROJ)</b> axiom returns an item from a tuple.
   */
  public static final Type PROJ = new Type(true, "PROJ");
  
  /**
   * The <b>(FST)</b> axiom returns the first item of a pair.
   */
  public static final Type FST = new Type(true, "FST");
  
  /**
   * The <b>(SND)</b> axiom returns the second item of a pair.
   */
  public static final Type SND = new Type(true, "SND");
  
  /**
   * Creates a new rule, which was applied to <code>expression</code>
   * and is of the given <code>type</code>.
   * @param expression the expression to which the rule was applied.
   * @param type the type of the rule.
   */
  Rule(Expression expression, Type type) {
    this.expression = expression;
    this.type = type;
  }
  
  /**
   * Compares this rule to the given <code>obj</code> and returns
   * <code>true</code> if they are equal.
   * @return <code>true</code> if this is equal to <code>obj</code>
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof Rule) {
      Rule rule = (Rule)obj;
      return (rule.type == this.type && rule.expression.equals(this.expression));
    }
    return false;
  }
  
  /**
   * Checks whether this rule is an axiom.
   * @return <code>true</code> if this rule is an axiom.
   */
  public final boolean isAxiom() {
    return this.type.isAxiom();
  }
  
  /**
   * Returns the expression to which this rule was applied.
   * @return the expression to which this rule was applied.
   */
  public final Expression getExpression() {
    return this.expression;
  }
  
  /**
   * Returns the name of this rule.
   * @return the name of this rule.
   */
  public final String getName() {
    return this.type.getName();
  }
  
  /**
   * Returns the string representation of the rule.
   * @return the string representation of the rule.
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    return getName();
  }

  private Expression expression;
  private Type type;
}
