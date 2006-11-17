package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;


public class ASTMouseListener implements MouseListener
{
  private ASTUI aSTUI ;


  public ASTMouseListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  public void mouseClicked ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  public void mouseEntered ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  public void mouseExited ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  public void mousePressed ( MouseEvent pMouseEvent )
  {
    if ( pMouseEvent.isPopupTrigger ( ) )
    {
      int x = pMouseEvent.getX ( ) ;
      int y = pMouseEvent.getY ( ) ;
      TreePath treePath = this.aSTUI.getJAbstractSyntaxTree ( )
          .getPathForLocation ( x , y ) ;
      if ( treePath == null )
      {
        return ;
      }
      this.aSTUI.getJAbstractSyntaxTree ( ).setSelectionPath ( treePath ) ;
      this.aSTUI.getJPopupMenu ( ).show ( pMouseEvent.getComponent ( ) , x , y ) ;
    }
    setStatus ( ) ;
  }


  public void setStatus ( )
  {
    TreePath treePath = this.aSTUI.getJAbstractSyntaxTree ( )
        .getSelectionPath ( ) ;
    if ( treePath == null )
    {
      return ;
    }
    DefaultMutableTreeNode d = ( DefaultMutableTreeNode ) treePath
        .getLastPathComponent ( ) ;
    this.aSTUI.getJMenuItemExpand ( ).setEnabled ( true ) ;
    this.aSTUI.getJButtonExpand ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemExpandAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJButtonExpandAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( true ) ;
    this.aSTUI.getJButtonCollapse ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJButtonCollapseAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemClose ( ).setEnabled ( true ) ;
    this.aSTUI.getJButtonClose ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJButtonCloseAll ( ).setEnabled ( true ) ;
    if ( d.getChildCount ( ) > 0 )
    {
      boolean selectedChildVisible = this.aSTUI.getJAbstractSyntaxTree ( )
          .isVisible (
              this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( )
                  .pathByAddingChild ( d.getChildAt ( 0 ) ) ) ;
      boolean rootChildVisible = this.aSTUI.getJAbstractSyntaxTree ( )
          .isVisible (
              this.aSTUI.getJAbstractSyntaxTree ( ).getPathForRow ( 0 )
                  .pathByAddingChild ( d.getChildAt ( 0 ) ) ) ;
      this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJButtonCollapse ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJButtonCollapseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJMenuItemClose ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJButtonClose ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJButtonCloseAll ( ).setEnabled ( rootChildVisible ) ;
    }
    else
    {
      this.aSTUI.getJMenuItemExpand ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonExpand ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemExpandAll ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonExpandAll ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonCollapse ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemClose ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonClose ( ).setEnabled ( false ) ;
    }
  }


  public void mouseReleased ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }
}
