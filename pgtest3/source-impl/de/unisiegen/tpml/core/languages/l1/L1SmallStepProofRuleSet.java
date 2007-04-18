package de.unisiegen.tpml.core.languages.l1 ;


import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.BinaryOperatorException ;
import de.unisiegen.tpml.core.expressions.BooleanConstant ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.Not ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;
import de.unisiegen.tpml.core.languages.l0.L0SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Small step proof rules for the <b>L1</b> and derived languages.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1166 $
 * @see de.unisiegen.tpml.core.languages.l0.L0SmallStepProofRuleSet
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 */
public class L1SmallStepProofRuleSet extends L0SmallStepProofRuleSet
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>L1SmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L1</tt> or a derived
   * language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L0SmallStepProofRuleSet#L0SmallStepProofRuleSet(L0Language)
   */
  public L1SmallStepProofRuleSet ( L1Language language )
  {
    super ( language ) ;
    // register small step rules
    register ( L1Language.L1 , "AND-EVAL" , false ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "AND-FALSE" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "AND-TRUE" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "COND-EVAL" , false ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "COND-TRUE" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "COND-FALSE" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "LET-EVAL" , false ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "LET-EXEC" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "NOT" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "OP" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "OR-EVAL" , false ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "OR-FALSE" , true ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "OR-TRUE" , true ) ; //$NON-NLS-1$
  }


  /**
   * The <code>apply()</code> method for applications, used to handle binary
   * operators.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the first operand.
   * @param e2 the second operand.
   * @return the resulting expression.
   */
  public Expression applyApplication ( SmallStepProofContext context ,
      Application application , Application e1 , Expression e2 )
  {
    return applyBinaryOperator ( context , application , ( BinaryOperator ) e1
        .getE1 ( ) , e1.getE2 ( ) , e2 ) ;
  }


  /**
   * Applies the <code>op</code> to <code>e1</code> and <code>e2</code>
   * using the <code>context</code>.
   * 
   * @param context the {@link SmallStepProofContext} for the application of
   *          <code>op</code>.
   * @param applicationOrInfix the
   *          {@link de.unisiegen.tpml.core.expressions.Application} or the
   *          {@link de.unisiegen.tpml.core.expressions.InfixOperation} to which
   *          the <code>op</code>, <code>e1</code> and <code>e2</code>
   *          belong.
   * @param op the binary operator to apply to <code>e1</code> and
   *          <code>e2</code>.
   * @param e1 the first operand for <code>op</code>.
   * @param e2 the second operand for <code>op</code>.
   * @return the resulting expression.
   */
  protected Expression applyBinaryOperator ( SmallStepProofContext context ,
      Expression applicationOrInfix , BinaryOperator op , Expression e1 ,
      Expression e2 )
  {
    try
    {
      Expression e = op.applyTo ( e1 , e2 ) ;
      context.addProofStep ( getRuleByName ( "OP" ) , applicationOrInfix ) ; //$NON-NLS-1$
      return e ;
    }
    catch ( BinaryOperatorException e )
    {
      return applicationOrInfix ;
    }
  }


  /**
   * Applies the {@link de.unisiegen.tpml.core.expressions.Not} operator
   * <code>e1</code> to the {@link BooleanConstant} <code>e2</code> using
   * the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the not operator.
   * @param e2 the operand.
   * @return the resulting expression.
   */
  public Expression applyNot ( SmallStepProofContext context ,
      Application application , Not e1 , BooleanConstant e2 )
  {
    try
    {
      Expression e = e1.applyTo ( e2 ) ;
      context.addProofStep ( getRuleByName ( "NOT" ) , application ) ; //$NON-NLS-1$
      return e ;
    }
    catch ( UnaryOperatorException e )
    {
      return application ;
    }
  }


  /**
   * Evaluates the <code>and</code> expression using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param and the {@link And} expression to evaluate.
   * @return the resulting expression.
   */
  public Expression evaluateAnd ( SmallStepProofContext context , And and )
  {
    // determine the sub expressions
    Expression e1 = and.getE1 ( ) ;
    Expression e2 = and.getE2 ( ) ;
    // check if e1 is not already a boolean constant
    if ( ! ( e1 instanceof BooleanConstant ) )
    {
      // we're about to perform (AND-EVAL)
      context.addProofStep ( getRuleByName ( "AND-EVAL" ) , and ) ; //$NON-NLS-1$
      // try to evaluate e1
      e1 = evaluate ( context , e1 ) ;
      // exceptions need special handling
      return e1.isException ( ) ? e1 : new And ( e1 , e2 ) ;
    }
    // determine the boolean constant value
    if ( ( ( BooleanConstant ) e1 ).booleanValue ( ) )
    {
      // jep, that's (AND-TRUE) then
      context.addProofStep ( getRuleByName ( "AND-TRUE" ) , and ) ; //$NON-NLS-1$
      return e2 ;
    }
    // jep, that's (AND-FALSE) then
    context.addProofStep ( getRuleByName ( "AND-FALSE" ) , and ) ; //$NON-NLS-1$
    return new BooleanConstant ( false ) ;
  }


  /**
   * Evaluates the <code>infixOperation</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param infixOperation the infix operation to evaluate.
   * @return the resulting expression.
   */
  public Expression evaluateInfixOperation ( SmallStepProofContext context ,
      InfixOperation infixOperation )
  {
    // determine the sub expressions and the operator
    Expression e1 = infixOperation.getE1 ( ) ;
    Expression e2 = infixOperation.getE2 ( ) ;
    BinaryOperator op = infixOperation.getOp ( ) ;
    // check if e1 is not already an integer constant
    if ( ! e1.isValue ( ) )
    {
      // we're about to perform (APP-LEFT) and (APP-RIGHT)
      context.addProofStep ( getRuleByName ( "APP-LEFT" ) , infixOperation ) ; //$NON-NLS-1$
      context.addProofStep ( getRuleByName ( "APP-RIGHT" ) , infixOperation ) ; //$NON-NLS-1$
      // try to evaluate e1
      e1 = evaluate ( context , e1 ) ;
      // exceptions need special handling
      return e1.isException ( ) ? e1 : new InfixOperation ( op , e1 , e2 ) ;
    }
    // check if e2 is not already a value
    if ( ! e2.isValue ( ) )
    {
      // we're about to perform (APP-RIGHT)
      context.addProofStep ( getRuleByName ( "APP-RIGHT" ) , infixOperation ) ; //$NON-NLS-1$
      // try to evaluate e2
      e2 = evaluate ( context , e2 ) ;
      // exceptions need special handling
      return e2.isException ( ) ? e2 : new InfixOperation ( op , e1 , e2 ) ;
    }
    // try to perform the application
    return applyBinaryOperator ( context , infixOperation , op , e1 , e2 ) ;
  }


  /**
   * Evaluates the <code>condition</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param condition the condition to evaluate.
   * @return the resulting expression.
   */
  public Expression evaluateCondition ( SmallStepProofContext context ,
      Condition condition )
  {
    // determine the sub expression
    Expression e0 = condition.getE0 ( ) ;
    Expression e1 = condition.getE1 ( ) ;
    Expression e2 = condition.getE2 ( ) ;
    // check if e0 is not already a boolean constant
    if ( ! ( e0 instanceof BooleanConstant ) )
    {
      // we're about to perform (COND-EVAL)
      context.addProofStep ( getRuleByName ( "COND-EVAL" ) , condition ) ; //$NON-NLS-1$
      // try to evaluate e0
      e0 = evaluate ( context , e0 ) ;
      // exceptions need special handling
      return e0.isException ( ) ? e0 : new Condition ( e0 , e1 , e2 ) ;
    }
    // determine the boolean constant value
    if ( ( ( BooleanConstant ) e0 ).booleanValue ( ) )
    {
      // jep, that's (COND-TRUE) then
      context.addProofStep ( getRuleByName ( "COND-TRUE" ) , condition ) ; //$NON-NLS-1$
      return e1 ;
    }
    // jep, that's (COND-FALSE) then
    context.addProofStep ( getRuleByName ( "COND-FALSE" ) , condition ) ; //$NON-NLS-1$
    return e2 ;
  }


  /**
   * Evaluates the curried let expression <code>curriedLet</code> using
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param curriedLet the curried let expression.
   * @return the resulting expression.
   */
  public Expression evaluateCurriedLet ( SmallStepProofContext context ,
      CurriedLet curriedLet )
  {
    // determine the sub expressions and the identifiers
    Identifier [ ] identifiers = curriedLet.getIdentifiers ( ) ;
    MonoType [ ] types = curriedLet.getTypes ( ) ;
    Expression e1 = curriedLet.getE1 ( ) ;
    Expression e2 = curriedLet.getE2 ( ) ;
    // prepend the lambda abstractions to e1
    for ( int n = identifiers.length - 1 ; n >= 1 ; -- n )
    {
      e1 = new Lambda ( identifiers [ n ] , types [ n ] , e1 ) ;
    }
    // we can simply perform (LET-EXEC)
    context.addProofStep ( getRuleByName ( "LET-EXEC" ) , curriedLet ) ; //$NON-NLS-1$
    // and perform the substitution
    return e2.substitute ( identifiers [ 0 ] , e1 ) ;
  }


  /**
   * Evaluates the <code>let</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param let the let expression to evaluate.
   * @return the resulting expression.
   */
  public Expression evaluateLet ( SmallStepProofContext context , Let let )
  {
    // determine the sub expressions and the identifier
    Expression e1 = let.getE1 ( ) ;
    Expression e2 = let.getE2 ( ) ;
    // check if e1 is not already a value
    if ( ! e1.isValue ( ) )
    {
      // we're about to perform (LET-EVAL)
      context.addProofStep ( getRuleByName ( "LET-EVAL" ) , let ) ; //$NON-NLS-1$
      // try to evaluate e1
      e1 = evaluate ( context , e1 ) ;
      // exceptions need special treatment
      return e1.isException ( ) ? e1 : new Let ( let.getId ( ) ,
          let.getTau ( ) , e1 , e2 ) ;
    }
    // we can perform (LET-EXEC)
    context.addProofStep ( getRuleByName ( "LET-EXEC" ) , let ) ; //$NON-NLS-1$
    // and perform the substitution
    return e2.substitute ( let.getId ( ) , e1 ) ;
  }


  /**
   * Evaluates the <code>or</code> expression using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param or the {@link Or} expression to evaluate.
   * @return the resulting expression.
   */
  public Expression evaluateOr ( SmallStepProofContext context , Or or )
  {
    // determine the sub expressions
    Expression e1 = or.getE1 ( ) ;
    Expression e2 = or.getE2 ( ) ;
    // check if e1 is not already a boolean constant
    if ( ! ( e1 instanceof BooleanConstant ) )
    {
      // we're about to perform (OR-EVAL)
      context.addProofStep ( getRuleByName ( "OR-EVAL" ) , or ) ; //$NON-NLS-1$
      // try to evaluate e1
      e1 = evaluate ( context , e1 ) ;
      // exceptions need special treatment
      return e1.isException ( ) ? e1 : new Or ( e1 , e2 ) ;
    }
    // determine the boolean constant value
    if ( ( ( BooleanConstant ) e1 ).booleanValue ( ) )
    {
      // jep, that's (OR-TRUE) then
      context.addProofStep ( getRuleByName ( "OR-TRUE" ) , or ) ; //$NON-NLS-1$
      return new BooleanConstant ( true ) ;
    }
    // jep, that's (OR-FALSE) then
    context.addProofStep ( getRuleByName ( "OR-FALSE" ) , or ) ; //$NON-NLS-1$
    return e2 ;
  }
}
