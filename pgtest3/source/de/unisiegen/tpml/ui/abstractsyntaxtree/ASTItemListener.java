package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;


public class ASTItemListener implements ItemListener
{
  private ASTUI aSTUI ;


  public ASTItemListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  public void itemStateChanged ( ItemEvent pItemEvent )
  {
    if ( pItemEvent.getSource ( ).equals ( this.aSTUI.getJCheckBoxReplace ( ) ) )
    {
      ASTNode
          .setReplaceGeneral ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      String replace = "false" ;
      if ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED )
      {
        replace = "true" ;
      }
      this.aSTUI.getAbstractSyntaxTree ( ).getPreferences ( ).put (
          "checkedReplace" , replace ) ;
    }
    else if ( pItemEvent.getSource ( ).equals (
        this.aSTUI.getJCheckBoxBindings ( ) ) )
    {
      ASTNode
          .setShowBindings ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      String bindings = "false" ;
      if ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED )
      {
        bindings = "true" ;
      }
      this.aSTUI.getAbstractSyntaxTree ( ).getPreferences ( ).put (
          "checkedBindings" , bindings ) ;
    }
  }
}
