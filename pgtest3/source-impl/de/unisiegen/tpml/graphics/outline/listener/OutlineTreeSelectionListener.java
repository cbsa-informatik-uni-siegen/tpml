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
  public OutlineTreeSelectionListener ( final DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * Resets the root node and all its children.
   */
  public final void reset ( )
  {
    final OutlineNode outlineNode = ( OutlineNode ) this.defaultOutline
        .getOutlineUI ( ).getTreeModel ( ).getRoot ( ) ;
    if ( outlineNode == null )
    {
      return ;
    }
    this.reset ( outlineNode ) ;
  }


  /**
   * Resets the given node and all its children.
   * 
   * @param pOutlineNode The node, which should be reseted.
   */
  private final void reset ( final OutlineNode pOutlineNode )
  {
    pOutlineNode.setReplaceInThisNode ( false ) ;
    pOutlineNode.setOutlineBinding ( null ) ;
    pOutlineNode.setBoundedIdentifier ( null ) ;
    pOutlineNode.updateCaption ( ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      this.reset ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pTreePath The selected <code>TreePath</code>.
   */
  public final void update ( final TreePath pTreePath )
  {
    final OutlineNode rootNode = ( OutlineNode ) this.defaultOutline
        .getOutlineUI ( ).getTreeModel ( ).getRoot ( ) ;
    if ( rootNode == null )
    {
      return ;
    }
    this.reset ( rootNode ) ;
    final ArrayList < OutlineNode > list = new ArrayList < OutlineNode > ( ) ;
    final Object [ ] path = pTreePath.getPath ( ) ;
    for ( int i = 0 ; i < pTreePath.getPathCount ( ) ; i ++ )
    {
      list.add ( ( OutlineNode ) path [ i ] ) ;
    }
    final OutlineNode selectedNode = list.get ( list.size ( ) - 1 ) ;
    // Expression
    if ( selectedNode.isExpression ( ) )
    {
      this.updateExpression ( list , pTreePath ) ;
    }
    // Expression
    else if ( selectedNode.isIdentifier ( ) )
    {
      this.updateIdentifier ( list , pTreePath ) ;
    }
    // Type
    else if ( selectedNode.isType ( ) )
    {
      this.updateType ( list , pTreePath ) ;
    }
    this.defaultOutline.updateBreaks ( ) ;
  }


  /**
   * Updates the caption of the selected node and its higher nodes.
   * 
   * @param pList The parent nodes of the selected node.
   * @param pTreePath The selected <code>TreePath</code>.
   */
  private final void updateExpression ( final ArrayList < OutlineNode > pList ,
      final TreePath pTreePath )
  {
    final OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    for ( int i = 0 ; i < pList.size ( ) ; i ++ )
    {
      if ( ( selectedNode.getPrettyPrintable ( ) instanceof Identifier )
          && ( i < pList.size ( ) - 1 )
          && ( ( ( Identifier ) selectedNode.getPrettyPrintable ( ) )
              .getBoundedToExpression ( ) != null ) )
      {
        try
        {
          final Identifier identifier = ( Identifier ) selectedNode
              .getPrettyPrintable ( ) ;
          /*
           * Highlight the bounded Identifiers in the other childs of a parent
           * row.
           */
          if ( ( pList.get ( i ).getPrettyPrintable ( ) instanceof Attribute )
              || ( pList.get ( i ).getPrettyPrintable ( ) instanceof Method )
              || ( pList.get ( i ).getPrettyPrintable ( ) instanceof CurriedMethod ) )
          {
            final OutlineNode nodeRowChild = ( OutlineNode ) pTreePath
                .getPath ( ) [ i ] ;
            final OutlineNode nodeRow = ( OutlineNode ) pTreePath.getPath ( ) [ i - 1 ] ;
            for ( int j = nodeRow.getIndex ( nodeRowChild ) ; j >= 0 ; j -- )
            {
              final OutlineNode currentOutlineNode = ( OutlineNode ) nodeRow
                  .getChildAt ( j ) ;
              if ( currentOutlineNode.getPrettyPrintable ( ) == identifier
                  .getBoundedToExpression ( ) )
              {
                /*
                 * Highlight the Identifier in the Expression
                 */
                currentOutlineNode.setBoundedIdentifier ( identifier
                    .getBoundedToIdentifier ( ) ) ;
                currentOutlineNode.updateCaption ( ) ;
                /*
                 * Highlight the Identifier
                 */
                for ( int k = 0 ; k < currentOutlineNode.getChildCount ( ) ; k ++ )
                {
                  final OutlineNode nodeId = ( OutlineNode ) currentOutlineNode
                      .getChildAt ( k ) ;
                  if ( nodeId.getPrettyPrintable ( ) == identifier
                      .getBoundedToIdentifier ( ) )
                  {
                    nodeId.setBoundedIdentifier ( identifier
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
                final OutlineNode nodeId = ( OutlineNode ) pList.get ( i )
                    .getChildAt ( j ) ;
                if ( nodeId.getPrettyPrintable ( ) == identifier
                    .getBoundedToIdentifier ( ) )
                {
                  nodeId.setBoundedIdentifier ( identifier
                      .getBoundedToIdentifier ( ) ) ;
                  nodeId.updateCaption ( ) ;
                  break ;
                }
              }
            }
            /*
             * Highlight the Identifier in the node.
             */
            pList.get ( i ).setBoundedIdentifier (
                identifier.getBoundedToIdentifier ( ) ) ;
          }
        }
        catch ( final IllegalArgumentException e )
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
      final PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getPrettyPrintable ( ).toPrettyString ( ).getAnnotationForPrintable (
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
  private final void updateIdentifier ( final ArrayList < OutlineNode > pList ,
      final TreePath pTreePath )
  {
    final OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
    final OutlineNode nodeAttribute = pList.get ( pList.size ( ) - 2 ) ;
    /*
     * Highlight the bounded Identifiers of an Attribute in the other childs of
     * the parent row.
     */
    if ( nodeAttribute.getPrettyPrintable ( ) instanceof Attribute )
    {
      final OutlineNode nodeRow = pList.get ( pList.size ( ) - 3 ) ;
      for ( int i = nodeRow.getIndex ( nodeAttribute ) + 1 ; i < nodeRow
          .getChildCount ( ) ; i ++ )
      {
        final OutlineNode currentRowChild = ( OutlineNode ) nodeRow
            .getChildAt ( i ) ;
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
      final PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getPrettyPrintable ( ).toPrettyString ( ).getAnnotationForPrintable (
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
  private final void updateType ( final ArrayList < OutlineNode > pList ,
      final TreePath pTreePath )
  {
    final OutlineNode selectedNode = pList.get ( pList.size ( ) - 1 ) ;
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
      final PrettyAnnotation prettyAnnotation = pList.get ( i )
          .getPrettyPrintable ( ).toPrettyString ( ).getAnnotationForPrintable (
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
  public final void valueChanged ( final TreeSelectionEvent pTreeSelectionEvent )
  {
    if ( pTreeSelectionEvent.getSource ( ).equals (
        this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
            .getSelectionModel ( ) ) )
    {
      final TreePath newTreePath = pTreeSelectionEvent.getPath ( ) ;
      if ( newTreePath == null )
      {
        return ;
      }
      this.update ( newTreePath ) ;
      final TreePath oldTreePath = pTreeSelectionEvent
          .getOldLeadSelectionPath ( ) ;
      if ( oldTreePath != null )
      {
        final Object [ ] objects = oldTreePath.getPath ( ) ;
        for ( Object element : objects )
        {
          this.defaultOutline.getOutlineUI ( ).getTreeModel ( ).nodeChanged (
              ( OutlineNode ) element ) ;
        }
      }
    }
  }
}
