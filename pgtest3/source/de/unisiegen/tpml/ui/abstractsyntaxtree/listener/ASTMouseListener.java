package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.Container ;
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
 * This class listens for mouse events. It handles mouse events on the
 * components AbstractSyntaxTree, SmallStepView, BigStepView and
 * TypeCheckerView. Sets a new Expression in the AbstractSyntaxTree. Views the
 * JPopupMenu in the AbstractSyntaxTree.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTMouseListener implements MouseListener
{
  /**
   * The AbstractSyntaxTree UI.
   */
  private ASTUI aSTUI ;


  /**
   * The CompoundExpression.
   */
  private CompoundExpression < ? , ? > compoundExpression ;


  /**
   * The view, one of SmallStepView, BigStepView or TypeCheckerView.
   */
  private Container view ;


  /**
   * Initializes the ASTMouseListener with the given ASTUI. This constructer is
   * used, if the ASTMouseListener listens for mouse events on the AST.
   * 
   * @param pASTUI The ASTUI.
   */
  public ASTMouseListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
    this.compoundExpression = null ;
    this.view = null ;
  }


  /**
   * Initializes the ASTMouseListener with the given ASTUI. This constructer is
   * used, if the ASTMouseListener listens for mouse events on the
   * SmallStepView, BigStepView or TypeCheckerView.
   * 
   * @param pCompoundExpression The CompoundExpression.
   */
  public ASTMouseListener ( CompoundExpression < ? , ? > pCompoundExpression )
  {
    this.aSTUI = null ;
    this.compoundExpression = pCompoundExpression ;
    this.view = null ;
  }


  /**
   * Returns true, if all children of the given TreePath are visible, otherwise
   * false.
   * 
   * @param pTreePath The TreePath to check for.
   * @return True, if all children of the given TreePath are visible, otherwise
   *         false.
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
   * Handles mouse events on the components AbstractSyntaxTree, SmallStepView,
   * BigStepView and TypeCheckerView. Sets a new Expression in the
   * AbstractSyntaxTree. Views the JPopupMenu in the AbstractSyntaxTree.
   * 
   * @param pMouseEvent The mouse event.
   */
  private void handleMouseEvent ( MouseEvent pMouseEvent )
  {
    // AbstractSyntaxTree
    if ( ( this.aSTUI != null )
        && ( pMouseEvent.getSource ( ).equals ( this.aSTUI
            .getJTreeAbstractSyntaxTree ( ) ) ) )
    {
      if ( pMouseEvent.isPopupTrigger ( ) )
      {
        int x = pMouseEvent.getX ( ) ;
        int y = pMouseEvent.getY ( ) ;
        TreePath treePath = this.aSTUI.getJTreeAbstractSyntaxTree ( )
            .getPathForLocation ( x , y ) ;
        if ( treePath == null )
        {
          return ;
        }
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).setSelectionPath ( treePath ) ;
        setStatus ( ) ;
        this.aSTUI.getJPopupMenu ( ).show ( pMouseEvent.getComponent ( ) , x ,
            y ) ;
      }
    }
    /*
     * MouseEvent on one of the view SmallStepView, BigStepView and
     * TypeCheckerView.
     */
    else if ( ( pMouseEvent.getSource ( ) instanceof CompoundExpression )
        || ( pMouseEvent.getSource ( ) instanceof JLabel ) )
    {
      if ( this.view == null )
      {
        this.view = this.compoundExpression.getParent ( ).getParent ( )
            .getParent ( ).getParent ( ).getParent ( ).getParent ( ) ;
      }
      // SmallStepView
      if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON1 )
      {
        if ( this.view instanceof SmallStepView )
        {
          ( ( SmallStepView ) this.view ).getAbstractSyntaxTree ( )
              .loadExpression ( this.compoundExpression.getExpression ( ) ,
                  "mouse_smallstep" ) ; //$NON-NLS-1$
        }
        // BigStepView
        else if ( this.view instanceof BigStepView )
        {
          ( ( BigStepView ) this.view ).getAbstractSyntaxTree ( )
              .loadExpression ( this.compoundExpression.getExpression ( ) ,
                  "mouse_bigstep" ) ; //$NON-NLS-1$
        }
        // TypeCheckerView
        else if ( this.view instanceof TypeCheckerView )
        {
          ( ( TypeCheckerView ) this.view ).getAbstractSyntaxTree ( )
              .loadExpression ( this.compoundExpression.getExpression ( ) ,
                  "mouse_typechecker" ) ; //$NON-NLS-1$
        }
      }
    }
  }


  /**
   * Mouse is clicked on the component, which listens on mouse events.
   * 
   * @param pMouseEvent The mouse event.
   * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
   */
  public void mouseClicked ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse entered the component, which listens on mouse events.
   * 
   * @param pMouseEvent The mouse event.
   * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
   */
  public void mouseEntered ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse exited the component, which listens on mouse events.
   * 
   * @param pMouseEvent The mouse event.
   * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
   */
  public void mouseExited ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse is pressed on the component, which listens on mouse events.
   * 
   * @param pMouseEvent The mouse event.
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
  public void mousePressed ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * Mouse is released on the component, which listens on mouse events.
   * 
   * @param pMouseEvent The mouse event.
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
  public void mouseReleased ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * Sets the status of the menu items in the popup menu in the
   * AbstractSyntaxTree.
   */
  private void setStatus ( )
  {
    TreePath treePath = this.aSTUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionPath ( ) ;
    if ( treePath == null )
    {
      // No node is selected.
      return ;
    }
    DefaultMutableTreeNode selectedNode = ( DefaultMutableTreeNode ) treePath
        .getLastPathComponent ( ) ;
    this.aSTUI.getJMenuItemExpand ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemExpandAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemClose ( ).setEnabled ( true ) ;
    this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( true ) ;
    boolean allVisible = allChildrenVisible ( this.aSTUI
        .getJTreeAbstractSyntaxTree ( ).getPathForRow ( 0 ) ) ;
    this.aSTUI.getJMenuItemExpandAll ( ).setEnabled ( ! allVisible ) ;
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
      this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( rootChildVisible ) ;
      this.aSTUI.getJMenuItemClose ( ).setEnabled ( selectedChildVisible ) ;
      this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( rootChildVisible ) ;
    }
    // Selected node is a leaf
    else
    {
      this.aSTUI.getJMenuItemExpand ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemCollapse ( ).setEnabled ( false ) ;
      this.aSTUI.getJMenuItemClose ( ).setEnabled ( false ) ;
      // If the root is the only node, disable items
      DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) this.aSTUI
          .getTreeModel ( ).getRoot ( ) ;
      this.aSTUI.getJMenuItemCloseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
      this.aSTUI.getJMenuItemCollapseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
    }
  }
}
