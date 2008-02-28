package de.unisiegen.tpml.graphics.outline.listener;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI;


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
  private DefaultOutline defaultOutline;


  /**
   * Initializes the {@link OutlineItemListener}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineItemListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline;
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
    this.defaultOutline.getPreferences ().setAutoUpdate ( pSelected );
    this.defaultOutline.getUI ().getJCheckBoxAutoUpdate ().setSelected (
        pSelected );
    this.defaultOutline.getUI ().getJMenuItemAutoUpdate ().setSelected (
        pSelected );
    if ( this.defaultOutline.getSyncOutline () != null )
    {
      this.defaultOutline.getSyncOutline ().getUI ().getJCheckBoxAutoUpdate ()
          .setSelected ( pSelected );
      this.defaultOutline.getSyncOutline ().getUI ().getJMenuItemAutoUpdate ()
          .setSelected ( pSelected );
    }
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
    OutlineNode.setBinding ( pSelected );
    this.defaultOutline.resetNode ();
    this.defaultOutline.update ( this.defaultOutline.getUI ()
        .getJTreeOutline ().getSelectionPath () );
    this.defaultOutline.getPreferences ().setBinding ( pSelected );
    this.defaultOutline.getUI ().getJCheckBoxBinding ()
        .setSelected ( pSelected );
    this.defaultOutline.getUI ().getJMenuItemBinding ()
        .setSelected ( pSelected );
    if ( this.defaultOutline.getSyncOutline () != null )
    {
      this.defaultOutline.getSyncOutline ().getUI ().getJCheckBoxBinding ()
          .setSelected ( pSelected );
      this.defaultOutline.getSyncOutline ().getUI ().getJMenuItemBinding ()
          .setSelected ( pSelected );
    }
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
    OutlineNode.setFree ( pSelected );
    this.defaultOutline.resetNode ();
    this.defaultOutline.update ( this.defaultOutline.getUI ()
        .getJTreeOutline ().getSelectionPath () );
    this.defaultOutline.getPreferences ().setFree ( pSelected );
    this.defaultOutline.getUI ().getJCheckBoxFree ().setSelected ( pSelected );
    this.defaultOutline.getUI ().getJMenuItemFree ().setSelected ( pSelected );
    if ( this.defaultOutline.getSyncOutline () != null )
    {
      this.defaultOutline.getSyncOutline ().getUI ().getJCheckBoxFree ()
          .setSelected ( pSelected );
      this.defaultOutline.getSyncOutline ().getUI ().getJMenuItemFree ()
          .setSelected ( pSelected );
    }
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
    this.defaultOutline.updateHighlighSourceCode ( pSelected );
    this.defaultOutline.getPreferences ().setHighlightSourceCode ( pSelected );
    this.defaultOutline.getUI ().getJCheckBoxHighlightSourceCode ()
        .setSelected ( pSelected );
    this.defaultOutline.getUI ().getJMenuItemHighlightSourceCode ()
        .setSelected ( pSelected );
    if ( this.defaultOutline.getSyncOutline () != null )
    {
      this.defaultOutline.getSyncOutline ().getUI ()
          .getJCheckBoxHighlightSourceCode ().setSelected ( pSelected );
      this.defaultOutline.getSyncOutline ().getUI ()
          .getJMenuItemHighlightSourceCode ().setSelected ( pSelected );
    }
  }


  /**
   * This method is invoked if a item state has changed.
   * 
   * @param pItemEvent The <code>ItemEvent</code>.
   * @see ItemListener#itemStateChanged(ItemEvent)
   */
  public final void itemStateChanged ( ItemEvent pItemEvent )
  {
    update ( pItemEvent.getStateChange () == ItemEvent.SELECTED, pItemEvent
        .getSource (), null );
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
    OutlineNode.setReplace ( pSelected );
    this.defaultOutline.resetNode ();
    this.defaultOutline.update ( this.defaultOutline.getUI ()
        .getJTreeOutline ().getSelectionPath () );
    this.defaultOutline.getPreferences ().setReplace ( pSelected );
    this.defaultOutline.getUI ().getJCheckBoxReplace ()
        .setSelected ( pSelected );
    this.defaultOutline.getUI ().getJMenuItemReplace ()
        .setSelected ( pSelected );
    if ( this.defaultOutline.getSyncOutline () != null )
    {
      this.defaultOutline.getSyncOutline ().getUI ().getJCheckBoxReplace ()
          .setSelected ( pSelected );
      this.defaultOutline.getSyncOutline ().getUI ().getJMenuItemReplace ()
          .setSelected ( pSelected );
    }
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
    OutlineNode.setSelection ( pSelected );
    this.defaultOutline.resetNode ();
    this.defaultOutline.update ( this.defaultOutline.getUI ()
        .getJTreeOutline ().getSelectionPath () );
    this.defaultOutline.getPreferences ().setSelection ( pSelected );
    this.defaultOutline.getUI ().getJCheckBoxSelection ().setSelected (
        pSelected );
    this.defaultOutline.getUI ().getJMenuItemSelection ().setSelected (
        pSelected );
    if ( this.defaultOutline.getSyncOutline () != null )
    {
      this.defaultOutline.getSyncOutline ().getUI ().getJCheckBoxSelection ()
          .setSelected ( pSelected );
      this.defaultOutline.getSyncOutline ().getUI ().getJMenuItemSelection ()
          .setSelected ( pSelected );
    }
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
  public final void update ( boolean pSelected, Object pSource,
      String pActionCommand )
  {
    // Replace
    if ( ( OutlineUI.REPLACE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getUI ().getJCheckBoxReplace () ) ) ) )
    {
      replace ( pSelected );
    }
    // Binding
    else if ( ( OutlineUI.BINDING.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getUI ().getJCheckBoxBinding () ) ) ) )
    {
      binding ( pSelected );
    }
    // Free
    else if ( ( OutlineUI.FREE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getUI ().getJCheckBoxFree () ) ) ) )
    {
      free ( pSelected );
    }
    // Selection
    else if ( ( OutlineUI.SELECTION.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getUI ().getJCheckBoxSelection () ) ) ) )
    {
      selection ( pSelected );
    }
    // HighlightSourceCode
    else if ( ( OutlineUI.HIGHLIGHTSOURCECODE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getUI ().getJCheckBoxHighlightSourceCode () ) ) ) )
    {
      highlightSourceCode ( pSelected );
    }
    // AutoUpdate
    else if ( ( OutlineUI.AUTOUPDATE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.defaultOutline
            .getUI ().getJCheckBoxAutoUpdate () ) ) ) )
    {
      autoUpdate ( pSelected );
    }
  }
}
