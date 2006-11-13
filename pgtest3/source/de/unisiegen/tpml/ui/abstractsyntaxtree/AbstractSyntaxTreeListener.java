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
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;


public class AbstractSyntaxTreeListener implements TreeSelectionListener
{
  private AbstractSyntaxTreeUI abstractSyntaxTreeUI ;


  public AbstractSyntaxTreeListener ( AbstractSyntaxTreeUI pAbstractSyntaxTreeUI )
  {
    this.abstractSyntaxTreeUI = pAbstractSyntaxTreeUI ;
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
    this.abstractSyntaxTreeUI.nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  private void resetHighlighting ( DefaultMutableTreeNode pNode )
  {
    AbstractSyntaxTreeNode abstractSyntaxTreeNode = ( AbstractSyntaxTreeNode ) pNode
        .getUserObject ( ) ;
    abstractSyntaxTreeNode.resetCaption ( ) ;
    this.abstractSyntaxTreeUI.nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      resetHighlighting ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  public void valueChanged ( TreeSelectionEvent pEvent )
  {
    TreePath treePath = pEvent.getPath ( ) ;
    if ( treePath == null )
    {
      System.err.println ( "treePath == null" ) ;
      return ;
    }
    LinkedList < AbstractSyntaxTreeNode > list = new LinkedList < AbstractSyntaxTreeNode > ( ) ;
    for ( int i = 0 ; i < treePath.getPathCount ( ) ; i ++ )
    {
      Object tmp = ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ i ] )
          .getUserObject ( ) ;
      if ( ( tmp instanceof AbstractSyntaxTreeNode ) )
      {
        list.add ( ( AbstractSyntaxTreeNode ) tmp ) ;
      }
    }
    resetHighlighting ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ 0 ] ) ;
    AbstractSyntaxTreeNode last = list.getLast ( ) ;
    AbstractSyntaxTreeNode secondlast = null ;
    if ( list.size ( ) >= 2 )
    {
      secondlast = list.get ( list.size ( ) - 2 ) ;
    }
    // No Expression
    if ( ( last.getStartIndex ( ) != - 1 ) && ( last.getEndIndex ( ) != - 1 ) )
    {
      for ( int i = 0 ; i < list.size ( ) - 1 ; i ++ )
      {
        PrettyString prettyString = list.get ( i ).getExpression ( )
            .toPrettyString ( ) ;
        PrettyAnnotation prettyAnnotation = prettyString
            .getAnnotationForPrintable ( secondlast.getExpression ( ) ) ;
        int childIndex = childIndex ( ( DefaultMutableTreeNode ) treePath
            .getPath ( ) [ treePath.getPathCount ( ) - 2 ] ,
            ( DefaultMutableTreeNode ) treePath.getPath ( ) [ treePath
                .getPathCount ( ) - 1 ] ) ;
        if ( ! ( secondlast.getExpression ( ) instanceof Lambda )
            && ! ( secondlast.getExpression ( ) instanceof MultiLambda )
            && ! ( secondlast.getExpression ( ) instanceof MultiLet )
            && ! ( secondlast.getExpression ( ) instanceof LetRec )
            && ! ( secondlast.getExpression ( ) instanceof Let )
            && ! ( secondlast.getExpression ( ) instanceof CurriedLetRec )
            && ! ( secondlast.getExpression ( ) instanceof CurriedLet ) )
        {
          childIndex = - 1 ;
        }
        list.get ( i ).updateCaption (
            prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
            prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) ,
            childIndex ) ;
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
        list.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) , - 1 ) ;
      }
    }
    repaint ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ 0 ] ) ;
  }
}
