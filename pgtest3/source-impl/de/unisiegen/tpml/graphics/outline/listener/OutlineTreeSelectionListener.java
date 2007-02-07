package de.unisiegen.tpml.graphics.outline.listener ;


import java.util.ArrayList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
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
    OutlineNode tmp = ( OutlineNode ) pNode.getUserObject ( ) ;
    if ( tmp.getExpression ( ) != null )
    {
      System.out
          .println ( tmp.getExpression ( ).toPrettyString ( ).toString ( ) ) ;
    }
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
    outlineNode.setBoundedStart ( OutlineNode.NO_BINDING ) ;
    outlineNode.setBoundedEnd ( OutlineNode.NO_BINDING ) ;
    outlineNode.resetCaption ( ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Sets the {@link OutlineBinding} to all children of the node.
   * 
   * @param pParent The parent node.
   * @param pOutlineBinding The {@link OutlineBinding}.
   */
  private final void setBindingToChildren ( DefaultMutableTreeNode pParent ,
      OutlineBinding pOutlineBinding )
  {
    for ( int i = 0 ; i < pParent.getChildCount ( ) ; i ++ )
    {
      DefaultMutableTreeNode child = ( DefaultMutableTreeNode ) pParent
          .getChildAt ( i ) ;
      OutlineNode node = ( OutlineNode ) child.getUserObject ( ) ;
      if ( node.getExpression ( ) != null )
      {
        node.setOutlineBinding ( pOutlineBinding ) ;
        node.updateCaption ( ) ;
        this.outlineUI.getTreeModel ( ).nodeChanged ( child ) ;
      }
      setBindingToChildren ( child , pOutlineBinding ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void update ( TreePath pTreePath )
  {
    DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) this.outlineUI
        .getTreeModel ( ).getRoot ( ) ;
    reset ( rootNode ) ;
    if ( pTreePath == null )
    {
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
    // Type
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
    // Identifier
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
          DefaultMutableTreeNode child = ( DefaultMutableTreeNode ) nodeRow
              .getChildAt ( i ) ;
          setBindingToChildren ( child , last.getOutlineBinding ( ) ) ;
          OutlineNode outlineNodeRowChild = ( OutlineNode ) child
              .getUserObject ( ) ;
          outlineNodeRowChild.setOutlineBinding ( last.getOutlineBinding ( ) ) ;
          outlineNodeRowChild.updateCaption ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( child ) ;
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
        if ( ( last.getExpression ( ) instanceof Identifier )
            && ( i < list.size ( ) - 1 )
            && ( ( ( Identifier ) last.getExpression ( ) ).boundedExpression ( ) != null ) )
        {
          try
          {
            Identifier identifier = ( Identifier ) last.getExpression ( ) ;
            /*
             * Highlight the bounded Identifiers in the other childs of a parent
             * row.
             */
            if ( ( list.get ( i ).getExpression ( ) instanceof Attr )
                || ( list.get ( i ).getExpression ( ) instanceof Meth )
                || ( list.get ( i ).getExpression ( ) instanceof CurriedMeth ) )
            {
              DefaultMutableTreeNode nodeRowChild = ( DefaultMutableTreeNode ) pTreePath
                  .getPath ( ) [ i ] ;
              DefaultMutableTreeNode nodeRow = ( DefaultMutableTreeNode ) pTreePath
                  .getPath ( ) [ i - 1 ] ;
              for ( int j = nodeRow.getIndex ( nodeRowChild ) ; j >= 0 ; j -- )
              {
                DefaultMutableTreeNode currentRowChild = ( DefaultMutableTreeNode ) nodeRow
                    .getChildAt ( j ) ;
                OutlineNode currentOutlineNode = ( OutlineNode ) currentRowChild
                    .getUserObject ( ) ;
                if ( currentOutlineNode.getExpression ( ) == identifier
                    .boundedExpression ( ) )
                {
                  /*
                   * Highlight the first identifier
                   */
                  currentOutlineNode.setBoundedStart ( identifier
                      .boundedStart ( ) ) ;
                  currentOutlineNode.setBoundedEnd ( identifier.boundedEnd ( ) ) ;
                  currentOutlineNode.updateCaption ( ) ;
                  this.outlineUI.getTreeModel ( )
                      .nodeChanged ( currentRowChild ) ;
                  /*
                   * Highlight the identifier in the first child
                   */
                  DefaultMutableTreeNode nodeId = ( DefaultMutableTreeNode ) currentRowChild
                      .getChildAt ( identifier.boundedIdentifierIndex ( ) ) ;
                  OutlineNode idOutlineNode = ( OutlineNode ) nodeId
                      .getUserObject ( ) ;
                  idOutlineNode.enableBindingColor ( ) ;
                  this.outlineUI.getTreeModel ( ).nodeChanged ( nodeId ) ;
                  break ;
                }
              }
            }
            else
            {
              if ( list.get ( i ).getExpression ( ) == identifier
                  .boundedExpression ( ) )
              {
                DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) ( ( DefaultMutableTreeNode ) pTreePath
                    .getPath ( ) [ i ] ).getChildAt ( identifier
                    .boundedIdentifierIndex ( ) ) ;
                ( ( OutlineNode ) node.getUserObject ( ) )
                    .enableBindingColor ( ) ;
                this.outlineUI.getTreeModel ( ).nodeChanged ( node ) ;
              }
              PrettyAnnotation prettyAnnotation = list
                  .get ( i )
                  .getExpression ( )
                  .toPrettyString ( )
                  .getAnnotationForPrintable ( identifier.boundedExpression ( ) ) ;
              list.get ( i ).setBoundedStart (
                  prettyAnnotation.getStartOffset ( )
                      + identifier.boundedStart ( ) ) ;
              list.get ( i ).setBoundedEnd (
                  prettyAnnotation.getStartOffset ( )
                      + identifier.boundedEnd ( ) ) ;
            }
          }
          catch ( IllegalArgumentException e )
          {
            // Do nothing
          }
        }
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
      update ( pTreeSelectionEvent.getPath ( ) ) ;
    }
  }
}
