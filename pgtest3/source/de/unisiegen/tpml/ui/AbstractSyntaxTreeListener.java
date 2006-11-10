package de.unisiegen.tpml.ui ;


import java.util.LinkedList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;


public class AbstractSyntaxTreeListener implements TreeSelectionListener
{
  private AbstractSyntaxTree abstractSyntaxTree ;


  public AbstractSyntaxTreeListener ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  private void repaint ( DefaultMutableTreeNode pNode )
  {
    abstractSyntaxTree.getTreeModel ( ).nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  private void resetHighlighting ( DefaultMutableTreeNode pNode )
  {
    AbstractSyntaxTreeNode abstractSyntaxTreeNode = ( AbstractSyntaxTreeNode ) pNode
        .getUserObject ( ) ;
    abstractSyntaxTreeNode.resetHtml ( ) ;
    abstractSyntaxTree.getTreeModel ( ).nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      resetHighlighting ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  public void valueChanged ( TreeSelectionEvent pEvent )
  {
    TreePath treePath = pEvent.getNewLeadSelectionPath ( ) ;
    if ( treePath == null )
    {
      System.err.println ( "TreePath == null" ) ;
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
        list.get ( i ).updateHtml (
            prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
            prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) ) ;
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
        PrettyCharIterator p = prettyString.toCharacterIterator ( ) ;
        char c = p.first ( ) ;
        String s = "" ;
        while ( c != PrettyCharIterator.DONE )
        {
          if ( p.getStyle ( ) == PrettyStyle.KEYWORD )
          {
            s += "X" ;
          }
          else
          {
            s += " " ;
          }
          c = p.next ( ) ;
        }
        list.get ( i ).setKeyword ( s ) ;
        list.get ( i ).updateHtml ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) ) ;
      }
    }
    repaint ( ( DefaultMutableTreeNode ) treePath.getPath ( ) [ 0 ] ) ;
  }
}
