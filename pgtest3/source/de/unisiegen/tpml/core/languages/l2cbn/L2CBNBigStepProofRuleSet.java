package de.unisiegen.tpml.core.languages.l2cbn ;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Projection ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.languages.l0.L0BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;
import de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;


/**
 * Big step proof rules for the <b>L2CBN</b> and derived languages.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class L2CBNBigStepProofRuleSet extends L2BigStepProofRuleSet
{
  /**
   * Allocates a new <code>L2CBNBigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L2CBN</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L0BigStepProofRuleSet#L0BigStepProofRuleSet(L0Language)
   * @see L1BigStepProofRuleSet#L1BigStepProofRuleSet(L1Language)
   * @see L2BigStepProofRuleSet#L2BigStepProofRuleSet(L2Language)
   */
  public L2CBNBigStepProofRuleSet ( L2Language language )
  {
    super ( language ) ;
    unregister ( "APP" ) ;
    registerByMethodName ( L0Language.L0 , "APP-LEFT" , "applyApplicationLeft" ,
        "updateApplication" ) ;
    registerByMethodName ( L0Language.L0 , "APP-RIGHT" ,
        "applyApplicationRight" , "updateApplication" ) ;
    unregister ( "BETA-V" ) ;
    registerByMethodName ( L0Language.L0 , "BETA" , "applyBeta" ,
        "updateBetaValue" ) ;
    // Unregister and register because the guess function does not work
    unregister ( "VAL" ) ;
    registerByMethodName ( L0Language.L0 , "VAL" , "applyValue" ) ;
    unregister ( "AND-FALSE" ) ;
    registerByMethodName ( L2Language.L2 ,
        "AND-FALSE" , "applyAnd" , "updateAndFalse" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "AND-TRUE" ) ;
    registerByMethodName ( L2Language.L2 ,
        "AND-TRUE" , "applyAnd" , "updateAndTrue" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "COND-FALSE" ) ;
    registerByMethodName ( L1Language.L1 ,
        "COND-FALSE" , "applyCond" , "updateCondFalse" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "COND-TRUE" ) ;
    registerByMethodName ( L1Language.L1 ,
        "COND-TRUE" , "applyCond" , "updateCondTrue" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "LET" ) ;
    registerByMethodName ( L1Language.L1 , "LET" , "applyLet" , "updateLet" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "NOT" ) ;
    registerByMethodName ( L1Language.L1 , "NOT" , "applyNot" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "OP" ) ;
    registerByMethodName ( L1Language.L1 , "OP" , "applyOp" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "OR-FALSE" ) ;
    registerByMethodName ( L2Language.L2 ,
        "OR-FALSE" , "applyOr" , "updateOrFalse" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "OR-TRUE" ) ;
    registerByMethodName ( L2Language.L2 ,
        "OR-TRUE" , "applyOr" , "updateOrTrue" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "UNFOLD" ) ;
    registerByMethodName ( L2Language.L2 , "UNFOLD" , "applyUnfold" ,
        "updateUnfold" ) ;
  }


  /**
   * Applies the <b>(APP-LEFT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(APP-LEFT)</b> rule to.
   */
  public void applyApplicationLeft ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    // check which type of "application" we have here
    Expression e = node.getExpression ( ) ;
    if ( e instanceof Application )
    {
      // add the first node for the application
      Application application = ( Application ) e ;
      if ( application.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException (
            "(APP-LEFT) can only be applied if e1 is not already a value" ) ;
      }
      context.addProofNode ( node , application.getE1 ( ) ) ;
      // we can add the second node as well if memory is disabled
      if ( ! context.isMemoryEnabled ( ) )
      {
        context.addProofNode ( node , application.getE2 ( ) ) ;
      }
    }
    else
    {
      // otherwise it must be an infix operation
      InfixOperation infixOperation = ( InfixOperation ) e ;
      if ( infixOperation.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException (
            "(APP-LEFT) can only be applied if e1 is not already a value" ) ;
      }
      context.addProofNode ( node , new Application ( infixOperation.getOp ( ) ,
          infixOperation.getE1 ( ) ) ) ;
      // we can add the second as well if memory is disabled
      if ( ! context.isMemoryEnabled ( ) )
      {
        context.addProofNode ( node , infixOperation.getE2 ( ) ) ;
      }
    }
  }


  /**
   * Applies the <b>(APP-RIGHT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(APP-RIGHT)</b> rule to.
   */
  public void applyApplicationRight ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    // check which type of "application" we have here
    Expression e = node.getExpression ( ) ;
    if ( e instanceof Application )
    {
      // add the first node for the application
      Application application = ( Application ) e ;
      if ( ! application.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException (
            "(APP-RIGHT) can only be applied if e1 is already a value" ) ;
      }
      if ( application.getE1 ( ) instanceof Lambda )
      {
        throw new IllegalArgumentException (
            "(APP-RIGHT) can only be applied if v1 is a instance of Lambda" ) ;
      }
      context.addProofNode ( node , application.getE1 ( ) ) ;
      // we can add the second node as well if memory is disabled
      if ( ! context.isMemoryEnabled ( ) )
      {
        context.addProofNode ( node , application.getE2 ( ) ) ;
      }
    }
    else
    {
      // otherwise it must be an infix operation
      InfixOperation infixOperation = ( InfixOperation ) e ;
      if ( ! infixOperation.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException (
            "(APP-RIGHT) can only be applied if e1 is already a value" ) ;
      }
      if ( infixOperation.getE1 ( ) instanceof Lambda )
      {
        throw new IllegalArgumentException (
            "(APP-RIGHT) can only be applied if v1 is a instance of Lambda" ) ;
      }
      context.addProofNode ( node , new Application ( infixOperation.getOp ( ) ,
          infixOperation.getE1 ( ) ) ) ;
      // we can add the second as well if memory is disabled
      if ( ! context.isMemoryEnabled ( ) )
      {
        context.addProofNode ( node , infixOperation.getE2 ( ) ) ;
      }
    }
  }


  /**
   * Applies the <b>(BETA)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(BETA)</b> rule to.
   */
  public void applyBeta ( BigStepProofContext context , BigStepProofNode node )
  {
    // the expression must be an application to a value...
    Application application = ( Application ) node.getExpression ( ) ;
    Expression e2 = application.getE2 ( ) ;
    // ...with a lambda or multi lambda expression
    Expression e1 = application.getE1 ( ) ;
    // TODO MultiLambda
    if ( e1 instanceof MultiLambda )
    {
      // multi lambda is special
      MultiLambda multiLambda = ( MultiLambda ) e1 ;
      Expression e = multiLambda.getE ( ) ;
      // perform the required substitutions
      String [ ] identifiers = multiLambda.getIdentifiers ( ) ;
      for ( int n = 0 ; n < identifiers.length ; ++ n )
      {
        // substitute: (#l_n e2) for id
        e = e.substitute ( identifiers [ n ] , new Application (
            new Projection ( identifiers.length , n + 1 ) , e2 ) ) ;
      }
      // add the proof node for e
      context.addProofNode ( node , e ) ;
    }
    else
    {
      Lambda lambda = ( Lambda ) application.getE1 ( ) ;
      context.addProofNode ( node , lambda.getE ( ).substitute (
          lambda.getId ( ) , e2 ) ) ;
    }
  }


  /**
   * Applies the <b>(LET)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(LET)</b> rule to.
   */
  @ Override
  public void applyLet ( BigStepProofContext context , BigStepProofNode node )
  {
    Expression e = node.getExpression ( ) ;
    // TODO CurriedLet || CurriedLetRec
    if ( e instanceof CurriedLet || e instanceof CurriedLetRec )
    {
      // determine the first sub expression
      CurriedLet curriedLet = ( CurriedLet ) e ;
      Expression e1 = curriedLet.getE1 ( ) ;
      // generate the appropriate lambda abstractions
      String [ ] identifiers = curriedLet.getIdentifiers ( ) ;
      for ( int n = identifiers.length - 1 ; n > 0 ; -- n )
      {
        e1 = new Lambda ( identifiers [ n ] , null , e1 ) ;
      }
      // add the recursion for letrec
      if ( e instanceof CurriedLetRec )
      {
        e1 = new Recursion ( identifiers [ 0 ] , null , e1 ) ;
      }
      // add the proof node
      context.addProofNode ( node , e1 ) ;
    }
    // TODO MultiLet
    else if ( e instanceof MultiLet )
    {
      // prove the first sub expression
      context.addProofNode ( node , ( ( MultiLet ) e ).getE1 ( ) ) ;
    }
    else
    {
      // determine the first sub expression
      Let let = ( Let ) e ;
      Expression e1 = let.getE1 ( ) ;
      // add the recursion for letrec
      if ( e instanceof LetRec )
      {
        LetRec letRec = ( LetRec ) e ;
        e1 = new Recursion ( letRec.getId ( ) , letRec.getTau ( ) , e1 ) ;
      }
      // add the proof node
      // CALL BY NAME
      context.addProofNode ( node , let.getE2 ( ).substitute ( let.getId ( ) ,
          let.getE1 ( ) ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(LET)</b> was applied
   * previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(LET)</b>.
   */
  @ Override
  public void updateLet ( BigStepProofContext context , BigStepProofNode node )
  {
    // check if we have exactly one proven child node
    if ( node.getChildCount ( ) == 1 && node.getChildAt ( 0 ).isProven ( ) )
    {
      // determine the value of the first child node
      Expression value0 = node.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      // determine the expression for the node
      Expression e = node.getExpression ( ) ;
      // check the expression type
      // TODO CurriedLet
      if ( e instanceof CurriedLet )
      {
        // add a proof node for e2 (CurriedLet/CurriedLetRec)
        CurriedLet curriedLet = ( CurriedLet ) e ;
        context.addProofNode ( node , curriedLet.getE2 ( ).substitute (
            curriedLet.getIdentifiers ( ) [ 0 ] , value0 ) ) ;
      }
      // TODO MultiLet
      else if ( e instanceof MultiLet )
      {
        // determine the second sub expression e2 (MultiLet)
        MultiLet multiLet = ( MultiLet ) e ;
        Expression e2 = multiLet.getE2 ( ) ;
        // perform the required substitutions
        String [ ] identifiers = multiLet.getIdentifiers ( ) ;
        for ( int n = 0 ; n < identifiers.length ; ++ n )
        {
          // substitute: (#l_n value0) for id
          e2 = e2.substitute ( identifiers [ n ] , new Application (
              new Projection ( identifiers.length , n + 1 ) , value0 ) ) ;
        }
        // add a proof node for e2
        context.addProofNode ( node , e2 ) ;
      }
      else
      {
        // add a proof node for e2 (Let/LetRec)
        Let let = ( Let ) e ;
        // CALL BY NAME
        context.addProofNode ( node , let.getE2 ( ).substitute ( let.getId ( ) ,
            let.getE1 ( ) ) ) ;
      }
    }
    else if ( node.getChildCount ( ) == 2 )
    {
      // forward the result of the second child node
      context.setProofNodeResult ( node , node.getChildAt ( 1 ).getResult ( ) ) ;
    }
  }
}
