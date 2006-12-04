package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.util.ArrayList ;
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
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * This class listens for tree selection events. It updates the caption of the
 * selected node and its higher nodes.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTTreeSelectionListener implements TreeSelectionListener
{
  /**
   * The AbstractSyntaxTree UI.
   */
  private ASTUI aSTUI ;


  /**
   * Initializes the ASTTreeSelectionListener with the given ASTUI.
   * 
   * @param pASTUI The AbstractSyntaxTree UI.
   */
  public ASTTreeSelectionListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  /**
   * Returns the index of the Identifier in the parent node.
   * 
   * @param pParent The parent node.
   * @param pIdentifier The Identifier node.
   * @return The index of the Identifier in the parent node.
   */
  private int identifierIndex ( DefaultMutableTreeNode pParent ,
      DefaultMutableTreeNode pIdentifier )
  {
    for ( int i = 0 ; i < pParent.getChildCount ( ) ; i ++ )
    {
      if ( pParent.getChildAt ( i ).equals ( pIdentifier ) )
      {
        return i ;
      }
    }
    return ASTNode.NO_BINDING ;
  }


  /**
   * Repaints the given node and all its children.
   * 
   * @param pNode The node, which should be repainted.
   */
  private void repaint ( DefaultMutableTreeNode pNode )
  {
    this.aSTUI.getTreeModel ( ).nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Resets the given node and all its children.
   * 
   * @param pNode The node, which should be reseted.
   */
  private void reset ( DefaultMutableTreeNode pNode )
  {
    ASTNode aSTNode = ( ASTNode ) pNode.getUserObject ( ) ;
    aSTNode.resetCaption ( ) ;
    aSTNode.setReplaceInThisNode ( false ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pTreePath The selected TreePath.
   */
  public void update ( TreePath pTreePath )
  {
    if ( pTreePath == null )
    {
      DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) this.aSTUI
          .getTreeModel ( ).getRoot ( ) ;
      reset ( rootNode ) ;
      repaint ( rootNode ) ;
      return ;
    }
    ArrayList < ASTNode > list = new ArrayList < ASTNode > ( ) ;
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
    // TODO not very good, because everything is reseted.
    reset ( rootNode ) ;
    ASTNode last = list.get ( list.size ( ) - 1 ) ;
    // Identifier
    if ( ( last.getStartIndex ( ) != - 1 ) && ( last.getEndIndex ( ) != - 1 ) )
    {
      ASTNode secondlast = list.get ( list.size ( ) - 2 ) ;
      // Highlight the selected Identifier
      last.enableSelectedColor ( ) ;
      for ( int i = 0 ; i < list.size ( ) - 1 ; i ++ )
      {
        int childIndex = identifierIndex ( ( DefaultMutableTreeNode ) pTreePath
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
          childIndex = ASTNode.NO_BINDING ;
        }
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                secondlast.getExpression ( ) ) ;
        list.get ( i ).setASTBinding ( last.getASTBinding ( ) ) ;
        list.get ( i ).setReplaceInThisNode ( true ) ;
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
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                last.getExpression ( ) ) ;
        if ( i < list.size ( ) - 1 )
        {
          list.get ( i ).setReplaceInThisNode ( true ) ;
        }
        list.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) , ASTNode.NO_BINDING ) ;
      }
    }
    repaint ( rootNode ) ;
  }


  /**
   * This method is invoked if a node value has changed.
   * 
   * @param pTreeSelectionEvent
   * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
   */
  public void valueChanged ( TreeSelectionEvent pTreeSelectionEvent )
  {
    TreePath t = pTreeSelectionEvent.getOldLeadSelectionPath ( ) ;
    if ( ( t != null ) && ( t.getPathCount ( ) > 1 ) )
    {
      reset ( ( DefaultMutableTreeNode ) t.getPathComponent ( 1 ) ) ;
    }
    update ( pTreeSelectionEvent.getPath ( ) ) ;
  }
}
