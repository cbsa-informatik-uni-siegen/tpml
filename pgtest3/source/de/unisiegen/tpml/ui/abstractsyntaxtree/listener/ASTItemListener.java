package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * TODO
 *
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTItemListener implements ItemListener
{
  /**
   * TODO
   */
  private ASTUI aSTUI ;


  /**
   * TODO
   *
   * @param pASTUI
   */
  public ASTItemListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  /**
   * TODO
   * 
   * @param pItemEvent
   * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
   */
  public void itemStateChanged ( ItemEvent pItemEvent )
  {
    update ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED , pItemEvent
        .getSource ( ) , "" ) ;
  }


  /**
   * TODO
   *
   * @param pSelected
   * @param pSource
   * @param pActionCommand
   */
  public void update ( boolean pSelected , Object pSource ,
      String pActionCommand )
  {
    if ( ( pActionCommand.equals ( "replace" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxReplace ( ) ) ) ) )
    {
      ASTNode.setReplace ( pSelected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setReplace (
          pSelected ) ;
      this.aSTUI.getJCheckBoxReplace ( ).setSelected ( pSelected ) ;
      this.aSTUI.getJMenuItemReplace ( ).setSelected ( pSelected ) ;
    }
    else if ( ( pActionCommand.equals ( "binding" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxBinding ( ) ) ) ) )
    {
      ASTNode.setBinding ( pSelected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setBinding (
          pSelected ) ;
      this.aSTUI.getJCheckBoxBinding ( ).setSelected ( pSelected ) ;
      this.aSTUI.getJMenuItemBinding ( ).setSelected ( pSelected ) ;
    }
    else if ( ( pActionCommand.equals ( "selection" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxSelection ( ) ) ) ) )
    {
      ASTNode.setSelection ( pSelected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJTreeAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setSelection (
          pSelected ) ;
      this.aSTUI.getJCheckBoxSelection ( ).setSelected ( pSelected ) ;
      this.aSTUI.getJMenuItemSelection ( ).setSelected ( pSelected ) ;
    }
    else if ( ( pActionCommand.equals ( "autoupdate" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxAutoUpdate ( ) ) ) ) )
    {
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setAutoUpdate (
          pSelected ) ;
      this.aSTUI.getJCheckBoxAutoUpdate ( ).setSelected ( pSelected ) ;
      this.aSTUI.getJMenuItemAutoUpdate ( ).setSelected ( pSelected ) ;
    }
  }
}
