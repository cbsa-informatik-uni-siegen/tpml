package de.unisiegen.tpml.ui ;


import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;


public class AbstractSyntaxTreeListener implements TreeSelectionListener
{
  private AbstractSyntaxTree abstractSyntaxTree ;


  public AbstractSyntaxTreeListener ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  public void valueChanged ( TreeSelectionEvent event )
  {
    TreePath treePath = event.getNewLeadSelectionPath ( ) ;
    DefaultMutableTreeNode selectedTreeNode = ( DefaultMutableTreeNode ) treePath
        .getLastPathComponent ( ) ;
    Object object = selectedTreeNode.getUserObject ( ) ;
    AbstractSyntaxTreeNode rootNode = ( ( AbstractSyntaxTreeNode ) abstractSyntaxTree
        .getRoot ( ).getUserObject ( ) ) ;
    if ( object instanceof AbstractSyntaxTreeNode )
    {
      AbstractSyntaxTreeNode selectedNode = ( AbstractSyntaxTreeNode ) object ;
      if ( selectedNode.getExpression ( ) != null )
      {
        Expression rootExpr = rootNode.getExpression ( ) ;
        Expression currentExpr = selectedNode.getExpression ( ) ;
        PrettyString prettyString = rootExpr.toPrettyString ( ) ;
        PrettyAnnotation prettyAnnotation = prettyString
            .getAnnotationForPrintable ( currentExpr ) ;
        rootNode.updateHtml ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) ) ;
        abstractSyntaxTree.getRoot ( ).setUserObject ( rootNode ) ;
        abstractSyntaxTree.getTreeModel ( ).nodeChanged (
            abstractSyntaxTree.getRoot ( ) ) ;
        return ;
      }
    }
    rootNode.resetHtml ( ) ;
    abstractSyntaxTree.getRoot ( ).setUserObject ( rootNode ) ;
    abstractSyntaxTree.getTreeModel ( ).nodeChanged (
        abstractSyntaxTree.getRoot ( ) ) ;
  }
}
