package de.unisiegen.tpml.core.languages.l2cbn ;


import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;
import de.unisiegen.tpml.core.languages.l0.L0SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Small step proof rules for the <code>L2CBN</code> language.
 * 
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.languages.l0.L0SmallStepProofRuleSet
 * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet
 * @see de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet
 */
public class L2CBNSmallStepProofRuleSet extends L2SmallStepProofRuleSet
{
  /**
   * Allocates a new <code>L2CBNSmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L2CBN</tt> or a
   * derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L0SmallStepProofRuleSet#L0SmallStepProofRuleSet(L0Language)
   * @see L1SmallStepProofRuleSet#L1SmallStepProofRuleSet(L1Language)
   * @see L2SmallStepProofRuleSet#L2SmallStepProofRuleSet(L2Language)
   */
  public L2CBNSmallStepProofRuleSet ( L2Language language )
  {
    super ( language ) ;
    unregister ( "LET-EVAL" ) ;
    unregister ( "APP-LEFT" ) ;
    register ( L0Language.L0 , "APP-LEFT" , false ) ;
    unregister ( "APP-RIGHT" ) ;
    register ( L0Language.L0 , "APP-RIGHT" , false ) ;
    unregister ( "BETA-V" ) ;
    register ( L0Language.L0 , "BETA" , true ) ;
    unregister ( "AND-EVAL" ) ;
    register ( L2Language.L2 , "AND-EVAL" , false ) ;
    unregister ( "AND-FALSE" ) ;
    register ( L2Language.L2 , "AND-FALSE" , true ) ;
    unregister ( "AND-TRUE" ) ;
    register ( L2Language.L2 , "AND-TRUE" , true ) ;
    unregister ( "COND-EVAL" ) ;
    register ( L1Language.L1 , "COND-EVAL" , false ) ;
    unregister ( "COND-TRUE" ) ;
    register ( L1Language.L1 , "COND-TRUE" , true ) ;
    unregister ( "COND-FALSE" ) ;
    register ( L1Language.L1 , "COND-FALSE" , true ) ;
    unregister ( "LET-EXEC" ) ;
    register ( L1Language.L1 , "LET-EXEC" , true ) ;
    unregister ( "NOT" ) ;
    register ( L1Language.L1 , "NOT" , true ) ;
    unregister ( "OP" ) ;
    register ( L1Language.L1 , "OP" , true ) ;
    unregister ( "OR-EVAL" ) ;
    register ( L2Language.L2 , "OR-EVAL" , false ) ;
    unregister ( "OR-FALSE" ) ;
    register ( L2Language.L2 , "OR-FALSE" , true ) ;
    unregister ( "OR-TRUE" ) ;
    register ( L2Language.L2 , "OR-TRUE" , true ) ;
    unregister ( "UNFOLD" ) ;
    register ( L2Language.L2 , "UNFOLD" , true ) ;
  }


  /**
   * Applies the <b>(BETA)</b> rule to the <code>application</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param lambda the first operand of the <code>application</code>.
   * @param v the second operand of the <code>application</code>.
   * @return the resulting expression.
   */
  @ Override
  public Expression applyLambda ( SmallStepProofContext context ,
      Application application , Lambda lambda , Expression v )
  {
    context.addProofStep ( getRuleByName ( "BETA" ) , application ) ;
    return lambda.getE ( ).substitute ( lambda.getId ( ) , v ) ;
  }


  /**
   * Evaluates the <code>application</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application to evaluate.
   * @return the resulting expression.
   */
  @ Override
  public Expression evaluateApplication ( SmallStepProofContext context ,
      Application application )
  {
    // determine the sub expressions
    Expression e1 = application.getE1 ( ) ;
    Expression e2 = application.getE2 ( ) ;
    // check if e1 is not already a value
    if ( ! e1.isValue ( ) )
    {
      // we're about to perform (APP-LEFT)
      context.addProofStep ( getRuleByName ( "APP-LEFT" ) , application ) ;
      // try to evaluate e1
      e1 = evaluate ( context , e1 ) ;
      // exceptions need special handling
      return e1.isException ( ) ? e1 : new Application ( e1 , e2 ) ;
    }
    // check if e2 is not already a value
    if ( ( ! e2.isValue ( ) ) && ( ! ( e1 instanceof Lambda ) ) )
    {
      // we're about to perform (APP-RIGHT)
      context.addProofStep ( getRuleByName ( "APP-RIGHT" ) , application ) ;
      // try to evaluate e2
      e2 = evaluate ( context , e2 ) ; // exceptions need special handling
      return e2.isException ( ) ? e2 : new Application ( e1 , e2 ) ;
    }
    // perform the application
    return apply ( context , application , e1 , e2 ) ;
  }


  /**
   * Evaluates the <code>let</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param let the let expression to evaluate.
   * @return the resulting expression.
   */
  @ Override
  public Expression evaluateLet ( SmallStepProofContext context , Let let )
  {
    // determine the sub expressions and the identifier
    Expression e1 = let.getE1 ( ) ;
    Expression e2 = let.getE2 ( ) ;
    String id = let.getId ( ) ;
    // TODO Exception
    // we can perform (LET-EXEC)
    context.addProofStep ( getRuleByName ( "LET-EXEC" ) , let ) ;
    // and perform the substitution
    return e2.substitute ( id , e1 ) ;
  }


  /**
   * Evaluates the recursive let expression <code>letRec</code> using
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param letRec the recursive let expression.
   * @return the resulting expression.
   */
  @ Override
  public Expression evaluateLetRec ( SmallStepProofContext context ,
      LetRec letRec )
  {
    // determine the expressions and the identifier
    Expression e1 = letRec.getE1 ( ) ;
    Expression e2 = letRec.getE2 ( ) ;
    String id = letRec.getId ( ) ;
    // we perform (UNFOLD), which includes a (LET-EVAL)
    // context.addProofStep(getRuleByName("LET-EVAL"), letRec);
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , letRec ) ;
    // perform the substitution on e1
    e1 = e1.substitute ( id , new Recursion ( id , letRec.getTau ( ) , e1 ) ) ;
    // generate the new (LET) expression
    return new Let ( id , letRec.getTau ( ) , e1 , e2 ) ;
  }


  /**
   * Evaluates the recursive curried let expression <code>curriedLetRec</code>
   * using <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param curriedLetRec the recursive curried let expression.
   * @return the resulting expression.
   */
  @ Override
  public Expression evaluateCurriedLetRec ( SmallStepProofContext context ,
      CurriedLetRec curriedLetRec )
  {
    // determine the sub expressions and the identifiers
    String [ ] identifiers = curriedLetRec.getIdentifiers ( ) ;
    MonoType [ ] types = curriedLetRec.getTypes ( ) ;
    Expression e1 = curriedLetRec.getE1 ( ) ;
    Expression e2 = curriedLetRec.getE2 ( ) ;
    // prepend the lambda abstractions to e1
    for ( int n = identifiers.length - 1 ; n >= 1 ; -- n )
    {
      e1 = new Lambda ( identifiers [ n ] , types [ n ] , e1 ) ;
    }
    // we can perform (UNFOLD), which includes a (LET-EVAL)
    // context.addProofStep(getRuleByName("LET-EVAL"), curriedLetRec);
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , curriedLetRec ) ;
    // perform the substitution on e1
    e1 = e1.substitute ( identifiers [ 0 ] , new Recursion ( identifiers [ 0 ] ,
        types [ 0 ] , e1 ) ) ;
    // generate the new (LET) expression
    return new Let ( identifiers [ 0 ] , types [ 0 ] , e1 , e2 ) ;
  }
}
