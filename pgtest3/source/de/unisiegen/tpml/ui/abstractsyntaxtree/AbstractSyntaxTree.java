package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.Debug ;
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
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTBinding ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTIdentifier ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTPair ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTUnbound ;


/**
 * This class is the main class of the AbstractSyntaxTree. It loads the
 * preferences, creates the GUI and loads new Expressions.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class AbstractSyntaxTree
{
  /**
   * String, between the description of the parent node, like e1, and the
   * description of the current node, like Identifier.
   */
  private static final String BETWEEN = "  -  " ; //$NON-NLS-1$


  /**
   * Indicates, that an Expression has only one child.
   */
  private static final int ONLY_ONE_CHILD = - 1 ;


  /**
   * Caption of the Identifiers.
   */
  private static final String IDENTIFIER = "Identifier" ; //$NON-NLS-1$


  /**
   * Caption of the Expression.
   */
  private static final String EXPRESSION = "e" ; //$NON-NLS-1$


  /**
   * The AbstractSyntaxTree UI.
   * 
   * @see #getASTUI()
   */
  private ASTUI aSTUI ;


  /**
   * The AbstractSyntaxTree Preferences.
   * 
   * @see #getASTPreferences()
   */
  private ASTPreferences aSTPreferences ;


  /**
   * The old Expression to check if the Expression has changed.
   */
  private Expression oldExpression ;


  /**
   * The aSTUnbound, in which the unbound Identifiers in the given Expression
   * are saved.
   */
  private ASTUnbound aSTUnbound ;


  /**
   * The timer for executing.
   */
  private Timer aSTTimer ;


  /**
   * Initilizes the preferences and the AbstractSyntaxTree GUI.
   */
  public AbstractSyntaxTree ( )
  {
    this.oldExpression = null ;
    this.aSTPreferences = new ASTPreferences ( ) ;
    this.aSTUI = new ASTUI ( this ) ;
  }


  /**
   * Creates the children with the given Expression and adds them to the given
   * node.
   * 
   * @param pExpression The Expression, with which the children should be
   *          created.
   * @param pNode The node where the children should be added.
   */
  private void createChildren ( Expression pExpression ,
      DefaultMutableTreeNode pNode )
  {
    Enumeration < Expression > children = pExpression.children ( ) ;
    int childIndex = minimumChildIndex ( pExpression ) ;
    Expression child ;
    DefaultMutableTreeNode treeNode ;
    ASTNode node ;
    while ( children.hasMoreElements ( ) )
    {
      child = children.nextElement ( ) ;
      treeNode = expression ( child ) ;
      node = ( ASTNode ) treeNode.getUserObject ( ) ;
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
   * Returns the node, which represents the given CurriedLet.
   * 
   * @param pCurriedLet The input Expression.
   * @return The node, which represents the given CurriedLet.
   */
  private DefaultMutableTreeNode curriedLet ( CurriedLet pCurriedLet )
  {
    String [ ] idList = pCurriedLet.getIdentifiers ( ) ;
    ASTBinding aSTBinding = new ASTBinding ( pCurriedLet ) ;
    aSTBinding.add ( pCurriedLet.getE2 ( ) , pCurriedLet.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pCurriedLet.getIdentifiers ( ).length ; i ++ )
    {
      aSTBinding
          .add ( pCurriedLet.getE1 ( ) , pCurriedLet.getIdentifiers ( i ) ) ;
    }
    aSTBinding.find ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pCurriedLet , this.aSTUnbound ) ) ;
    ArrayList < ASTPair > index = ASTIdentifier.getIndex ( pCurriedLet ) ;
    ASTPair aSTPair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      aSTPair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , aSTPair.getStart ( ) , aSTPair.getEnd ( ) ,
          aSTBinding , this.aSTUnbound ) ) ) ;
    }
    createChildren ( pCurriedLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given CurriedLetRec.
   * 
   * @param pCurriedLetRec The input Expression.
   * @return The node, which represents the given CurriedLetRec.
   */
  private DefaultMutableTreeNode curriedLetRec ( CurriedLetRec pCurriedLetRec )
  {
    String [ ] idList = pCurriedLetRec.getIdentifiers ( ) ;
    ASTBinding aSTBinding = new ASTBinding ( pCurriedLetRec ) ;
    aSTBinding.add ( pCurriedLetRec , pCurriedLetRec.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pCurriedLetRec.getIdentifiers ( ).length ; i ++ )
    {
      aSTBinding.add ( pCurriedLetRec.getE1 ( ) , pCurriedLetRec
          .getIdentifiers ( i ) ) ;
    }
    aSTBinding.find ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pCurriedLetRec , this.aSTUnbound ) ) ;
    ArrayList < ASTPair > index = ASTIdentifier.getIndex ( pCurriedLetRec ) ;
    ASTPair aSTPair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      aSTPair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , aSTPair.getStart ( ) , aSTPair.getEnd ( ) ,
          aSTBinding , this.aSTUnbound ) ) ) ;
    }
    createChildren ( pCurriedLetRec , node ) ;
    return node ;
  }


  /**
   * Execute the AbstractSyntaxTree UI.
   */
  public void execute ( )
  {
    Debug.out.println ( "Execute" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
    this.aSTUnbound = new ASTUnbound ( this.oldExpression ) ;
    final DefaultMutableTreeNode root = expression ( this.oldExpression ) ;
    SwingUtilities.invokeLater ( new Runnable ( )
    {
      public void run ( )
      {
        AbstractSyntaxTree.this.getASTUI ( ).setRootNode ( root ) ;
      }
    } ) ;
  }


  /**
   * Returns the node, which represents the given Expression.
   * 
   * @param pExpression The input Expression.
   * @return The node, which represents the given Expression.
   */
  private DefaultMutableTreeNode expression ( Expression pExpression )
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
    return other ( pExpression ) ;
  }


  /**
   * Returns the AbstractSyntaxTree Preferences.
   * 
   * @return The AbstractSyntaxTree Preferences.
   * @see #aSTPreferences
   */
  public ASTPreferences getASTPreferences ( )
  {
    return this.aSTPreferences ;
  }


  /**
   * Returns the AbstractSyntaxTree UI.
   * 
   * @return The AbstractSyntaxTree UI.
   * @see #aSTUI
   */
  public ASTUI getASTUI ( )
  {
    return this.aSTUI ;
  }


  /**
   * Returns the oldExpression.
   * 
   * @return The oldExpression.
   * @see #oldExpression
   */
  public Expression getOldExpression ( )
  {
    return this.oldExpression ;
  }


  /**
   * Returns the node, which represents the given InfixOperation.
   * 
   * @param pInfixOperation The input Expression.
   * @return The node, which represents the given InfixOperation.
   */
  private DefaultMutableTreeNode infixOperation ( InfixOperation pInfixOperation )
  {
    Expression e1 = pInfixOperation.getE1 ( ) ;
    Expression e2 = pInfixOperation.getE2 ( ) ;
    BinaryOperator binary = pInfixOperation.getOp ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pInfixOperation , this.aSTUnbound ) ) ;
    DefaultMutableTreeNode node0 = expression ( e1 ) ;
    ASTNode astNode0 = ( ASTNode ) node0.getUserObject ( ) ;
    astNode0.appendDescription ( EXPRESSION + "1" + BETWEEN ) ; //$NON-NLS-1$
    astNode0.resetCaption ( ) ;
    node.add ( node0 ) ;
    /*
     * PrettyAnnotation prettyAnnotation = pExpression .toPrettyString (
     * ).getAnnotationForPrintable ( binary ) ;
     */
    int start = pInfixOperation.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) , e1.toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode ( new ASTNode (
        binary.getClass ( ).getSimpleName ( ) , binary.toString ( ) , start ,
        end , null , this.aSTUnbound ) ) ;
    node.add ( node1 ) ;
    DefaultMutableTreeNode ex2 = expression ( e2 ) ;
    ASTNode node2 = ( ASTNode ) ex2.getUserObject ( ) ;
    node2.appendDescription ( EXPRESSION + "2" + BETWEEN ) ; //$NON-NLS-1$
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given Lambda.
   * 
   * @param pLambda The input Expression.
   * @return The node, which represents the given Lambda.
   */
  private DefaultMutableTreeNode lambda ( Lambda pLambda )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pLambda , this.aSTUnbound ) ) ;
    ASTPair aSTPair = ASTIdentifier.getIndex ( pLambda ).get ( 0 ) ;
    ASTBinding aSTBinding = new ASTBinding ( pLambda ) ;
    aSTBinding.add ( pLambda , pLambda.getId ( ) ) ;
    aSTBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER , pLambda
        .getId ( ) , aSTPair.getStart ( ) , aSTPair.getEnd ( ) , aSTBinding ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given Let.
   * 
   * @param pLet The input Expression.
   * @return The node, which represents the given Let.
   */
  private DefaultMutableTreeNode let ( Let pLet )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pLet , this.aSTUnbound ) ) ;
    ASTPair aSTPair = ASTIdentifier.getIndex ( pLet ).get ( 0 ) ;
    ASTBinding aSTBinding = new ASTBinding ( pLet ) ;
    aSTBinding.add ( pLet.getE2 ( ) , pLet.getId ( ) ) ;
    aSTBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER , pLet
        .getId ( ) , aSTPair.getStart ( ) , aSTPair.getEnd ( ) , aSTBinding ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given LetRec.
   * 
   * @param pLetRec The input Expression.
   * @return The node, which represents the given LetRec.
   */
  private DefaultMutableTreeNode letRec ( LetRec pLetRec )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pLetRec , this.aSTUnbound ) ) ;
    ASTPair aSTPair = ASTIdentifier.getIndex ( pLetRec ).get ( 0 ) ;
    ASTBinding aSTBinding = new ASTBinding ( pLetRec ) ;
    aSTBinding.add ( pLetRec , pLetRec.getId ( ) ) ;
    aSTBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER , pLetRec
        .getId ( ) , aSTPair.getStart ( ) , aSTPair.getEnd ( ) , aSTBinding ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pLetRec , node ) ;
    return node ;
  }


  /**
   * This method loads a new Expression into the AbstractSyntaxTree. It checks
   * if the new Expression is different to the current loaded Expression, if not
   * it does nothing and returns. It does also nothing if the auto update is
   * disabled and the change does not come from a mouse event. In the BigStep
   * and the TypeChecker view it does also nothing if the change does not come
   * from a mouse event.
   * 
   * @param pExpression The new Expression.
   * @param pDescription The description who is calling this method.
   */
  public void loadExpression ( Expression pExpression , String pDescription )
  {
    if ( ( this.oldExpression != null )
        && ( pExpression.equals ( this.oldExpression ) ) )
    {
      Debug.err.println ( "Expression has not changed" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      return ;
    }
    if ( ( ! this.aSTPreferences.isAutoUpdate ( ) )
        && ( pDescription.startsWith ( "change" ) ) ) //$NON-NLS-1$
    {
      Debug.err.println ( "No AutoUpdate selected" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      return ;
    }
    if ( pDescription.equals ( "change_bigstep" ) ) //$NON-NLS-1$
    {
      Debug.err.println ( "No update in the BigStep view" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      return ;
    }
    if ( pDescription.equals ( "change_typechecker" ) ) //$NON-NLS-1$
    {
      Debug.err
          .println ( "No update in the TypeChecker View" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
      return ;
    }
    this.oldExpression = pExpression ;
    if ( this.aSTTimer != null )
    {
      this.aSTTimer.cancel ( ) ;
      this.aSTTimer = null ;
    }
    Debug.out.println ( "Load" , Debug.CHRISTIAN ) ; //$NON-NLS-1$
    this.aSTTimer = new Timer ( ) ;
    this.aSTTimer.schedule ( new ASTTimerTask ( this ) , 500 ) ;
  }


  /**
   * Returns the node, which represents the given Location.
   * 
   * @param pLocation The input Expression.
   * @return The node, which represents the given Location.
   */
  private DefaultMutableTreeNode location ( Location pLocation )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pLocation , this.aSTUnbound ) ) ;
    int start = 0 ;
    int end = start - 1 + pLocation.getName ( ).length ( ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Name" , pLocation //$NON-NLS-1$
        .getName ( ) , start , end , null , this.aSTUnbound ) ) ) ;
    return node ;
  }


  /**
   * Returns the minimum child index. For example 0 if the Expression is an
   * instance of Condition, or 1 if the Expression is an instance of Let.
   * 
   * @param pExpression The Expression to check for.
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
   * Returns the node, which represents the given MultiLambda.
   * 
   * @param pMultiLambda The input Expression.
   * @return The node, which represents the given MultiLambda.
   */
  private DefaultMutableTreeNode multiLambda ( MultiLambda pMultiLambda )
  {
    ASTBinding aSTBinding = new ASTBinding ( pMultiLambda ) ;
    for ( String id : pMultiLambda.getIdentifiers ( ) )
    {
      aSTBinding.add ( pMultiLambda.getE ( ) , id ) ;
    }
    aSTBinding.find ( ) ;
    String idList[] = pMultiLambda.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pMultiLambda , this.aSTUnbound ) ) ;
    ArrayList < ASTPair > index = ASTIdentifier.getIndex ( pMultiLambda ) ;
    ASTPair aSTPair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      aSTPair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , aSTPair.getStart ( ) , aSTPair.getEnd ( ) ,
          aSTBinding , this.aSTUnbound ) ) ) ;
    }
    createChildren ( pMultiLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given MultiLet.
   * 
   * @param pMultiLet The input Expression.
   * @return The node, which represents the given MultiLet.
   */
  private DefaultMutableTreeNode multiLet ( MultiLet pMultiLet )
  {
    ASTBinding aSTBinding = new ASTBinding ( pMultiLet ) ;
    for ( String id : pMultiLet.getIdentifiers ( ) )
    {
      aSTBinding.add ( pMultiLet.getE2 ( ) , id ) ;
    }
    aSTBinding.find ( ) ;
    String [ ] idList = pMultiLet.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pMultiLet , this.aSTUnbound ) ) ;
    ArrayList < ASTPair > index = ASTIdentifier.getIndex ( pMultiLet ) ;
    ASTPair aSTPair ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      aSTPair = index.get ( i ) ;
      node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , aSTPair.getStart ( ) , aSTPair.getEnd ( ) ,
          aSTBinding , this.aSTUnbound ) ) ) ;
    }
    createChildren ( pMultiLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given Expression.
   * 
   * @param pExpression The input Expression.
   * @return The node, which represents the given Expression.
   */
  private DefaultMutableTreeNode other ( Expression pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given Recursion.
   * 
   * @param pRecursion The input Expression.
   * @return The node, which represents the given Recursion.
   */
  private DefaultMutableTreeNode recursion ( Recursion pRecursion )
  {
    String id = pRecursion.getId ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pRecursion , this.aSTUnbound ) ) ;
    ASTPair aSTPair = ASTIdentifier.getIndex ( pRecursion ).get ( 0 ) ;
    ASTBinding aSTBinding = new ASTBinding ( pRecursion ) ;
    aSTBinding.add ( pRecursion , pRecursion.getId ( ) ) ;
    aSTBinding.find ( ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER , id ,
        aSTPair.getStart ( ) , aSTPair.getEnd ( ) , aSTBinding ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pRecursion , node ) ;
    return node ;
  }
}
