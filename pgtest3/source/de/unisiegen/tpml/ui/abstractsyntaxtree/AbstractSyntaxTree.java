package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.lang.reflect.Method ;
import java.util.Enumeration ;
import java.util.LinkedList ;
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
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTHoleBinding ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTPair ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTUnbound ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class AbstractSyntaxTree
{
  /**
   * TODO
   */
  private static final String BEFORE = "" ;


  /**
   * TODO
   */
  private static final String AFTER = "  -  " ;


  /**
   * TODO
   */
  private static final int ONLY_ONE_EXPRESSION = - 1 ;


  /**
   * TODO
   */
  private static final String IDENTIFIER = "Identifier" ;


  /**
   * TODO
   */
  private ASTUI aSTUI ;


  /**
   * TODO
   */
  private ASTPreferences aSTPreferences ;


  /**
   * TODO
   */
  private Expression oldExpression ;


  /**
   * TODO
   */
  private ASTUnbound aSTUnbound ;


  /**
   * TODO
   */
  public AbstractSyntaxTree ( )
  {
    this.oldExpression = null ;
    this.aSTPreferences = new ASTPreferences ( ) ;
    this.aSTUI = new ASTUI ( this ) ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   */
  public void calculateBindings ( Expression pExpression )
  {
    ASTHoleBinding aSTHoleBinding = new ASTHoleBinding ( pExpression ) ;
    LinkedList < ASTPair > list = aSTHoleBinding.getASTPairs ( ) ;
    for ( ASTPair identifier : list )
    {
      Debug.out.println ( "Identifier " + identifier.getStart ( ) + " -> "
          + identifier.getEnd ( ) , Debug.CHRISTIAN ) ;
      for ( int i = 0 ; i < identifier.size ( ) ; i ++ )
      {
        ASTPair binding = identifier.get ( i ) ;
        Debug.out.println ( "Cf - Binding    " + binding.getStart ( ) + " -> "
            + binding.getEnd ( ) , Debug.CHRISTIAN ) ;
      }
      Debug.out.println ( "" , Debug.CHRISTIAN ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @param pNode
   */
  private void createChildren ( Expression pExpression ,
      DefaultMutableTreeNode pNode )
  {
    Enumeration < Expression > children = pExpression.children ( ) ;
    int startIndex = minimumChildExpression ( pExpression ) ;
    Expression child ;
    DefaultMutableTreeNode treeNode ;
    ASTNode node1 ;
    while ( children.hasMoreElements ( ) )
    {
      child = children.nextElement ( ) ;
      treeNode = expression ( child ) ;
      node1 = ( ASTNode ) treeNode.getUserObject ( ) ;
      if ( startIndex == ONLY_ONE_EXPRESSION )
      {
        node1.appendDescription ( BEFORE + "e" + AFTER ) ;
      }
      else
      {
        node1.appendDescription ( BEFORE + "e" + startIndex + AFTER ) ;
      }
      node1.resetCaption ( ) ;
      pNode.add ( treeNode ) ;
      startIndex ++ ;
    }
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode curriedLet ( CurriedLet pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    ASTBinding aSTBinding = new ASTBinding ( pExpression ) ;
    aSTBinding.add ( pExpression.getE2 ( ) , pExpression.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      aSTBinding
          .add ( pExpression.getE1 ( ) , pExpression.getIdentifiers ( i ) ) ;
    }
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    DefaultMutableTreeNode node0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
          idList [ i ] ) ;
      int end = start + idList [ i ].length ( ) - 1 ;
      node0 = new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , new ASTPair ( start , end ) , aSTBinding ,
          this.aSTUnbound ) ) ;
      node.add ( node0 ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode curriedLetRec ( CurriedLetRec pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    ASTBinding aSTBinding = new ASTBinding ( pExpression ) ;
    aSTBinding.add ( pExpression , pExpression.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      aSTBinding
          .add ( pExpression.getE1 ( ) , pExpression.getIdentifiers ( i ) ) ;
    }
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    DefaultMutableTreeNode node0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
          idList [ i ] ) ;
      int end = start + idList [ i ].length ( ) - 1 ;
      node0 = new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , new ASTPair ( start , end ) , aSTBinding ,
          this.aSTUnbound ) ) ;
      node.add ( node0 ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
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
   * TODO
   * 
   * @return TODO
   */
  public ASTPreferences getASTPreferences ( )
  {
    return this.aSTPreferences ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public ASTUI getASTUI ( )
  {
    return this.aSTUI ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode infixOperation ( InfixOperation pExpression )
  {
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    BinaryOperator binary = pExpression.getOp ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    DefaultMutableTreeNode node0 = expression ( e1 ) ;
    ASTNode astNode0 = ( ASTNode ) node0.getUserObject ( ) ;
    astNode0.appendDescription ( BEFORE + "e1" + AFTER ) ;
    astNode0.resetCaption ( ) ;
    node.add ( node0 ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) , e1.toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode ( new ASTNode (
        binary.getClass ( ).getSimpleName ( ) , binary.toString ( ) ,
        new ASTPair ( start , end ) , null , this.aSTUnbound ) ) ;
    node.add ( node1 ) ;
    DefaultMutableTreeNode ex2 = expression ( e2 ) ;
    ASTNode node2 = ( ASTNode ) ex2.getUserObject ( ) ;
    node2.appendDescription ( BEFORE + "e2" + AFTER ) ;
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode lambda ( Lambda pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        pExpression.getId ( ) ) ;
    int end = start + pExpression.getId ( ).length ( ) - 1 ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
        pExpression.getId ( ) , new ASTPair ( start , end ) , new ASTBinding (
            pExpression , pExpression , pExpression.getId ( ) ) ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode let ( Let pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        pExpression.getId ( ) ) ;
    int end = start + pExpression.getId ( ).length ( ) - 1 ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
        pExpression.getId ( ) , new ASTPair ( start , end ) , new ASTBinding (
            pExpression , pExpression.getE2 ( ) , pExpression.getId ( ) ) ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode letRec ( LetRec pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        pExpression.getId ( ) ) ;
    int end = start + pExpression.getId ( ).length ( ) - 1 ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
        pExpression.getId ( ) , new ASTPair ( start , end ) , new ASTBinding (
            pExpression , pExpression , pExpression.getId ( ) ) ,
        this.aSTUnbound ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode location ( Location pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Name" , pExpression
        .getName ( ) ,
        new ASTPair ( 0 , pExpression.getName ( ).length ( ) - 1 ) , null ,
        this.aSTUnbound ) ) ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private int minimumChildExpression ( Expression pExpression )
  {
    int result = Integer.MAX_VALUE ;
    for ( Method method : pExpression.getClass ( ).getMethods ( ) )
    {
      if ( method.getName ( ).matches ( "getE[0-9]{1}" ) )
      {
        result = Math.min ( result , Integer.parseInt ( String.valueOf ( method
            .getName ( ).charAt ( 4 ) ) ) ) ;
      }
      if ( method.getName ( ).matches ( "getE" ) )
      {
        return ONLY_ONE_EXPRESSION ;
      }
    }
    if ( result == Integer.MAX_VALUE )
    {
      return 1 ;
    }
    return result ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode multiLambda ( MultiLambda pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression , pExpression ,
        pExpression.getIdentifiers ( ) ) ;
    String idList[] = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    int lengthIdentifier = 0 ;
    DefaultMutableTreeNode node0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
          idList [ i ] ) ;
      int end = start + idList [ i ].length ( ) - 1 ;
      node0 = new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ i ] , new ASTPair ( start , end ) , aSTBinding ,
          this.aSTUnbound ) ) ;
      lengthIdentifier += idList [ i ].length ( ) ;
      node.add ( node0 ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode multiLet ( MultiLet pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression ,
        pExpression.getE2 ( ) , pExpression.getIdentifiers ( ) ) ;
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    int lengthIdentifier = 0 ;
    DefaultMutableTreeNode node0 ;
    final int length = idList.length ;
    for ( int index = 0 ; index < length ; index ++ )
    {
      int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
          idList [ index ] ) ;
      int end = start + idList [ index ].length ( ) - 1 ;
      node0 = new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER ,
          idList [ index ] , new ASTPair ( start , end ) , aSTBinding ,
          this.aSTUnbound ) ) ;
      lengthIdentifier += idList [ index ].length ( ) ;
      node.add ( node0 ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode other ( Expression pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @return TODO
   */
  private DefaultMutableTreeNode recursion ( Recursion pExpression )
  {
    String id = pExpression.getId ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression , this.aSTUnbound ) ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        pExpression.getId ( ) ) ;
    int end = start + pExpression.getId ( ).length ( ) - 1 ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( IDENTIFIER , id ,
        new ASTPair ( start , end ) , new ASTBinding ( pExpression ,
            pExpression , pExpression.getId ( ) ) , this.aSTUnbound ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @param pDescription
   */
  public void setExpression ( Expression pExpression , String pDescription )
  {
    if ( ( this.oldExpression != null )
        && ( pExpression.equals ( this.oldExpression ) ) )
    {
      Debug.err.println ( "Expression has not changed" , Debug.CHRISTIAN ) ;
      return ;
    }
    if ( ( ! this.aSTPreferences.isAutoUpdate ( ) )
        && ( pDescription.startsWith ( "change" ) ) )
    {
      Debug.err.println ( "No AutoUpdate selected" , Debug.CHRISTIAN ) ;
      return ;
    }
    if ( ( pDescription.endsWith ( "bigstep" ) )
        && ( pDescription.startsWith ( "change" ) ) )
    {
      Debug.err.println ( "No update in the BigStep view" , Debug.CHRISTIAN ) ;
      return ;
    }
    if ( ( pDescription.endsWith ( "typechecker" ) )
        && ( pDescription.startsWith ( "change" ) ) )
    {
      Debug.err
          .println ( "No update in the TypeChecker View" , Debug.CHRISTIAN ) ;
      return ;
    }
    this.aSTUnbound = new ASTUnbound ( pExpression ) ;
    this.oldExpression = pExpression ;
    this.aSTUI.setRootNode ( expression ( pExpression ) ) ;
  }
}
