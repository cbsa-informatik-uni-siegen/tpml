package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;


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
    }
    else if ( actionCommand.equals ( "close_all" ) )
    {
      closeAll ( ) ;
    }
    else if ( actionCommand.equals ( "expand" ) )
    {
      expand ( ) ;
    }
    else if ( actionCommand.equals ( "expand_all" ) )
    {
      expandAll ( ) ;
    }
    else if ( actionCommand.equals ( "collapse" ) )
    {
      collapse ( ) ;
    }
    else if ( actionCommand.equals ( "collapse_all" ) )
    {
      collapseAll ( ) ;
    }
  }


  public void close ( )
  {
    int row[] = this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionRows ( ) ;
    if ( row == null )
    {
      return ;
    }
    for ( int i = this.aSTUI.getJAbstractSyntaxTree ( ).getRowCount ( ) - 1 ; i >= row [ 0 ] ; i -- )
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
    int row[] = this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionRows ( ) ;
    if ( row == null )
    {
      return ;
    }
    this.aSTUI.getJAbstractSyntaxTree ( ).collapseRow ( row [ 0 ] ) ;
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
    DefaultMutableTreeNode b = ( DefaultMutableTreeNode ) pTreePath
        .getLastPathComponent ( ) ;
    for ( int i = 0 ; i < b.getChildCount ( ) ; i ++ )
    {
      expandTreePath ( pTreePath.pathByAddingChild ( b.getChildAt ( i ) ) ) ;
    }
    this.aSTUI.getJAbstractSyntaxTree ( ).expandPath ( pTreePath ) ;
  }
}
