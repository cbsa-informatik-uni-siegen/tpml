package de.unisiegen.tpml.ui ;


import java.util.LinkedList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;


public class AbstractSyntaxTreeListener implements TreeSelectionListener
{
  private AbstractSyntaxTree abstractSyntaxTree ;


  public AbstractSyntaxTreeListener ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  public void valueChanged ( TreeSelectionEvent pEvent )
  {
    TreePath treePath = pEvent.getNewLeadSelectionPath ( ) ;
    if ( treePath == null )
    {
      return ;
    }
    LinkedList < AbstractSyntaxTreeNode > list = new LinkedList < AbstractSyntaxTreeNode > ( ) ;
    for ( int i = 0 ; i < treePath.getPathCount ( ) ; i ++ )
    {
      Object tmp = ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ i ] )
          .getUserObject ( ) ;
      if ( ( tmp instanceof AbstractSyntaxTreeNode )
          && ( ( ( AbstractSyntaxTreeNode ) tmp ).getExpression ( ) != null ) )
      {
        list.add ( ( AbstractSyntaxTreeNode ) tmp ) ;
      }
    }
    resetHighlighting ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ 0 ] ) ;
    Expression last = list.getLast ( ).getExpression ( ) ;
    Expression secondLast = list.get ( list.size ( ) - 2 ).getExpression ( ) ;
    if ( last instanceof Identifier )
    {
      if ( secondLast instanceof Lambda )
      {
        Identifier id = ( Identifier ) last ;
        for ( int i = 0 ; i < list.size ( ) - 1 ; i ++ )
        {
          PrettyString prettyString = list.get ( i ).getExpression ( )
              .toPrettyString ( ) ;
          PrettyAnnotation prettyAnnotation = prettyString
              .getAnnotationForPrintable ( secondLast ) ;
          int start = prettyAnnotation.getStartOffset ( ) + 1 ;
          int end = start + id.getName ( ).length ( ) ;
          list.get ( i ).updateHtml ( start , end ) ;
        }
      }
    }
    else
    {
      for ( int i = 0 ; i < list.size ( ) ; i ++ )
      {
        // Get the start and end offset
        PrettyString prettyString = list.get ( i ).getExpression ( )
            .toPrettyString ( ) ;
        PrettyAnnotation prettyAnnotation = prettyString
            .getAnnotationForPrintable ( last ) ;
        // Update the HTML Caption
        list.get ( i ).updateHtml ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) ) ;
      }
    }
    repaint ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ 0 ] ) ;
  }


  private void resetHighlighting ( DefaultMutableTreeNode pNode )
  {
    AbstractSyntaxTreeNode a = ( AbstractSyntaxTreeNode ) pNode
        .getUserObject ( ) ;
    a.resetHtml ( ) ;
    abstractSyntaxTree.getTreeModel ( ).nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      resetHighlighting ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  private void repaint ( DefaultMutableTreeNode pNode )
  {
    abstractSyntaxTree.getTreeModel ( ).nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }
}
