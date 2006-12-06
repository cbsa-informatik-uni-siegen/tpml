package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTClipboard ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * This class listens for action events. It implements the methods for closing,
 * collapsing and expanding the current node and all nodes.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTActionListener implements ActionListener
{
  /**
   * The AbstractSyntaxTree UI.
   */
  private ASTUI aSTUI ;


  /**
   * The aSTClipboard.
   */
  private ASTClipboard aSTClipboard ;


  /**
   * Initializes the ASTActionListener.
   * 
   * @param pASTUI The AbstractSyntaxTree UI.
   */
  public ASTActionListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
    this.aSTClipboard = new ASTClipboard ( ) ;
  }


  /**
   * This method is invoked if a menu item is pushed.
   * 
   * @param pActionEvent The action event
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
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
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemSelection ( ).isSelected ( ) , null ,
          "selection" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "binding" ) ) //$NON-NLS-1$
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemBinding ( ).isSelected ( ) , null , "binding" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "unbound" ) ) //$NON-NLS-1$
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemUnbound ( ).isSelected ( ) , null , "unbound" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "replace" ) ) //$NON-NLS-1$
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemReplace ( ).isSelected ( ) , null , "replace" ) ; //$NON-NLS-1$
    }
    else if ( actionCommand.equals ( "autoUpdate" ) ) //$NON-NLS-1$
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemAutoUpdate ( ).isSelected ( ) , null ,
          "autoupdate" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Closes the selected node.
   */
  public void close ( )
  {
    int selectionRows[] = this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( selectionRows == null )
    {
      return ;
    }
    for ( int i = this.aSTUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= selectionRows [ 0 ] ; i -- )
    {
      this.aSTUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Closes all nodes.
   */
  public void closeAll ( )
  {
    for ( int i = this.aSTUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.aSTUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Collapses the selected node.
   */
  public void collapse ( )
  {
    int selectionRows[] = this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( selectionRows == null )
    {
      return ;
    }
    this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .collapseRow ( selectionRows [ 0 ] ) ;
  }


  /**
   * Collapses all nodes.
   */
  public void collapseAll ( )
  {
    this.aSTUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( 0 ) ;
  }


  /**
   * Copies the selection into the clipboard.
   */
  public void copy ( )
  {
    DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) this.aSTUI
        .getJTreeAbstractSyntaxTree ( ).getSelectionPath ( )
        .getLastPathComponent ( ) ;
    if ( node != null )
    {
      this.aSTClipboard.copy ( ( ( ASTNode ) node.getUserObject ( ) )
          .getExpressionString ( ) ) ;
    }
  }


  /**
   * Expands the selected node.
   */
  public void expand ( )
  {
    expandTreePath ( this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionPath ( ) ) ;
  }


  /**
   * Expands all nodes.
   */
  public void expandAll ( )
  {
    int i = 0 ;
    while ( i < this.aSTUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) )
    {
      this.aSTUI.getJTreeAbstractSyntaxTree ( ).expandRow ( i ) ;
      i ++ ;
    }
  }


  /**
   * This method expands the given TreePath.
   * 
   * @param pTreePath The TreePath, which should be expand.
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
    this.aSTUI.getJTreeAbstractSyntaxTree ( ).expandPath ( pTreePath ) ;
  }
}
