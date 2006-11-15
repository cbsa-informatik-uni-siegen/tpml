package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;


public class ASTActionListener implements ActionListener
{
  private AbstractSyntaxTreeUI abstractSyntaxTreeUI ;


  public ASTActionListener ( AbstractSyntaxTreeUI pAbstractSyntaxTreeUI )
  {
    this.abstractSyntaxTreeUI = pAbstractSyntaxTreeUI ;
  }


  public void actionPerformed ( ActionEvent pActionEvent )
  {
    String actionCommand = pActionEvent.getActionCommand ( ) ;
    if ( actionCommand.equals ( "expand" ) )
    {
      expand ( ) ;
    }
    else if ( actionCommand.equals ( "expand_all" ) )
    {
      expandAll ( ) ;
    }
    else if ( actionCommand.equals ( "collapse_all" ) )
    {
      collapseAll ( ) ;
    }
    else if ( actionCommand.equals ( "collapse" ) )
    {
      collapse ( ) ;
    }
    else if ( actionCommand.equals ( "close_all" ) )
    {
      closeAll ( ) ;
    }
  }


  public void closeAll ( )
  {
    for ( int i = this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
        .getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( ).collapseRow ( i ) ;
    }
  }


  public void expand ( )
  {
    expandTreePath ( this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
        .getSelectionPath ( ) ) ;
  }


  public void expandAll ( )
  {
    int i = 0 ;
    while ( i < this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
        .getRowCount ( ) )
    {
      this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( ).expandRow ( i ) ;
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
    this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
        .expandPath ( pTreePath ) ;
  }


  public void collapse ( )
  {
    int row[] = this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
        .getSelectionRows ( ) ;
    if ( row == null )
    {
      return ;
    }
    this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
        .collapseRow ( row [ 0 ] ) ;
  }


  public void collapseAll ( )
  {
    this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( ).collapseRow ( 0 ) ;
  }
}
