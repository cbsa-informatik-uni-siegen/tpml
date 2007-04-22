package de.unisiegen.tpml.core.languages.l1cbn ;


import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.languages.l0.L0BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;
import de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Big step proof rules for the <b>L1CBN</b> and derived languages.
 * 
 * @author Christian Fehler
 * @version $Rev: 1069 $
 */
public class L1CBNBigStepProofRuleSet extends L1BigStepProofRuleSet
{
  /**
   * Allocates a new <code>L1CBNBigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L1CBN</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L0BigStepProofRuleSet#L0BigStepProofRuleSet(L0Language)
   * @see L1BigStepProofRuleSet#L1BigStepProofRuleSet(L1Language)
   */
  public L1CBNBigStepProofRuleSet ( L1Language language )
  {
    super ( language ) ;
    unregister ( "APP" ) ; //$NON-NLS-1$
    registerByMethodName ( L1CBNLanguage.L1CBN ,
        "APP-LEFT" , "applyApplicationLeft" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateApplicationLeft" ) ; //$NON-NLS-1$
    registerByMethodName ( L1CBNLanguage.L1CBN , "APP-RIGHT" , //$NON-NLS-1$
        "applyApplicationRight" , "updateApplicationRight" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "BETA-V" ) ; //$NON-NLS-1$
    registerByMethodName ( L1CBNLanguage.L1CBN , "BETA" , "applyBeta" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateBeta" ) ; //$NON-NLS-1$
    /*
     * Unregister and register, because the guess does otherwise not work.
     */
    unregister ( "VAL" ) ; //$NON-NLS-1$
    registerByMethodName ( L0Language.L0 , "VAL" , "applyValue" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "AND-FALSE" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 ,
        "AND-FALSE" , "applyAnd" , "updateAndFalse" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "AND-TRUE" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 ,
        "AND-TRUE" , "applyAnd" , "updateAndTrue" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "COND-FALSE" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 ,
        "COND-FALSE" , "applyCond" , "updateCondFalse" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "COND-TRUE" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 ,
        "COND-TRUE" , "applyCond" , "updateCondTrue" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "LET" ) ; //$NON-NLS-1$
    registerByMethodName ( L1CBNLanguage.L1CBN ,
        "LET" , "applyLet" , "updateLet" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "NOT" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 , "NOT" , "applyNot" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "OP" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 , "OP" , "applyOp" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "OR-FALSE" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 ,
        "OR-FALSE" , "applyOr" , "updateOrFalse" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    unregister ( "OR-TRUE" ) ; //$NON-NLS-1$
    registerByMethodName ( L1Language.L1 ,
        "OR-TRUE" , "applyOr" , "updateOrTrue" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
        throw new IllegalArgumentException ( Messages
            .getString ( "LxCBNBigStepProofRuleSet.0" ) ) ; //$NON-NLS-1$
      }
      context.addProofNode ( node , application.getE1 ( ) ) ;
    }
    else
    {
      // otherwise it must be an infix operation
      InfixOperation infixOperation = ( InfixOperation ) e ;
      if ( infixOperation.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "LxCBNBigStepProofRuleSet.0" ) ) ; //$NON-NLS-1$
      }
      context.addProofNode ( node , new Application ( infixOperation.getOp ( ) ,
          infixOperation.getE1 ( ) ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(APP-LEFT)</b> was applied
   * previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(APP-LEFT)</b>.
   */
  public void updateApplicationLeft ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    // determine the expression for the node
    Expression e = node.getExpression ( ) ;
    // further operation depends on the number of child nodes
    if ( node.getChildCount ( ) == 1 && node.getChildAt ( 0 ).isProven ( ) )
    {
      // determine the first child node
      BigStepProofNode node0 = node.getChildAt ( 0 ) ;
      // add the second child node for the application/infixOperation
      if ( e instanceof Application )
      {
        // the Application case
        Application application = ( Application ) e ;
        Application newNode = new Application (
            node0.getResult ( ).getValue ( ) , application.getE2 ( ) ) ;
        context.addProofNode ( node , newNode ) ;
      }
      else
      {
        // the InfixOperation case
        InfixOperation infixOperation = ( InfixOperation ) e ;
        Application newNode = new Application (
            node0.getResult ( ).getValue ( ) , infixOperation.getE2 ( ) ) ;
        context.addProofNode ( node , newNode ) ;
      }
    }
    else if ( node.getChildCount ( ) == 2 )
    {
      // check if both child nodes are proven
      if ( node.getChildAt ( 0 ).isProven ( )
          && node.getChildAt ( 1 ).isProven ( ) )
      {
        context
            .setProofNodeResult ( node , node.getChildAt ( 1 ).getResult ( ) ) ;
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
        throw new IllegalArgumentException ( Messages
            .getString ( "LxCBNBigStepProofRuleSet.1" ) ) ; //$NON-NLS-1$
      }
      if ( application.getE1 ( ) instanceof Lambda )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "LxCBNBigStepProofRuleSet.2" ) ) ; //$NON-NLS-1$
      }
      context.addProofNode ( node , application.getE2 ( ) ) ;
    }
    else
    {
      // otherwise it must be an infix operation
      InfixOperation infixOperation = ( InfixOperation ) e ;
      if ( ! infixOperation.getE1 ( ).isValue ( ) )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "LxCBNBigStepProofRuleSet.1" ) ) ; //$NON-NLS-1$
      }
      if ( infixOperation.getE1 ( ) instanceof Lambda )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "LxCBNBigStepProofRuleSet.2" ) ) ; //$NON-NLS-1$
      }
      context.addProofNode ( node , infixOperation.getE2 ( ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(APP-RIGHT)</b> was applied
   * previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(APP-RIGHT)</b>.
   */
  public void updateApplicationRight ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    // determine the expression for the node
    Expression e = node.getExpression ( ) ;
    // further operation depends on the number of child nodes
    if ( node.getChildCount ( ) == 1 && node.getChildAt ( 0 ).isProven ( ) )
    {
      // determine the first child node
      BigStepProofNode node0 = node.getChildAt ( 0 ) ;
      // add the second child node for the application/infixOperation
      if ( e instanceof Application )
      {
        // the Application case
        Application application = ( Application ) e ;
        Application newNode = new Application ( application.getE1 ( ) , node0
            .getResult ( ).getValue ( ) ) ;
        context.addProofNode ( node , newNode ) ;
      }
      else
      {
        // the InfixOperation case
        InfixOperation infixOperation = ( InfixOperation ) e ;
        Application newNode = new Application ( new Application (
            infixOperation.getOp ( ) , infixOperation.getE1 ( ) ) , node0
            .getResult ( ).getValue ( ) ) ;
        context.addProofNode ( node , newNode ) ;
      }
    }
    else if ( node.getChildCount ( ) == 2 )
    {
      // check if both child nodes are proven
      if ( node.getChildAt ( 0 ).isProven ( )
          && node.getChildAt ( 1 ).isProven ( ) )
      {
        context
            .setProofNodeResult ( node , node.getChildAt ( 1 ).getResult ( ) ) ;
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
    Application application = ( Application ) node.getExpression ( ) ;
    Lambda lambda = ( Lambda ) application.getE1 ( ) ;
    context.addProofNode ( node , lambda.getE ( ).substitute (
        lambda.getId ( ) , application.getE2 ( ) ) ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(BETA)</b> was applied
   * previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(BETA)</b>.
   */
  public void updateBeta ( BigStepProofContext context , BigStepProofNode node )
  {
    // forward the result of the first child node to this node (may be null)
    context.setProofNodeResult ( node , node.getChildAt ( 0 ).getResult ( ) ) ;
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
    if ( e instanceof CurriedLet || e instanceof CurriedLetRec )
    {
      // determine the first sub expression
      CurriedLet curriedLet = ( CurriedLet ) e ;
      Expression e1 = curriedLet.getE1 ( ) ;
      // generate the appropriate lambda abstractions
      Identifier [ ] identifiers = curriedLet.getIdentifiers ( ) ;
      MonoType [ ] types = curriedLet.getTypes ( ) ;
      for ( int n = identifiers.length - 1 ; n > 0 ; -- n )
      {
        e1 = new Lambda ( identifiers [ n ] , types [ n ] , e1 ) ;
      }
      // add the recursion for letrec
      if ( e instanceof CurriedLetRec )
      {
        e1 = new Recursion ( identifiers [ 0 ] , types [ 0 ] , e1 ) ;
      }
      // add the proof node
      context.addProofNode ( node , curriedLet.getE2 ( ).substitute (
          identifiers [ 0 ] , e1 ) ) ;
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
      context.addProofNode ( node , let.getE2 ( ).substitute ( let.getId ( ) ,
          e1 ) ) ;
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
      // forward the result of the first child node
      context.setProofNodeResult ( node , node.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }
}
