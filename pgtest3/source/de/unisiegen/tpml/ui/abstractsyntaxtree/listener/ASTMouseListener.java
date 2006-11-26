package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.JLabel ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.components.CompoundExpression ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTMouseListener implements MouseListener
{
  /**
   * TODO
   */
  private ASTUI aSTUI ;


  /**
   * TODO
   */
  private CompoundExpression < ? , ? > compoundExpression ;


  /**
   * TODO
   * 
   * @param pASTUI
   */
  public ASTMouseListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
    this.compoundExpression = null ;
  }


  /**
   * TODO
   * 
   * @param pCompoundExpression
   */
  public ASTMouseListener ( CompoundExpression < ? , ? > pCompoundExpression )
  {
    this.aSTUI = null ;
    this.compoundExpression = pCompoundExpression ;
  }


  /**
   * TODO
   * 
   * @param pTreePath
   * @return TODO
   */
  private boolean allChildrenVisible ( TreePath pTreePath )
  {
    DefaultMutableTreeNode lastNode = ( DefaultMutableTreeNode ) pTreePath
        .getLastPathComponent ( ) ;
    if ( lastNode.getChildCount ( ) == 0 )
    {
      return true ;
    }
    boolean childVisible = true ;
    final int count = lastNode.getChildCount ( ) ;
    for ( int i = 0 ; i < count ; i ++ )
    {
      if ( ! this.aSTUI.getJTreeAbstractSyntaxTree ( ).isVisible (
          pTreePath.pathByAddingChild ( lastNode.getChildAt ( i ) ) ) )
      {
        return false ;
      }
      childVisible = childVisible
          && allChildrenVisible ( pTreePath.pathByAddingChild ( lastNode
              .getChildAt ( i ) ) ) ;
    }
    return childVisible ;
  }


  /**
   * TODO
   * 
   * @param pMouseEvent
   */
  private void handleMouseEvent ( MouseEvent pMouseEvent )
  {
    if ( ( this.aSTUI != null )
        && ( pMouseEvent.getSource ( ).equals ( this.aSTUI
            .getJTreeAbstractSyntaxTree ( ) ) ) )
    {
      int x = pMouseEvent.getX ( ) ;
      int y = pMouseEvent.getY ( ) ;
      TreePath treePath = this.aSTUI.getJTreeAbstractSyntaxTree ( )
          .getPathForLocation ( x , y ) ;
      if ( treePath == null )
      {
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).setSelectionPath ( null ) ;
        this.aSTUI.getASTTreeSelectionListener ( ).reset (
            ( DefaultMutableTreeNode ) this.aSTUI.getTreeModel ( ).getRoot ( ) ) ;
        return ;
      }
      if ( pMouseEvent.isPopupTrigger ( ) )
      {
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).setSelectionPath ( treePath ) ;
        this.aSTUI.getJPopupMenu ( ).show ( pMouseEvent.getComponent ( ) , x ,
            y ) ;
      }
      setStatus ( ) ;
    }
    else if ( ( pMouseEvent.getSource ( ) instanceof CompoundExpression )
        || ( pMouseEvent.getSource ( ) instanceof JLabel ) )
    {
      if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON1 )
      {
        if ( this.compoundExpression.getParent ( ).getParent ( ).getParent ( )
            .getParent ( ).getParent ( ).getParent ( ) instanceof SmallStepView )
        {
          SmallStepView view = ( SmallStepView ) this.compoundExpression
              .getParent ( ).getParent ( ).getParent ( ).getParent ( )
              .getParent ( ).getParent ( ) ;
          view.getAbstractSyntaxTree ( ).setExpression (
              this.compoundExpression.getExpression ( ) , "mouse_smallstep" ) ;
        }
        else if ( this.compoundExpression.getParent ( ).getParent ( )
            .getParent ( ).getParent ( ).getParent ( ).getParent ( ) instanceof BigStepView )
        {
          BigStepView view = ( BigStepView ) this.compoundExpression
              .getParent ( ).getParent ( ).getParent ( ).getParent ( )
              .getParent ( ).getParent ( ) ;
          view.getAbstractSyntaxTree ( ).setExpression (
              this.compoundExpression.getExpression ( ) , "mouse_bigstep" ) ;
        }
        else if ( this.compoundExpression.getParent ( ).getParent ( )
            .getParent ( ).getParent ( ).getParent ( ).getParent ( ) instanceof TypeCheckerView )
        {
          TypeCheckerView view = ( TypeCheckerView ) this.compoundExpression
              .getParent ( ).getParent ( ).getParent ( ).getParent ( )
              .getParent ( ).getParent ( ) ;
          view.getAbstractSyntaxTree ( ).setExpression (
              this.compoundExpression.getExpression ( ) , "mouse_typechecker" ) ;
        }
      }
    }
  }


  /**
   * TODO
   * 
   * @param pMouseEvent
   * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
   */
  public void mouseClicked ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * TODO
   * 
   * @param pMouseEvent
   * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
   */
  public void mouseEntered ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * TODO
   * 
   * @param pMouseEvent
   * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
   */
  public void mouseExited ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * TODO
   * 
   * @param pMouseEvent
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
  public void mousePressed ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * TODO
   * 
   * @param pMouseEvent
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
  public void mouseReleased ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * TODO
   */
  public void setStatus ( )
  {
    TreePath treePath = this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionPath ( ) ;
    if ( treePath == null )
    {
      return ;
    }
    DefaultMutableTreeNode selectedNode = ( DefaultMutableTreeNode ) treePath
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
    boolean allVisible = allChildrenVisible ( this.aSTUI
        .getJTreeAbstractSyntaxTree ( ).getPathForRow ( 0 ) ) ;
    this.aSTUI.getJMenuItemExpandAll ( ).setEnabled ( ! allVisible ) ;
    this.aSTUI.getJButtonExpandAll ( ).setEnabled ( ! allVisible ) ;
    // Selected node is not a leaf
    if ( selectedNode.getChildCount ( ) > 0 )
    {
      boolean allChildrenVisible = allChildrenVisible ( treePath ) ;
      boolean selectedChildVisible = this.aSTUI.getJTreeAbstractSyntaxTree ( )
          .isVisible (
              treePath.pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      boolean rootChildVisible = this.aSTUI.getJTreeAbstractSyntaxTree ( )
          .isVisible (
              this.aSTUI.getJTreeAbstractSyntaxTree ( ).getPathForRow ( 0 )
                  .pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      this.aSTUI.getJMenuItemExpand ( ).setEnabled ( ! allChildrenVisible ) ;
      this.aSTUI.getJButtonExpand ( ).setEnabled ( ! allChildrenVisible ) ;
      this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJButtonCollapse ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJButtonCollapseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJMenuItemClose ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJButtonClose ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJButtonCloseAll ( ).setEnabled ( rootChildVisible ) ;
    }
    // Selected node is a leaf
    else
    {
      this.aSTUI.getJMenuItemExpand ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonExpand ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonCollapse ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemClose ( ).setEnabled ( false ) ;
      this.aSTUI.getJButtonClose ( ).setEnabled ( false ) ;
      // If the root is the only node, disable buttons
      DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) this.aSTUI
          .getTreeModel ( ).getRoot ( ) ;
      this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
      this.aSTUI.getJButtonCloseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
      this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
      this.aSTUI.getJButtonCollapseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
    }
  }
}
