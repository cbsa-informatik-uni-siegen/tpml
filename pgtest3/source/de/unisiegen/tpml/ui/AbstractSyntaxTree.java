package de.unisiegen.tpml.ui ;


import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import javax.swing.JFrame ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.tree.MutableTreeNode ;
import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.BinaryCons ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.BooleanConstant ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.Condition1 ;
import de.unisiegen.tpml.core.expressions.EmptyList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Fst ;
import de.unisiegen.tpml.core.expressions.Hd ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.IsEmpty ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.expressions.Not ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.expressions.Projection ;
import de.unisiegen.tpml.core.expressions.RelationalOperator ;
import de.unisiegen.tpml.core.expressions.Snd ;
import de.unisiegen.tpml.core.expressions.Tl ;
import de.unisiegen.tpml.core.expressions.Tuple ;
import de.unisiegen.tpml.core.expressions.UnaryCons ;
import de.unisiegen.tpml.core.expressions.UnitConstant ;


public class AbstractSyntaxTree
{
  private JFrame jFrameTest ;


  private GridBagConstraints gridBagConstraints ;


  private JTree jTreeSyntax ;


  private DefaultMutableTreeNode root ;


  private DefaultTreeModel treeModel ;


  private static AbstractSyntaxTree abstractSyntax = null ;


  public static AbstractSyntaxTree getInstance ( )
  {
    if ( abstractSyntax == null )
    {
      abstractSyntax = new AbstractSyntaxTree ( ) ;
      return abstractSyntax ;
    }
    else
    {
      return abstractSyntax ;
    }
  }


  public void setExpression ( Expression pExpression )
  {
    treeModel.setRoot ( new DefaultMutableTreeNode ( pExpression
        .toPrettyString ( ) ) ) ;
    treeModel.insertNodeInto ( expression ( pExpression ) ,
        ( MutableTreeNode ) treeModel.getRoot ( ) , 0 ) ;
    this.jTreeSyntax.expandRow ( 0 ) ;
  }


  private AbstractSyntaxTree ( )
  {
    this.root = new DefaultMutableTreeNode ( "root" ) ;
    this.treeModel = new DefaultTreeModel ( this.root ) ;
    this.jTreeSyntax = new JTree ( this.treeModel ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    this.gridBagConstraints.insets = new Insets ( 2 , 2 , 2 , 2 ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.jFrameTest = new JFrame ( ) ;
    this.jFrameTest.setLayout ( new GridBagLayout ( ) ) ;
    this.jFrameTest.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameTest.setSize ( 600 , 450 ) ;
    this.jFrameTest.setLocation ( 50 , 50 ) ;
    this.jFrameTest.setDefaultCloseOperation ( JFrame.HIDE_ON_CLOSE ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jFrameTest.getContentPane ( ).add ( this.jTreeSyntax ,
        this.gridBagConstraints ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.jFrameTest.setVisible ( pVisible ) ;
  }


  private DefaultMutableTreeNode expression ( Expression pExpression )
  {
    if ( pExpression instanceof Application )
    {
      return application ( ( Application ) pExpression ) ;
    }
    else if ( pExpression instanceof Let )
    {
      return let ( ( Let ) pExpression ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      return lambda ( ( Lambda ) pExpression ) ;
    }
    else if ( pExpression instanceof ArithmeticOperator )
    {
      return arithmeticOperator ( ( ArithmeticOperator ) pExpression ) ;
    }
    else if ( pExpression instanceof InfixOperation )
    {
      return infixOperation ( ( InfixOperation ) pExpression ) ;
    }
    else if ( pExpression instanceof Condition )
    {
      return condition ( ( Condition ) pExpression ) ;
    }
    else if ( pExpression instanceof Condition1 )
    {
      return condition1 ( ( Condition1 ) pExpression ) ;
    }
    else if ( pExpression instanceof Identifier )
    {
      return identifier ( ( Identifier ) pExpression ) ;
    }
    else if ( pExpression instanceof And )
    {
      return and ( ( And ) pExpression ) ;
    }
    else if ( pExpression instanceof Or )
    {
      return or ( ( Or ) pExpression ) ;
    }
    else if ( pExpression instanceof Not )
    {
      return not ( ( Not ) pExpression ) ;
    }
    else if ( pExpression instanceof Tuple )
    {
      return tuple ( ( Tuple ) pExpression ) ;
    }
    else if ( pExpression instanceof Fst )
    {
      return fst ( ( Fst ) pExpression ) ;
    }
    else if ( pExpression instanceof Snd )
    {
      return snd ( ( Snd ) pExpression ) ;
    }
    else if ( pExpression instanceof Projection )
    {
      return projection ( ( Projection ) pExpression ) ;
    }
    else if ( pExpression instanceof List )
    {
      return list ( ( List ) pExpression ) ;
    }
    else if ( pExpression instanceof IsEmpty )
    {
      return isEmpty ( ( IsEmpty ) pExpression ) ;
    }
    else if ( pExpression instanceof UnaryCons )
    {
      return unaryCons ( ( UnaryCons ) pExpression ) ;
    }
    else if ( pExpression instanceof EmptyList )
    {
      return emptyList ( ( EmptyList ) pExpression ) ;
    }
    else if ( pExpression instanceof BinaryCons )
    {
      return binaryCons ( ( BinaryCons ) pExpression ) ;
    }
    else if ( pExpression instanceof Hd )
    {
      return hd ( ( Hd ) pExpression ) ;
    }
    else if ( pExpression instanceof Tl )
    {
      return tl ( ( Tl ) pExpression ) ;
    }
    else if ( pExpression instanceof IntegerConstant )
    {
      return integerConstant ( ( IntegerConstant ) pExpression ) ;
    }
    else if ( pExpression instanceof BooleanConstant )
    {
      return booleanConstant ( ( BooleanConstant ) pExpression ) ;
    }
    else if ( pExpression instanceof UnitConstant )
    {
      return unitConstant ( ( UnitConstant ) pExpression ) ;
    }
    else
    {
      return new DefaultMutableTreeNode ( "UNKNOWN" ) ;
    }
  }


  private DefaultMutableTreeNode application ( Application pApplication )
  {
    Expression e1 = pApplication.getE1 ( ) ;
    Expression e2 = pApplication.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "Application { " + pApplication.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode let ( Let pLet )
  {
    String id = pLet.getId ( ) ;
    Expression e1 = pLet.getE1 ( ) ;
    Expression e2 = pLet.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Let { "
        + pLet.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
        "Identifier { " + id + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild2 , 0 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( expression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode lambda ( Lambda pLambda )
  {
    String id = pLambda.getId ( ) ;
    Expression e = pLambda.getE ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Lamdba { "
        + pLambda.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
        "Identifier { " + id + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e { "
        + e.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode arithmeticOperator ( ArithmeticOperator expr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "ArithmeticOperator { " + expr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode infixOperation ( InfixOperation pInfixOperation )
  {
    Expression e1 = pInfixOperation.getE1 ( ) ;
    Expression e2 = pInfixOperation.getE2 ( ) ;
    BinaryOperator b = pInfixOperation.getOp ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "InfixOperation { " + pInfixOperation.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    String text ;
    text = "BinaryOperator { " + b.toPrettyString ( ) + " }" ;
    if ( b instanceof ArithmeticOperator )
    {
      text = "ArithmeticOperator { " + b.toPrettyString ( ) + " }" ;
    }
    else if ( b instanceof RelationalOperator )
    {
      text = "RelationalOperator { " + b.toPrettyString ( ) + " }" ;
    }
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( text ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( expression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode condition ( Condition pCondition )
  {
    Expression e0 = pCondition.getE0 ( ) ;
    Expression e1 = pCondition.getE1 ( ) ;
    Expression e2 = pCondition.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Condition { "
        + pCondition.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e0 { "
        + e0.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( expression ( e0 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild2 , 0 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( expression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode condition1 ( Condition1 pCondition1 )
  {
    Expression e0 = pCondition1.getE0 ( ) ;
    Expression e1 = pCondition1.getE1 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Condition1 { "
        + pCondition1.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e0 { "
        + e0.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( expression ( e0 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode identifier ( Identifier pIdentifier )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Identifier { "
        + pIdentifier.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode integerConstant (
      IntegerConstant pIntegerConstant )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "IntegerConstant { " + pIntegerConstant.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode booleanConstant (
      BooleanConstant pBooleanConstant )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "BooleanConstant { " + pBooleanConstant.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode unitConstant ( UnitConstant pBooleanConstant )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "UnitConstant { " + pBooleanConstant.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode tuple ( Tuple pTuple )
  {
    Expression [ ] e = pTuple.getExpressions ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Tuple { "
        + pTuple.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < e.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode ( "e" + i
          + " { " + e [ i ].toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
      treeModel.insertNodeInto ( expression ( e [ i ] ) , subchild , 0 ) ;
    }
    return child ;
  }


  private DefaultMutableTreeNode fst ( Fst pFst )
  {
    return new DefaultMutableTreeNode ( "Fst { " + pFst.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode snd ( Snd pSnd )
  {
    return new DefaultMutableTreeNode ( "Snd { " + pSnd.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode projection ( Projection pProjection )
  {
    return new DefaultMutableTreeNode ( "Projection { "
        + pProjection.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode list ( List pList )
  {
    Expression [ ] e = pList.getExpressions ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "List { "
        + pList.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < e.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode ( "e" + i
          + " { " + e [ i ].toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
      treeModel.insertNodeInto ( expression ( e [ i ] ) , subchild , 0 ) ;
    }
    return child ;
  }


  private DefaultMutableTreeNode emptyList ( EmptyList pEmptyList )
  {
    return new DefaultMutableTreeNode ( "EmptyList { "
        + pEmptyList.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode isEmpty ( IsEmpty expr )
  {
    return new DefaultMutableTreeNode ( "IsEmpty { " + expr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode unaryCons ( UnaryCons pUnaryCons )
  {
    return new DefaultMutableTreeNode ( "UnaryCons { "
        + pUnaryCons.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode binaryCons ( BinaryCons pBinaryCons )
  {
    return new DefaultMutableTreeNode ( "BinaryCons { "
        + pBinaryCons.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode hd ( Hd pHd )
  {
    return new DefaultMutableTreeNode ( "Hd { " + pHd.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode tl ( Tl pTl )
  {
    return new DefaultMutableTreeNode ( "Tl { " + pTl.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode and ( And pAnd )
  {
    Expression e1 = pAnd.getE1 ( ) ;
    Expression e2 = pAnd.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "And { "
        + pAnd.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode or ( Or pOr )
  {
    Expression e1 = pOr.getE1 ( ) ;
    Expression e2 = pOr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Or { "
        + pOr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( expression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( expression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode not ( Not pNot )
  {
    return new DefaultMutableTreeNode ( "Not { " + pNot.toPrettyString ( )
        + " }" ) ;
  }


  public JTree getJTreeSyntax ( )
  {
    return jTreeSyntax ;
  }
}
