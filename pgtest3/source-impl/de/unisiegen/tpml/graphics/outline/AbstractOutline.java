package de.unisiegen.tpml.graphics.outline ;


import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
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
 * This class is the main class of the <code>Outline</code>. It loads the
 * <code>OutlinePreferences</code>, creates the <code>OutlineUI</code> and
 * loads new <code>Expressions</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class AbstractOutline implements Outline
{
  /**
   * String, between the description of the parent node, like e1, and the
   * description of the current node, like Identifier.
   */
  private static final String BETWEEN = "  -  " ; //$NON-NLS-1$


  /**
   * Indicates, that an <code>Expression</code> has only one child.
   */
  private static final int ONLY_ONE_CHILD = - 1 ;


  /**
   * Caption of the <code>Identifiers</code>.
   */
  private static final String IDENTIFIER = "Identifier" ; //$NON-NLS-1$


  /**
   * Caption of the <code>Expression</code>.
   */
  private static final String EXPRESSION = "e" ; //$NON-NLS-1$


  /**
   * The <code>OutlineUI</code>.
   */
  private OutlineUI outlineUI ;


  /**
   * The <code>OutlinePreferences</code>.
   * 
   * @see #getOutlinePreferences()
   */
  private OutlinePreferences outlinePreferences ;


  /**
   * The old <code>Expression</code> to check if the <code>Expression</code>
   * has changed.
   */
  private Expression oldExpression ;


  /**
   * The <code>OutlineUnbound</code>, in which the unbound
   * <code>Identifiers</code> in the given <code>Expression</code> are
   * saved.
   */
  private OutlineUnbound outlineUnbound ;


  /**
   * The <code>Timer</code> for the executing.
   */
  private Timer outlineTimer ;


  /**
   * Initilizes the <code>OutlinePreferences</code> and the
   * <code>OutlineUI</code>.
   */
  public AbstractOutline ( )
  {
    this.oldExpression = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
  }


  /**
   * Creates the children with the given <code>Expression</code> and adds them
   * to the given node.
   * 
   * @param pExpression The <code>Expression</code>, with which the children
   *          should be created.
   * @param pNode The node where the children should be added.
   */
  private void createChildren ( Expression pExpression ,
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
   * Returns the node, which represents the given <code>CurriedLet</code>.
   * 
   * @param pCurriedLet The input <code>Expression</code>.
   * @return The node, which represents the given <code>CurriedLet</code>.
   */
  private DefaultMutableTreeNode curriedLet ( CurriedLet pCurriedLet )
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
   * Returns the node, which represents the given <code>CurriedLetRec</code>.
   * 
   * @param pCurriedLetRec The input <code>Expression</code>.
   * @return The node, which represents the given <code>CurriedLetRec</code>.
   */
  private DefaultMutableTreeNode curriedLetRec ( CurriedLetRec pCurriedLetRec )
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
  public void disableAutoUpdate ( )
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
   * Execute the rebuild of a new tree in the <code>Outline</code>.
   */
  public void execute ( )
  {
    this.outlineUnbound = new OutlineUnbound ( this.oldExpression ) ;
    DefaultMutableTreeNode root = checkExpression ( this.oldExpression ) ;
    SwingUtilities
        .invokeLater ( new OutlineDisplayTree ( this.outlineUI , root ) ) ;
  }


  /**
   * Returns the node, which represents the given <code>Expression</code>.
   * 
   * @param pExpression The input <code>Expression</code>.
   * @return The node, which represents the given <code>Expression</code>.
   */
  private DefaultMutableTreeNode checkExpression ( Expression pExpression )
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
   * Returns the <code>JPanel</code> of the <code>OutlineUI</code>.
   * 
   * @return The <code>JPanel</code> of the <code>OutlineUI</code>.
   * @see de.unisiegen.tpml.graphics.outline.Outline#getJPanelOutline()
   */
  public JPanel getJPanelOutline ( )
  {
    return this.outlineUI.getJPanelMain ( ) ;
  }


  /**
   * Returns the <code>OutlinePreferences</code>.
   * 
   * @return The <code>OutlinePreferences</code>.
   * @see #outlinePreferences
   */
  public OutlinePreferences getOutlinePreferences ( )
  {
    return this.outlinePreferences ;
  }


  /**
   * Returns the node, which represents the given <code>InfixOperation</code>.
   * 
   * @param pInfixOperation The input <code>Expression</code>.
   * @return The node, which represents the given <code>InfixOperation</code>.
   */
  private DefaultMutableTreeNode infixOperation ( InfixOperation pInfixOperation )
  {
    Expression e1 = pInfixOperation.getE1 ( ) ;
    Expression e2 = pInfixOperation.getE2 ( ) ;
    BinaryOperator binary = pInfixOperation.getOp ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pInfixOperation , this.outlineUnbound ) ) ;
    DefaultMutableTreeNode node0 = checkExpression ( e1 ) ;
    OutlineNode astNode0 = ( OutlineNode ) node0.getUserObject ( ) ;
    astNode0.appendDescription ( EXPRESSION + "1" + BETWEEN ) ; //$NON-NLS-1$
    astNode0.resetCaption ( ) ;
    node.add ( node0 ) ;
    int start = pInfixOperation.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) , e1.toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode (
        new OutlineNode ( binary , binary.getClass ( ).getSimpleName ( ) ,
            binary.toString ( ) , start , end , null , this.outlineUnbound ) ) ;
    node.add ( node1 ) ;
    DefaultMutableTreeNode ex2 = checkExpression ( e2 ) ;
    OutlineNode node2 = ( OutlineNode ) ex2.getUserObject ( ) ;
    node2.appendDescription ( EXPRESSION + "2" + BETWEEN ) ; //$NON-NLS-1$
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given <code>Lambda</code>.
   * 
   * @param pLambda The input <code>Expression</code>.
   * @return The node, which represents the given <code>Lambda</code>.
   */
  private DefaultMutableTreeNode lambda ( Lambda pLambda )
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
   * Returns the node, which represents the given <code>Let</code>.
   * 
   * @param pLet The input <code>Expression</code>.
   * @return The node, which represents the given <code>Let</code>.
   */
  private DefaultMutableTreeNode let ( Let pLet )
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
   * Returns the node, which represents the given <code>LetRec</code>.
   * 
   * @param pLetRec The input <code>Expression</code>.
   * @return The node, which represents the given <code>LetRec</code>.
   */
  private DefaultMutableTreeNode letRec ( LetRec pLetRec )
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
   * This method loads a new <code>Expression</code> into the
   * <code>Outline</code>. It checks if the new <code>Expression</code> is
   * different to the current loaded <code>Expression</code>, if not it does
   * nothing and returns. It does also nothing if the auto update is disabled
   * and the change does not come from a <code>MouseEvent</code>. In the
   * <code>BigStep</code> and the <code>TypeChecker</code> view it does also
   * nothing if the change does not come from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new <code>Expression</code>.
   * @param pDescription The description who is calling this method.
   */
  public void loadExpression ( Expression pExpression , String pDescription )
  {
    if ( ( this.oldExpression != null )
        && ( pExpression.equals ( this.oldExpression ) ) )
    {
      return ;
    }
    if ( ( ! this.outlinePreferences.isAutoUpdate ( ) )
        && ( pDescription.startsWith ( "change" ) ) ) //$NON-NLS-1$
    {
      return ;
    }
    if ( pDescription.equals ( "change_bigstep" ) ) //$NON-NLS-1$
    {
      return ;
    }
    if ( pDescription.equals ( "change_typechecker" ) ) //$NON-NLS-1$
    {
      return ;
    }
    this.oldExpression = pExpression ;
    if ( this.outlineTimer != null )
    {
      this.outlineTimer.cancel ( ) ;
      this.outlineTimer = null ;
    }
    this.outlineTimer = new Timer ( ) ;
    this.outlineTimer.schedule ( new OutlineTimerTask ( this ) , 250 ) ;
  }


  /**
   * Returns the node, which represents the given <code>Location</code>.
   * 
   * @param pLocation The input <code>Expression</code>.
   * @return The node, which represents the given <code>Location</code>.
   */
  private DefaultMutableTreeNode location ( Location pLocation )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLocation , this.outlineUnbound ) ) ;
    int start = 0 ;
    int end = start - 1 + pLocation.getName ( ).length ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode (
        "Name" , pLocation //$NON-NLS-1$
            .getName ( ) , start , end , null , this.outlineUnbound ) ) ) ;
    return node ;
  }


  /**
   * Returns the minimum child index. For example 0 if the
   * <code>Expression</code> is an instance of <code>Condition</code>, or 1
   * if the <code>Expression</code> is an instance of <code>Let</code>.
   * 
   * @param pExpression The <code>Expression</code> to check for.
   * @return The minimum child index.
   */
  private int minimumChildIndex ( Expression pExpression )
  {
    int result = 10 ;
    for ( Method method : pExpression.getClass ( ).getMethods ( ) )
    {
      if ( method.getName ( ).equals ( "getE" ) ) //$NON-NLS-1$
      {
        return ONLY_ONE_CHILD ;
      }
      if ( method.getName ( ).matches ( "getE[0-9]{1}" ) ) //$NON-NLS-1$
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
   * Returns the node, which represents the given <code>MultiLambda</code>.
   * 
   * @param pMultiLambda The input <code>Expression</code>.
   * @return The node, which represents the given <code>MultiLambda</code>.
   */
  private DefaultMutableTreeNode multiLambda ( MultiLambda pMultiLambda )
  {
    OutlineBinding aSTBinding = new OutlineBinding ( pMultiLambda ) ;
    for ( String id : pMultiLambda.getIdentifiers ( ) )
    {
      aSTBinding.add ( pMultiLambda.getE ( ) , id ) ;
    }
    aSTBinding.find ( ) ;
    String idList[] = pMultiLambda.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMultiLambda , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > index = OutlineIdentifier
        .getIndex ( pMultiLambda ) ;
    OutlinePair outlinePair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          idList [ i ] , outlinePair.getStart ( ) , outlinePair.getEnd ( ) ,
          aSTBinding , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pMultiLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given <code>MultiLet</code>.
   * 
   * @param pMultiLet The input <code>Expression</code>.
   * @return The node, which represents the given <code>MultiLet</code>.
   */
  private DefaultMutableTreeNode multiLet ( MultiLet pMultiLet )
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
   * Returns the node, which represents the given <code>Expression</code>.
   * 
   * @param pExpression The input <code>Expression</code>.
   * @return The node, which represents the given <code>Expression</code>.
   */
  private DefaultMutableTreeNode otherExpression ( Expression pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given <code>Recursion</code>.
   * 
   * @param pRecursion The input <code>Expression</code>.
   * @return The node, which represents the given <code>Recursion</code>.
   */
  private DefaultMutableTreeNode recursion ( Recursion pRecursion )
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
}
