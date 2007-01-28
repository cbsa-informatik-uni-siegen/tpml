package de.unisiegen.tpml.graphics.outline ;


import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineIdentifier ;
import de.unisiegen.tpml.graphics.outline.binding.OutlinePair ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineDisplayTree ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineTimerTask ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;


/**
 * This class is the main class of the {@link Outline}. It loads the
 * {@link OutlinePreferences}, creates the {@link OutlineUI} and loads new
 * {@link Expression}s.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class AbstractOutline implements Outline
{
  /**
   * The <code>String</code> for the name of a {@link Location}.
   */
  private static final String NAME = "Name" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GETEX = "getE[0-9]{1}" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for only one child.
   */
  private static final String GETE = "getE" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the integer one.
   */
  private static final String ONE = "1" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the integer two.
   */
  private static final String TWO = "2" ; //$NON-NLS-1$


  /**
   * String, between the description of the parent node, like e1, and the
   * description of the current node, like Identifier.
   */
  private static final String BETWEEN = "  -  " ; //$NON-NLS-1$


  /**
   * Indicates, that an {@link Expression} has only one child.
   */
  private static final int ONLY_ONE_CHILD = - 1 ;


  /**
   * Caption of the {@link Identifier}s.
   */
  private static final String IDENTIFIER = "Identifier" ; //$NON-NLS-1$


  /**
   * Caption of the {@link Expression}.
   */
  private static final String EXPRESSION = "e" ; //$NON-NLS-1$


  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * The {@link OutlinePreferences}.
   * 
   * @see #getOutlinePreferences()
   */
  private OutlinePreferences outlinePreferences ;


  /**
   * The old {@link Expression} to check if the {@link Expression} has changed.
   */
  private Expression oldExpression ;


  /**
   * The {@link OutlineUnbound}, in which the unbound {@link Identifier}s in
   * the given {@link Expression} are saved.
   */
  private OutlineUnbound outlineUnbound ;


  /**
   * The <code>Timer</code> for the executing.
   */
  private Timer outlineTimer ;


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   */
  public AbstractOutline ( )
  {
    this.oldExpression = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
  }


  /**
   * Cancels the execute <code>Timer</code>.
   */
  private final void cancelExecuteTimer ( )
  {
    if ( this.outlineTimer != null )
    {
      this.outlineTimer.cancel ( ) ;
      this.outlineTimer = null ;
    }
  }


  /**
   * Returns the node, which represents the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Expression}.
   */
  private final DefaultMutableTreeNode checkExpression ( Expression pExpression )
  {
    if ( pExpression instanceof MultiLambda )
    {
      return multiLambda ( ( MultiLambda ) pExpression ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      return lambda ( ( Lambda ) pExpression ) ;
    }
    else if ( pExpression instanceof Location )
    {
      return location ( ( Location ) pExpression ) ;
    }
    else if ( pExpression instanceof LetRec )
    {
      return letRec ( ( LetRec ) pExpression ) ;
    }
    else if ( pExpression instanceof Let )
    {
      return let ( ( Let ) pExpression ) ;
    }
    else if ( pExpression instanceof CurriedLetRec )
    {
      return curriedLetRec ( ( CurriedLetRec ) pExpression ) ;
    }
    else if ( pExpression instanceof CurriedLet )
    {
      return curriedLet ( ( CurriedLet ) pExpression ) ;
    }
    else if ( pExpression instanceof MultiLet )
    {
      return multiLet ( ( MultiLet ) pExpression ) ;
    }
    else if ( pExpression instanceof Recursion )
    {
      return recursion ( ( Recursion ) pExpression ) ;
    }
    else if ( pExpression instanceof InfixOperation )
    {
      return infixOperation ( ( InfixOperation ) pExpression ) ;
    }
    return otherExpression ( pExpression ) ;
  }


  /**
   * Creates the children with the given {@link Expression} and adds them to the
   * given node.
   * 
   * @param pExpression The {@link Expression}, with which the children should
   *          be created.
   * @param pNode The node where the children should be added.
   */
  private final void createChildren ( Expression pExpression ,
      DefaultMutableTreeNode pNode )
  {
    Enumeration < Expression > children = pExpression.children ( ) ;
    int childIndex = minimumChildIndex ( pExpression ) ;
    Expression child ;
    DefaultMutableTreeNode treeNode ;
    OutlineNode node ;
    while ( children.hasMoreElements ( ) )
    {
      child = children.nextElement ( ) ;
      treeNode = checkExpression ( child ) ;
      node = ( OutlineNode ) treeNode.getUserObject ( ) ;
      if ( childIndex == ONLY_ONE_CHILD )
      {
        node.appendDescription ( EXPRESSION + BETWEEN ) ;
      }
      else
      {
        node.appendDescription ( EXPRESSION + childIndex + BETWEEN ) ;
      }
      node.resetCaption ( ) ;
      pNode.add ( treeNode ) ;
      childIndex ++ ;
    }
  }


  /**
   * Returns the node, which represents the given {@link CurriedLet}.
   * 
   * @param pCurriedLet The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedLet}.
   */
  private final DefaultMutableTreeNode curriedLet ( CurriedLet pCurriedLet )
  {
    String [ ] idList = pCurriedLet.getIdentifiers ( ) ;
    OutlineBinding aSTBinding = new OutlineBinding ( pCurriedLet ) ;
    aSTBinding.add ( pCurriedLet.getE2 ( ) , pCurriedLet.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pCurriedLet.getIdentifiers ( ).length ; i ++ )
    {
      aSTBinding
          .add ( pCurriedLet.getE1 ( ) , pCurriedLet.getIdentifiers ( i ) ) ;
    }
    aSTBinding.find ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pCurriedLet , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > index = OutlineIdentifier.getIndex ( pCurriedLet ) ;
    OutlinePair outlinePair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          idList [ i ] , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
          aSTBinding , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pCurriedLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedLetRec}.
   * 
   * @param pCurriedLetRec The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedLetRec}.
   */
  private final DefaultMutableTreeNode curriedLetRec (
      CurriedLetRec pCurriedLetRec )
  {
    String [ ] idList = pCurriedLetRec.getIdentifiers ( ) ;
    OutlineBinding aSTBinding = new OutlineBinding ( pCurriedLetRec ) ;
    aSTBinding.add ( pCurriedLetRec , pCurriedLetRec.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pCurriedLetRec.getIdentifiers ( ).length ; i ++ )
    {
      aSTBinding.add ( pCurriedLetRec.getE1 ( ) , pCurriedLetRec
          .getIdentifiers ( i ) ) ;
    }
    aSTBinding.find ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pCurriedLetRec , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > index = OutlineIdentifier
        .getIndex ( pCurriedLetRec ) ;
    OutlinePair outlinePair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          idList [ i ] , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
          aSTBinding , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pCurriedLetRec , node ) ;
    return node ;
  }


  /**
   * Disables the auto update <code>JCheckBox</code> and the
   * <code>JMenuItem</code>. Removes the <code>ItemListener</code> and the
   * <code>ActionListener</code>.
   */
  public final void disableAutoUpdate ( )
  {
    // Disable AutoUpdate, remove Listener and deselect
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).removeItemListener (
        this.outlineUI.getJCheckBoxAutoUpdate ( ).getItemListeners ( ) [ 0 ] ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).removeActionListener (
        this.outlineUI.getJMenuItemAutoUpdate ( ).getActionListeners ( ) [ 0 ] ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
  }


  /**
   * Execute the rebuild of a new tree in the {@link Outline}.
   */
  public final void execute ( )
  {
    this.outlineUnbound = new OutlineUnbound ( this.oldExpression ) ;
    DefaultMutableTreeNode root = checkExpression ( this.oldExpression ) ;
    SwingUtilities
        .invokeLater ( new OutlineDisplayTree ( this.outlineUI , root ) ) ;
  }


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI}.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI}.
   * @see de.unisiegen.tpml.graphics.outline.Outline#getJPanelOutline()
   */
  public final JPanel getJPanelOutline ( )
  {
    return this.outlineUI.getJPanelMain ( ) ;
  }


  /**
   * Returns the {@link OutlinePreferences}.
   * 
   * @return The {@link OutlinePreferences}.
   * @see #outlinePreferences
   */
  public final OutlinePreferences getOutlinePreferences ( )
  {
    return this.outlinePreferences ;
  }


  /**
   * Returns the node, which represents the given {@link InfixOperation}.
   * 
   * @param pInfixOperation The input {@link Expression}.
   * @return The node, which represents the given {@link InfixOperation}.
   */
  private final DefaultMutableTreeNode infixOperation (
      InfixOperation pInfixOperation )
  {
    Expression e1 = pInfixOperation.getE1 ( ) ;
    Expression e2 = pInfixOperation.getE2 ( ) ;
    BinaryOperator binary = pInfixOperation.getOp ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pInfixOperation , this.outlineUnbound ) ) ;
    DefaultMutableTreeNode node0 = checkExpression ( e1 ) ;
    OutlineNode astNode0 = ( OutlineNode ) node0.getUserObject ( ) ;
    astNode0.appendDescription ( EXPRESSION + ONE + BETWEEN ) ;
    astNode0.resetCaption ( ) ;
    node.add ( node0 ) ;
    int start = pInfixOperation.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) , e1.toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode (
        new OutlineNode ( binary , binary.toString ( ) , start , end , null ,
            this.outlineUnbound ) ) ;
    node.add ( node1 ) ;
    DefaultMutableTreeNode ex2 = checkExpression ( e2 ) ;
    OutlineNode node2 = ( OutlineNode ) ex2.getUserObject ( ) ;
    node2.appendDescription ( EXPRESSION + TWO + BETWEEN ) ;
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Lambda}.
   * 
   * @param pLambda The input {@link Expression}.
   * @return The node, which represents the given {@link Lambda}.
   */
  private final DefaultMutableTreeNode lambda ( Lambda pLambda )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLambda , this.outlineUnbound ) ) ;
    OutlinePair outlinePair = OutlineIdentifier.getIndex ( pLambda ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLambda ) ;
    outlineBinding.add ( pLambda , pLambda.getId ( ) ) ;
    outlineBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pLambda.getId ( ) , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
        outlineBinding , this.outlineUnbound ) ) ) ;
    createChildren ( pLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Let}.
   * 
   * @param pLet The input {@link Expression}.
   * @return The node, which represents the given {@link Let}.
   */
  private final DefaultMutableTreeNode let ( Let pLet )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLet , this.outlineUnbound ) ) ;
    OutlinePair outlinePair = OutlineIdentifier.getIndex ( pLet ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLet ) ;
    outlineBinding.add ( pLet.getE2 ( ) , pLet.getId ( ) ) ;
    outlineBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER , pLet
        .getId ( ) , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
        outlineBinding , this.outlineUnbound ) ) ) ;
    createChildren ( pLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link LetRec}.
   * 
   * @param pLetRec The input {@link Expression}.
   * @return The node, which represents the given {@link LetRec}.
   */
  private final DefaultMutableTreeNode letRec ( LetRec pLetRec )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLetRec , this.outlineUnbound ) ) ;
    OutlinePair outlinePair = OutlineIdentifier.getIndex ( pLetRec ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLetRec ) ;
    outlineBinding.add ( pLetRec , pLetRec.getId ( ) ) ;
    outlineBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pLetRec.getId ( ) , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
        outlineBinding , this.outlineUnbound ) ) ) ;
    createChildren ( pLetRec , node ) ;
    return node ;
  }


  /**
   * This method loads a new {@link Expression} into the {@link Outline}. It
   * checks if the new {@link Expression} is different to the current loaded
   * {@link Expression}, if not it does nothing and returns. It does also
   * nothing if the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>. In the <code>BigStep</code> and the
   * <code>TypeChecker</code> view it does also nothing if the change does not
   * come from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new {@link Expression}.
   * @param pModus The modus who is calling this method.
   */
  public final void loadExpression ( Expression pExpression , int pModus )
  {
    if ( ( this.oldExpression != null )
        && ( pExpression.equals ( this.oldExpression ) ) )
    {
      return ;
    }
    if ( ( pModus == Outline.CHANGE_SMALLSTEP )
        && ( ! this.outlinePreferences.isAutoUpdate ( ) ) )
    {
      return ;
    }
    if ( pModus == Outline.CHANGE_BIGSTEP )
    {
      return ;
    }
    if ( pModus == Outline.CHANGE_TYPECHECKER )
    {
      return ;
    }
    this.oldExpression = pExpression ;
    cancelExecuteTimer ( ) ;
    if ( ( pModus == Outline.INIT ) || ( pModus == Outline.MOUSE_CLICK ) )
    {
      execute ( ) ;
    }
    else
    {
      startExecuteTimer ( ) ;
    }
  }


  /**
   * Returns the node, which represents the given {@link Location}.
   * 
   * @param pLocation The input {@link Expression}.
   * @return The node, which represents the given {@link Location}.
   */
  private final DefaultMutableTreeNode location ( Location pLocation )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLocation , this.outlineUnbound ) ) ;
    int start = 0 ;
    int end = start - 1 + pLocation.getName ( ).length ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( NAME , pLocation
        .getName ( ) , start , end , null , this.outlineUnbound ) ) ) ;
    return node ;
  }


  /**
   * Returns the minimum child index. For example 0 if the {@link Expression} is
   * an instance of {@link Condition}, or 1 if the {@link Expression} is an
   * instance of {@link Let}.
   * 
   * @param pExpression The {@link Expression} to check for.
   * @return The minimum child index.
   */
  private final int minimumChildIndex ( Expression pExpression )
  {
    int result = 10 ;
    for ( Method method : pExpression.getClass ( ).getMethods ( ) )
    {
      if ( GETE.equals ( method.getName ( ) ) )
      {
        return ONLY_ONE_CHILD ;
      }
      if ( method.getName ( ).matches ( GETEX ) )
      {
        result = Math.min ( result , Integer.parseInt ( String.valueOf ( method
            .getName ( ).charAt ( 4 ) ) ) ) ;
      }
    }
    if ( result == 10 )
    {
      return 1 ;
    }
    return result ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLambda}.
   * 
   * @param pMultiLambda The input {@link Expression}.
   * @return The node, which represents the given {@link MultiLambda}.
   */
  private final DefaultMutableTreeNode multiLambda ( MultiLambda pMultiLambda )
  {
    OutlineBinding aSTBinding = new OutlineBinding ( pMultiLambda ) ;
    for ( String id : pMultiLambda.getIdentifiers ( ) )
    {
      aSTBinding.add ( pMultiLambda.getE ( ) , id ) ;
    }
    aSTBinding.find ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMultiLambda , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > index = OutlineIdentifier
        .getIndex ( pMultiLambda ) ;
    OutlinePair outlinePair ;
    final int length = pMultiLambda.getIdentifiers ( ).length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          pMultiLambda.getIdentifiers ( ) [ i ] , outlinePair.getStart ( ) ,
          outlinePair.getEnd ( ) , aSTBinding , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pMultiLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLet}.
   * 
   * @param pMultiLet The input {@link Expression}.
   * @return The node, which represents the given {@link MultiLet}.
   */
  private final DefaultMutableTreeNode multiLet ( MultiLet pMultiLet )
  {
    OutlineBinding aSTBinding = new OutlineBinding ( pMultiLet ) ;
    for ( String id : pMultiLet.getIdentifiers ( ) )
    {
      aSTBinding.add ( pMultiLet.getE2 ( ) , id ) ;
    }
    aSTBinding.find ( ) ;
    String [ ] idList = pMultiLet.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMultiLet , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > index = OutlineIdentifier.getIndex ( pMultiLet ) ;
    OutlinePair outlinePair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          idList [ i ] , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
          aSTBinding , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pMultiLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Expression}.
   */
  private final DefaultMutableTreeNode otherExpression ( Expression pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Recursion}.
   * 
   * @param pRecursion The input {@link Expression}.
   * @return The node, which represents the given {@link Recursion}.
   */
  private final DefaultMutableTreeNode recursion ( Recursion pRecursion )
  {
    String id = pRecursion.getId ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pRecursion , this.outlineUnbound ) ) ;
    OutlinePair outlinePair = OutlineIdentifier.getIndex ( pRecursion )
        .get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pRecursion ) ;
    outlineBinding.add ( pRecursion , pRecursion.getId ( ) ) ;
    outlineBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER , id ,
        outlinePair.getStart ( ) , outlinePair.getEnd ( ) , outlineBinding ,
        this.outlineUnbound ) ) ) ;
    createChildren ( pRecursion , node ) ;
    return node ;
  }


  /**
   * Starts the execute <code>Timer</code>, which will execute the rebuild of
   * a new tree in the {@link Outline} after 250ms, if it is not canceled during
   * this time.
   */
  private final void startExecuteTimer ( )
  {
    this.outlineTimer = new Timer ( ) ;
    this.outlineTimer.schedule ( new OutlineTimerTask ( this ) , 250 ) ;
  }
}
