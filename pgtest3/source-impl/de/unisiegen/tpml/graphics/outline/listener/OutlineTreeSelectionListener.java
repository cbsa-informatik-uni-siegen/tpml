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
    pOutlineNode.setBindingIdentifier ( null ) ;
    pOutlineNode.updateCaption ( ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
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
    // Expression
    if ( selectedNode.isExpression ( ) )
    {
      updateExpression ( list , pTreePath ) ;
    }
    // Expression
    else if ( selectedNode.isIdentifier ( ) )
    {
      updateIdentifier ( list , pTreePath ) ;
    }
    // Type
    else if ( selectedNode.isType ( ) )
    {
      updateType ( list , pTreePath ) ;
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
      if ( ( selectedNode.getPrettyPrintable ( ) instanceof Identifier )
          && ( i < pList.size ( ) - 1 )
          && ( ( ( Identifier ) selectedNode.getPrettyPrintable ( ) )
              .getBoundedToExpression ( ) != null ) )
      {
        try
        {
          Identifier identifier = ( Identifier ) selectedNode
              .getPrettyPrintable ( ) ;
          /*
           * Highlight the bounded Identifiers in the other childs of a parent
           * row.
           */
          if ( ( pList.get ( i ).getPrettyPrintable ( ) instanceof Attribute )
              || ( pList.get ( i ).getPrettyPrintable ( ) instanceof Method )
              || ( pList.get ( i ).getPrettyPrintable ( ) instanceof CurriedMethod ) )
          {
            OutlineNode nodeRowChild = ( OutlineNode ) pTreePath.getPath ( ) [ i ] ;
            OutlineNode nodeRow = ( OutlineNode ) pTreePath.getPath ( ) [ i - 1 ] ;
            for ( int j = nodeRow.getIndex ( nodeRowChild ) ; j >= 0 ; j -- )
            {
              OutlineNode currentOutlineNode = ( OutlineNode ) nodeRow
                  .getChildAt ( j ) ;
              if ( currentOutlineNode.getPrettyPrintable ( ) == identifier
                  .getBoundedToExpression ( ) )
              {
                /*
                 * Highlight the first identifier
                 */
                currentOutlineNode.setBindingIdentifier ( identifier
                    .getBoundedToIdentifier ( ) ) ;
                currentOutlineNode.updateCaption ( ) ;
                /*
                 * Highlight the Identifier in the first child
                 */
                for ( int k = 0 ; k < currentOutlineNode.getChildCount ( ) ; k ++ )
                {
                  OutlineNode nodeId = ( OutlineNode ) currentOutlineNode
                      .getChildAt ( k ) ;
                  if ( nodeId.getPrettyPrintable ( ) == identifier
                      .getBoundedToIdentifier ( ) )
                  {
                    nodeId.setBindingIdentifier ( identifier
                        .getBoundedToIdentifier ( ) ) ;
                    nodeId.updateCaption ( ) ;
                    break ;
                  }
                }
              }
            }
          }
          else
          {
            /*
             * Highlight the Identifier in the child node with the bounded
             * Identifier index.
             */
            if ( pList.get ( i ).getPrettyPrintable ( ) == identifier
                .getBoundedToExpression ( ) )
            {
              for ( int j = 0 ; j < pList.get ( i ).getChildCount ( ) ; j ++ )
              {
                OutlineNode nodeId = ( OutlineNode ) pList.get ( i )
                    .getChildAt ( j ) ;
                if ( nodeId.getPrettyPrintable ( ) == identifier
                    .getBoundedToIdentifier ( ) )
                {
                  nodeId.setBindingIdentifier ( identifier
                      .getBoundedToIdentifier ( ) ) ;
                  nodeId.updateCaption ( ) ;
                  break ;
                }
              }
            }
            /*
             * Highlight the Identifier in the node.
             */
            pList.get ( i ).setBindingIdentifier (
                identifier.getBoundedToIdentifier ( ) ) ;
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
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
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
    OutlineNode nodeAttribute = pList.get ( pList.size ( ) - 2 ) ;
    /*
     * Highlight the bounded Identifiers of an Attribute in the other childs of
     * the parent row.
     */
    if ( nodeAttribute.getPrettyPrintable ( ) instanceof Attribute )
    {
      OutlineNode nodeRow = pList.get ( pList.size ( ) - 3 ) ;
      for ( int i = nodeRow.getIndex ( nodeAttribute ) + 1 ; i < nodeRow
          .getChildCount ( ) ; i ++ )
      {
        OutlineNode currentRowChild = ( OutlineNode ) nodeRow.getChildAt ( i ) ;
        currentRowChild.setOutlineBinding ( selectedNode.getOutlineBinding ( ) ) ;
        currentRowChild.updateCaption ( ) ;
      }
    }
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
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
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
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
      if ( pList.get ( i ).isIdentifier ( ) )
      {
        continue ;
      }
      PrettyAnnotation prettyAnnotation = pList.get ( i ).getPrettyPrintable ( )
          .toPrettyString ( ).getAnnotationForPrintable (
              selectedNode.getPrettyPrintable ( ) ) ;
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
