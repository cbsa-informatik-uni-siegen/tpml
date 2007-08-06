package de.unisiegen.tpml.core.languages.l4 ;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.Assign ;
import de.unisiegen.tpml.core.expressions.Condition1 ;
import de.unisiegen.tpml.core.expressions.Deref ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.expressions.Sequence ;
import de.unisiegen.tpml.core.expressions.UnitConstant ;
import de.unisiegen.tpml.core.expressions.While ;
import de.unisiegen.tpml.core.interpreters.Store ;
import de.unisiegen.tpml.core.languages.l3.L3BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l3.L3Language ;


/**
 * Big step proof rules for the <b>L4</b> and derived languages.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev: 287 $
 * @see de.unisiegen.tpml.core.languages.l3.L3BigStepProofRuleSet
 */
public class L4BigStepProofRuleSet extends L3BigStepProofRuleSet
{
  /**
   * Allocates a new <code>L4BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L4</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L3BigStepProofRuleSet#L3BigStepProofRuleSet(L3Language)
   */
  public L4BigStepProofRuleSet ( L4Language language )
  {
    super ( language ) ;
    // register the big step rules (order is important for guessing!)
    registerByMethodName ( L4Language.L4 , "ASSIGN" , "applyAssign" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L4Language.L4 , "DEREF" , "applyDeref" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L4Language.L4 , "REF" , "applyRef" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L4Language.L4 , "SEQ" , "applySeq" , "updateSeq" ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L4Language.L4 , "WHILE" , "applyWhile" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateWhile" ) ; //$NON-NLS-1$
  }


  /**
   * Applies the <b>(ASSIGN)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyAssign ( BigStepProofContext context , BigStepProofNode node )
  {
    // depends on whether we have an Application or InfixOperation
    @ SuppressWarnings ( "unused" )
    Assign assign ;
    Expression e1 ;
    Expression e2 ;
    // check if Application or InfixOperation
    Expression e = node.getExpression ( ) ;
    if ( e instanceof Application )
    {
      // Application: (:= e1) e2
      Application a1 = ( Application ) e ;
      Application a2 = ( Application ) a1.getE1 ( ) ;
      assign = ( Assign ) a2.getE1 ( ) ;
      e1 = a2.getE2 ( ) ;
      e2 = a1.getE2 ( ) ;
    }
    else
    {
      // otherwise must be an InfixOperation
      InfixOperation infixOperation = ( InfixOperation ) e ;
      assign = ( Assign ) infixOperation.getOp ( ) ;
      e1 = infixOperation.getE1 ( ) ;
      e2 = infixOperation.getE2 ( ) ;
    }
    // assign a new value to the location
    Store store = node.getStore ( ) ;
    store.put ( ( Location ) e1 , e2 ) ;
    context.setProofNodeResult ( node , new UnitConstant ( ) , store ) ;
  }


  /**
   * Applies the <b>(DEREF)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyDeref ( BigStepProofContext context , BigStepProofNode node )
  {
    // the expression must be an application of the deref operator...
    Application application = ( Application ) node.getExpression ( ) ;
    @ SuppressWarnings ( "unused" )
    Deref e1 = ( Deref ) application.getE1 ( ) ;
    // ...to a location using the node's store
    Location e2 = ( Location ) application.getE2 ( ) ;
    context.setProofNodeResult ( node , node.getStore ( ).get ( e2 ) ) ;
  }


  /**
   * Applies the <b>(REF)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyRef ( BigStepProofContext context , BigStepProofNode node )
  {
    // the expression must be an application a the ref operator...
    Application application = ( Application ) node.getExpression ( ) ;
    @ SuppressWarnings ( "unused" )
    Ref e1 = ( Ref ) application.getE1 ( ) ;
    // ...to a value using the node's store
    Store store = node.getStore ( ) ;
    Location location = store.alloc ( ) ;
    store.put ( location , application.getE2 ( ) ) ;
    context.setProofNodeResult ( node , location , store ) ;
  }


  /**
   * Applies the <b>(SEQ)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applySeq ( BigStepProofContext context , BigStepProofNode node )
  {
    // add the proof node for e1
    Sequence sequence = ( Sequence ) node.getExpression ( ) ;
    context.addProofNode ( node , sequence.getE1 ( ) ) ;
    // add the proof node for e2 if memory is disabled
    if ( ! context.isMemoryEnabled ( ) )
    {
      context.addProofNode ( node , sequence.getE2 ( ) ) ;
    }
  }


  /**
   * Updates the <code>node</code>, to which <b>(SEQ)</b> was applied
   * previously, using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void updateSeq ( BigStepProofContext context , BigStepProofNode node )
  {
    // check if the first (and only) node is proven
    if ( node.getChildCount ( ) == 1 && node.getChildAt ( 0 ).isProven ( ) )
    {
      // add the proof node for e2
      context.addProofNode ( node , ( ( Sequence ) node.getExpression ( ) )
          .getE2 ( ) ) ;
    }
    else if ( node.getChildCount ( ) == 2 )
    {
      // forward the result of e2 (may be null)
      context.setProofNodeResult ( node , node.getChildAt ( 1 ).getResult ( ) ) ;
    }
  }


  /**
   * Applies the <b>(WHILE)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyWhile ( BigStepProofContext context , BigStepProofNode node )
  {
    While loop = ( While ) node.getExpression ( ) ;
    context.addProofNode ( node , new Condition1 ( loop.getE1 ( ) ,
        new Sequence ( loop.getE2 ( ) , loop ) ) ) ;
  }


  /**
   * Updates the <code>node</code>, to which <b>(WHILE)</b> was applied
   * previously, using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void updateWhile ( BigStepProofContext context , BigStepProofNode node )
  {
    // forward the proof result (may be null)
    context.setProofNodeResult ( node , node.getChildAt ( 0 ).getResult ( ) ) ;
  }
}
