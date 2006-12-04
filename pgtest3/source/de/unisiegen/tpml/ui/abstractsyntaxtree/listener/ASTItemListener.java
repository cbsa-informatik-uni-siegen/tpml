package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * This class listens for item events. It updates the CheckBox selection and the
 * MenuItem selection. It sets the ASTNode values replace, binding, unbound and
 * autoupdate.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTItemListener implements ItemListener
{
  /**
   * The AbstractSyntaxTree UI.
   */
  private ASTUI aSTUI ;


  /**
   * Initializes the ASTItemListener.
   * 
   * @param pASTUI The AbstractSyntaxTree UI.
   */
  public ASTItemListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
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
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setAutoUpdate (
        pSelected ) ;
    this.aSTUI.getJCheckBoxAutoUpdate ( ).setSelected ( pSelected ) ;
    this.aSTUI.getJMenuItemAutoUpdate ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the ASTNode value binding.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void binding ( boolean pSelected )
  {
    ASTNode.setBinding ( pSelected ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.aSTUI.getTreeModel ( ).getRoot ( ) ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).update (
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setBinding (
        pSelected ) ;
    this.aSTUI.getJCheckBoxBinding ( ).setSelected ( pSelected ) ;
    this.aSTUI.getJMenuItemBinding ( ).setSelected ( pSelected ) ;
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
        .getSource ( ) , "" ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the ASTNode value replace.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void replace ( boolean pSelected )
  {
    ASTNode.setReplace ( pSelected ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.aSTUI.getTreeModel ( ).getRoot ( ) ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).update (
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setReplace (
        pSelected ) ;
    this.aSTUI.getJCheckBoxReplace ( ).setSelected ( pSelected ) ;
    this.aSTUI.getJMenuItemReplace ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the ASTNode value selection.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void selection ( boolean pSelected )
  {
    ASTNode.setSelection ( pSelected ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.aSTUI.getTreeModel ( ).getRoot ( ) ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).update (
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setSelection (
        pSelected ) ;
    this.aSTUI.getJCheckBoxSelection ( ).setSelected ( pSelected ) ;
    this.aSTUI.getJMenuItemSelection ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the ASTNode value unbound.
   * 
   * @param pSelected The selection of the JCheckBox selection or the
   *          JCheckBoxMenuItem selection.
   */
  private void unbound ( boolean pSelected )
  {
    ASTNode.setUnbound ( pSelected ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).reset (
        ( DefaultMutableTreeNode ) this.aSTUI.getTreeModel ( ).getRoot ( ) ) ;
    this.aSTUI.getASTTreeSelectionListener ( ).update (
        this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setUnbound (
        pSelected ) ;
    this.aSTUI.getJCheckBoxUnbound ( ).setSelected ( pSelected ) ;
    this.aSTUI.getJMenuItemUnbound ( ).setSelected ( pSelected ) ;
  }


  /**
   * This method updates the JCheckBox selection and the JCheckBoxMenuItem
   * selection. It sets the ASTNode values replace, binding, unbound and
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
    if ( ( pActionCommand.equals ( "replace" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxReplace ( ) ) ) ) )
    {
      replace ( pSelected ) ;
    }
    // Binding
    else if ( ( pActionCommand.equals ( "binding" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxBinding ( ) ) ) ) )
    {
      binding ( pSelected ) ;
    }
    // Unbound
    else if ( ( pActionCommand.equals ( "unbound" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxUnbound ( ) ) ) ) )
    {
      unbound ( pSelected ) ;
    }
    // Selection
    else if ( ( pActionCommand.equals ( "selection" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxSelection ( ) ) ) ) )
    {
      selection ( pSelected ) ;
    }
    // AutoUpdate
    else if ( ( pActionCommand.equals ( "autoupdate" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxAutoUpdate ( ) ) ) ) )
    {
      autoUpdate ( pSelected ) ;
    }
  }
}
