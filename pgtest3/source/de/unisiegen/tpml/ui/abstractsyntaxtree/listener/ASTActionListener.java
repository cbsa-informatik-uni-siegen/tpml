package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


public class ASTActionListener implements ActionListener
{
  private ASTUI aSTUI ;


  public ASTActionListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  public void actionPerformed ( ActionEvent pActionEvent )
  {
    String actionCommand = pActionEvent.getActionCommand ( ) ;
    if ( actionCommand.equals ( "close" ) )
    {
      close ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "close_all" ) )
    {
      closeAll ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "expand" ) )
    {
      expand ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "expand_all" ) )
    {
      expandAll ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "collapse" ) )
    {
      collapse ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
    else if ( actionCommand.equals ( "collapse_all" ) )
    {
      collapseAll ( ) ;
      this.aSTUI.getASTMouseListener ( ).setStatus ( ) ;
    }
  }


  public void close ( )
  {
    int selectionRows[] = this.aSTUI.getJAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( selectionRows == null )
    {
      return ;
    }
    for ( int i = this.aSTUI.getJAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= selectionRows [ 0 ] ; i -- )
    {
      this.aSTUI.getJAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  public void closeAll ( )
  {
    for ( int i = this.aSTUI.getJAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.aSTUI.getJAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  public void collapse ( )
  {
    int selectionRows[] = this.aSTUI.getJAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( selectionRows == null )
    {
      return ;
    }
    this.aSTUI.getJAbstractSyntaxTree ( ).collapseRow ( selectionRows [ 0 ] ) ;
  }


  public void collapseAll ( )
  {
    this.aSTUI.getJAbstractSyntaxTree ( ).collapseRow ( 0 ) ;
  }


  public void expand ( )
  {
    expandTreePath ( this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
  }


  public void expandAll ( )
  {
    int i = 0 ;
    while ( i < this.aSTUI.getJAbstractSyntaxTree ( ).getRowCount ( ) )
    {
      this.aSTUI.getJAbstractSyntaxTree ( ).expandRow ( i ) ;
      i ++ ;
    }
  }


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
    this.aSTUI.getJAbstractSyntaxTree ( ).expandPath ( pTreePath ) ;
  }
}
