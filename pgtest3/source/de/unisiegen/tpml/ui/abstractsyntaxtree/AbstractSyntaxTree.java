package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.lang.reflect.Method ;
import java.util.Enumeration ;
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
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;


public class AbstractSyntaxTree
{
  private static AbstractSyntaxTree abstractSyntaxTree = null ;


  private static final String BEFORE = "" ;


  private static final String AFTER = "  -  " ;


  public static AbstractSyntaxTree getInstance ( )
  {
    if ( abstractSyntaxTree == null )
    {
      abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
      return abstractSyntaxTree ;
    }
    return abstractSyntaxTree ;
  }


  private ASTUI aSTUI ;


  private ASTPreferences aSTPreferences ;


  private AbstractSyntaxTree ( )
  {
    this.aSTPreferences = new ASTPreferences ( ) ;
    this.aSTUI = new ASTUI ( this ) ;
  }


  private DefaultMutableTreeNode createNode ( String pDescription ,
      Expression pExpr )
  {
    return new DefaultMutableTreeNode ( new ASTNode ( pDescription , pExpr ) ) ;
  }


  private DefaultMutableTreeNode createNode ( String pDescription ,
      Expression pExpr , ASTBindings pRelations )
  {
    return new DefaultMutableTreeNode ( new ASTNode ( pDescription , pExpr ,
        pRelations ) ) ;
  }


  private DefaultMutableTreeNode createNode ( String pDescription ,
      String pName , int pStart , int pEnd )
  {
    return new DefaultMutableTreeNode ( new ASTNode ( pDescription , pName ,
        pStart , pEnd ) ) ;
  }


  private DefaultMutableTreeNode curriedLet ( CurriedLet pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    ASTBindings free = new ASTBindings ( ) ;
    free.add ( pExpression.getE2 ( ) , pExpression.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      free.add ( pExpression.getE1 ( ) , pExpression.getIdentifiers ( i ) ) ;
    }
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , free ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchilds = createNode ( "Identifier" ,
          idList [ i ] , length + 4 + i , length + 3 + idList [ i ].length ( )
              + i ) ;
      length += idList [ i ].length ( ) ;
      node.add ( subchilds ) ;
    }
    DefaultMutableTreeNode e1 = expression ( pExpression.getE1 ( ) ) ;
    ASTNode node1 = ( ASTNode ) e1.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e1" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e1 ) ;
    DefaultMutableTreeNode e2 = expression ( pExpression.getE2 ( ) ) ;
    ASTNode node2 = ( ASTNode ) e2.getUserObject ( ) ;
    node2.setDescription ( BEFORE + "e2" + AFTER + node2.getDescription ( ) ) ;
    node2.resetCaption ( ) ;
    node.add ( e2 ) ;
    return node ;
  }


  private DefaultMutableTreeNode curriedLetRec ( CurriedLetRec pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    ASTBindings free = new ASTBindings ( ) ;
    free.add ( pExpression , pExpression.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      free.add ( pExpression.getE1 ( ) , pExpression.getIdentifiers ( i ) ) ;
    }
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , free ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchilds = createNode ( "Identifier" ,
          idList [ i ] , length + 8 + i , length + 7 + idList [ i ].length ( )
              + i ) ;
      length += idList [ i ].length ( ) ;
      node.add ( subchilds ) ;
    }
    DefaultMutableTreeNode e1 = expression ( pExpression.getE1 ( ) ) ;
    ASTNode node1 = ( ASTNode ) e1.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e1" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e1 ) ;
    DefaultMutableTreeNode e2 = expression ( pExpression.getE2 ( ) ) ;
    ASTNode node2 = ( ASTNode ) e2.getUserObject ( ) ;
    node2.setDescription ( BEFORE + "e2" + AFTER + node2.getDescription ( ) ) ;
    node2.resetCaption ( ) ;
    node.add ( e2 ) ;
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
    return normal ( pExpression ) ;
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
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression ) ;
    PrettyString prettyString = pExpression.toPrettyString ( ) ;
    PrettyAnnotation prettyAnnotation = prettyString
        .getAnnotationForPrintable ( e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( b.getClass ( )
        .getSimpleName ( ) , b.toString ( ) , ( 2 * prettyAnnotation
        .getStartOffset ( ) )
        + e1.toPrettyString ( ).toString ( ).length ( ) ,
        ( 2 * prettyAnnotation.getStartOffset ( ) )
            + e1.toPrettyString ( ).toString ( ).length ( )
            + b.toString ( ).length ( ) ) ;
    DefaultMutableTreeNode ex1 = expression ( e1 ) ;
    ASTNode node1 = ( ASTNode ) ex1.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e1" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( ex1 ) ;
    node.add ( subchild2 ) ;
    DefaultMutableTreeNode ex2 = expression ( e2 ) ;
    ASTNode node2 = ( ASTNode ) ex2.getUserObject ( ) ;
    node2.setDescription ( BEFORE + "e2" + AFTER + node2.getDescription ( ) ) ;
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  private DefaultMutableTreeNode lambda ( Lambda pExpression )
  {
    String id = pExpression.getId ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new ASTBindings ( pExpression ,
        pExpression.getId ( ) ) ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 1 , id
        .length ( ) ) ;
    node.add ( subchild1 ) ;
    DefaultMutableTreeNode e = expression ( pExpression.getE ( ) ) ;
    ASTNode node1 = ( ASTNode ) e.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e ) ;
    return node ;
  }


  private DefaultMutableTreeNode let ( Let pExpression )
  {
    String id = pExpression.getId ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new ASTBindings ( pExpression
        .getE2 ( ) , pExpression.getId ( ) ) ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 4 ,
        3 + id.length ( ) ) ;
    node.add ( subchild1 ) ;
    DefaultMutableTreeNode e1 = expression ( pExpression.getE1 ( ) ) ;
    ASTNode node1 = ( ASTNode ) e1.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e1" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e1 ) ;
    DefaultMutableTreeNode e2 = expression ( pExpression.getE2 ( ) ) ;
    ASTNode node2 = ( ASTNode ) e2.getUserObject ( ) ;
    node2.setDescription ( BEFORE + "e2" + AFTER + node2.getDescription ( ) ) ;
    node2.resetCaption ( ) ;
    node.add ( e2 ) ;
    return node ;
  }


  private DefaultMutableTreeNode letRec ( LetRec pExpression )
  {
    String id = pExpression.getId ( ) ;
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new ASTBindings ( pExpression ,
        pExpression.getId ( ) ) ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 8 ,
        7 + id.length ( ) ) ;
    node.add ( subchild1 ) ;
    DefaultMutableTreeNode ex1 = expression ( e1 ) ;
    ASTNode node1 = ( ASTNode ) ex1.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e1" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( ex1 ) ;
    DefaultMutableTreeNode ex2 = expression ( e2 ) ;
    ASTNode node2 = ( ASTNode ) ex2.getUserObject ( ) ;
    node2.setDescription ( BEFORE + "e2" + AFTER + node2.getDescription ( ) ) ;
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
    return node ;
  }


  private DefaultMutableTreeNode location ( Location pExpression )
  {
    String name = pExpression.getName ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Name" , name , 0 , name
        .length ( ) - 1 ) ;
    node.add ( subchild1 ) ;
    return node ;
  }


  private DefaultMutableTreeNode multiLambda ( MultiLambda pExpression )
  {
    String idList[] = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new ASTBindings ( pExpression ,
        pExpression.getIdentifiers ( ) ) ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" ,
          idList [ i ] , length + 2 + ( i * 2 ) , length + 1
              + idList [ i ].length ( ) + ( i * 2 ) ) ;
      length += idList [ i ].length ( ) ;
      node.add ( subchild ) ;
    }
    DefaultMutableTreeNode e = expression ( pExpression.getE ( ) ) ;
    ASTNode node1 = ( ASTNode ) e.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e ) ;
    return node ;
  }


  private DefaultMutableTreeNode multiLet ( MultiLet pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new ASTBindings ( pExpression
        .getE2 ( ) , pExpression.getIdentifiers ( ) ) ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" ,
          idList [ i ] , length + 5 + ( i * 2 ) , length + 4
              + idList [ i ].length ( ) + ( i * 2 ) ) ;
      length += idList [ i ].length ( ) ;
      node.add ( subchild ) ;
    }
    DefaultMutableTreeNode e1 = expression ( pExpression.getE1 ( ) ) ;
    ASTNode node1 = ( ASTNode ) e1.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e1" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e1 ) ;
    DefaultMutableTreeNode e2 = expression ( pExpression.getE2 ( ) ) ;
    ASTNode node2 = ( ASTNode ) e2.getUserObject ( ) ;
    node2.setDescription ( BEFORE + "e2" + AFTER + node2.getDescription ( ) ) ;
    node2.resetCaption ( ) ;
    node.add ( e2 ) ;
    return node ;
  }


  private DefaultMutableTreeNode normal ( Expression pExpression )
  {
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression ) ;
    Enumeration < Expression > children = pExpression.children ( ) ;
    int startIndex = startIndex ( pExpression ) ;
    while ( children.hasMoreElements ( ) )
    {
      Expression child = children.nextElement ( ) ;
      DefaultMutableTreeNode e = expression ( child ) ;
      ASTNode node1 = ( ASTNode ) e.getUserObject ( ) ;
      node1.setDescription ( BEFORE + "e" + startIndex + AFTER
          + node1.getDescription ( ) ) ;
      node1.resetCaption ( ) ;
      node.add ( e ) ;
      startIndex ++ ;
    }
    return node ;
  }


  private DefaultMutableTreeNode recursion ( Recursion pExpression )
  {
    String id = pExpression.getId ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new ASTBindings ( pExpression ,
        pExpression.getId ( ) ) ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 4 ,
        3 + id.length ( ) ) ;
    node.add ( subchild1 ) ;
    DefaultMutableTreeNode e = expression ( pExpression.getE ( ) ) ;
    ASTNode node1 = ( ASTNode ) e.getUserObject ( ) ;
    node1.setDescription ( BEFORE + "e" + AFTER + node1.getDescription ( ) ) ;
    node1.resetCaption ( ) ;
    node.add ( e ) ;
    return node ;
  }


  public void setExpression ( Expression pExpression )
  {
    this.aSTUI.setRootNode ( expression ( pExpression ) ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.aSTUI.setVisible ( pVisible ) ;
  }


  private int startIndex ( Expression pExpression )
  {
    int result = Integer.MAX_VALUE ;
    for ( Method method : pExpression.getClass ( ).getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).matches ( "getE[0-9]{1}" ) )
      {
        result = Math.min ( result , Integer.parseInt ( String.valueOf ( method
            .getName ( ).charAt ( 4 ) ) ) ) ;
      }
    }
    return result ;
  }
}
