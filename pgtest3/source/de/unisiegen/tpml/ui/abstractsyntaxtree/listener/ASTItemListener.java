package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


public class ASTItemListener implements ItemListener
{
  private ASTUI aSTUI ;


  public ASTItemListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  public void itemStateChanged ( ItemEvent pItemEvent )
  {
    update ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED , pItemEvent
        .getSource ( ) , "" ) ;
  }


  public void update ( boolean pSelected , Object pSource ,
      String pActionCommand )
  {
    if ( ( pActionCommand.equals ( "replace" ) )
        || ( ( pSource != null ) && ( pSource.equals ( this.aSTUI
            .getJCheckBoxReplace ( ) ) ) ) )
    {
      ASTNode.setReplace ( pSelected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
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
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setBindings (
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
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setSelected (
          pSelected ) ;
      this.aSTUI.getJCheckBoxSelection ( ).setSelected ( pSelected ) ;
      this.aSTUI.getJMenuItemSelection ( ).setSelected ( pSelected ) ;
    }
  }
}
