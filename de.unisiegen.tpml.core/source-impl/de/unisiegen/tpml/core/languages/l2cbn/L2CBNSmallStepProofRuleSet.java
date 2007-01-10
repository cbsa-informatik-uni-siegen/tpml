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
 * @version $Rev$
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
    unregister ( "UNFOLD" ) ; //$NON-NLS-1$
    register ( L2Language.L2 , "UNFOLD" , true ) ; //$NON-NLS-1$
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
    // we perform (UNFOLD)
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , letRec ) ; //$NON-NLS-1$
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
    // we can perform (UNFOLD)
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , curriedLetRec ) ; //$NON-NLS-1$
    // perform the substitution on e1
    e1 = e1.substitute ( identifiers [ 0 ] , new Recursion ( identifiers [ 0 ] ,
        types [ 0 ] , e1 ) ) ;
    // generate the new (LET) expression
    return new Let ( identifiers [ 0 ] , types [ 0 ] , e1 , e2 ) ;
  }
}
