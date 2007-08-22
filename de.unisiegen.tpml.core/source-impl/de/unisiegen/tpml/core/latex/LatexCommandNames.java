package de.unisiegen.tpml.core.latex ;


import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Coercion ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.Condition1 ;
import de.unisiegen.tpml.core.expressions.Constant ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Exn ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Inherit ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.New ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.expressions.Sequence ;
import de.unisiegen.tpml.core.expressions.Tuple ;
import de.unisiegen.tpml.core.expressions.While ;
import de.unisiegen.tpml.core.interpreters.Store ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType ;
import de.unisiegen.tpml.core.typechecker.SeenTypes ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.typeinference.TypeEquationListTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.typeinference.TypeSubType ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.BooleanType ;
import de.unisiegen.tpml.core.types.ClassType ;
import de.unisiegen.tpml.core.types.IntegerType ;
import de.unisiegen.tpml.core.types.ListType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.PolyType ;
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RefType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TupleType ;
import de.unisiegen.tpml.core.types.TypeName ;
import de.unisiegen.tpml.core.types.TypeVariable ;
import de.unisiegen.tpml.core.types.UnifyType ;
import de.unisiegen.tpml.core.types.UnitType ;


/**
 * This interface includes the latex print commands.
 * 
 * @author Christian Fehler
 * @see LatexPrintable
 */
public interface LatexCommandNames
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


  /**
   * The latex print command for a dot.
   */
  public static final String LATEX_DOT = "." ; //$NON-NLS-1$


  /**
   * The latex print command for a mult.
   */
  public static final String LATEX_MULT = "*" ; //$NON-NLS-1$


  /**
   * The latex print command for a bar.
   */
  public static final String LATEX_BAR = "'" ; //$NON-NLS-1$


  /**
   * The latex print command for an empty string.
   */
  public static final String LATEX_EMPTY_STRING = "" ; //$NON-NLS-1$


  /**
   * The latex print command for a for all.
   */
  public static final String LATEX_FORALL = "\\forall" ; //$NON-NLS-1$


  /**
   * The latex print command for an empty set.
   */
  public static final String LATEX_EMPTYSET = "\\emptyset" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>attr</code>.
   */
  public static final String LATEX_KEYWORD_ATTR = "KeywordAttr" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>bool</code>.
   */
  public static final String LATEX_KEYWORD_BOOL = "KeywordBool" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>int</code>.
   */
  public static final String LATEX_KEYWORD_INT = "KeywordInt" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>unit</code>.
   */
  public static final String LATEX_KEYWORD_UNIT = "KeywordUnit" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>unify</code>.
   */
  public static final String LATEX_KEYWORD_UNIFY = "KeywordUnify" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>zeta</code>.
   */
  public static final String LATEX_KEYWORD_ZETA = "KeywordZeta" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>list</code>.
   */
  public static final String LATEX_KEYWORD_LIST = "KeywordList" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>ref</code>.
   */
  public static final String LATEX_KEYWORD_REF = "KeywordRef" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>mu</code>.
   */
  public static final String LATEX_KEYWORD_MU = "KeywordMu" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ArrowType}.
   */
  public static final String LATEX_ARROW_TYPE = "TypeArrowType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link BooleanType}.
   */
  public static final String LATEX_BOOLEAN_TYPE = "TypeBooleanType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ClassType}.
   */
  public static final String LATEX_CLASS_TYPE = "TypeClassType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link IntegerType}.
   */
  public static final String LATEX_INTEGER_TYPE = "TypeIntegerType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ListType}.
   */
  public static final String LATEX_LIST_TYPE = "TypeListType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ObjectType}.
   */
  public static final String LATEX_OBJECT_TYPE = "TypeObjectType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link PolyType}.
   */
  public static final String LATEX_POLY_TYPE = "TypePolyType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RecType}.
   */
  public static final String LATEX_REC_TYPE = "TypeRecType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RefType}.
   */
  public static final String LATEX_REF_TYPE = "TypeRefType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RowType}.
   */
  public static final String LATEX_ROW_TYPE = "TypeRowType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TupleType}.
   */
  public static final String LATEX_TUPLE_TYPE = "TypeTupleType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeName}.
   */
  public static final String LATEX_TYPE_NAME = "TypeTypeName" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeVariable}.
   */
  public static final String LATEX_TYPE_VARIABLE = "TypeTypeVariable" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link UnifyType}.
   */
  public static final String LATEX_UNIFY_TYPE = "TypeUnifyType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link UnitType}.
   */
  public static final String LATEX_UNIT_TYPE = "TypeUnitType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Store}.
   */
  public static final String LATEX_STORE = "Store" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link DefaultSubType}.
   */
  public static final String LATEX_SUB_TYPE = "SubType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEnvironment}.
   */
  public static final String LATEX_TYPE_ENVIRONMENT = "TypeEnvironment" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link SeenTypes}.
   */
  public static final String LATEX_SEEN_TYPES = "SeenTypes" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationTypeChecker}.
   */
  public static final String LATEX_TYPE_EQUATION_TYPE_CHECKER = "TypeEquationTypeChecker" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeSubstitution}.
   */
  public static final String LATEX_TYPE_SUBSTITUTION = "TypeSubstitution" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationListTypeChecker}.
   */
  public static final String LATEX_TYPE_EQUATION_LIST_TYPE_CHECKER = "TypeEquationListTypeChecker" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationTypeInference}.
   */
  public static final String LATEX_TYPE_EQUATION_TYPE_INFERENCE = "TypeEquationTypeInference" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeSubType}.
   */
  public static final String LATEX_TYPE_SUB_TYPE = "TypeSubType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationListTypeInference}.
   */
  public static final String LATEX_TYPE_EQUATION_LIST_TYPE_INFERENCE = "TypeEquationListTypeInference" ; //$NON-NLS-1$
}
