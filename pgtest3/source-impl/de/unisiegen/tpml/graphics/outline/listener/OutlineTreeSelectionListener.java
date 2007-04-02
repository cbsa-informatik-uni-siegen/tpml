package de.unisiegen.tpml.graphics.outline.listener ;


import java.util.ArrayList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;


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
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * Initializes the {@link OutlineTreeSelectionListener} with the given
   * {@link DefaultOutline}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineTreeSelectionListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * Repaints the given node and all its children.
   */
  private final void repaint ( )
  {
    OutlineNode rootNode = ( OutlineNode ) this.defaultOutline.getOutlineUI ( )
        .getTreeModel ( ).getRoot ( ) ;
    repaint ( rootNode ) ;
  }


  /**
   * Repaints the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void repaint ( OutlineNode pOutlineNode )
  {
    this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
        pOutlineNode ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Resets the root node and all its children.
   */
  public final void reset ( )
  {
    OutlineNode outlineNode = ( OutlineNode ) this.defaultOutline
        .getOutlineUI ( ).getTreeModel ( ).getRoot ( ) ;
    if ( outlineNode == null )
    {
      return ;
    }
    reset ( outlineNode ) ;
  }


  /**
   * Resets the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be reseted.
   */
  private final void reset ( OutlineNode pOutlineNode )
  {
    pOutlineNode.setReplaceInThisNode ( false ) ;
    pOutlineNode.setOutlineBinding ( null ) ;
    pOutlineNode.setBoundedStart ( OutlineNode.NO_BINDING ) ;
    pOutlineNode.setBoundedEnd ( OutlineNode.NO_BINDING ) ;
    pOutlineNode.resetCaption ( ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Sets the {@link OutlineBinding} to all children of the node.
   * 
   * @param pOutlineNode The parent node.
   * @param pOutlineBinding The {@link OutlineBinding}.
   */
  private final void setBindingToChildren ( OutlineNode pOutlineNode ,
      OutlineBinding pOutlineBinding )
  {
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      OutlineNode child = ( OutlineNode ) pOutlineNode.getChildAt ( i ) ;
      if ( child.isExpression ( ) )
      {
        child.setOutlineBinding ( pOutlineBinding ) ;
        child.updateCaption ( ) ;
        this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
            child ) ;
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
    OutlineNode rootNode = ( OutlineNode ) this.defaultOutline.getOutlineUI ( )
        .getTreeModel ( ).getRoot ( ) ;
    setOutlineBinding ( pOutlineBinding , rootNode ) ;
  }


  /**
   * Sets the given {@link OutlineBinding} to the given node.
   * 
   * @param pOutlineBinding The {@link OutlineBinding}.
   * @param pOutlineNode The node.
   */
  private final void setOutlineBinding ( OutlineBinding pOutlineBinding ,
      OutlineNode pOutlineNode )
  {
    pOutlineNode.setOutlineBinding ( pOutlineBinding ) ;
    pOutlineNode.resetCaption ( ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      setOutlineBinding ( pOutlineBinding , ( OutlineNode ) pOutlineNode
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
    OutlineNode rootNode = ( OutlineNode ) this.defaultOutline.getOutlineUI ( )
        .getTreeModel ( ).getRoot ( ) ;
    if ( rootNode == null )
    {
      return ;
    }
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
      list.add ( ( OutlineNode ) path [ i ] ) ;
    }
    OutlineNode selectedNode = list.get ( list.size ( ) - 1 ) ;
    // Type
    if ( selectedNode.getType ( ) != null )
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
    this.defaultOutline.updateBreaks ( ) ;
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateExpression ( ArrayList < OutlineNode > pList ,
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
            OutlineNode nodeRowChild = ( OutlineNode ) pTreePath.getPath ( ) [ i ] ;
            OutlineNode nodeRow = ( OutlineNode ) pTreePath.getPath ( ) [ i - 1 ] ;
            for ( int j = nodeRow.getIndex ( nodeRowChild ) ; j >= 0 ; j -- )
            {
              OutlineNode currentOutlineNode = ( OutlineNode ) nodeRow
                  .getChildAt ( j ) ;
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
                OutlineNode nodeId = ( OutlineNode ) currentOutlineNode
                    .getChildAt ( identifier.boundedIdentifierIndex ( ) ) ;
                nodeId.enableBindingColor ( ) ;
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
              OutlineNode nodeIdentifier = ( OutlineNode ) ( ( OutlineNode ) pTreePath
                  .getPath ( ) [ i ] ).getChildAt ( identifier
                  .boundedIdentifierIndex ( ) ) ;
              nodeIdentifier.enableBindingColor ( ) ;
              // Highlight the other bounded Identifiers. Disabled because of
              // performance.
              // pList.get ( i ).setOutlineBinding (
              // nodeIdentifier.getOutlineBinding ( ) ) ;
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
      this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateIdentifier ( ArrayList < OutlineNode > pList ,
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
      OutlineNode nodeRow = ( OutlineNode ) pTreePath.getPath ( ) [ pTreePath
          .getPathCount ( ) - 3 ] ;
      OutlineNode nodeAttribute = ( OutlineNode ) pTreePath.getPath ( ) [ pTreePath
          .getPathCount ( ) - 2 ] ;
      for ( int i = nodeRow.getIndex ( nodeAttribute ) + 1 ; i < nodeRow
          .getChildCount ( ) ; i ++ )
      {
        OutlineNode currentRowChild = ( OutlineNode ) nodeRow.getChildAt ( i ) ;
        setBindingToChildren ( currentRowChild , selectedNode
            .getOutlineBinding ( ) ) ;
        currentRowChild.setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
        currentRowChild.updateCaption ( ) ;
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
      this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
    }
    // Highlight the bounded Identifiers in the hole tree. Disabled because of
    // performance.
    // setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
    // repaint();
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateType ( ArrayList < OutlineNode > pList ,
      TreePath pTreePath )
  {
    OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
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
      PrettyAnnotation prettyAnnotation ;
      if ( pList.get ( i ).getExpression ( ) != null )
      {
        prettyAnnotation = pList.get ( i ).getExpression ( ).toPrettyString ( )
            .getAnnotationForPrintable ( selectedNode.getType ( ) ) ;
      }
      else if ( pList.get ( i ).getType ( ) != null )
      {
        prettyAnnotation = pList.get ( i ).getType ( ).toPrettyString ( )
            .getAnnotationForPrintable ( selectedNode.getType ( ) ) ;
      }
      else
      {
        // Identifier - Type
        continue ;
      }
      pList.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
          prettyAnnotation.getEndOffset ( ) ) ;
      /*
       * Node has changed and can be repainted
       */
      this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
          ( ( OutlineNode ) pTreePath.getPath ( ) [ i ] ) ) ;
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
        this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
            .getSelectionModel ( ) ) )
    {
      TreePath newTreePath = pTreeSelectionEvent.getPath ( ) ;
      if ( newTreePath == null )
      {
        return ;
      }
      update ( newTreePath ) ;
      TreePath oldTreePath = pTreeSelectionEvent.getOldLeadSelectionPath ( ) ;
      if ( oldTreePath != null )
      {
        Object [ ] objects = oldTreePath.getPath ( ) ;
        for ( int i = 0 ; i < objects.length ; i ++ )
        {
          this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
              ( OutlineNode ) objects [ i ] ) ;
        }
      }
    }
  }
}
