package de.unisiegen.tpml.core.languages.l2 ;


import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Small step proof rules for the <code>L2</code> language.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet
 */
public class L2SmallStepProofRuleSet extends L1SmallStepProofRuleSet
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>L2SmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L2</tt> or a derived
   * language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L1SmallStepProofRuleSet#L1SmallStepProofRuleSet(L1Language)
   */
  public L2SmallStepProofRuleSet ( L2Language language )
  {
    super ( language ) ;
    // register small step rules
    register ( L2Language.L2 , "UNFOLD" , true ) ;
  }


  //
  // The (UNFOLD) rule
  //
  /**
   * Evaluates the recursive curried let expression <code>curriedLetRec</code>
   * using <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param curriedLetRec the recursive curried let expression.
   * @return the resulting expression.
   */
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
    context.addProofStep ( getRuleByName ( "LET-EVAL" ) , curriedLetRec ) ;
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , curriedLetRec ) ;
    // perform the substitution on e1
    e1 = e1.substitute ( identifiers [ 0 ] , new Recursion ( identifiers [ 0 ] ,
        types [ 0 ] , e1 ) ) ;
    // generate the new (LET) expression
    return new Let ( identifiers [ 0 ] , types [ 0 ] , e1 , e2 ) ;
  }


  /**
   * Evaluates the recursive let expression <code>letRec</code> using
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param letRec the recursive let expression.
   * @return the resulting expression.
   */
  public Expression evaluateLetRec ( SmallStepProofContext context ,
      LetRec letRec )
  {
    // determine the expressions and the identifier
    Expression e1 = letRec.getE1 ( ) ;
    Expression e2 = letRec.getE2 ( ) ;
    String id = letRec.getId ( ) ;
    // we perform (UNFOLD), which includes a (LET-EVAL)
    context.addProofStep ( getRuleByName ( "LET-EVAL" ) , letRec ) ;
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , letRec ) ;
    // perform the substitution on e1
    e1 = e1.substitute ( id , new Recursion ( id , letRec.getTau ( ) , e1 ) ) ;
    // generate the new (LET) expression
    return new Let ( id , letRec.getTau ( ) , e1 , e2 ) ;
  }


  /**
   * Evaluates the recursive expression <code>recursion</code> using
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param recursion the recursive expression.
   * @return the resulting expression.
   */
  public Expression evaluateRecursion ( SmallStepProofContext context ,
      Recursion recursion )
  {
    // determine the expression and the identifier
    Expression e = recursion.getE ( ) ;
    String id = recursion.getId ( ) ;
    // we can perform (UNFOLD)
    context.addProofStep ( getRuleByName ( "UNFOLD" ) , recursion ) ;
    // perform the substitution
    return e.substitute ( id , recursion ) ;
  }
}
