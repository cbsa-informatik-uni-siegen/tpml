package de.unisiegen.tpml.core.languages.l0cbn ;


import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.languages.l0.L0BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;


/**
 * Big step proof rules for the <b>L0CBN</b> and derived languages.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class L0CBNBigStepProofRuleSet extends L0BigStepProofRuleSet
{
  /**
   * Allocates a new <code>L0CBNBigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L0CBN</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L0BigStepProofRuleSet#L0BigStepProofRuleSet(L0Language)
   */
  public L0CBNBigStepProofRuleSet ( L0Language language )
  {
    super ( language ) ;
    unregister ( "APP" ) ; //$NON-NLS-1$
    registerByMethodName ( L0Language.L0 , "APP-LEFT" , "applyApplicationLeft" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateApplicationLeft" ) ; //$NON-NLS-1$
    registerByMethodName ( L0Language.L0 , "APP-RIGHT" , //$NON-NLS-1$
        "applyApplicationRight" , "updateApplicationRight" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    unregister ( "BETA-V" ) ; //$NON-NLS-1$
    registerByMethodName ( L0Language.L0 , "BETA" , "applyBeta" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateBeta" ) ; //$NON-NLS-1$
    /*
     * Unregister and register, because the guess does otherwise not work.
     */
    unregister ( "VAL" ) ; //$NON-NLS-1$
    registerByMethodName ( L0Language.L0 , "VAL" , "applyValue" ) ; //$NON-NLS-1$ //$NON-NLS-2$
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
}