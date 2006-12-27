package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.Container ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.JLabel ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.components.CompoundExpression ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;


/**
 * This class listens for <code>MouseEvents</code>. It handles
 * <code>MouseEvents</code> on the components {@link Outline},
 * {@link SmallStepView}, {@link BigStepView} and {@link TypeCheckerView}.
 * Sets a new {@link Expression} in the {@link Outline}. It views the
 * <code>JPopupMenu</code> in the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineMouseListener implements MouseListener
{
  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * The {@link CompoundExpression}.
   */
  private CompoundExpression < ? , ? > compoundExpression ;


  /**
   * The view, one of {@link SmallStepView}, {@link BigStepView} or
   * {@link TypeCheckerView}.
   */
  private Container view ;


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link OutlineUI}. This constructer is used, if the
   * {@link OutlineMouseListener} listens for <code>MouseEvents</code> on the
   * {@link Outline}.
   * 
   * @param pOutlineUI The {@link OutlineUI}.
   */
  public OutlineMouseListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
    this.compoundExpression = null ;
    this.view = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link OutlineUI}. This constructer is used, if the
   * {@link OutlineMouseListener} listens for <code>MouseEvents</code> on the
   * {@link SmallStepView}, {@link BigStepView} or {@link TypeCheckerView}.
   * 
   * @param pCompoundExpression The {@link CompoundExpression}.
   */
  public OutlineMouseListener ( CompoundExpression < ? , ? > pCompoundExpression )
  {
    this.outlineUI = null ;
    this.compoundExpression = pCompoundExpression ;
    this.view = null ;
  }


  /**
   * Returns true, if all children of the given <code>TreePath</code> are
   * visible, otherwise false.
   * 
   * @param pTreePath The <code>TreePath</code> to check for.
   * @return True, if all children of the given <code>TreePath</code> are
   *         visible, otherwise false.
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
   * Handles <code>MouseEvents</code> on the components {@link Outline},
   * {@link SmallStepView}, {@link BigStepView} and {@link TypeCheckerView}.
   * Sets a new {@link Expression} in the {@link Outline}. Views the
   * <code>JPopupMenu</code> in the {@link Outline}.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
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
          ( ( SmallStepView ) this.view ).getOutline ( ).loadExpression (
              this.compoundExpression.getExpression ( ) , "mouse_smallstep" ) ; //$NON-NLS-1$
        }
        // BigStepView
        else if ( this.view instanceof BigStepView )
        {
          ( ( BigStepView ) this.view ).getOutline ( ).loadExpression (
              this.compoundExpression.getExpression ( ) , "mouse_bigstep" ) ; //$NON-NLS-1$
        }
        // TypeCheckerView
        else if ( this.view instanceof TypeCheckerView )
        {
          ( ( TypeCheckerView ) this.view ).getOutline ( ).loadExpression (
              this.compoundExpression.getExpression ( ) , "mouse_typechecker" ) ; //$NON-NLS-1$
        }
      }
    }
  }


  /**
   * Mouse is clicked on the component, which listens on
   * <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mouseClicked(MouseEvent)
   */
  public void mouseClicked ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse entered the component, which listens on <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mouseEntered(MouseEvent)
   */
  public void mouseEntered ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse exited the component, which listens on <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mouseExited(MouseEvent)
   */
  public void mouseExited ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse is pressed on the component, which listens on
   * <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mousePressed(MouseEvent)
   */
  public void mousePressed ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * Mouse is released on the component, which listens on
   * <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mouseReleased(MouseEvent)
   */
  public void mouseReleased ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * Sets the status of the <code>JMenuItems</code> in the
   * <code>JPopupMenu</code> in the {@link Outline}.
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
