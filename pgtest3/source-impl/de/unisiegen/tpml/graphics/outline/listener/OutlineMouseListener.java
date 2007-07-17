package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.bigstep.BigStepNodeComponent ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingNodeComponent ;
import de.unisiegen.tpml.graphics.minimaltyping.MinimalTypingView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.subtyping.NewSubTypingNodeComponent ;
import de.unisiegen.tpml.graphics.subtyping.NewSubTypingView ;
import de.unisiegen.tpml.graphics.subtyping.StyledTypeEnterField ;
import de.unisiegen.tpml.graphics.subtyping.SubTypingSourceView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;
import de.unisiegen.tpml.ui.editor.TextEditorPanel ;


/**
 * This class listens for <code>MouseEvents</code>. It handles
 * <code>MouseEvents</code> on the components {@link Outline},
 * {@link SmallStepView}, {@link BigStepView}, {@link TypeCheckerView}
 * {@link TextEditorPanel}, {@link MinimalTypingNodeComponent},
 * {@link NewSubTypingNodeComponent} and {@link SubTypingSourceView}. Sets a
 * new {@link Expression} or {@link Type} in the {@link Outline}. It views the
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
  private DefaultOutline defaultOutline = null ;


  /**
   * The {@link TextEditorPanel}.
   */
  private TextEditorPanel sourceView = null ;


  /**
   * The {@link TypeCheckerNodeComponent}.
   */
  private TypeCheckerNodeComponent typeCheckerNodeComponent = null ;


  /**
   * The {@link MinimalTypingNodeComponent}.
   */
  private MinimalTypingNodeComponent minimalTypingNodeComponent = null ;


  /**
   * The {@link BigStepNodeComponent}.
   */
  private BigStepNodeComponent bigStepNodeComponent = null ;


  /**
   * The {@link NewSubTypingNodeComponent}.
   */
  private NewSubTypingNodeComponent subTypingNodeComponent = null ;


  /**
   * The {@link SubTypingSourceView}.
   */
  private SubTypingSourceView subTypingSourceView = null ;


  /**
   * The {@link SmallStepNodeComponent}.
   */
  private SmallStepNodeComponent smallStepNodeComponent = null ;


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link BigStepNodeComponent}.
   * 
   * @param pBigStepNodeComponent The {@link BigStepNodeComponent}.
   */
  public OutlineMouseListener ( BigStepNodeComponent pBigStepNodeComponent )
  {
    this.bigStepNodeComponent = pBigStepNodeComponent ;
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
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link SubTypingSourceView}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   * @param pSubTypingSourceView The {@link SubTypingSourceView}.
   */
  public OutlineMouseListener ( DefaultOutline pDefaultOutline ,
      SubTypingSourceView pSubTypingSourceView )
  {
    this.defaultOutline = pDefaultOutline ;
    this.subTypingSourceView = pSubTypingSourceView ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link TextEditorPanel}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   * @param pSourceView The {@link TextEditorPanel}.
   */
  public OutlineMouseListener ( DefaultOutline pDefaultOutline ,
      TextEditorPanel pSourceView )
  {
    this.defaultOutline = pDefaultOutline ;
    this.sourceView = pSourceView ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link MinimalTypingNodeComponent}.
   * 
   * @param pMinimalTypingNodeComponent The {@link MinimalTypingNodeComponent}.
   */
  public OutlineMouseListener (
      MinimalTypingNodeComponent pMinimalTypingNodeComponent )
  {
    this.minimalTypingNodeComponent = pMinimalTypingNodeComponent ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link NewSubTypingNodeComponent}.
   * 
   * @param pSubTypingNodeComponent The {@link NewSubTypingNodeComponent}.
   */
  public OutlineMouseListener (
      NewSubTypingNodeComponent pSubTypingNodeComponent )
  {
    this.subTypingNodeComponent = pSubTypingNodeComponent ;
  }


  /**
   * Initializes the {@link OutlineMouseListener} with the given
   * {@link SmallStepNodeComponent}.
   * 
   * @param pSmallStepNodeComponent The {@link SmallStepNodeComponent}.
   */
  public OutlineMouseListener ( SmallStepNodeComponent pSmallStepNodeComponent )
  {
    this.smallStepNodeComponent = pSmallStepNodeComponent ;
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
    this.typeCheckerNodeComponent = pTypeCheckerNodeComponent ;
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
      if ( ! this.defaultOutline.getUI ( ).getJTreeOutline ( ).isVisible (
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
   * Handles <code>MouseEvents</code> on the big step view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleBigStep ( MouseEvent pMouseEvent )
  {
    // IndexLabel
    if ( pMouseEvent.getSource ( ).equals (
        this.bigStepNodeComponent.getIndexLabel ( ) ) )
    {
      ( ( BigStepView ) this.bigStepNodeComponent.getParent ( ).getParent ( )
          .getParent ( ).getParent ( ).getParent ( ) ).getOutline ( )
          .load (
              this.bigStepNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.BIGSTEP ) ;
    }
    // CompoundExpression
    else if ( pMouseEvent.getSource ( ).equals (
        this.bigStepNodeComponent.getCompoundExpression ( ) ) )
    {
      ( ( BigStepView ) this.bigStepNodeComponent.getParent ( ).getParent ( )
          .getParent ( ).getParent ( ).getParent ( ) ).getOutline ( )
          .load (
              this.bigStepNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.BIGSTEP ) ;
    }
    // ResultCompoundExpression
    else if ( pMouseEvent.getSource ( ).equals (
        this.bigStepNodeComponent.getResultCompoundExpression ( ) ) )
    {
      ( ( BigStepView ) this.bigStepNodeComponent.getParent ( ).getParent ( )
          .getParent ( ).getParent ( ).getParent ( ) ).getOutline ( ).load (
          this.bigStepNodeComponent.getResultCompoundExpression ( )
              .getExpression ( ) , Outline.ExecuteMouseClick.BIGSTEP ) ;
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the minimal typing view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleMinimalTyping ( MouseEvent pMouseEvent )
  {
    // IndexLabel
    if ( pMouseEvent.getSource ( ).equals (
        this.minimalTypingNodeComponent.getIndexLabel ( ) ) )
    {
      ( ( MinimalTypingView ) this.minimalTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.minimalTypingNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.MINIMALTYPING ) ;
    }
    // CompoundExpression.
    else if ( pMouseEvent.getSource ( ).equals (
        this.minimalTypingNodeComponent.getCompoundExpression ( ) ) )
    {
      ( ( MinimalTypingView ) this.minimalTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.minimalTypingNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.MINIMALTYPING ) ;
    }
    // TypeComponent
    else if ( pMouseEvent.getSource ( ).equals (
        this.minimalTypingNodeComponent.getTypeComponent ( ) ) )
    {
      ( ( MinimalTypingView ) this.minimalTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.minimalTypingNodeComponent.getTypeComponent ( ).getType ( ) ,
              Outline.ExecuteMouseClick.MINIMALTYPING ) ;
    }
    // TypeComponent2
    else if ( pMouseEvent.getSource ( ).equals (
        this.minimalTypingNodeComponent.getTypeComponent2 ( ) ) )
    {
      ( ( MinimalTypingView ) this.minimalTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( )
          .load (
              this.minimalTypingNodeComponent.getTypeComponent2 ( ).getType ( ) ,
              Outline.ExecuteMouseClick.MINIMALTYPING ) ;
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the different components.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleMouseEvent ( MouseEvent pMouseEvent )
  {
    if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON1 )
    {
      // Outline
      if ( this.defaultOutline != null )
      {
        handleOutline ( pMouseEvent ) ;
      }
      // SourceView
      if ( this.sourceView != null )
      {
        handleSourceView ( pMouseEvent ) ;
      }
      // TypeChecker
      if ( this.typeCheckerNodeComponent != null )
      {
        handleTypeChecker ( pMouseEvent ) ;
      }
      // MinimalTyping
      if ( this.minimalTypingNodeComponent != null )
      {
        handleMinimalTyping ( pMouseEvent ) ;
      }
      // SubTyping
      if ( this.subTypingNodeComponent != null )
      {
        handleSubTyping ( pMouseEvent ) ;
      }
      // SubTypingSourceView
      if ( this.subTypingSourceView != null )
      {
        handleSubTypingSourceView ( pMouseEvent ) ;
      }
      // BigStep
      if ( this.bigStepNodeComponent != null )
      {
        handleBigStep ( pMouseEvent ) ;
      }
      // SmallStep
      if ( this.smallStepNodeComponent != null )
      {
        handleSmallStep ( pMouseEvent ) ;
      }
    }
    else if ( pMouseEvent.getButton ( ) == MouseEvent.BUTTON3 )
    {
      // Outline
      if ( this.defaultOutline != null )
      {
        handleOutlinePopup ( pMouseEvent ) ;
      }
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the outline.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleOutline ( MouseEvent pMouseEvent )
  {
    if ( pMouseEvent.getSource ( ).equals (
        this.defaultOutline.getUI ( ).getJTreeOutline ( ) ) )
    {
      if ( ( pMouseEvent.getClickCount ( ) >= 2 )
          || ( this.defaultOutline.getPreferences ( ).isHighlightSourceCode ( ) ) )
      {
        this.defaultOutline.updateHighlighSourceCode ( true ) ;
      }
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the outline.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleOutlinePopup ( MouseEvent pMouseEvent )
  {
    // TreeOutline
    if ( pMouseEvent.getSource ( ).equals (
        this.defaultOutline.getUI ( ).getJTreeOutline ( ) ) )
    {
      int x = pMouseEvent.getX ( ) ;
      int y = pMouseEvent.getY ( ) ;
      TreePath treePath = this.defaultOutline.getUI ( ).getJTreeOutline ( )
          .getPathForLocation ( x , y ) ;
      if ( treePath == null )
      {
        return ;
      }
      this.defaultOutline.getUI ( ).getJTreeOutline ( ).setSelectionPath (
          treePath ) ;
      setStatus ( ) ;
      this.defaultOutline.getUI ( ).getJPopupMenu ( ).show (
          pMouseEvent.getComponent ( ) , x , y ) ;
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the small step view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleSmallStep ( MouseEvent pMouseEvent )
  {
    // CompoundExpression
    if ( pMouseEvent.getSource ( ).equals (
        this.smallStepNodeComponent.getCompoundExpression ( ) ) )
    {
      ( ( SmallStepView ) this.smallStepNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.smallStepNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.SMALLSTEP ) ;
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the source code view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleSourceView ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    Expression expression = null ;
    try
    {
      expression = this.sourceView.getDocument ( ).getExpression ( ) ;
    }
    catch ( Exception e )
    {
      // Do nothing
    }
    this.defaultOutline.updateHighlighSourceCode ( false ) ;
    this.defaultOutline.load ( expression , Outline.ExecuteMouseClick.EDITOR ) ;
  }


  /**
   * Handles <code>MouseEvents</code> on the subtyping view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleSubTyping ( MouseEvent pMouseEvent )
  {
    // IndexLabel
    if ( pMouseEvent.getSource ( ).equals (
        this.subTypingNodeComponent.getIndexLabel ( ) ) )
    {
      ( ( NewSubTypingView ) this.subTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline1 ( ).load (
              this.subTypingNodeComponent.getTypeComponent ( ).getType ( ) ,
              Outline.ExecuteMouseClick.SUBTYPING ) ;
      ( ( NewSubTypingView ) this.subTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline2 ( ).load (
              this.subTypingNodeComponent.getTypeComponent2 ( ).getType ( ) ,
              Outline.ExecuteMouseClick.SUBTYPING ) ;
    }
    // TypeComponent
    else if ( pMouseEvent.getSource ( ).equals (
        this.subTypingNodeComponent.getTypeComponent ( ) ) )
    {
      ( ( NewSubTypingView ) this.subTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline1 ( ).load (
              this.subTypingNodeComponent.getTypeComponent ( ).getType ( ) ,
              Outline.ExecuteMouseClick.SUBTYPING ) ;
      ( ( NewSubTypingView ) this.subTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline2 ( ).load (
              this.subTypingNodeComponent.getTypeComponent2 ( ).getType ( ) ,
              Outline.ExecuteMouseClick.SUBTYPING ) ;
    }
    // TypeComponent2
    else if ( pMouseEvent.getSource ( ).equals (
        this.subTypingNodeComponent.getTypeComponent2 ( ) ) )
    {
      ( ( NewSubTypingView ) this.subTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline1 ( ).load (
              this.subTypingNodeComponent.getTypeComponent ( ).getType ( ) ,
              Outline.ExecuteMouseClick.SUBTYPING ) ;
      ( ( NewSubTypingView ) this.subTypingNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline2 ( ).load (
              this.subTypingNodeComponent.getTypeComponent2 ( ).getType ( ) ,
              Outline.ExecuteMouseClick.SUBTYPING ) ;
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the source code view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleSubTypingSourceView ( @ SuppressWarnings ( "unused" )
  MouseEvent pMouseEvent )
  {
    if ( pMouseEvent.getSource ( ).equals (
        this.subTypingSourceView.getEditor ( ) ) )
    {
      Type type = null ;
      try
      {
        type = ( ( StyledTypeEnterField ) this.subTypingSourceView.getEditor ( )
            .getDocument ( ) ).getType ( ) ;
      }
      catch ( Exception e )
      {
        // Do nothing
      }
      this.defaultOutline.updateHighlighSourceCode ( false ) ;
      this.defaultOutline.load ( type ,
          Outline.ExecuteMouseClick.SUBTYPING_SOURCE ) ;
    }
    else if ( pMouseEvent.getSource ( ).equals (
        this.subTypingSourceView.getEditor2 ( ) ) )
    {
      Type type = null ;
      try
      {
        type = ( ( StyledTypeEnterField ) this.subTypingSourceView
            .getEditor2 ( ).getDocument ( ) ).getType ( ) ;
      }
      catch ( Exception e )
      {
        // Do nothing
      }
      this.defaultOutline.updateHighlighSourceCode ( false ) ;
      this.defaultOutline.load ( type ,
          Outline.ExecuteMouseClick.SUBTYPING_SOURCE ) ;
    }
  }


  /**
   * Handles <code>MouseEvents</code> on the type checker view.
   * 
   * @param pMouseEvent The <code>MouseEvent</code>.
   */
  private final void handleTypeChecker ( MouseEvent pMouseEvent )
  {
    // IndexLabel
    if ( pMouseEvent.getSource ( ).equals (
        this.typeCheckerNodeComponent.getIndexLabel ( ) ) )
    {
      ( ( TypeCheckerView ) this.typeCheckerNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.typeCheckerNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.TYPECHECKER ) ;
    }
    // CompoundExpression
    else if ( pMouseEvent.getSource ( ).equals (
        this.typeCheckerNodeComponent.getCompoundExpression ( ) ) )
    {
      ( ( TypeCheckerView ) this.typeCheckerNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.typeCheckerNodeComponent.getCompoundExpression ( )
                  .getExpression ( ) , Outline.ExecuteMouseClick.TYPECHECKER ) ;
    }
    // TypeComponent
    else if ( pMouseEvent.getSource ( ).equals (
        this.typeCheckerNodeComponent.getTypeComponent ( ) ) )
    {
      ( ( TypeCheckerView ) this.typeCheckerNodeComponent.getParent ( )
          .getParent ( ).getParent ( ).getParent ( ).getParent ( ) )
          .getOutline ( ).load (
              this.typeCheckerNodeComponent.getTypeComponent ( ).getType ( ) ,
              Outline.ExecuteMouseClick.TYPECHECKER ) ;
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
    TreePath treePath = this.defaultOutline.getUI ( ).getJTreeOutline ( )
        .getSelectionPath ( ) ;
    if ( treePath == null )
    {
      // No node is selected.
      return ;
    }
    OutlineNode selectedNode = ( OutlineNode ) treePath.getLastPathComponent ( ) ;
    this.defaultOutline.getUI ( ).getJMenuItemExpand ( ).setEnabled ( true ) ;
    this.defaultOutline.getUI ( ).getJMenuItemExpandAll ( ).setEnabled ( true ) ;
    this.defaultOutline.getUI ( ).getJMenuItemCollapse ( ).setEnabled ( true ) ;
    this.defaultOutline.getUI ( ).getJMenuItemCollapseAll ( )
        .setEnabled ( true ) ;
    this.defaultOutline.getUI ( ).getJMenuItemClose ( ).setEnabled ( true ) ;
    this.defaultOutline.getUI ( ).getJMenuItemCloseAll ( ).setEnabled ( true ) ;
    boolean allVisible = allChildrenVisible ( this.defaultOutline.getUI ( )
        .getJTreeOutline ( ).getPathForRow ( 0 ) ) ;
    this.defaultOutline.getUI ( ).getJMenuItemExpandAll ( ).setEnabled (
        ! allVisible ) ;
    // Selected node is not a leaf
    if ( selectedNode.getChildCount ( ) > 0 )
    {
      boolean allChildrenVisible = allChildrenVisible ( treePath ) ;
      boolean selectedChildVisible = this.defaultOutline.getUI ( )
          .getJTreeOutline ( ).isVisible (
              treePath.pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      boolean rootChildVisible = this.defaultOutline.getUI ( )
          .getJTreeOutline ( ).isVisible (
              this.defaultOutline.getUI ( ).getJTreeOutline ( ).getPathForRow (
                  0 ).pathByAddingChild ( selectedNode.getChildAt ( 0 ) ) ) ;
      this.defaultOutline.getUI ( ).getJMenuItemExpand ( ).setEnabled (
          ! allChildrenVisible ) ;
      this.defaultOutline.getUI ( ).getJMenuItemCollapse ( ).setEnabled (
          selectedChildVisible ) ;
      this.defaultOutline.getUI ( ).getJMenuItemCollapseAll ( ).setEnabled (
          rootChildVisible ) ;
      this.defaultOutline.getUI ( ).getJMenuItemClose ( ).setEnabled (
          selectedChildVisible ) ;
      this.defaultOutline.getUI ( ).getJMenuItemCloseAll ( ).setEnabled (
          rootChildVisible ) ;
    }
    // Selected node is a leaf
    else
    {
      this.defaultOutline.getUI ( ).getJMenuItemExpand ( ).setEnabled ( false ) ;
      this.defaultOutline.getUI ( ).getJMenuItemCollapse ( )
          .setEnabled ( false ) ;
      this.defaultOutline.getUI ( ).getJMenuItemClose ( ).setEnabled ( false ) ;
      // If the root is the only node, disable items
      OutlineNode root = ( OutlineNode ) this.defaultOutline.getUI ( )
          .getTreeModel ( ).getRoot ( ) ;
      this.defaultOutline.getUI ( ).getJMenuItemCloseAll ( ).setEnabled (
          ! root.isLeaf ( ) ) ;
      this.defaultOutline.getUI ( ).getJMenuItemCollapseAll ( ).setEnabled (
          ! root.isLeaf ( ) ) ;
    }
  }
}
