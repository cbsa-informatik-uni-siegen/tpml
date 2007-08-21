package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.latex.LatexStringBuilder ;


/**
 * This interface includes the latex print commands.
 * 
 * @author Christian Fehler
 * @see Expression
 * @see LatexStringBuilder
 */
public interface LatexCommands
{
  /**
   * The latex print command for a space.
   */
  public static final String LATEX_SPACE = "\\ " ; //$NON-NLS-1$


  /**
   * The latex print command for a colon.
   */
  public static final String LATEX_COLON = "\\colon" ; //$NON-NLS-1$


  /**
   * The latex print command for a comma.
   */
  public static final String LATEX_COMMA = "," ; //$NON-NLS-1$


  /**
   * The latex print command for a semi.
   */
  public static final String LATEX_SEMI = ";" ; //$NON-NLS-1$


  /**
   * The latex print command for an equal.
   */
  public static final String LATEX_EQUAL = "=" ; //$NON-NLS-1$


  /**
   * The latex print command for a left paren.
   */
  public static final String LATEX_LPAREN = "(" ; //$NON-NLS-1$


  /**
   * The latex print command for a right paren.
   */
  public static final String LATEX_RPAREN = ")" ; //$NON-NLS-1$


  /**
   * The latex print command for an epsilon.
   */
  public static final String LATEX_EPSILON = "\\epsilon" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link And}.
   */
  public static final String LATEX_AND = "expAnd" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Application}.
   */
  public static final String LATEX_APPLICATION = "expApplication" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Attribute}.
   */
  public static final String LATEX_ATTRIBUTE = "expAttribute" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Class}.
   */
  public static final String LATEX_CLASS = "expClass" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Coercion}.
   */
  public static final String LATEX_COERCION = "expCoercion" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Condition}.
   */
  public static final String LATEX_CONDITION = "expCondition" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Condition1}.
   */
  public static final String LATEX_CONDITION1 = "expConditionOne" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Constant}.
   */
  public static final String LATEX_CONSTANT = "expConstant" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link CurriedLet}.
   */
  public static final String LATEX_CURRIED_LET = "expCurriedLet" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link CurriedLetRec}.
   */
  public static final String LATEX_CURRIED_LET_REC = "expCurriedLetRec" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link CurriedMethod}.
   */
  public static final String LATEX_CURRIED_METHOD = "expCurriedMethod" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Duplication}.
   */
  public static final String LATEX_DUPLICATION = "expDuplication" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Exn}.
   */
  public static final String LATEX_EXN = "expExn" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Identifier}.
   */
  public static final String LATEX_IDENTIFIER = "expIdentifier" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link InfixOperation}.
   */
  public static final String LATEX_INFIX_OPERATION = "expInfixOperation" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Inherit}.
   */
  public static final String LATEX_INHERIT = "expInherit" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Lambda}.
   */
  public static final String LATEX_LAMBDA = "expLambda" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Let}.
   */
  public static final String LATEX_LET = "expLet" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link List}.
   */
  public static final String LATEX_LIST = "expList" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Location}.
   */
  public static final String LATEX_LOCATION = "expLocation" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Method}.
   */
  public static final String LATEX_METHOD = "expMethod" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MultiLambda}.
   */
  public static final String LATEX_MULTI_LAMBDA = "expMultiLambda" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MultiLet}.
   */
  public static final String LATEX_MULTI_LET = "expMultiLet" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link New}.
   */
  public static final String LATEX_NEW = "expNew" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ObjectExpr}.
   */
  public static final String LATEX_OBJECT_EXPR = "expObjectExpr" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link LetRec}.
   */
  public static final String LATEX_LET_REC = "expLetRec" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Or}.
   */
  public static final String LATEX_OR = "expOr" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Recursion}.
   */
  public static final String LATEX_RECURSION = "expRecursion" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Row}.
   */
  public static final String LATEX_ROW = "expRow" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Send}.
   */
  public static final String LATEX_SEND = "expSend" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Sequence}.
   */
  public static final String LATEX_SEQUENCE = "expSequence" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Tuple}.
   */
  public static final String LATEX_TUPLE = "expTuple" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link While}.
   */
  public static final String LATEX_WHILE = "expWhile" ; //$NON-NLS-1$
}
