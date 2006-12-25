package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;


/**
 * This class listens for item events. It updates the CheckBox selection and the
 * MenuItem selection. It sets the OutlineNode values replace, binding, unbound
 * and autoupdate.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineItemListener implements ItemListener
{
  /**
   * The Outline UI.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the OutlineItemListener.
   * 
   * @param pOutlineUI The Outline UI.
   */
  public OutlineItemListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void autoUpdate ( boolean pSelected )
  {
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( )
        .setAutoUpdate ( pSelected ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the OutlineNode value binding.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void binding ( boolean pSelected )
  {
    OutlineNode.setBinding ( pSelected ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.outlineUI.getTreeModel ( ).getRoot ( ) ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( ).setBinding (
        pSelected ) ;
    this.outlineUI.getJCheckBoxBinding ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemBinding ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method is invoked if a item state has changed.
   * 
   * @param pItemEvent The item event.
   * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
   */
  public void itemStateChanged ( ItemEvent pItemEvent )
  {
    update ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED , pItemEvent
        .getSource ( ) , "" ) ; //$NON-NLS-1$
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the OutlineNode value replace.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void replace ( boolean pSelected )
  {
    OutlineNode.setReplace ( pSelected ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.outlineUI.getTreeModel ( ).getRoot ( ) ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( ).setReplace (
        pSelected ) ;
    this.outlineUI.getJCheckBoxReplace ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemReplace ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the OutlineNode value selection.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void selection ( boolean pSelected )
  {
    OutlineNode.setSelection ( pSelected ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.outlineUI.getTreeModel ( ).getRoot ( ) ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( )
        .setSelection ( pSelected ) ;
    this.outlineUI.getJCheckBoxSelection ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemSelection ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the OutlineNode value unbound.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void unbound ( boolean pSelected )
  {
    OutlineNode.setUnbound ( pSelected ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.outlineUI.getTreeModel ( ).getRoot ( ) ) ;
    this.outlineUI.getOutlineTreeSelectionListener ( ).update (
        this.outlineUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.outlineUI.getAbstractOutline ( ).getOutlinePreferences ( ).setUnbound (
        pSelected ) ;
    this.outlineUI.getJCheckBoxUnbound ( ).setSelected ( pSelected ) ;
    this.outlineUI.getJMenuItemUnbound ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the OutlineNode values replace, binding, unbound and
   * selection.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   * @param pSource The source if it is a instance of JCheckBox, otherwise null.
   * @param pActionCommand The action command, if the source is a instance of
   *          JCheckBoxMenuItem.
   */
  public void update ( boolean pSelected , Object pSource ,
      String pActionCommand )
  {
    // Replace
    if ( ( pActionCommand.equals ( "replace" ) ) //$NON-NLS-1$
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxReplace ( ) ) ) ) )
    {
      replace ( pSelected ) ;
    }
    // Binding
    else if ( ( pActionCommand.equals ( "binding" ) ) //$NON-NLS-1$
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxBinding ( ) ) ) ) )
    {
      binding ( pSelected ) ;
    }
    // Unbound
    else if ( ( pActionCommand.equals ( "unbound" ) ) //$NON-NLS-1$
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxUnbound ( ) ) ) ) )
    {
      unbound ( pSelected ) ;
    }
    // Selection
    else if ( ( pActionCommand.equals ( "selection" ) ) //$NON-NLS-1$
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxSelection ( ) ) ) ) )
    {
      selection ( pSelected ) ;
    }
    // AutoUpdate
    else if ( ( pActionCommand.equals ( "autoupdate" ) ) //$NON-NLS-1$
        || ( ( pSource != null ) && ( pSource.equals ( this.outlineUI
            .getJCheckBoxAutoUpdate ( ) ) ) ) )
    {
      autoUpdate ( pSelected ) ;
    }
  }
}
