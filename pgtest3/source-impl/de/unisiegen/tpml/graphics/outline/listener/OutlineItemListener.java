package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;


/**
 * This class listens for item events. It updates the <code>JCheckBox</code>
 * selection and the <code>JCheckBoxMenuItem</code> selection. It sets the
 * {@link OutlineNode} values replace, binding, unbound and autoupdate.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineItemListener implements ItemListener
{
  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * Initializes the {@link OutlineItemListener}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineItemListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void autoUpdate ( boolean pSelected )
  {
    this.defaultOutline.getOutlinePreferences ( ).setAutoUpdate ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJCheckBoxAutoUpdate ( )
        .setSelected ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemAutoUpdate ( )
        .setSelected ( pSelected ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection. It sets the {@link OutlineNode}
   * value binding.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void binding ( boolean pSelected )
  {
    OutlineNode.setBinding ( pSelected ) ;
    this.defaultOutline.resetNode ( ) ;
    this.defaultOutline.update ( this.defaultOutline.getOutlineUI ( )
        .getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.defaultOutline.getOutlinePreferences ( ).setBinding ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJCheckBoxBinding ( ).setSelected (
        pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemBinding ( ).setSelected (
        pSelected ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection. It sets the {@link OutlineNode}
   * value unbound.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void free ( boolean pSelected )
  {
    OutlineNode.setFree ( pSelected ) ;
    this.defaultOutline.resetNode ( ) ;
    this.defaultOutline.update ( this.defaultOutline.getOutlineUI ( )
        .getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.defaultOutline.getOutlinePreferences ( ).setFree ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJCheckBoxFree ( ).setSelected (
        pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemFree ( ).setSelected (
        pSelected ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void highlightSourceCode ( boolean pSelected )
  {
    this.defaultOutline.updateHighlighSourceCode ( pSelected ) ;
    this.defaultOutline.getOutlinePreferences ( ).setHighlightSourceCode (
        pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJCheckBoxHighlightSourceCode ( )
        .setSelected ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemHighlightSourceCode ( )
        .setSelected ( pSelected ) ;
  }


  /**
   * This method is invoked if a item state has changed.
   * 
   * @param pItemEvent The <code>ItemEvent</code>.
   * @see ItemListener#itemStateChanged(ItemEvent)
   */
  public final void itemStateChanged ( ItemEvent pItemEvent )
  {
    update ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED , pItemEvent
        .getSource ( ) , null ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection. It sets the {@link OutlineNode}
   * value replace.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void replace ( boolean pSelected )
  {
    OutlineNode.setReplace ( pSelected ) ;
    this.defaultOutline.resetNode ( ) ;
    this.defaultOutline.update ( this.defaultOutline.getOutlineUI ( )
        .getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.defaultOutline.getOutlinePreferences ( ).setReplace ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJCheckBoxReplace ( ).setSelected (
        pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemReplace ( ).setSelected (
        pSelected ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection. It sets the {@link OutlineNode}
   * value selection.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void selection ( boolean pSelected )
  {
    OutlineNode.setSelection ( pSelected ) ;
    this.defaultOutline.resetNode ( ) ;
    this.defaultOutline.update ( this.defaultOutline.getOutlineUI ( )
        .getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.defaultOutline.getOutlinePreferences ( ).setSelection ( pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJCheckBoxSelection ( ).setSelected (
        pSelected ) ;
    this.defaultOutline.getOutlineUI ( ).getJMenuItemSelection ( ).setSelected (
        pSelected ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection. It sets the {@link OutlineNode}
   * values replace, binding, unbound and selection.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   * @param pSource The source if it is a instance of <code>JCheckBox</code>,
   *          otherwise null.
   * @param pActionCommand The <code>ActionCommand</code>, if the source is a
   *          instance of <code>JCheckBoxMenuItem</code>.
   */
  public final void update ( boolean pSelected , Object pSource ,
      String pActionCommand )
  {
    // Replace
    if ( ( OutlineUI.REPLACE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getOutlineUI ( ).getJCheckBoxReplace ( ) ) ) ) )
    {
      replace ( pSelected ) ;
    }
    // Binding
    else if ( ( OutlineUI.BINDING.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getOutlineUI ( ).getJCheckBoxBinding ( ) ) ) ) )
    {
      binding ( pSelected ) ;
    }
    // Free
    else if ( ( OutlineUI.FREE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getOutlineUI ( ).getJCheckBoxFree ( ) ) ) ) )
    {
      free ( pSelected ) ;
    }
    // Selection
    else if ( ( OutlineUI.SELECTION.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getOutlineUI ( ).getJCheckBoxSelection ( ) ) ) ) )
    {
      selection ( pSelected ) ;
    }
    // HighlightSourceCode
    else if ( ( OutlineUI.HIGHLIGHTSOURCECODE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getOutlineUI ( ).getJCheckBoxHighlightSourceCode ( ) ) ) ) )
    {
      highlightSourceCode ( pSelected ) ;
    }
    // AutoUpdate
    else if ( ( OutlineUI.AUTOUPDATE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getOutlineUI ( ).getJCheckBoxAutoUpdate ( ) ) ) ) )
    {
      autoUpdate ( pSelected ) ;
    }
  }
}
