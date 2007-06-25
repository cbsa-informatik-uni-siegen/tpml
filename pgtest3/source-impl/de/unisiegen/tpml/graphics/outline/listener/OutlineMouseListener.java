package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.graphics.StyledLanguageEditor ;
import de.unisiegen.tpml.graphics.bigstep.BigStepNodeComponent ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.subtyping.StyledTypeEnterField ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent ;
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
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * The {@link TextEditorPanel}.
   */
  private TextEditorPanel textEditorPanel ;


  /**
   * The {@link StyledLanguageEditor}
   */
  private StyledLanguageEditor styledLanguageEditor ;


  /**
   * The {@link TypeCheckerNodeComponent}.
   */
  private TypeCheckerNodeComponent typeCheckerNodeComponent ;


  /**
   * The {@link BigStepNodeComponent}.
   */
  private BigStepNodeComponent bigStepNodeComponent ;


  /**
   * The {@link SmallStepNodeComponent}.
   */
  private SmallStepNodeComponent smallStepNodeComponent ;


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link BigStepNodeComponent}.
   * 
   * @param pBigStepNodeComponent The {@link BigStepNodeComponent}.
   */
  public OutlineMouseListener ( BigStepNodeComponent pBigStepNodeComponent )
  {
    this.defaultOutline = null ;
    this.textEditorPanel = null ;
    this.typeCheckerNodeComponent = null ;
    this.bigStepNodeComponent = pBigStepNodeComponent ;
    this.smallStepNodeComponent = null ;
    this.styledLanguageEditor = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link DefaultOutline}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineMouseListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
    this.textEditorPanel = null ;
    this.typeCheckerNodeComponent = null ;
    this.bigStepNodeComponent = null ;
    this.smallStepNodeComponent = null ;
    this.styledLanguageEditor = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link SmallStepNodeComponent}.
   * 
   * @param pSmallStepNodeComponent The {@link SmallStepNodeComponent}.
   */
  public OutlineMouseListener ( SmallStepNodeComponent pSmallStepNodeComponent )
  {
    this.defaultOutline = null ;
    this.textEditorPanel = null ;
    this.typeCheckerNodeComponent = null ;
    this.bigStepNodeComponent = null ;
    this.smallStepNodeComponent = pSmallStepNodeComponent ;
    this.styledLanguageEditor = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link StyledLanguageEditor}.
   * 
   * @param pStyledLanguageEditor The {@link StyledLanguageEditor}.
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineMouseListener ( StyledLanguageEditor pStyledLanguageEditor ,
      DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
    this.textEditorPanel = null ;
    this.typeCheckerNodeComponent = null ;
    this.bigStepNodeComponent = null ;
    this.smallStepNodeComponent = null ;
    this.styledLanguageEditor = pStyledLanguageEditor ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link TextEditorPanel}.
   * 
   * @param pTextEditorPanel The {@link TextEditorPanel}.
   */
  public OutlineMouseListener ( TextEditorPanel pTextEditorPanel )
  {
    this.defaultOutline = null ;
    this.textEditorPanel = pTextEditorPanel ;
    this.typeCheckerNodeComponent = null ;
    this.bigStepNodeComponent = null ;
    this.smallStepNodeComponent = null ;
    this.styledLanguageEditor = null ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link TypeCheckerProofNode}.
   * 
   * @param pTypeCheckerNodeComponent The {@link TypeCheckerNodeComponent}.
   */
  public OutlineMouseListener (
      TypeCheckerNodeComponent pTypeCheckerNodeComponent )
  {
    this.defaultOutline = null ;
    this.textEditorPanel = null ;
    this.typeCheckerNodeComponent = pTypeCheckerNodeComponent ;
    this.bigStepNodeComponent = null ;
    this.smallStepNodeComponent = null ;
    this.styledLanguageEditor = null ;
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
      if ( ! this.defaultOutline
          .getOutlineUI ( )
          .getJTreeOutline ( )
          .isVisible ( pTreePath.pathByAddingChild ( lastNode.getChildAt ( i ) ) ) )
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
    if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON1 )
    {
      /*
       * Outline.
       */
      if ( this.defaultOutline != null )
      {
        if ( pMouseEvent.getSource ( ).equals (
            this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ) ) )
        {
          /*
           * Highlight the source code
           */
          if ( ( ( this.defaultOutline.getTextEditorPanel ( ) != null ) || ( this.defaultOutline
              .getSubTypingEnterTypes ( ) != null ) )
              && ( ( pMouseEvent.getClickCount ( ) >= 2 ) || ( this.defaultOutline
                  .getOutlinePreferences ( ).isHighlightSourceCode ( ) ) ) )
          {
            this.defaultOutline.updateHighlighSourceCode ( ) ;
          }
        }
      }
      /*
       * StyledLanguageEditor.
       */
      if ( this.styledLanguageEditor != null )
      {
        if ( pMouseEvent.getSource ( ).equals ( this.styledLanguageEditor ) )
        {
          MonoType type = null ;
          try
          {
            type = ( ( StyledTypeEnterField ) this.styledLanguageEditor
                .getDocument ( ) ).getType ( ) ;
          }
          catch ( Exception e )
          {
            // Do nothing
          }
          this.defaultOutline.loadPrettyPrintable ( type ,
              Outline.Execute.MOUSE_CLICK_EDITOR ) ;
        }
      }
      /*
       * Source code editor.
       */
      if ( this.textEditorPanel != null )
      {
        if ( pMouseEvent.getSource ( ).equals (
            this.textEditorPanel.getEditor ( ) ) )
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
          this.textEditorPanel.getOutline ( ).loadPrettyPrintable ( expression ,
              Outline.Execute.MOUSE_CLICK_EDITOR ) ;
        }
      }
      /*
       * Type checker.
       */
      if ( this.typeCheckerNodeComponent != null )
      {
        /*
         * Index label.
         */
        if ( pMouseEvent.getSource ( ).equals (
            this.typeCheckerNodeComponent.getIndexLabel ( ) ) )
        {
          ( ( TypeCheckerView ) this.typeCheckerNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( ).loadPrettyPrintable (
                  this.typeCheckerNodeComponent.getCompoundExpression ( )
                      .getExpression ( ) ,
                  Outline.Execute.MOUSE_CLICK_TYPECHECKER ) ;
        }
        /*
         * Compound expression.
         */
        else if ( pMouseEvent.getSource ( ).equals (
            this.typeCheckerNodeComponent.getCompoundExpression ( ) ) )
        {
          ( ( TypeCheckerView ) this.typeCheckerNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( ).loadPrettyPrintable (
                  this.typeCheckerNodeComponent.getCompoundExpression ( )
                      .getExpression ( ) ,
                  Outline.Execute.MOUSE_CLICK_TYPECHECKER ) ;
        }
        /*
         * Type label.
         */
        else if ( pMouseEvent.getSource ( ).equals (
            this.typeCheckerNodeComponent.getTypeComponent ( ) ) )
        {
          ( ( TypeCheckerView ) this.typeCheckerNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( )
              .loadPrettyPrintable (
                  this.typeCheckerNodeComponent.getTypeComponent ( ).getType ( ) ,
                  Outline.Execute.MOUSE_CLICK_TYPECHECKER ) ;
        }
      }
      /*
       * Big step.
       */
      if ( this.bigStepNodeComponent != null )
      {
        /*
         * Index label.
         */
        if ( pMouseEvent.getSource ( ).equals (
            this.bigStepNodeComponent.getIndexLabel ( ) ) )
        {
          ( ( BigStepView ) this.bigStepNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( ).loadPrettyPrintable (
                  this.bigStepNodeComponent.getCompoundExpression ( )
                      .getExpression ( ) , Outline.Execute.MOUSE_CLICK_BIGSTEP ) ;
        }
        /*
         * Compound expression.
         */
        else if ( pMouseEvent.getSource ( ).equals (
            this.bigStepNodeComponent.getCompoundExpression ( ) ) )
        {
          ( ( BigStepView ) this.bigStepNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( ).loadPrettyPrintable (
                  this.bigStepNodeComponent.getCompoundExpression ( )
                      .getExpression ( ) , Outline.Execute.MOUSE_CLICK_BIGSTEP ) ;
        }
        /*
         * Result compound expression.
         */
        else if ( pMouseEvent.getSource ( ).equals (
            this.bigStepNodeComponent.getResultCompoundExpression ( ) ) )
        {
          ( ( BigStepView ) this.bigStepNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( ).loadPrettyPrintable (
                  this.bigStepNodeComponent.getResultCompoundExpression ( )
                      .getExpression ( ) , Outline.Execute.MOUSE_CLICK_BIGSTEP ) ;
        }
      }
      /*
       * Small step.
       */
      if ( this.smallStepNodeComponent != null )
      {
        /*
         * Compound expression.
         */
        if ( pMouseEvent.getSource ( ).equals (
            this.smallStepNodeComponent.getCompoundExpression ( ) ) )
        {
          ( ( SmallStepView ) this.smallStepNodeComponent.getParent ( )
              .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
              .getOutline ( ).loadPrettyPrintable (
                  this.smallStepNodeComponent.getCompoundExpression ( )
                      .getExpression ( ) ,
                  Outline.Execute.MOUSE_CLICK_SMALLSTEP ) ;
        }
      }
    }
    else if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON3 )
    {
      /*
       * Outline.
       */
      if ( this.defaultOutline != null )
      {
        /*
         * Popupmenu
         */
        if ( pMouseEvent.getSource ( ).equals (
            this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ) ) )
        {
          int x = pMouseEvent.getX ( ) ;
          int y = pMouseEvent.getY ( ) ;
          TreePath treePath = this.defaultOutline.getOutlineUI ( )
              .getJTreeOutline ( ).getPathForLocation ( x , y ) ;
          if ( treePath == null )
          {
            return ;
          }
          this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
              .setSelectionPath ( treePath ) ;
          setStatus ( ) ;
          this.defaultOutline.getOutlineUI ( ).getJPopupMenu ( ).show (
              pMouseEvent.getComponent ( ) , x , y ) ;
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
    TreePath treePath = this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getSelectionPath ( ) ;
    if ( treePath == null )
    {
      // No node is selected.
      return ;
    }
    OutlineNode selectedNode = ( OutlineNode ) treePath.getLastPathComponent ( ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemExpand ( ).setEnabled (
        true ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemExpandAll ( ).setEnabled (
        true ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemCollapse ( ).setEnabled (
        true ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemCollapseAll ( )
        .setEnabled ( true ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemClose ( ).setEnabled (
        true ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemCloseAll ( ).setEnabled (
        true ) ;
    boolean allVisible = allChildrenVisible ( this.defaultOutline
        .getOutlineUI ( ).getJTreeOutline ( ).getPathForRow ( 0 ) ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemExpandAll ( ).setEnabled (
        ! allVisible ) ;
    // Selected node is not a leaf
    if ( selectedNode.getChildCount ( ) > 0 )
    {
      boolean allChildrenVisible = allChildrenVisible ( treePath ) ;
      boolean selectedChildVisible = this.defaultOutline.getOutlineUI ( )
          .getJTreeOutline ( ).isVisible (
              treePath.pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      boolean rootChildVisible = this.defaultOutline.getOutlineUI ( )
          .getJTreeOutline ( ).isVisible (
              this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
                  .getPathForRow ( 0 ).pathByAddingChild (
                      selectedNode.getChildAt ( 0 ) ) ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemExpand ( ).setEnabled (
          ! allChildrenVisible ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemCollapse ( ).setEnabled (
          selectedChildVisible ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemCollapseAll ( )
          .setEnabled ( rootChildVisible ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemClose ( ).setEnabled (
          selectedChildVisible ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemCloseAll ( ).setEnabled (
          rootChildVisible ) ;
    }
    // Selected node is a leaf
    else
    {
      this.defaultOutline.getOutlineUI ( ).getJMenuItemExpand ( ).setEnabled (
          false ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemCollapse ( ).setEnabled (
          false ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemClose ( ).setEnabled (
          false ) ;
      // If the root is the only node, disable items
      OutlineNode root = ( OutlineNode ) this.defaultOutline.getOutlineUI ( )
          .getTreeModel ( ).getRoot ( ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemCloseAll ( ).setEnabled (
          ! root.isLeaf ( ) ) ;
      this.defaultOutline.getOutlineUI ( ).getJMenuItemCollapseAll ( )
          .setEnabled ( ! root.isLeaf ( ) ) ;
    }
  }
}
