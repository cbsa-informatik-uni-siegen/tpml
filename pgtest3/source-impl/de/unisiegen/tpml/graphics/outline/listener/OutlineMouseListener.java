package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.Container ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.JLabel ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.StyledLanguageDocument ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.components.CompoundExpression ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;
import de.unisiegen.tpml.ui.editor.TextEditorPanel ;


/**
 * This class listens for <code>MouseEvents</code>. It handles
 * <code>MouseEvents</code> on the components {@link Outline},
 * {@link SmallStepView}, {@link BigStepView} and {@link TypeCheckerView}.
 * Sets a new {@link Expression} in the {@link Outline}. It views the
 * <code>JPopupMenu</code> in the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineMouseListener implements MouseListener
{
  /**
   * The unused <code>String</code> for the <code>SuppressWarnings</code>.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * The {@link CompoundExpression}.
   */
  private CompoundExpression < ? , ? > compoundExpression ;


  /**
   * The {@link TextEditorPanel}.
   */
  private TextEditorPanel textEditorPanel ;


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
    this.textEditorPanel = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link CompoundExpression}. This constructer is used, if the
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
    this.textEditorPanel = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link StyledLanguageDocument}.
   * 
   * @param pTextEditorPanel The {@link StyledLanguageDocument}.
   */
  public OutlineMouseListener ( TextEditorPanel pTextEditorPanel )
  {
    this.outlineUI = null ;
    this.compoundExpression = null ;
    this.view = null ;
    this.textEditorPanel = pTextEditorPanel ;
  }


  /**
   * Returns true, if all children of the given <code>TreePath</code> are
   * visible, otherwise false.
   * 
   * @param pTreePath The <code>TreePath</code> to check for.
   * @return True, if all children of the given <code>TreePath</code> are
   *         visible, otherwise false.
   */
  private final boolean allChildrenVisible ( TreePath pTreePath )
  {
    OutlineNode lastNode = ( OutlineNode ) pTreePath.getLastPathComponent ( ) ;
    if ( lastNode.getChildCount ( ) == 0 )
    {
      return true ;
    }
    boolean childVisible = true ;
    final int count = lastNode.getChildCount ( ) ;
    for ( int i = 0 ; i < count ; i ++ )
    {
      if ( ! this.outlineUI.getJTreeOutline ( ).isVisible (
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
  private final void handleMouseEvent ( MouseEvent pMouseEvent )
  {
    // Outline
    if ( ( this.outlineUI != null )
        && ( pMouseEvent.getSource ( ).equals ( this.outlineUI
            .getJTreeOutline ( ) ) ) )
    {
      if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON3 )
      {
        int x = pMouseEvent.getX ( ) ;
        int y = pMouseEvent.getY ( ) ;
        TreePath treePath = this.outlineUI.getJTreeOutline ( )
            .getPathForLocation ( x , y ) ;
        if ( treePath == null )
        {
          return ;
        }
        this.outlineUI.getJTreeOutline ( ).setSelectionPath ( treePath ) ;
        setStatus ( ) ;
        this.outlineUI.getJPopupMenu ( ).show ( pMouseEvent.getComponent ( ) ,
            x , y ) ;
      }
    }
    /*
     * Editor
     */
    else if ( ( this.textEditorPanel != null )
        && ( pMouseEvent.getSource ( ).equals ( this.textEditorPanel
            .getEditor ( ) ) ) )
    {
      if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON1 )
      {
        Expression expression = null ;
        try
        {
          expression = this.textEditorPanel.getDocument ( ).getExpression ( ) ;
        }
        catch ( Exception e )
        {
          // Do nothing
        }
        this.textEditorPanel.getOutline ( ).loadExpression ( expression ,
            Outline.Execute.MOUSE_CLICK_EDITOR ) ;
      }
    }
    /*
     * SmallStepView, BigStepView and TypeCheckerView.
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
              this.compoundExpression.getExpression ( ) ,
              Outline.Execute.MOUSE_CLICK_SMALLSTEP ) ;
        }
        // BigStepView
        else if ( this.view instanceof BigStepView )
        {
          ( ( BigStepView ) this.view ).getOutline ( ).loadExpression (
              this.compoundExpression.getExpression ( ) ,
              Outline.Execute.MOUSE_CLICK_BIGSTEP ) ;
        }
        // TypeCheckerView
        else if ( this.view instanceof TypeCheckerView )
        {
          ( ( TypeCheckerView ) this.view ).getOutline ( ).loadExpression (
              this.compoundExpression.getExpression ( ) ,
              Outline.Execute.MOUSE_CLICK_TYPECHECKER ) ;
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
  public final void mouseClicked ( MouseEvent pMouseEvent )
  {
    handleMouseEvent ( pMouseEvent ) ;
  }


  /**
   * Mouse entered the component, which listens on <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mouseEntered(MouseEvent)
   */
  public final void mouseEntered ( @ SuppressWarnings ( UNUSED )
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
  public final void mouseExited ( @ SuppressWarnings ( UNUSED )
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
  public final void mousePressed ( @ SuppressWarnings ( UNUSED )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Mouse is released on the component, which listens on
   * <code>MouseEvents</code>.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   * @see MouseListener#mouseReleased(MouseEvent)
   */
  public final void mouseReleased ( @ SuppressWarnings ( UNUSED )
  MouseEvent pMouseEvent )
  {
    // Do Nothing
  }


  /**
   * Sets the status of the <code>JMenuItems</code> in the
   * <code>JPopupMenu</code> in the {@link Outline}.
   */
  private final void setStatus ( )
  {
    TreePath treePath = this.outlineUI.getJTreeOutline ( ).getSelectionPath ( ) ;
    if ( treePath == null )
    {
      // No node is selected.
      return ;
    }
    OutlineNode selectedNode = ( OutlineNode ) treePath.getLastPathComponent ( ) ;
    this.outlineUI.getJMenuItemExpand ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemCollapse ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemClose ( ).setEnabled ( true ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).setEnabled ( true ) ;
    boolean allVisible = allChildrenVisible ( this.outlineUI.getJTreeOutline ( )
        .getPathForRow ( 0 ) ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).setEnabled ( ! allVisible ) ;
    // Selected node is not a leaf
    if ( selectedNode.getChildCount ( ) > 0 )
    {
      boolean allChildrenVisible = allChildrenVisible ( treePath ) ;
      boolean selectedChildVisible = this.outlineUI.getJTreeOutline ( )
          .isVisible (
              treePath.pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      boolean rootChildVisible = this.outlineUI.getJTreeOutline ( ).isVisible (
          this.outlineUI.getJTreeOutline ( ).getPathForRow ( 0 )
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
      OutlineNode root = ( OutlineNode ) this.outlineUI.getTreeModel ( )
          .getRoot ( ) ;
      this.outlineUI.getJMenuItemCloseAll ( ).setEnabled ( ! root.isLeaf ( ) ) ;
      this.outlineUI.getJMenuItemCollapseAll ( )
          .setEnabled ( ! root.isLeaf ( ) ) ;
    }
  }
}
