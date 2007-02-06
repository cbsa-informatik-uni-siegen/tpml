package de.unisiegen.tpml.graphics.outline.listener ;


import java.util.ArrayList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;


/**
 * This class listens for <code>TreeSelectionEvent</code>. It updates the
 * caption of the selected and higher nodes.
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
    outlineNode.setReplaceInThisNode ( false ) ;
    outlineNode.setOutlineBinding ( null ) ;
    outlineNode.resetCaption ( ) ;
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
    // Expression - Identifier - Type
    if ( ( last.getStartIndex ( ) != - 1 ) && ( last.getEndIndex ( ) != - 1 )
        && ( list.get ( list.size ( ) - 2 ).getStartIndex ( ) != - 1 )
        && ( list.get ( list.size ( ) - 2 ).getEndIndex ( ) != - 1 ) )
    {
      OutlineNode lastButTwo = list.get ( list.size ( ) - 3 ) ;
      /*
       * Highlight the selected Type
       */
      last.enableSelectionColor ( ) ;
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( DefaultMutableTreeNode ) pTreePath.getLastPathComponent ( ) ) ) ;
      for ( int i = 0 ; i < list.size ( ) - 2 ; i ++ )
      {
        /*
         * Sets the new binding in higher nodes
         */
        list.get ( i ).setOutlineBinding ( last.getOutlineBinding ( ) ) ;
        /*
         * It should be replaced in higher nodes
         */
        list.get ( i ).setReplaceInThisNode ( true ) ;
        /*
         * Update the caption of the node
         */
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                lastButTwo.getExpression ( ) ) ;
        list.get ( i ).updateCaption (
            prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
            prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) ) ;
        /*
         * Node has changed and can be repainted
         */
        this.outlineUI.getTreeModel ( ).nodeChanged (
            ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
      }
    }
    // Expression - Identifier or Expression - Type
    else if ( ( last.getStartIndex ( ) != - 1 )
        && ( last.getEndIndex ( ) != - 1 ) )
    {
      OutlineNode secondLast = list.get ( list.size ( ) - 2 ) ;
      /*
       * Highlight the selected Type
       */
      last.enableSelectionColor ( ) ;
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( DefaultMutableTreeNode ) pTreePath.getLastPathComponent ( ) ) ) ;
      /*
       * Highlight the bounded Identifiers of an Attribute in the other childs
       * of the parent row.
       */
      if ( secondLast.getExpression ( ) instanceof Attr )
      {
        DefaultMutableTreeNode nodeRow = ( DefaultMutableTreeNode ) pTreePath
            .getPath ( ) [ pTreePath.getPathCount ( ) - 3 ] ;
        DefaultMutableTreeNode nodeAttr = ( DefaultMutableTreeNode ) pTreePath
            .getPath ( ) [ pTreePath.getPathCount ( ) - 2 ] ;
        for ( int i = nodeRow.getIndex ( nodeAttr ) + 1 ; i < nodeRow
            .getChildCount ( ) ; i ++ )
        {
          OutlineNode outlineNodeRowChild = ( OutlineNode ) ( ( DefaultMutableTreeNode ) nodeRow
              .getChildAt ( i ) ).getUserObject ( ) ;
          outlineNodeRowChild.setOutlineBinding ( last.getOutlineBinding ( ) ) ;
          outlineNodeRowChild.updateCaption ( ) ;
          this.outlineUI.getTreeModel ( )
              .nodeChanged ( nodeRow.getChildAt ( i ) ) ;
        }
      }
      for ( int i = 0 ; i < list.size ( ) - 1 ; i ++ )
      {
        /*
         * Sets the new binding in higher nodes
         */
        list.get ( i ).setOutlineBinding ( last.getOutlineBinding ( ) ) ;
        /*
         * It should be replaced in higher nodes
         */
        list.get ( i ).setReplaceInThisNode ( true ) ;
        /*
         * Update the caption of the node
         */
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                secondLast.getExpression ( ) ) ;
        list.get ( i ).updateCaption (
            prettyAnnotation.getStartOffset ( ) + last.getStartIndex ( ) ,
            prettyAnnotation.getStartOffset ( ) + last.getEndIndex ( ) ) ;
        /*
         * Node has changed and can be repainted
         */
        this.outlineUI.getTreeModel ( ).nodeChanged (
            ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
      }
    }
    // Expression
    else
    {
      for ( int i = 0 ; i < list.size ( ) ; i ++ )
      {
        /*
         * It should be replaced in higher nodes, but not the selected node
         */
        if ( i < list.size ( ) - 1 )
        {
          list.get ( i ).setReplaceInThisNode ( true ) ;
        }
        /*
         * If only the root is selected, there should not be replaced
         */
        if ( list.size ( ) == 1 )
        {
          list.get ( i ).setReplaceInThisNode ( false ) ;
        }
        /*
         * If a Expression is selected, any node has a no binding
         */
        list.get ( i ).setOutlineBinding ( null ) ;
        /*
         * Update the caption of the node
         */
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                last.getExpression ( ) ) ;
        list.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) ) ;
        /*
         * Node has changed and can be repainted
         */
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
