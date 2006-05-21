package typing;

import java.util.Collection;
import java.util.LinkedList;

import expressions.Abstraction;
import expressions.And;
import expressions.Application;
import expressions.Condition;
import expressions.Constant;
import expressions.Expression;
import expressions.Identifier;
import expressions.InfixOperation;
import expressions.Let;
import expressions.LetRec;
import expressions.Or;
import expressions.Recursion;
import expressions.Tuple;


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
   * The polymorphic <b>(P-CONST)</b> type rule.
   */
  public static final Rule P_CONST = new Rule("P-CONST");
  
  /**
   * The <b>(ID)</b> type rule.
   */
  public static final Rule ID = new Rule("ID");
  
  /**
   * The polymorphic <b>(P-ID)</b> type rule.
   */
  public static final Rule P_ID = new Rule("P-ID");
  
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
   * The polymorphic <b>(P-LET)</b> type rule.
   */
  public static final Rule P_LET = new Rule("P-LET");
  
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
   * The <b>(TUPLE)</b> type rule.
   */
  public static final Rule TUPLE = new Rule("TUPLE");
  
  /**
   * Returns the list of all available rules.
   * 
   * @return the list of all available rules.
   */
  public static Collection<Rule> getAllRules() {
    LinkedList<Rule> rules = new LinkedList<Rule>();
    rules.add(CONST);
    rules.add(P_CONST);
    rules.add(ID);
    rules.add(P_ID);
    rules.add(APP);
    rules.add(COND);
    rules.add(ABSTR);
    rules.add(LET);
    rules.add(P_LET);
    rules.add(LET_REC);
    rules.add(REC);
    rules.add(INFIX);
    rules.add(AND);
    rules.add(OR);
    rules.add(TUPLE);
    return rules;
  }
  
  /**
   * Returns the type rule that can be applied to
   * the <code>expression</code> in the specified
   * type <code>environment</code>.
   * 
   * @param expression the {@link expressions.Expression} for which
   *                   to determine the type {@link Rule}.
   * @param environment the type {@link Environment}.                   
   *
   * @return the type rule for <code>expression</code> in the
   *         <code>environment</code>.
   *                           
   * @throws UnknownIdentifierException if <code>expression</code> is
   *                                    an unknown identifier. 
   */
  public static Rule getRuleForExpression(Expression expression, Environment environment) throws UnknownIdentifierException {
    if (expression instanceof Constant) {
      Type type = Type.getTypeForExpression(expression);
      return (type instanceof PolyType) ? Rule.P_CONST : Rule.CONST;
    }
    else if (expression instanceof Identifier) {
      Type type = environment.get(((Identifier)expression).getName());
      return (type instanceof PolyType) ? Rule.P_ID : Rule.ID;
    }
    else if (expression instanceof Application) {
      return Rule.APP;
    }
    else if (expression instanceof Condition) {
      return Rule.COND;
    }
    else if (expression instanceof Abstraction) {
      return Rule.ABSTR;
    }
    else if (expression instanceof Let) {
      Expression e1 = ((Let)expression).getE1();
      return e1.isValue() ? Rule.P_LET : Rule.LET;
    }
    else if (expression instanceof LetRec) {
      return Rule.LET_REC;
    }
    else if (expression instanceof Recursion) {
      return Rule.REC;
    }
    else if (expression instanceof InfixOperation) {
      return Rule.INFIX;
    }
    else if (expression instanceof And) {
      return Rule.AND;
    }
    else if (expression instanceof Or) {
      return Rule.OR;
    }
    else if (expression instanceof Tuple) {
      return Rule.TUPLE;
    }
    else {
      throw new IllegalArgumentException("Invalid expression " + expression);
    }
  }
  
  private Rule(String name) {
    this.name = name;
  }
  
  // member attributes
  private String name;
}
