package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.Container ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.JLabel ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.components.CompoundExpression ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;


/**
 * This class listens for mouse events. It handles mouse events on the
 * components Outline, SmallStepView, BigStepView and TypeCheckerView. Sets a
 * new Expression in the Outline. Views the JPopupMenu in the Outline.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineMouseListener implements MouseListener
{
  /**
   * The Outline UI.
   */
  private OutlineUI outlineUI ;


  /**
   * The CompoundExpression.
   */
  private CompoundExpression < ? , ? > compoundExpression ;


  /**
   * The view, one of SmallStepView, BigStepView or TypeCheckerView.
   */
  private Container view ;


  /**
   * Initializes the OutlineMouseListener with the given OutlineUI. This
   * constructer is used, if the OutlineMouseListener listens for mouse events
   * on the Outline.
   * 
   * @param pOutlineUI The Outline UI.
   */
  public OutlineMouseListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
    this.compoundExpression = null ;
    this.view = null ;
  }


  /**
   * Initializes the OutlineMouseListener with the given OutlineUI. This
   * constructer is used, if the OutlineMouseListener listens for mouse events
   * on the SmallStepView, BigStepView or TypeCheckerView.
   * 
   * @param pCompoundExpression The CompoundExpression.
   */
  public OutlineMouseListener ( CompoundExpression < ? , ? > pCompoundExpression )
  {
    this.outlineUI = null ;
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
      if ( ! this.outlineUI.getJTreeAbstractSyntaxTree ( ).isVisible (
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
   * Handles mouse events on the components AbstractOutline, SmallStepView,
   * BigStepView and TypeCheckerView. Sets a new Expression in the
   * AbstractOutline. Views the JPopupMenu in the Outline.
   * 
   * @param pMouseEvent The mouse event.
   */
  private void handleMouseEvent ( MouseEvent pMouseEvent )
  {
    // AbstractOutline
    if ( ( this.outlineUI != null )
        && ( pMouseEvent.getSource ( ).equals ( this.outlineUI
            .getJTreeAbstractSyntaxTree ( ) ) ) )
    {
      if ( pMouseEvent.isPopupTrigger ( ) )
      {
        int x = pMouseEvent.getX ( ) ;
        int y = pMouseEvent.getY ( ) ;
        TreePath treePath = this.outlineUI.getJTreeAbstractSyntaxTree ( )
            .getPathForLocation ( x , y ) ;
        if ( treePath == null )
        {
          return ;
        }
        this.outlineUI.getJTreeAbstractSyntaxTree ( ).setSelectionPath (
            treePath ) ;
        setStatus ( ) ;
        this.outlineUI.getJPopupMenu ( ).show ( pMouseEvent.getComponent ( ) ,
            x , y ) ;
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
          ( ( SmallStepView ) this.view ).getOutline ( )
              .loadExpression ( this.compoundExpression.getExpression ( ) ,
                  "mouse_smallstep" ) ; //$NON-NLS-1$
        }
        // BigStepView
        else if ( this.view instanceof BigStepView )
        {
          ( ( BigStepView ) this.view ).getOutline ( )
              .loadExpression ( this.compoundExpression.getExpression ( ) ,
                  "mouse_bigstep" ) ; //$NON-NLS-1$
        }
        // TypeCheckerView
        else if ( this.view instanceof TypeCheckerView )
        {
          ( ( TypeCheckerView ) this.view ).getOutline ( )
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
   * Sets the status of the menu items in the popup menu in the Outline.
   */
  private void setStatus ( )
  {
    TreePath treePath = this.outlineUI.getJTreeAbstractSyntaxTree ( )
        .getSelectionPath ( ) ;
    if ( treePath == null )
    {
      // No node is selected.
      return ;
    }
    DefaultMutableTreeNode selectedNode = ( DefaultMutableTreeNode ) treePath
        .getLastPathComponent ( ) ;
    this.outlineUI.getJMenuItemExpand ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemCollapse ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemClose ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).setEnabled ( true ) ;
    boolean allVisible = allChildrenVisible ( this.outlineUI
        .getJTreeAbstractSyntaxTree ( ).getPathForRow ( 0 ) ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).setEnabled ( ! allVisible ) ;
    // Selected node is not a leaf
    if ( selectedNode.getChildCount ( ) > 0 )
    {
      boolean allChildrenVisible = allChildrenVisible ( treePath ) ;
      boolean selectedChildVisible = this.outlineUI
          .getJTreeAbstractSyntaxTree ( ).isVisible (
              treePath.pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      boolean rootChildVisible = this.outlineUI.getJTreeAbstractSyntaxTree ( )
          .isVisible (
              this.outlineUI.getJTreeAbstractSyntaxTree ( ).getPathForRow ( 0 )
                  .pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      this.outlineUI.getJMenuItemExpand ( ).setEnabled ( ! allChildrenVisible ) ;
      this.outlineUI.getJMenuItemCollapse ( )
          .setEnabled ( selectedChildVisible ) ;
      this.outlineUI.getJMenuItemCollapseAll ( ).setEnabled ( rootChildVisible ) ;
      this.outlineUI.getJMenuItemClose ( ).setEnabled ( selectedChildVisible ) ;
      this.outlineUI.getJMenuItemCloseAll ( ).setEnabled ( rootChildVisible ) ;
    }
    // Selected node is a leaf
    else
    {
      this.outlineUI.getJMenuItemExpand ( ).setEnabled ( false ) ;
      this.outlineUI.getJMenuItemCollapse ( ).setEnabled ( false ) ;
      this.outlineUI.getJMenuItemClose ( ).setEnabled ( false ) ;
      // If the root is the only node, disable items
      DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) this.outlineUI
          .getTreeModel ( ).getRoot ( ) ;
      this.outlineUI.getJMenuItemCloseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
      this.outlineUI.getJMenuItemCollapseAll ( )
          .setEnabled ( ! root.isLeaf ( ) ) ;
    }
  }
}
