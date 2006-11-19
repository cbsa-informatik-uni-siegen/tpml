package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI;


public class ASTItemListener implements ItemListener
{
  private ASTUI aSTUI ;


  public ASTItemListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  public void itemStateChanged ( ItemEvent pItemEvent )
  {
    boolean selected = pItemEvent.getStateChange ( ) == ItemEvent.SELECTED ;
    if ( pItemEvent.getSource ( ).equals ( this.aSTUI.getJCheckBoxReplace ( ) ) )
    {
      ASTNode.setCheckedReplace ( selected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( )
          .setReplace ( selected ) ;
    }
    else if ( pItemEvent.getSource ( ).equals (
        this.aSTUI.getJCheckBoxBindings ( ) ) )
    {
      ASTNode.setCheckedBindings ( selected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( )
          .setBindings ( selected ) ;
    }
    else if ( pItemEvent.getSource ( ).equals (
        this.aSTUI.getJCheckBoxSelected ( ) ) )
    {
      ASTNode.setCheckedSelected ( selected ) ;
      this.aSTUI.getASTTreeSelectionListener ( ).update (
          this.aSTUI.getJAbstractSyntaxTree ( ).getSelectionPath ( ) ) ;
      this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( )
          .setSelected ( selected ) ;
    }
  }
}
