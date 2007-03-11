package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
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
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the {@link OutlineItemListener}.
   * 
   * @param pOutlineUI The {@link OutlineUI}.
   */
  public OutlineItemListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
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
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( )
        .setAutoUpdate ( pSelected ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( pSelected ) ;
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
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset ( ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( ).setBinding (
        pSelected ) ;
    this.outlineUI.getJCheckBoxBinding ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemBinding ( ).setSelected ( pSelected ) ;
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
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset ( ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( ).setReplace (
        pSelected ) ;
    this.outlineUI.getJCheckBoxReplace ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemReplace ( ).setSelected ( pSelected ) ;
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
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset ( ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( )
        .setSelection ( pSelected ) ;
    this.outlineUI.getJCheckBoxSelection ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemSelection ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the <code>JCheckBox</code> selection and the
   * <code>JCheckBoxMenuItem</code> selection. It sets the {@link OutlineNode}
   * value unbound.
   * 
   * @param pSelected The selection of the <code>JCheckBox</code> selection or
   *          the <code>JCheckBoxMenuItem</code> selection.
   */
  private final void unbound ( boolean pSelected )
  {
    OutlineNode.setUnbound ( pSelected ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset ( ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeOutline ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( ).setUnbound (
        pSelected ) ;
    this.outlineUI.getJCheckBoxUnbound ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemUnbound ( ).setSelected ( pSelected ) ;
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
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxReplace ( ) ) ) ) )
    {
      replace ( pSelected ) ;
    }
    // Binding
    else if ( ( OutlineUI.BINDING.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxBinding ( ) ) ) ) )
    {
      binding ( pSelected ) ;
    }
    // Unbound
    else if ( ( OutlineUI.UNBOUND.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxUnbound ( ) ) ) ) )
    {
      unbound ( pSelected ) ;
    }
    // Selection
    else if ( ( OutlineUI.SELECTION.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxSelection ( ) ) ) ) )
    {
      selection ( pSelected ) ;
    }
    // AutoUpdate
    else if ( ( OutlineUI.AUTOUPDATE.equals ( pActionCommand ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxAutoUpdate ( ) ) ) ) )
    {
      autoUpdate ( pSelected ) ;
    }
  }
}
