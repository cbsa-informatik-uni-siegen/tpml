package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.LinkedList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;


public class ASTTreeSelectionListener implements TreeSelectionListener
{
  private ASTUI aSTUI ;


  public ASTTreeSelectionListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  private int childIndex ( DefaultMutableTreeNode pParent ,
      DefaultMutableTreeNode pChild )
  {
    for ( int i = 0 ; i < pParent.getChildCount ( ) ; i ++ )
    {
      if ( pParent.getChildAt ( i ).equals ( pChild ) )
      {
        return i ;
      }
    }
    return - 1 ;
  }


  private void repaint ( DefaultMutableTreeNode pNode )
  {
    this.aSTUI.nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  private void reset ( DefaultMutableTreeNode pNode )
  {
    ASTNode aSTNode = ( ASTNode ) pNode.getUserObject ( ) ;
    aSTNode.resetCaption ( ) ;
    aSTNode.setReplaceExpression ( false ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  public void update ( TreePath pTreePath )
  {
    if ( pTreePath == null )
    {
      return ;
    }
    LinkedList < ASTNode > list = new LinkedList < ASTNode > ( ) ;
    for ( int i = 0 ; i < pTreePath.getPathCount ( ) ; i ++ )
    {
      Object tmp = ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] )
          .getUserObject ( ) ;
      if ( ( tmp instanceof ASTNode ) )
      {
        list.add ( ( ASTNode ) tmp ) ;
      }
    }
    DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) pTreePath
        .getPath ( ) [ 0 ] ;
    reset ( rootNode ) ;
    ASTNode last = list.getLast ( ) ;
    ASTNode secondlast = null ;
    if ( list.size ( ) >= 2 )
    {
      secondlast = list.get ( list.size ( ) - 2 ) ;
    }
    // No Expression
    if ( ( last.getStartIndex ( ) != - 1 ) && ( last.getEndIndex ( ) != - 1 ) )
    {
      if ( secondlast == null )
      {
        return ;
      }
      list.get ( list.size ( ) - 1 ).enableSelectedColor ( ) ;
      for ( int i = 0 ; i < list.size ( ) - 1 ; i ++ )
      {
        PrettyString prettyString = list.get ( i ).getExpression ( )
            .toPrettyString ( ) ;
        PrettyAnnotation prettyAnnotation = prettyString
            .getAnnotationForPrintable ( secondlast.getExpression ( ) ) ;
        int childIndex = childIndex ( ( DefaultMutableTreeNode ) pTreePath
            .getPath ( ) [ pTreePath.getPathCount ( ) - 2 ] ,
            ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ pTreePath
                .getPathCount ( ) - 1 ] ) ;
        if ( ! ( secondlast.getExpression ( ) instanceof Lambda )
            && ! ( secondlast.getExpression ( ) instanceof MultiLambda )
            && ! ( secondlast.getExpression ( ) instanceof MultiLet )
            && ! ( secondlast.getExpression ( ) instanceof LetRec )
            && ! ( secondlast.getExpression ( ) instanceof Let )
            && ! ( secondlast.getExpression ( ) instanceof CurriedLetRec )
            && ! ( secondlast.getExpression ( ) instanceof CurriedLet )
            && ! ( secondlast.getExpression ( ) instanceof Recursion ) )
        {
          childIndex = - 1 ;
        }
        list.get ( i ).setReplaceExpression ( true ) ;
        // CHANGE CHRISTIAN
        if ( i == list.size ( ) - 2 )
        {
          list.get ( i ).updateCaption (
              prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
              prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) ,
              childIndex ) ;
        }
        else
        {
          list.get ( i ).updateCaption (
              prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
              prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) , - 1 ) ;
        }
      }
    }
    // Expression
    else
    {
      for ( int i = 0 ; i < list.size ( ) ; i ++ )
      {
        PrettyString prettyString = list.get ( i ).getExpression ( )
            .toPrettyString ( ) ;
        PrettyAnnotation prettyAnnotation = prettyString
            .getAnnotationForPrintable ( last.getExpression ( ) ) ;
        if ( i < list.size ( ) - 1 )
        {
          list.get ( i ).setReplaceExpression ( true ) ;
        }
        list.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) , - 1 ) ;
      }
    }
    repaint ( rootNode ) ;
  }


  public void valueChanged ( TreeSelectionEvent pTreeSelectionEvent )
  {
    update ( pTreeSelectionEvent.getPath ( ) ) ;
  }
}
