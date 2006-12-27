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
 * @version $Rev$
 */
public class OutlineActionListener implements ActionListener
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
  public void actionPerformed ( ActionEvent pActionEvent )
  {
    String actionCommand = pActionEvent.getActionCommand ( ) ;
    if ( actionCommand.equals ( "close" ) ) //$NON-NLS-1$
    {
      close ( ) ;
    }
    else if ( actionCommand.equals ( "closeAll" ) ) //$NON-NLS-1$
    {
      closeAll ( ) ;
    }
    else if ( actionCommand.equals ( "expand" ) ) //$NON-NLS-1$
    {
      expand ( ) ;
    }
    else if ( actionCommand.equals ( "expandAll" ) ) //$NON-NLS-1$
    {
      expandAll ( ) ;
    }
    else if ( actionCommand.equals ( "collapse" ) ) //$NON-NLS-1$
    {
      collapse ( ) ;
    }
    else if ( actionCommand.equals ( "collapseAll" ) ) //$NON-NLS-1$
    {
      collapseAll ( ) ;
    }
    else if ( actionCommand.equals ( "copy" ) ) //$NON-NLS-1$
    {
      copy ( ) ;
    }
    else if ( actionCommand.equals ( "selection" ) ) //$NON-NLS-1$
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemSelection ( ).isSelected ( ) , null ,
          "selection" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "binding" ) ) //$NON-NLS-1$
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemBinding ( ).isSelected ( ) , null ,
          "binding" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "unbound" ) ) //$NON-NLS-1$
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemUnbound ( ).isSelected ( ) , null ,
          "unbound" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "replace" ) ) //$NON-NLS-1$
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemReplace ( ).isSelected ( ) , null ,
          "replace" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "autoUpdate" ) ) //$NON-NLS-1$
    {
      this.outlineUI.getOutlineItemListener ( ).update (
          this.outlineUI.getJMenuItemAutoUpdate ( ).isSelected ( ) , null ,
          "autoupdate" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Closes the selected node.
   */
  public void close ( )
  {
    int selectionRows[] = this.outlineUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( selectionRows == null )
    {
      return ;
    }
    for ( int i = this.outlineUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= selectionRows [ 0 ] ; i -- )
    {
      this.outlineUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Closes all nodes.
   */
  public void closeAll ( )
  {
    for ( int i = this.outlineUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.outlineUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Collapses the selected node.
   */
  public void collapse ( )
  {
    int selectionRows[] = this.outlineUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( selectionRows == null )
    {
      return ;
    }
    this.outlineUI.getJTreeAbstractSyntaxTree ( ).collapseRow (
        selectionRows [ 0 ] ) ;
  }


  /**
   * Collapses all nodes.
   */
  public void collapseAll ( )
  {
    this.outlineUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( 0 ) ;
  }


  /**
   * Copies the selection into the {@link OutlineClipboard}.
   */
  public void copy ( )
  {
    DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) this.outlineUI
        .getJTreeAbstractSyntaxTree ( ).getSelectionPath ( )
        .getLastPathComponent ( ) ;
    if ( node != null )
    {
      OutlineClipboard.getInstance ( ).copy (
          ( ( OutlineNode ) node.getUserObject ( ) ).getExpressionString ( ) ) ;
    }
  }


  /**
   * Expands the selected node.
   */
  public void expand ( )
  {
    expandTreePath ( this.outlineUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionPath ( ) ) ;
  }


  /**
   * Expands all nodes.
   */
  public void expandAll ( )
  {
    int i = 0 ;
    while ( i < this.outlineUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) )
    {
      this.outlineUI.getJTreeAbstractSyntaxTree ( ).expandRow ( i ) ;
      i ++ ;
    }
  }


  /**
   * This method expands the given <code>TreePath</code>.
   * 
   * @param pTreePath The <code>TreePath</code>, which should be expand.
   */
  private void expandTreePath ( TreePath pTreePath )
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
    this.outlineUI.getJTreeAbstractSyntaxTree ( ).expandPath ( pTreePath ) ;
  }
}
