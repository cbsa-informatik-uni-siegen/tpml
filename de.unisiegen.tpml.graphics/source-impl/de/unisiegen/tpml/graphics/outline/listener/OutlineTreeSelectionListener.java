package de.unisiegen.tpml.graphics.outline.listener ;


import java.util.ArrayList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;


/**
 * This class listens for <code>TreeSelectionEvent</code>. It updates the
 * caption of the selected node and its higher nodes.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineTreeSelectionListener implements
    TreeSelectionListener
{
  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the {@link OutlineTreeSelectionListener} with the given
   * {@link OutlineUI}.
   * 
   * @param pOutlineUI The {@link OutlineUI}.
   */
  public OutlineTreeSelectionListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
  }


  /**
   * Returns the index of the {@link Identifier} in the parent node.
   * 
   * @param pParent The parent node.
   * @param pIdentifier The {@link Identifier} node.
   * @return The index of the {@link Identifier} in the parent node.
   */
  private final int identifierIndex ( DefaultMutableTreeNode pParent ,
      DefaultMutableTreeNode pIdentifier )
  {
    for ( int i = 0 ; i < pParent.getChildCount ( ) ; i ++ )
    {
      if ( pParent.getChildAt ( i ).equals ( pIdentifier ) )
      {
        return i ;
      }
    }
    return OutlineNode.NO_BINDING ;
  }


  /**
   * Repaints the given node and all its children.
   * 
   * @param pNode The node, which should be repainted.
   */
  private final void repaint ( DefaultMutableTreeNode pNode )
  {
    this.outlineUI.getTreeModel ( ).nodeChanged ( pNode ) ;
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
  public final void reset ( DefaultMutableTreeNode pNode )
  {
    OutlineNode outlineNode = ( OutlineNode ) pNode.getUserObject ( ) ;
    outlineNode.resetCaption ( ) ;
    outlineNode.setReplaceInThisNode ( false ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void update ( TreePath pTreePath )
  {
    if ( pTreePath == null )
    {
      DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) this.outlineUI
          .getTreeModel ( ).getRoot ( ) ;
      reset ( rootNode ) ;
      repaint ( rootNode ) ;
      return ;
    }
    ArrayList < OutlineNode > list = new ArrayList < OutlineNode > ( ) ;
    for ( int i = 0 ; i < pTreePath.getPathCount ( ) ; i ++ )
    {
      list.add ( ( OutlineNode ) ( ( DefaultMutableTreeNode ) pTreePath
          .getPath ( ) [ i ] ).getUserObject ( ) ) ;
    }
    OutlineNode last = list.get ( list.size ( ) - 1 ) ;
    // Identifier
    if ( ( last.getStartIndex ( ) != - 1 ) && ( last.getEndIndex ( ) != - 1 ) )
    {
      OutlineNode secondlast = list.get ( list.size ( ) - 2 ) ;
      // Highlight the selected Identifier
      last.enableSelectionColor ( ) ;
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( DefaultMutableTreeNode ) pTreePath.getLastPathComponent ( ) ) ) ;
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
          childIndex = OutlineNode.NO_BINDING ;
        }
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                secondlast.getExpression ( ) ) ;
        list.get ( i ).setOutlineBinding ( last.getOutlineBinding ( ) ) ;
        list.get ( i ).setReplaceInThisNode ( true ) ;
        list.get ( i ).updateCaption (
            prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
            prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) ,
            childIndex ) ;
        this.outlineUI.getTreeModel ( ).nodeChanged (
            ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
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
        if ( list.size ( ) == 1 )
        {
          list.get ( i ).setReplaceInThisNode ( false ) ;
        }
        list.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) , OutlineNode.NO_BINDING ) ;
        this.outlineUI.getTreeModel ( ).nodeChanged (
            ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
      }
    }
  }


  /**
   * This method is invoked if a node value has changed.
   * 
   * @param pTreeSelectionEvent The <code>TreeSelectionEvent</code>
   * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
   */
  public final void valueChanged ( TreeSelectionEvent pTreeSelectionEvent )
  {
    if ( pTreeSelectionEvent.getSource ( ).equals (
        this.outlineUI.getJTreeAbstractSyntaxTree ( ).getSelectionModel ( ) ) )
    {
      TreePath treePath = pTreeSelectionEvent.getOldLeadSelectionPath ( ) ;
      if ( ( treePath != null ) && ( treePath.getPathCount ( ) > 1 ) )
      {
        reset ( ( DefaultMutableTreeNode ) treePath.getPathComponent ( 1 ) ) ;
      }
      update ( pTreeSelectionEvent.getPath ( ) ) ;
    }
  }
}
