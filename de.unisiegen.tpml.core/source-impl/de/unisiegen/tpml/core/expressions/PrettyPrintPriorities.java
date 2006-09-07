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
   * The pretty print priority for lambda expressions.
   */
  public static final int PRIO_LAMBDA = 0;
  
  /**
   * The pretty print priority for the body of lambda expressions.
   */
  public static final int PRIO_LAMBDA_E = PRIO_LAMBDA;
}
