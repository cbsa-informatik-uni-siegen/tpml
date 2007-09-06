package de.unisiegen.tpml.core.latex ;


import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.bigstep.BigStepProofRule;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode;
import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.Coercion;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.CurriedMethod;
import de.unisiegen.tpml.core.expressions.Duplication;
import de.unisiegen.tpml.core.expressions.Exn;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Inherit;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.List;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.New;
import de.unisiegen.tpml.core.expressions.ObjectExpr;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.expressions.Send;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofRule;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofNode;
import de.unisiegen.tpml.core.smallstep.SmallStepProofRule;
import de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode;
import de.unisiegen.tpml.core.subtyping.SubTypingProofRule;
import de.unisiegen.tpml.core.subtypingrec.DefaultRecSubTypingProofNode;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofRule;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerTypeProofNode;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typeinference.TypeEquationListTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.core.typeinference.TypeJudgement;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.ClassType;
import de.unisiegen.tpml.core.types.IntegerType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.PolyType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.TypeName;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnifyType;
import de.unisiegen.tpml.core.types.UnitType;


/**
 * This interface includes the latex print commands.
 * 
 * @author Christian Fehler
 * @see LatexPrintable
 */
public interface LatexCommandNames
{
  /**
   * The indent of the latex child commands.
   */
  public static final int LATEX_INDENT = 2 ;


  /**
   * The string for a line break in the source code.
   */
  public static final String LATEX_LINE_BREAK_SOURCE_CODE = System
      .getProperty ( "line.separator" ) ; //$NON-NLS-1$


  /**
   * The string for a line break in the source code.
   */
  public static final String LATEX_LINE_BREAK_NEW_COMMAND = System
      .getProperty ( "line.separator" ) //$NON-NLS-1$
      + "             " ; //$NON-NLS-1$


  /**
   * The string for a line break in the source code.
   */
  public static final String LATEX_LINE_BREAK_NEW_COMMAND_INDENT = System
      .getProperty ( "line.separator" ) //$NON-NLS-1$
      + "               " ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword color.
   */
  public static final String LATEX_COLOR_KEYWORD = "ColorKeyword" ; //$NON-NLS-1$


  /**
   * The latex print command for the constant color.
   */
  public static final String LATEX_COLOR_CONSTANT = "ColorConstant" ; //$NON-NLS-1$


  /**
   * The latex print command for the identifier color.
   */
  public static final String LATEX_COLOR_IDENTIFIER = "ColorIdentifier" ; //$NON-NLS-1$


  /**
   * The latex print command for the expression color.
   */
  public static final String LATEX_COLOR_EXPRESSION = "ColorExpression" ; //$NON-NLS-1$


  /**
   * The latex print command for none color.
   */
  public static final String LATEX_COLOR_NONE = "ColorNone" ; //$NON-NLS-1$


  /**
   * The latex print command for the type color.
   */
  public static final String LATEX_COLOR_TYPE = "ColorType" ; //$NON-NLS-1$


  /**
   * The latex print command for the rule color.
   */
  public static final String LATEX_COLOR_RULE = "ColorRule" ; //$NON-NLS-1$


  /**
   * The latex print command for a new node in the type inference.
   */
  public static final String LATEX_TYPE_INFERENCE_NEW_NODE = "TypeInferenceNewNode" ; //$NON-NLS-1$


  /**
   * The latex print command for a new formula in the type inference.
   */
  public static final String LATEX_TYPE_INFERENCE_NEW_FORMULA = "TypeInferenceNewFormula" ; //$NON-NLS-1$


  /**
   * The latex print command for the completed rules.
   */
  public static final String LATEX_TYPE_INFERENCE_RULES_COMPLETED = "TypeInferenceRulesCompleted" ; //$NON-NLS-1$


  /**
   * The latex print command for a new node in the small step interpreter.
   */
  public static final String LATEX_SMALL_STEP_NEW_NODE = "SmallStepNewNode" ; //$NON-NLS-1$


  /**
   * The latex print command for the completed rules.
   */
  public static final String LATEX_SMALL_STEP_RULES_COMPLETED = "SmallStepRulesCompleted" ; //$NON-NLS-1$


  /**
   * The latex print command for a new rule in the small step interpreter.
   */
  public static final String LATEX_SMALL_STEP_NEW_RULE = "SmallStepNewRule" ; //$NON-NLS-1$


  /**
   * The latex print command for a space.
   */
  public static final String LATEX_SPACE = "\\ " ; //$NON-NLS-1$


  /**
   * The latex print command for a new line.
   */
  public static final String LATEX_NEW_LINE = "\\newline" ; //$NON-NLS-1$


  /**
   * The latex print command for a nail.
   */
  public static final String LATEX_NAIL = "\\vdash" ; //$NON-NLS-1$


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
   * The latex print command for a left bracket.
   */
  public static final String LATEX_LBRACKET = "[" ; //$NON-NLS-1$


  /**
   * The latex print command for a right bracket.
   */
  public static final String LATEX_RBRACKET = "]" ; //$NON-NLS-1$


  /**
   * The latex print command for an epsilon.
   */
  public static final String LATEX_EPSILON = "\\epsilon" ; //$NON-NLS-1$


  /**
   * The latex print command for the beginnig of keywords.
   */
  public static final String LATEX_KEY = "Key" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>val</code>.
   */
  public static final String LATEX_KEY_VAL = LATEX_KEY + "Val" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>&&</code>.
   */
  public static final String LATEX_KEY_AMPERAMPER = LATEX_KEY + "AmperAmper" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>||</code>.
   */
  public static final String LATEX_KEY_BARBAR = LATEX_KEY + "BarBar" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>class</code>.
   */
  public static final String LATEX_KEY_CLASS = LATEX_KEY + "Class" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>end</code>.
   */
  public static final String LATEX_KEY_END = LATEX_KEY + "End" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>if</code>.
   */
  public static final String LATEX_KEY_IF = LATEX_KEY + "If" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>then</code>.
   */
  public static final String LATEX_KEY_THEN = LATEX_KEY + "Then" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>else</code>.
   */
  public static final String LATEX_KEY_ELSE = LATEX_KEY + "Else" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>let</code>.
   */
  public static final String LATEX_KEY_LET = LATEX_KEY + "Let" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>in</code>.
   */
  public static final String LATEX_KEY_IN = LATEX_KEY + "In" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>rec</code>.
   */
  public static final String LATEX_KEY_REC = LATEX_KEY + "Rec" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>method</code>.
   */
  public static final String LATEX_KEY_METHOD = LATEX_KEY + "Method" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>inherit</code>.
   */
  public static final String LATEX_KEY_INHERIT = LATEX_KEY + "Inherit" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>from</code>.
   */
  public static final String LATEX_KEY_FROM = LATEX_KEY + "From" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>lambda</code>.
   */
  public static final String LATEX_KEY_LAMBDA = LATEX_KEY + "Lambda" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>new</code>.
   */
  public static final String LATEX_KEY_NEW = LATEX_KEY + "New" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>object</code>.
   */
  public static final String LATEX_KEY_OBJECT = LATEX_KEY + "Object" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>while</code>.
   */
  public static final String LATEX_KEY_WHILE = LATEX_KEY + "While" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>do</code>.
   */
  public static final String LATEX_KEY_DO = LATEX_KEY + "Do" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>solve</code>.
   */
  public static final String LATEX_KEY_SOLVE = LATEX_KEY + "Solve" ; //$NON-NLS-1$


  /**
   * The latex print command for the left paren of the keyword
   * <code>solve</code>.
   */
  public static final String LATEX_SOLVE_LPAREN = "SolveLeftParen" ; //$NON-NLS-1$


  /**
   * The latex print command for the right paren of the keyword
   * <code>solve</code>.
   */
  public static final String LATEX_SOLVE_RPAREN = "SolveRightParen" ; //$NON-NLS-1$


  /**
   * The latex print command for the substitutions begin.
   */
  public static final String LATEX_TYPE_INFERENCE_SUBSTITUTIONS_BEGIN = "TypeInferenceSubstitutionBegin" ; //$NON-NLS-1$


  /**
   * The latex print command for parentheis.
   */
  public static final String LATEX_PARENTHESIS = "Parenthesis" ; //$NON-NLS-1$


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
   * The latex print command for no type.
   */
  public static final String LATEX_NO_TYPE = "no type" ; //$NON-NLS-1$


  /**
   * The latex print command for no result.
   */
  public static final String LATEX_NO_RESULT = "no result" ; //$NON-NLS-1$


  /**
   * The latex print command for no rule.
   */
  public static final String LATEX_NO_RULE = "no rule" ; //$NON-NLS-1$


  /**
   * The latex print command for no store.
   */
  public static final String LATEX_NO_STORE = "no store" ; //$NON-NLS-1$

  /**
   * The latex print command for no quantified variables.
   */
  public static final String LATEX_NO_QUANTIFIED_VARIABLES = "no quantified variables" ; //$NON-NLS-1$


  /**
   * The latex print command for a for all.
   */
  public static final String LATEX_FORALL = "\\forall" ; //$NON-NLS-1$


  /**
   * The latex print command for a right triangle.
   */
  public static final String LATEX_RIGHT_TRIANGLE = "\\vartriangleright" ; //$NON-NLS-1$


  /**
   * The latex print command for an empty set.
   */
  public static final String LATEX_EMPTYSET = "\\emptyset" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>attr</code>.
   */
  public static final String LATEX_KEY_ATTR = LATEX_KEY + "Attr" ; //$NON-NLS-1$


  /**
   * The latex print command for a prefix of a command.
   */
  public static final String LATEX_PREFIX_COMMAND = "\\" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>bool</code>.
   */
  public static final String LATEX_KEY_BOOL = LATEX_KEY + "Bool" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>int</code>.
   */
  public static final String LATEX_KEY_INT = LATEX_KEY + "Int" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>unit</code>.
   */
  public static final String LATEX_KEY_UNIT = LATEX_KEY + "Unit" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>unify</code>.
   */
  public static final String LATEX_KEY_UNIFY = LATEX_KEY + "Unify" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>zeta</code>.
   */
  public static final String LATEX_KEY_ZETA = LATEX_KEY + "Zeta" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>list</code>.
   */
  public static final String LATEX_KEY_LIST = LATEX_KEY + "List" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>ref</code>.
   */
  public static final String LATEX_KEY_REF = LATEX_KEY + "Ref" ; //$NON-NLS-1$


  /**
   * The latex print command for the keyword <code>mu</code>.
   */
  public static final String LATEX_KEY_MU = LATEX_KEY + "Mu" ; //$NON-NLS-1$


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


  /**
   * The latex print command for {@link TypeSubstitutionList}.
   */
  public static final String LATEX_TYPE_SUBSTITUTION_LIST = "TypeSubstitutionList" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeJudgement}.
   */
  public static final String LATEX_TYPE_JUDGEMENT = "TypeJudgement" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link DefaultTypeCheckerExpressionProofNode}.
   */
  public static final String LATEX_TYPE_CHECKER_EXPRESSION_PROOF_NODE = "TypeCheckerExpressionProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link DefaultTypeCheckerTypeProofNode}.
   */
  public static final String LATEX_TYPE_CHECKER_TYPE_PROOF_NODE = "TypeCheckerTypeProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link DefaultBigStepProofNode}.
   */
  public static final String LATEX_BIG_STEP_PROOF_NODE = "BigStepProofNode" ;//$NON-NLS-1$


  /**
   * The latex print command for {@link SmallStepProofNode}.
   */
  public static final String LATEX_SMALL_STEP_PROOF_NODE = "SmallStepProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link BigStepProofResult}.
   */
  public static final String LATEX_BIG_STEP_PROOF_RESULT = "BigStepProofResult" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeInferenceProofNode}.
   */
  public static final String LATEX_TYPE_INFERENCE_PROOF_NODE = "TypeInferenceProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link DefaultSubTypingProofNode}.
   */
  public static final String LATEX_SUB_TYPING_PROOF_NODE = "SubTypingProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link DefaultRecSubTypingProofNode}.
   */
  public static final String LATEX_REC_SUB_TYPING_PROOF_NODE = "RecSubTypingProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MinimalTypingTypesProofNode}.
   */
  public static final String LATEX_MINIMAL_TYPING_TYPES_PROOF_NODE = "MinimalTypingTypesProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MinimalTypingExpressionProofNode}.
   */
  public static final String LATEX_MINIMAL_TYPING_EXPRESSION_PROOF_NODE = "MinimalTypingExpressionProofNode" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link SmallStepProofModel}.
   */
  public static final String LATEX_SMALL_STEP_PROOF_MODEL = "SmallStepProofModel" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeInferenceProofModel}.
   */
  public static final String LATEX_TYPE_INFERENCE_PROOF_MODEL = "TypeInferenceProofModel" ; //$NON-NLS-1$


  /**
   * The latex print command for small step arrow.
   */
  public static final String LATEX_SMALL_STEP_ARROW = "SmallStepArrow" ; //$NON-NLS-1$


  /**
   * The latex print command for a type inference equal.
   */
  public static final String LATEX_TYPE_INFERENCE_EQUAL = "TypeInferenceEqual" ; //$NON-NLS-1$


  /**
   * The latex print command for a type inference rule.
   */
  public static final String LATEX_TYPE_INFERENCE_RULE = "TypeInferenceRule" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link BigStepProofModel}.
   */
  public static final String LATEX_BIG_STEP_PROOF_MODEL = "BigStepProofModel" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MinimalTypingProofModel}.
   */
  public static final String LATEX_MINIMAL_TYPING_PROOF_MODEL = "BigStepProofModel" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link SmallStepProofRule}.
   */
  public static final String LATEX_SMALL_STEP_PROOF_RULE = "SmallStepProofRule" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link MinimalTypingProofRule}.
   */
  public static final String LATEX_MINIMAL_TYPING_PROOF_RULE = "MinimalTypingProofRule" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RecSubTypingProofRule}.
   */
  public static final String LATEX_REC_SUB_TYPING_PROOF_RULE = "RecSubTypingProofRule" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link SubTypingProofRule}.
   */
  public static final String LATEX_SUB_TYPING_PROOF_RULE = "SubTypingProofRule" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeCheckerProofRule}.
   */
  public static final String LATEX_TYPE_CHECKER_PROOF_RULE = "TypeCheckerProofRule" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link BigStepProofRule}.
   */
  public static final String LATEX_BIG_STEP_PROOF_RULE = "BigStepProofRule" ; //$NON-NLS-1$


  /**
   * The latex print command to build the tree
   */
  public static final String LATEX_MKTREE = "mktree" ; //$NON-NLS-1$


  /**
   * The latex print command to print an arrow.
   */
  public static final String LATEX_ARROW = "arrow" ; //$NON-NLS-1$


  /**
   * The latex print command to print by rule.
   */
  public static final String LATEX_BYRULE = "byrule" ; //$NON-NLS-1$
}
