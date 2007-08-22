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
   * The latex print command for the keyword <code>val</code>.
   */
  public static final String LATEX_KEYWORD_VAL = "KeywordVal" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>class</code>.
   */
  public static final String LATEX_KEYWORD_CLASS = "KeywordClass" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>end</code>.
   */
  public static final String LATEX_KEYWORD_END = "KeywordEnd" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>if</code>.
   */
  public static final String LATEX_KEYWORD_IF = "KeywordIf" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>then</code>.
   */
  public static final String LATEX_KEYWORD_THEN = "KeywordThen" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>else</code>.
   */
  public static final String LATEX_KEYWORD_ELSE = "KeywordElse" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>let</code>.
   */
  public static final String LATEX_KEYWORD_LET = "KeywordLet" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>in</code>.
   */
  public static final String LATEX_KEYWORD_IN = "KeywordIn" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>rec</code>.
   */
  public static final String LATEX_KEYWORD_REC = "KeywordRec" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>method</code>.
   */
  public static final String LATEX_KEYWORD_METHOD = "KeywordMethod" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>inherit</code>.
   */
  public static final String LATEX_KEYWORD_INHERIT = "KeywordInherit" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>from</code>.
   */
  public static final String LATEX_KEYWORD_FROM = "KeywordFrom" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>lambda</code>.
   */
  public static final String LATEX_KEYWORD_LAMBDA = "KeywordLambda" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>new</code>.
   */
  public static final String LATEX_KEYWORD_NEW = "KeywordNew" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>object</code>.
   */
  public static final String LATEX_KEYWORD_OBJECT = "KeywordObject" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>while</code>.
   */
  public static final String LATEX_KEYWORD_WHILE = "KeywordWhile" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>do</code>.
   */
  public static final String LATEX_KEYWORD_DO = "KeywordDo" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link And}.
   */
  public static final String LATEX_AND = "ExprAnd" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Application}.
   */
  public static final String LATEX_APPLICATION = "ExprApplication" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Attribute}.
   */
  public static final String LATEX_ATTRIBUTE = "ExprAttribute" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link BinaryOperator}.
   */
  public static final String LATEX_BINARY_OPERATOR = "ExprBinaryOperator" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Class}.
   */
  public static final String LATEX_CLASS = "ExprClass" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Coercion}.
   */
  public static final String LATEX_COERCION = "ExprCoercion" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Condition}.
   */
  public static final String LATEX_CONDITION = "ExprCondition" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Condition1}.
   */
  public static final String LATEX_CONDITION1 = "ExprConditionOne" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Constant}.
   */
  public static final String LATEX_CONSTANT = "ExprConstant" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link CurriedLet}.
   */
  public static final String LATEX_CURRIED_LET = "ExprCurriedLet" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link CurriedLetRec}.
   */
  public static final String LATEX_CURRIED_LET_REC = "ExprCurriedLetRec" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link CurriedMethod}.
   */
  public static final String LATEX_CURRIED_METHOD = "ExprCurriedMethod" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Duplication}.
   */
  public static final String LATEX_DUPLICATION = "ExprDuplication" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Exn}.
   */
  public static final String LATEX_EXN = "ExprExn" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Identifier}.
   */
  public static final String LATEX_IDENTIFIER = "ExprIdentifier" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link InfixOperation}.
   */
  public static final String LATEX_INFIX_OPERATION = "ExprInfixOperation" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Inherit}.
   */
  public static final String LATEX_INHERIT = "ExprInherit" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Lambda}.
   */
  public static final String LATEX_LAMBDA = "ExprLambda" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Let}.
   */
  public static final String LATEX_LET = "ExprLet" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link List}.
   */
  public static final String LATEX_LIST = "ExprList" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Location}.
   */
  public static final String LATEX_LOCATION = "ExprLocation" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Method}.
   */
  public static final String LATEX_METHOD = "ExprMethod" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MultiLambda}.
   */
  public static final String LATEX_MULTI_LAMBDA = "ExprMultiLambda" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MultiLet}.
   */
  public static final String LATEX_MULTI_LET = "ExprMultiLet" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link New}.
   */
  public static final String LATEX_NEW = "ExprNew" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ObjectExpr}.
   */
  public static final String LATEX_OBJECT_EXPR = "ExprObject" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link LetRec}.
   */
  public static final String LATEX_LET_REC = "ExprLetRec" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Or}.
   */
  public static final String LATEX_OR = "ExprOr" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Recursion}.
   */
  public static final String LATEX_RECURSION = "ExprRecursion" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Row}.
   */
  public static final String LATEX_ROW = "ExprRow" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Send}.
   */
  public static final String LATEX_SEND = "ExprSend" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Sequence}.
   */
  public static final String LATEX_SEQUENCE = "ExprSequence" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Tuple}.
   */
  public static final String LATEX_TUPLE = "ExprTuple" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link While}.
   */
  public static final String LATEX_WHILE = "ExprWhile" ; //$NON-NLS-1$
}
