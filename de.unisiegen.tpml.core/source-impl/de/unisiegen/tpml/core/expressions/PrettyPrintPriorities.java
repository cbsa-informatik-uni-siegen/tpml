package de.unisiegen.tpml.core.expressions;

/**
 * This interface includes the pretty print priorities, according to the priority grammar, for the various
 * expressions. It is implemented by the {@link de.unisiegen.tpml.core.expressions.Expression} class, so one
 * can easily use the constants in this interface without having to explicitly use this interface each time.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 */
interface PrettyPrintPriorities {
  /**
   * The pretty print priority for constants.
   */
  public static final int PRIO_CONSTANT = 6;
  
  /**
   * The pretty print priority for identifiers.
   */
  public static final int PRIO_IDENTIFIER = 6;
  
  /**
   * The pretty print priority for locations.
   */
  public static final int PRIO_LOCATION = 6;
  
  /**
   * The pretty print priority for applications.
   */
  public static final int PRIO_APPLICATION = 5;
  
  /**
   * The pretty print priority for the first expression in an application.
   */
  public static final int PRIO_APPLICATION_E1 = PRIO_APPLICATION;
  
  /**
   * The pretty print priority for the second expression in an application.
   */
  public static final int PRIO_APPLICATION_E2 = PRIO_APPLICATION + 1;
  
  /**
   * The pretty print priority for conditions.
   */
  public static final int PRIO_CONDITION = 0;
  
  /**
   * The pretty print priority for the e0 of conditions.
   */
  public static final int PRIO_CONDITION_E0 = PRIO_CONDITION;
  
  /**
   * The pretty print priority for the e1 of conditions.
   */
  public static final int PRIO_CONDITION_E1 = PRIO_CONDITION;
  
  /**
   * The pretty print priority for the e2 of conditions.
   */
  public static final int PRIO_CONDITION_E2 = PRIO_CONDITION;
  
  /**
   * The pretty print priority for exceptions.
   */
  public static final int PRIO_EXN = 2;
  
  /**
   * The pretty print priority for lambda expressions.
   */
  public static final int PRIO_LAMBDA = 0;
  
  /**
   * The pretty print priority for the type of the lambda expression (a type!).
   */
  public static final int PRIO_LAMBDA_TAU = 0;
  
  /**
   * The pretty print priority for the body of lambda expressions.
   */
  public static final int PRIO_LAMBDA_E = PRIO_LAMBDA;
  
  /**
   * The pretty print priority for let expressions.
   */
  public static final int PRIO_LET = 0;
  
  /**
   * The pretty print priority for the e1 of let expressions.
   */
  public static final int PRIO_LET_E1 = PRIO_LET;
  
  /**
   * The pretty print priority for the e2 of let expressions.
   */
  public static final int PRIO_LET_E2 = PRIO_LET;
  
  /**
   * The pretty print priority for recursions.
   */
  public static final int PRIO_REC = 0;
  
  /**
   * The pretty print priority for the e of recursions.
   */
  public static final int PRIO_REC_E = PRIO_REC;
  
  /**
   * The pretty print priority for infix plus.
   */
  public static final int PRIO_PLUS = 3;
  
  /**
   * The pretty print priority for infix minus.
   */
  public static final int PRIO_MINUS = PRIO_PLUS;
  
  /**
   * The pretty print priority for infix multiplication.
   */
  public static final int PRIO_MULT = 4;
  
  /**
   * The pretty print priority for infix division.
   */
  public static final int PRIO_DIV = PRIO_MULT;
  
  /**
   * The pretty print priority for infix modulo.
   */
  public static final int PRIO_MOD = PRIO_DIV;
  
  /**
   * The pretty print priority for relational operators (less, greater, equal, ...).
   */
  public static final int PRIO_RELATIONAL_OPERATOR = 2;
}
