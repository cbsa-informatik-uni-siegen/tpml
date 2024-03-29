package de.unisiegen.tpml.core.expressions ;


/**
 * This interface includes the pretty print priorities, according to the
 * priority grammar, for the various expressions. It is implemented by the
 * {@link de.unisiegen.tpml.core.expressions.Expression} class, so one can
 * easily use the constants in this interface without having to explicitly use
 * this interface each time.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 */
interface PrettyPrintPriorities
{
  /**
   * The pretty print priority for identifiers like "let x = 1 in 1".
   */
  public static final int PRIO_ID = 0 ;


  /**
   * The pretty print priority for constants.
   */
  public static final int PRIO_CONSTANT = 6 ;


  /**
   * The pretty print priority for identifiers.
   */
  public static final int PRIO_IDENTIFIER = 6 ;


  /**
   * The pretty print priority for locations.
   */
  public static final int PRIO_LOCATION = 6 ;


  /**
   * The pretty print priority for applications.
   */
  public static final int PRIO_APPLICATION = 5 ;


  /**
   * The pretty print priority for the first expression in an application.
   */
  public static final int PRIO_APPLICATION_E1 = PRIO_APPLICATION ;


  /**
   * The pretty print priority for the second expression in an application.
   */
  public static final int PRIO_APPLICATION_E2 = PRIO_APPLICATION + 1 ;


  /**
   * The pretty print priority for conditions.
   */
  public static final int PRIO_CONDITION = 0 ;


  /**
   * The pretty print priority for the e0 of conditions.
   */
  public static final int PRIO_CONDITION_E0 = PRIO_CONDITION ;


  /**
   * The pretty print priority for the e1 of conditions.
   */
  public static final int PRIO_CONDITION_E1 = PRIO_CONDITION ;


  /**
   * The pretty print priority for the e2 of conditions.
   */
  public static final int PRIO_CONDITION_E2 = PRIO_CONDITION ;


  /**
   * The pretty print priority for exceptions.
   */
  public static final int PRIO_EXN = 2 ;


  /**
   * The pretty print priority for coercion expressions.
   */
  public static final int PRIO_COERCION = 6 ;


  /**
   * The pretty print priority for the body of the coercion expression
   */
  public static final int PRIO_COERCION_E = 0 ;


  /**
   * The pretty print priority for the first type of the coercion expressions.
   */
  public static final int PRIO_COERCION_TAU1 = 0 ;


  /**
   * The pretty print priority for the second type of the coercion expressions.
   */
  public static final int PRIO_COERCION_TAU2 = 0 ;


  /**
   * The pretty print priority for lambda expressions.
   */
  public static final int PRIO_LAMBDA = 0 ;


  /**
   * The pretty print priority for the type of the lambda expression (a type!).
   */
  public static final int PRIO_LAMBDA_TAU = 0 ;


  /**
   * The pretty print priority for the body of lambda expressions.
   */
  public static final int PRIO_LAMBDA_E = PRIO_LAMBDA ;


  /**
   * The pretty print priority for let expressions.
   */
  public static final int PRIO_LET = 0 ;


  /**
   * The pretty print priority for types in let expressions (types!).
   */
  public static final int PRIO_LET_TAU = 0 ;


  /**
   * The pretty print priority for the e1 of let expressions.
   */
  public static final int PRIO_LET_E1 = PRIO_LET ;


  /**
   * The pretty print priority for the e2 of let expressions.
   */
  public static final int PRIO_LET_E2 = PRIO_LET ;


  /**
   * The pretty print priority for recursions.
   */
  public static final int PRIO_REC = 0 ;


  /**
   * The pretty print priority for the type of the recursion (a type!).
   */
  public static final int PRIO_REC_TAU = 0 ;


  /**
   * The pretty print priority for the e of recursions.
   */
  public static final int PRIO_REC_E = PRIO_REC ;


  /**
   * The pretty print priority for infix plus.
   */
  public static final int PRIO_PLUS = 3 ;


  /**
   * The pretty print priority for infix minus.
   */
  public static final int PRIO_MINUS = PRIO_PLUS ;


  /**
   * The pretty print priority for infix multiplication.
   */
  public static final int PRIO_MULT = 4 ;


  /**
   * The pretty print priority for infix division.
   */
  public static final int PRIO_DIV = PRIO_MULT ;


  /**
   * The pretty print priority for infix modulo.
   */
  public static final int PRIO_MOD = PRIO_DIV ;


  /**
   * The pretty print priority for relational operators (less, greater, equal,
   * ...).
   */
  public static final int PRIO_RELATIONAL_OPERATOR = 2 ;


  /**
   * The pretty print priority for the assign operator (<code>:=</code>).
   */
  public static final int PRIO_ASSIGN = 1 ;


  /**
   * The pretty print priority for the binary cons operator (<code>::</code>).
   */
  public static final int PRIO_BINARY_CONS = 1 ;


  /**
   * The pretty print priority for while expressions.
   */
  public static final int PRIO_WHILE = 0 ;


  /**
   * The pretty print priority for the e1 of while expressions.
   */
  public static final int PRIO_WHILE_E1 = PRIO_WHILE ;


  /**
   * The pretty print priority for the e2 of while expressions.
   */
  public static final int PRIO_WHILE_E2 = PRIO_WHILE ;


  /**
   * The pretty print priority for sequences.
   */
  public static final int PRIO_SEQUENCE = 0 ;


  /**
   * The pretty print priority for the e1 of sequences.
   */
  public static final int PRIO_SEQUENCE_E1 = PRIO_SEQUENCE + 1 ;


  /**
   * The pretty print priority for the e2 of sequences.
   */
  public static final int PRIO_SEQUENCE_E2 = PRIO_SEQUENCE ;


  /**
   * The pretty print priority for tuples.
   */
  public static final int PRIO_TUPLE = 6 ;


  /**
   * The pretty print priority for sub expressions of tuples.
   */
  public static final int PRIO_TUPLE_E = 0 ;


  /**
   * The pretty print priority for lists.
   */
  public static final int PRIO_LIST = 6 ;


  /**
   * The pretty print priority for sub expressions of lists.
   */
  public static final int PRIO_LIST_E = PRIO_SEQUENCE + 1 ;


  /**
   * The pretty print priority for new expressions.
   */
  public static final int PRIO_NEW = 0 ;


  /**
   * The pretty print priority for the e of new expressions.
   */
  public static final int PRIO_NEW_E = 0 ;


  /**
   * The pretty print priority for and expressions.
   */
  public static final int PRIO_AND = 1 ;


  /**
   * The pretty print priority for the e1 of and expressions.
   */
  public static final int PRIO_AND_E1 = PRIO_AND ;


  /**
   * The pretty print priority for the e2 of and expressions.
   */
  public static final int PRIO_AND_E2 = PRIO_AND_E1 + 1 ;


  /**
   * The pretty print priority for or expressions.
   */
  public static final int PRIO_OR = 1 ;


  /**
   * The pretty print priority for the e1 of or expressions.
   */
  public static final int PRIO_OR_E1 = PRIO_OR ;


  /**
   * The pretty print priority for the e2 of or expressions.
   */
  public static final int PRIO_OR_E2 = PRIO_OR_E1 + 1 ;


  /**
   * The pretty print priority for {@link Class}s.
   */
  public static final int PRIO_CLASS = 0 ;


  /**
   * The pretty print priority for sub body of {@link Class}.
   */
  public static final int PRIO_CLASS_BODY = 0 ;


  /**
   * The pretty print priority for the type of {@link Class}.
   */
  public static final int PRIO_CLASS_TAU = 0 ;


  /**
   * The pretty print priority for {@link Inherit}s.
   */
  public static final int PRIO_INHERIT = 0 ;


  /**
   * The pretty print priority for the expression of {@link Inherit}s.
   */
  public static final int PRIO_INHERIT_E = 0 ;


  /**
   * The pretty print priority for the body of {@link Inherit}s.
   */
  public static final int PRIO_INHERIT_B = 0 ;


  /**
   * The pretty print priority for {@link ObjectExpr}s.
   */
  public static final int PRIO_OBJECTEXPR = 0 ;


  /**
   * The pretty print priority for sub rows of {@link ObjectExpr}s.
   */
  public static final int PRIO_OBJECTEXPR_ROW = 0 ;


  /**
   * The pretty print priority for the type of {@link ObjectExpr}s.
   */
  public static final int PRIO_OBJECTEXPR_TAU = 0 ;


  /**
   * The pretty print priority for {@link Row}s.
   */
  public static final int PRIO_ROW = 0 ;


  /**
   * The pretty print priority for sub expressions of {@link Row}s.
   */
  public static final int PRIO_ROW_E = 0 ;


  /**
   * The pretty print priority for {@link Duplication}s.
   */
  public static final int PRIO_DUPLICATION = 5 ;


  /**
   * The pretty print priority for sub expressions of {@link Duplication}s.
   */
  public static final int PRIO_DUPLICATION_E = 1 ;


  /**
   * The pretty print priority for {@link Send}s.
   */
  public static final int PRIO_SEND = 6 ;


  /**
   * The pretty print priority for sub expressions of {@link Send}s.
   */
  public static final int PRIO_SEND_E = 6 ;


  /**
   * The pretty print priority for {@link Method}s.
   */
  public static final int PRIO_METHOD = 0 ;


  /**
   * The pretty print priority for sub expressions of {@link Method}s.
   */
  public static final int PRIO_METHOD_E = 0 ;


  /**
   * The pretty print priority for the type of {@link Method}s.
   */
  public static final int PRIO_METHOD_TAU = 0 ;


  /**
   * The pretty print priority for {@link CurriedMethod}s.
   */
  public static final int PRIO_CURRIED_METHOD = 0 ;


  /**
   * The pretty print priority for sub expressions of {@link CurriedMethod}s.
   */
  public static final int PRIO_CURRIED_METHOD_E = 0 ;


  /**
   * The pretty print priority for the type of {@link CurriedMethod}s.
   */
  public static final int PRIO_CURRIED_METHOD_TAU = 0 ;


  /**
   * The pretty print priority for {@link Attribute}s.
   */
  public static final int PRIO_ATTRIBUTE = 0 ;


  /**
   * The pretty print priority for sub expressions of {@link Attribute}s.
   */
  public static final int PRIO_ATTRIBUTE_E = 0 ;


  /**
   * The pretty print priority for the type of {@link Attribute}s.
   */
  public static final int PRIO_ATTRIBUTE_TAU = 0 ;
}
