package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTActionListener implements ActionListener
{
  /**
   * TODO
   */
  private ASTUI aSTUI ;


  /**
   * TODO
   * 
   * @param pASTUI
   */
  public ASTActionListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  /**
   * TODO
   * 
   * @param pActionEvent
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed ( ActionEvent pActionEvent )
  {
    String actionCommand = pActionEvent.getActionCommand ( ) ;
    if ( actionCommand.equals ( "close" ) )
    {
      close ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "closeAll" ) )
    {
      closeAll ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "expand" ) )
    {
      expand ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "expandAll" ) )
    {
      expandAll ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "collapse" ) )
    {
      collapse ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "collapseAll" ) )
    {
      collapseAll ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "selection" ) )
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemSelection ( ).isSelected ( ) , null ,
          "selection" ) ;
    }
    else if ( actionCommand.equals ( "binding" ) )
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemBinding ( ).isSelected ( ) , null , "binding" ) ;
    }
    else if ( actionCommand.equals ( "replace" ) )
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemReplace ( ).isSelected ( ) , null , "replace" ) ;
    }
    else if ( actionCommand.equals ( "autoUpdate" ) )
    {
      this.aSTUI.getASTItemListener ( ).update (
          this.aSTUI.getJMenuItemAutoUpdate ( ).isSelected ( ) , null ,
          "autoupdate" ) ;
    }
  }


  /**
   * TODO
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
   * TODO
   */
  public void closeAll ( )
  {
    for ( int i = this.aSTUI.getJTreeAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.aSTUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  /**
   * TODO
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
   * TODO
   */
  public void collapseAll ( )
  {
    this.aSTUI.getJTreeAbstractSyntaxTree ( ).collapseRow ( 0 ) ;
  }


  /**
   * TODO
   */
  public void expand ( )
  {
    expandTreePath ( this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionPath ( ) ) ;
  }


  /**
   * TODO
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
   * TODO
   * 
   * @param pTreePath
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
