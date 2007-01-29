package de.unisiegen.tpml.core.languages.l1cbn ;


import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;
import de.unisiegen.tpml.core.languages.l0.L0SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;


/**
 * Small step proof rules for the <code>L1CBN</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev: 1069 $
 * @see de.unisiegen.tpml.core.languages.l0.L0SmallStepProofRuleSet
 * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet
 */
public class L1CBNSmallStepProofRuleSet extends L1SmallStepProofRuleSet
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
   */
  public L1CBNSmallStepProofRuleSet ( L1Language language )
  {
    super ( language ) ;
    unregister ( "LET-EVAL" ) ; //$NON-NLS-1$
    unregister ( "APP-LEFT" ) ; //$NON-NLS-1$
    register ( L0Language.L0 , "APP-LEFT" , false ) ; //$NON-NLS-1$
    unregister ( "APP-RIGHT" ) ; //$NON-NLS-1$
    register ( L0Language.L0 , "APP-RIGHT" , false ) ; //$NON-NLS-1$
    unregister ( "BETA-V" ) ; //$NON-NLS-1$
    register ( L0Language.L0 , "BETA" , true ) ; //$NON-NLS-1$
    unregister ( "AND-EVAL" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "AND-EVAL" , false ) ; //$NON-NLS-1$
    unregister ( "AND-FALSE" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "AND-FALSE" , true ) ; //$NON-NLS-1$
    unregister ( "AND-TRUE" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "AND-TRUE" , true ) ; //$NON-NLS-1$
    unregister ( "COND-EVAL" ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "COND-EVAL" , false ) ; //$NON-NLS-1$
    unregister ( "COND-TRUE" ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "COND-TRUE" , true ) ; //$NON-NLS-1$
    unregister ( "COND-FALSE" ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "COND-FALSE" , true ) ; //$NON-NLS-1$
    unregister ( "LET-EXEC" ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "LET-EXEC" , true ) ; //$NON-NLS-1$
    unregister ( "NOT" ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "NOT" , true ) ; //$NON-NLS-1$
    unregister ( "OP" ) ; //$NON-NLS-1$
    register ( L1Language.L1 , "OP" , true ) ; //$NON-NLS-1$
    unregister ( "OR-EVAL" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "OR-EVAL" , false ) ; //$NON-NLS-1$
    unregister ( "OR-FALSE" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "OR-FALSE" , true ) ; //$NON-NLS-1$
    unregister ( "OR-TRUE" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "OR-TRUE" , true ) ; //$NON-NLS-1$
  }


  /**
   * Applies the <b>(BETA)</b> rule to the <code>application</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param lambda the first operand of the <code>application</code>.
   * @param e the second operand of the <code>application</code>.
   * @return the resulting expression.
   */
  @ Override
  public Expression applyLambda ( SmallStepProofContext context ,
      Application application , Lambda lambda , Expression e )
  {
    context.addProofStep ( getRuleByName ( "BETA" ) , application ) ; //$NON-NLS-1$
    return lambda.getE ( ).substitute ( lambda.getId ( ) , e ) ;
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
      context.addProofStep ( getRuleByName ( "APP-LEFT" ) , application ) ; //$NON-NLS-1$
      // try to evaluate e1
      e1 = evaluate ( context , e1 ) ;
      // exceptions need special handling
      return e1.isException ( ) ? e1 : new Application ( e1 , e2 ) ;
    }
    // check if e2 is not already a value and e1 is not an instance of Lambda
    if ( ( ! e2.isValue ( ) ) && ( ! ( e1 instanceof Lambda ) ) )
    {
      // we're about to perform (APP-RIGHT)
      context.addProofStep ( getRuleByName ( "APP-RIGHT" ) , application ) ; //$NON-NLS-1$
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
    // we can perform (LET-EXEC)
    context.addProofStep ( getRuleByName ( "LET-EXEC" ) , let ) ; //$NON-NLS-1$
    // and perform the substitution
    return let.getE2 ( ).substitute ( let.getId ( ) , let.getE1 ( ) ) ;
  }
}
