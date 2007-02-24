package de.unisiegen.tpml.graphics.outline.listener ;


import java.util.ArrayList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Method ;
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
   */
  private final void repaint ( )
  {
    DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) this.outlineUI
        .getTreeModel ( ).getRoot ( ) ;
    repaint ( rootNode ) ;
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
      if ( node.isExpression ( ) )
      {
        node.setOutlineBinding ( pOutlineBinding ) ;
        node.updateCaption ( ) ;
        this.outlineUI.getTreeModel ( ).nodeChanged ( child ) ;
      }
      setBindingToChildren ( child , pOutlineBinding ) ;
    }
  }


  /**
   * Sets the given {@link OutlineBinding} to all nodes.
   * 
   * @param pOutlineBinding The {@link OutlineBinding}.
   */
  @ SuppressWarnings ( "unused" )
  private final void setOutlineBinding ( OutlineBinding pOutlineBinding )
  {
    DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) this.outlineUI
        .getTreeModel ( ).getRoot ( ) ;
    setOutlineBinding ( pOutlineBinding , rootNode ) ;
  }


  /**
   * Sets the given {@link OutlineBinding} to the given node.
   * 
   * @param pOutlineBinding The {@link OutlineBinding}.
   * @param pNode The node.
   */
  private final void setOutlineBinding ( OutlineBinding pOutlineBinding ,
      DefaultMutableTreeNode pNode )
  {
    OutlineNode outlineNode = ( OutlineNode ) pNode.getUserObject ( ) ;
    outlineNode.setOutlineBinding ( pOutlineBinding ) ;
    outlineNode.resetCaption ( ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      setOutlineBinding ( pOutlineBinding , ( DefaultMutableTreeNode ) pNode
          .getChildAt ( i ) ) ;
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
      repaint ( ) ;
      return ;
    }
    ArrayList < OutlineNode > list = new ArrayList < OutlineNode > ( ) ;
    Object [ ] path = pTreePath.getPath ( ) ;
    for ( int i = 0 ; i < pTreePath.getPathCount ( ) ; i ++ )
    {
      list.add ( ( OutlineNode ) ( ( DefaultMutableTreeNode ) path [ i ] )
          .getUserObject ( ) ) ;
    }
    OutlineNode selectedNode = list.get ( list.size ( ) - 1 ) ;
    // Type
    if ( ( selectedNode.getExpression ( ) == null )
        && ( list.get ( list.size ( ) - 2 ).getExpression ( ) == null ) )
    {
      updateType ( list , pTreePath ) ;
    }
    // Identifier
    else if ( selectedNode.getExpression ( ) == null )
    {
      updateIdentifier ( list , pTreePath ) ;
    }
    // Expression
    else
    {
      updateExpression ( list , pTreePath ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void updateExpression ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
      if ( ( selectedNode.getExpression ( ) instanceof Identifier )
          && ( i < pList.size ( ) - 1 )
          && ( ( ( Identifier ) selectedNode.getExpression ( ) )
              .boundedExpression ( ) != null ) )
      {
        try
        {
          Identifier identifier = ( Identifier ) selectedNode.getExpression ( ) ;
          /*
           * Highlight the bounded Identifiers in the other childs of a parent
           * row.
           */
          if ( ( pList.get ( i ).getExpression ( ) instanceof Attribute )
              || ( pList.get ( i ).getExpression ( ) instanceof Method )
              || ( pList.get ( i ).getExpression ( ) instanceof CurriedMethod ) )
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
                currentOutlineNode
                    .setBoundedStart ( identifier.boundedStart ( ) ) ;
                currentOutlineNode.setBoundedEnd ( identifier.boundedEnd ( ) ) ;
                currentOutlineNode.updateCaption ( ) ;
                /*
                 * Highlight the Identifier in the first child
                 */
                DefaultMutableTreeNode nodeId = ( DefaultMutableTreeNode ) currentRowChild
                    .getChildAt ( identifier.boundedIdentifierIndex ( ) ) ;
                OutlineNode idOutlineNode = ( OutlineNode ) nodeId
                    .getUserObject ( ) ;
                idOutlineNode.enableBindingColor ( ) ;
                break ;
              }
            }
          }
          else
          {
            /*
             * Highlight the Identifier in the child node with the bounded
             * Identifier index.
             */
            if ( pList.get ( i ).getExpression ( ) == identifier
                .boundedExpression ( ) )
            {
              DefaultMutableTreeNode nodeIdentifier = ( DefaultMutableTreeNode ) ( ( DefaultMutableTreeNode ) pTreePath
                  .getPath ( ) [ i ] ).getChildAt ( identifier
                  .boundedIdentifierIndex ( ) ) ;
              OutlineNode outlineNodeIdentifier = ( OutlineNode ) nodeIdentifier
                  .getUserObject ( ) ;
              outlineNodeIdentifier.enableBindingColor ( ) ;
              // Highlight the other bounded Identifiers.
              // pList.get ( i ).setOutlineBinding (
              // outlineNodeId.getOutlineBinding ( ) ) ;
            }
            /*
             * Highlight the Identifier in the node.
             */
            PrettyAnnotation prettyAnnotation = pList.get ( i )
                .getExpression ( ).toPrettyString ( )
                .getAnnotationForPrintable ( identifier.boundedExpression ( ) ) ;
            pList.get ( i ).setBoundedStart (
                prettyAnnotation.getStartOffset ( )
                    + identifier.boundedStart ( ) ) ;
            pList.get ( i )
                .setBoundedEnd (
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
      if ( i < pList.size ( ) - 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( true ) ;
      }
      /*
       * If only the root is selected, there should not be replaced
       */
      if ( pList.size ( ) == 1 )
      {
        pList.get ( i ).setReplaceInThisNode ( false ) ;
      }
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getExpression ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getExpression ( ) ) ;
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
          prettyAnnotation.getEndOffset ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void updateIdentifier ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    OutlineNode secondLast = pList.get ( pList.size ( ) - 2 ) ;
    /*
     * Highlight the selected Identifier
     */
    selectedNode.enableSelectionColor ( ) ;
    /*
     * Highlight the bounded Identifiers of an Attribute in the other childs of
     * the parent row.
     */
    if ( secondLast.getExpression ( ) instanceof Attribute )
    {
      DefaultMutableTreeNode nodeRow = ( DefaultMutableTreeNode ) pTreePath
          .getPath ( ) [ pTreePath.getPathCount ( ) - 3 ] ;
      DefaultMutableTreeNode nodeAttribute = ( DefaultMutableTreeNode ) pTreePath
          .getPath ( ) [ pTreePath.getPathCount ( ) - 2 ] ;
      for ( int i = nodeRow.getIndex ( nodeAttribute ) + 1 ; i < nodeRow
          .getChildCount ( ) ; i ++ )
      {
        DefaultMutableTreeNode currentRowChild = ( DefaultMutableTreeNode ) nodeRow
            .getChildAt ( i ) ;
        setBindingToChildren ( currentRowChild , selectedNode
            .getOutlineBinding ( ) ) ;
        OutlineNode outlineNodeRowChild = ( OutlineNode ) currentRowChild
            .getUserObject ( ) ;
        outlineNodeRowChild.setOutlineBinding ( selectedNode
            .getOutlineBinding ( ) ) ;
        outlineNodeRowChild.updateCaption ( ) ;
      }
    }
    for ( int i = 0 ; i < pList.size ( ) - 1 ; i ++ )
    {
      /*
       * Sets the new binding in higher nodes
       */
      pList.get ( i ).setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
      /*
       * It should be replaced in higher nodes
       */
      pList.get ( i ).setReplaceInThisNode ( true ) ;
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getExpression ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              secondLast.getExpression ( ) ) ;
      pList.get ( i ).updateCaption (
          prettyAnnotation.getStartOffset ( ) + selectedNode.getStartIndex ( ) ,
          prettyAnnotation.getStartOffset ( ) + selectedNode.getEndIndex ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
    // Highlight the bounded Identifiers in the hole tree
    // setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
    // repaint();
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void updateType ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    OutlineNode lastButTwo = pList.get ( pList.size ( ) - 3 ) ;
    /*
     * Highlight the selected Type
     */
    selectedNode.enableSelectionColor ( ) ;
    for ( int i = 0 ; i < pList.size ( ) - 2 ; i ++ )
    {
      /*
       * Sets the new binding in higher nodes
       */
      pList.get ( i ).setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
      /*
       * It should be replaced in higher nodes
       */
      pList.get ( i ).setReplaceInThisNode ( true ) ;
      /*
       * Update the caption of the node
       */
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getExpression ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              lastButTwo.getExpression ( ) ) ;
      pList.get ( i ).updateCaption (
          prettyAnnotation.getStartOffset ( ) + selectedNode.getStartIndex ( ) ,
          prettyAnnotation.getStartOffset ( ) + selectedNode.getEndIndex ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.outlineUI.getTreeModel ( ).nodeChanged (
          ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }


  /**
   * This method is invoked if a node value has changed.
   * 
   * @param pTreeSelectionEvent The <code>TreeSelectionEvent</code>.
   * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
   */
  public final void valueChanged ( TreeSelectionEvent pTreeSelectionEvent )
  {
    if ( pTreeSelectionEvent.getSource ( ).equals (
        this.outlineUI.getJTreeOutline ( ).getSelectionModel ( ) ) )
    {
      update ( pTreeSelectionEvent.getPath ( ) ) ;
      TreePath treePath = pTreeSelectionEvent.getOldLeadSelectionPath ( ) ;
      if ( treePath != null )
      {
        Object [ ] objects = treePath.getPath ( ) ;
        for ( int i = 0 ; i < objects.length ; i ++ )
        {
          this.outlineUI.getTreeModel ( ).nodeChanged (
              ( DefaultMutableTreeNode ) objects [ i ] ) ;
        }
      }
    }
  }
}
