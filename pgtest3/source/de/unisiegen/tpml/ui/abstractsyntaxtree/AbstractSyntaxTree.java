package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.lang.reflect.Method ;
import java.util.Enumeration ;
import java.util.LinkedList ;
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
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTBinding ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTHoleBinding ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.binding.ASTPair ;


public class AbstractSyntaxTree
{
  private static AbstractSyntaxTree abstractSyntaxTree = null ;


  private static final String BEFORE = "" ;


  private static final String AFTER = "  -  " ;


  private static final int ONLY_ONE_EXPRESSION = - 1 ;


  public static AbstractSyntaxTree getInstance ( )
  {
    if ( abstractSyntaxTree == null )
    {
      abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
      return abstractSyntaxTree ;
    }
    return abstractSyntaxTree ;
  }


  public static AbstractSyntaxTree getNewInstance ( )
  {
    return new AbstractSyntaxTree ( ) ;
  }


  private ASTUI aSTUI ;


  private ASTPreferences aSTPreferences ;


  private AbstractSyntaxTree ( )
  {
    this.aSTPreferences = new ASTPreferences ( ) ;
    this.aSTUI = new ASTUI ( this ) ;
  }


  public void calculateBindings ( Expression pExpression )
  {
    ASTHoleBinding aSTHoleBinding = new ASTHoleBinding ( pExpression ) ;
    LinkedList < ASTPair > list = aSTHoleBinding.getASTPairs ( ) ;
    for ( ASTPair identifier : list )
    {
      System.out.println ( "Cf - Identifier " + identifier.getStart ( )
          + " -> " + identifier.getEnd ( ) ) ;
      for ( int i = 0 ; i < identifier.size ( ) ; i ++ )
      {
        ASTPair binding = identifier.get ( i ) ;
        System.out.println ( "Cf - Binding    " + binding.getStart ( ) + " -> "
            + binding.getEnd ( ) ) ;
      }
      System.out.println ( ) ;
    }
  }


  private void createChildren ( Expression pExpression ,
      DefaultMutableTreeNode pNode )
  {
    Enumeration < Expression > children = pExpression.children ( ) ;
    int startIndex = minimumChildExpression ( pExpression ) ;
    while ( children.hasMoreElements ( ) )
    {
      Expression child = children.nextElement ( ) ;
      DefaultMutableTreeNode treeNode = expression ( child ) ;
      ASTNode node1 = ( ASTNode ) treeNode.getUserObject ( ) ;
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
        pExpression ) ) ;
    int lengthIdentifier = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode node0 = new DefaultMutableTreeNode (
          new ASTNode ( "Identifier" , idList [ i ] , new ASTPair (
              lengthIdentifier + 4 + i , lengthIdentifier + 3
                  + idList [ i ].length ( ) + i ) , aSTBinding ) ) ;
      lengthIdentifier += idList [ i ].length ( ) ;
      node.add ( node0 ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


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
        pExpression ) ) ;
    int lengthIdentifier = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode node0 = new DefaultMutableTreeNode (
          new ASTNode ( "Identifier" , idList [ i ] , new ASTPair (
              lengthIdentifier + 8 + i , lengthIdentifier + 7
                  + idList [ i ].length ( ) + i ) , aSTBinding ) ) ;
      lengthIdentifier += idList [ i ].length ( ) ;
      node.add ( node0 ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


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


  public ASTPreferences getASTPreferences ( )
  {
    return this.aSTPreferences ;
  }


  private DefaultMutableTreeNode infixOperation ( InfixOperation pExpression )
  {
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    BinaryOperator b = pExpression.getOp ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    DefaultMutableTreeNode ex1 = expression ( e1 ) ;
    ASTNode node0 = ( ASTNode ) ex1.getUserObject ( ) ;
    node0.appendDescription ( BEFORE + "e1" + AFTER ) ;
    node0.resetCaption ( ) ;
    node.add ( ex1 ) ;
    PrettyAnnotation prettyAnnotation = pExpression.toPrettyString ( )
        .getAnnotationForPrintable ( e1 ) ;
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode ( new ASTNode ( b
        .getClass ( ).getSimpleName ( ) , b.toString ( ) , new ASTPair (
        ( 2 * prettyAnnotation.getStartOffset ( ) )
            + e1.toPrettyString ( ).toString ( ).length ( ) ,
        ( 2 * prettyAnnotation.getStartOffset ( ) )
            + e1.toPrettyString ( ).toString ( ).length ( )
            + b.toString ( ).length ( ) ) , null ) ) ;
    node.add ( node1 ) ;
    DefaultMutableTreeNode ex2 = expression ( e2 ) ;
    ASTNode node2 = ( ASTNode ) ex2.getUserObject ( ) ;
    node2.appendDescription ( BEFORE + "e2" + AFTER ) ;
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  private DefaultMutableTreeNode lambda ( Lambda pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression , pExpression ,
        pExpression.getId ( ) ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Identifier" ,
        pExpression.getId ( ) , new ASTPair ( 1 , pExpression.getId ( )
            .length ( ) ) , aSTBinding ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  private DefaultMutableTreeNode let ( Let pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression ,
        pExpression.getE2 ( ) , pExpression.getId ( ) ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Identifier" ,
        pExpression.getId ( ) , new ASTPair ( 4 , 3 + pExpression.getId ( )
            .length ( ) ) , aSTBinding ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  private DefaultMutableTreeNode letRec ( LetRec pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression , pExpression ,
        pExpression.getId ( ) ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Identifier" ,
        pExpression.getId ( ) , new ASTPair ( 8 , 7 + pExpression.getId ( )
            .length ( ) ) , aSTBinding ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  private DefaultMutableTreeNode location ( Location pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Name" , pExpression
        .getName ( ) ,
        new ASTPair ( 0 , pExpression.getName ( ).length ( ) - 1 ) , null ) ) ) ;
    return node ;
  }


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
        return - 1 ;
      }
    }
    if ( result == Integer.MAX_VALUE )
    {
      return 1 ;
    }
    return result ;
  }


  private DefaultMutableTreeNode multiLambda ( MultiLambda pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression , pExpression ,
        pExpression.getIdentifiers ( ) ) ;
    String idList[] = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    int lengthIdentifier = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode (
          new ASTNode ( "Identifier" , idList [ i ] , new ASTPair (
              lengthIdentifier + 2 + ( i * 2 ) , lengthIdentifier + 1
                  + idList [ i ].length ( ) + ( i * 2 ) ) , aSTBinding ) ) ;
      lengthIdentifier += idList [ i ].length ( ) ;
      node.add ( subchild ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  private DefaultMutableTreeNode multiLet ( MultiLet pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression ,
        pExpression.getE2 ( ) , pExpression.getIdentifiers ( ) ) ;
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    int lengthIdentifier = 0 ;
    final int length = idList.length ;
    for ( int index = 0 ; index < length ; index ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode (
          new ASTNode ( "Identifier" , idList [ index ] , new ASTPair (
              lengthIdentifier + 5 + ( index * 2 ) , lengthIdentifier + 4
                  + idList [ index ].length ( ) + ( index * 2 ) ) , aSTBinding ) ) ;
      lengthIdentifier += idList [ index ].length ( ) ;
      node.add ( subchild ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  private DefaultMutableTreeNode other ( Expression pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  private DefaultMutableTreeNode recursion ( Recursion pExpression )
  {
    ASTBinding aSTBinding = new ASTBinding ( pExpression , pExpression ,
        pExpression.getId ( ) ) ;
    String id = pExpression.getId ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new ASTNode (
        pExpression ) ) ;
    node.add ( new DefaultMutableTreeNode ( new ASTNode ( "Identifier" , id ,
        new ASTPair ( 4 , 3 + id.length ( ) ) , aSTBinding ) ) ) ;
    createChildren ( pExpression , node ) ;
    return node ;
  }


  public void setExpression ( Expression pExpression )
  {
    // TODO überprüfen ob neue Expression
    if ( pExpression == null )
    {
      return ;
    }
    // calculateBindings ( pExpression ) ;
    this.aSTUI.setRootNode ( expression ( pExpression ) ) ;
  }


  public ASTUI getASTUI ( )
  {
    return this.aSTUI ;
  }
}
