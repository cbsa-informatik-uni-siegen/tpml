package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlineClipboard ;


/**
 * This class listens for <code>ActionEvents</code>. It implements the
 * methods for closing, collapsing and expanding the current node and all nodes.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineActionListener implements ActionListener
{
  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the <code>OutlineActionListener</code>.
   * 
   * @param pOutlineUI The <code>OutlineUI</code>.
   */
  public OutlineActionListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
  }


  /**
   * This method is invoked if a <code>JMenuItem</code> is pushed.
   * 
   * @param pActionEvent The <code>ActionEvent</code>
   * @see ActionListener#actionPerformed(ActionEvent)
   */
  public final void actionPerformed ( ActionEvent pActionEvent )
  {
    String actionCommand = pActionEvent.getActionCommand ( ) ;
    if ( OutlineUI.CLOSE.equals ( actionCommand ) )
    {
      close ( ) ;
    }
    else if ( OutlineUI.CLOSEALL.equals ( actionCommand ) )
    {
      closeAll ( ) ;
    }
    else if ( OutlineUI.EXPAND.equals ( actionCommand ) )
    {
      expand ( ) ;
    }
    else if ( OutlineUI.EXPANDALL.equals ( actionCommand ) )
    {
      expandAll ( ) ;
    }
    else if ( OutlineUI.COLLAPSE.equals ( actionCommand ) )
    {
      collapse ( ) ;
    }
    else if ( OutlineUI.COLLAPSEALL.equals ( actionCommand ) )
    {
      collapseAll ( ) ;
    }
    else if ( OutlineUI.COPY.equals ( actionCommand ) )
    {
      copy ( ) ;
    }
    else if ( OutlineUI.SELECTION.equals ( actionCommand ) )
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemSelection ( ).isSelected ( ) , null ,
          OutlineUI.SELECTION ) ;
    }
    else if ( OutlineUI.BINDING.equals ( actionCommand ) )
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemBinding ( ).isSelected ( ) , null ,
          OutlineUI.BINDING ) ;
    }
    else if ( OutlineUI.UNBOUND.equals ( actionCommand ) )
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemUnbound ( ).isSelected ( ) , null ,
          OutlineUI.UNBOUND ) ;
    }
    else if ( OutlineUI.REPLACE.equals ( actionCommand ) )
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemReplace ( ).isSelected ( ) , null ,
          OutlineUI.REPLACE ) ;
    }
    else if ( OutlineUI.AUTOUPDATE.equals ( actionCommand ) )
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemAutoUpdate ( ).isSelected ( ) , null ,
          OutlineUI.AUTOUPDATE ) ;
    }
  }


  /**
   * Closes the selected node.
   */
  public final void close ( )
  {
    if ( this.outlineUI.getJTreeOutline ( ).getSelectionRows ( ) == null )
    {
      return ;
    }
    for ( int i = this.outlineUI.getJTreeOutline ( ).getRowCount ( ) - 1 ; i >= this.outlineUI
        .getJTreeOutline ( ).getSelectionRows ( ) [ 0 ] ; i -- )
    {
      this.outlineUI.getJTreeOutline ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Closes all nodes.
   */
  public final void closeAll ( )
  {
    for ( int i = this.outlineUI.getJTreeOutline ( ).getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.outlineUI.getJTreeOutline ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Collapses the selected node.
   */
  public final void collapse ( )
  {
    if ( this.outlineUI.getJTreeOutline ( ).getSelectionRows ( ) == null )
    {
      return ;
    }
    this.outlineUI.getJTreeOutline ( ).collapseRow (
        this.outlineUI.getJTreeOutline ( ).getSelectionRows ( ) [ 0 ] ) ;
  }


  /**
   * Collapses all nodes.
   */
  public final void collapseAll ( )
  {
    this.outlineUI.getJTreeOutline ( ).collapseRow ( 0 ) ;
  }


  /**
   * Copies the selection into the {@link OutlineClipboard}.
   */
  public final void copy ( )
  {
    DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) this.outlineUI
        .getJTreeOutline ( ).getSelectionPath ( ).getLastPathComponent ( ) ;
    if ( node != null )
    {
      OutlineClipboard.getInstance ( ).copy (
          ( ( OutlineNode ) node.getUserObject ( ) ).getExpressionString ( ) ) ;
    }
  }


  /**
   * Expands the selected node.
   */
  public final void expand ( )
  {
    expandTreePath ( this.outlineUI.getJTreeOutline ( ).getSelectionPath ( ) ) ;
  }


  /**
   * Expands all nodes.
   */
  public final void expandAll ( )
  {
    int i = 0 ;
    while ( i < this.outlineUI.getJTreeOutline ( ).getRowCount ( ) )
    {
      this.outlineUI.getJTreeOutline ( ).expandRow ( i ) ;
      i ++ ;
    }
  }


  /**
   * This method expands the given <code>TreePath</code>.
   * 
   * @param pTreePath The <code>TreePath</code>, which should be expand.
   */
  private final void expandTreePath ( TreePath pTreePath )
  {
    if ( pTreePath == null )
    {
      return ;
    }
    DefaultMutableTreeNode lastNode = ( DefaultMutableTreeNode ) pTreePath
        .getLastPathComponent ( ) ;
    for ( int i = 0 ; i < lastNode.getChildCount ( ) ; i ++ )
    {
      expandTreePath ( pTreePath.pathByAddingChild ( lastNode.getChildAt ( i ) ) ) ;
    }
    this.outlineUI.getJTreeOutline ( ).expandPath ( pTreePath ) ;
  }
}
