package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;


public class ASTItemListener implements ItemListener
{
  private AbstractSyntaxTreeUI abstractSyntaxTreeUI ;


  public ASTItemListener ( AbstractSyntaxTreeUI pAbstractSyntaxTreeUI )
  {
    this.abstractSyntaxTreeUI = pAbstractSyntaxTreeUI ;
  }


  public void itemStateChanged ( ItemEvent pItemEvent )
  {
    if ( pItemEvent.getSource ( ).equals (
        this.abstractSyntaxTreeUI.getJCheckBoxReplace ( ) ) )
    {
      AbstractSyntaxTreeNode
          .setReplaceGeneral ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED ) ;
      this.abstractSyntaxTreeUI.getASTTreeSelectionListener ( ).update (
          this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
              .getSelectionPath ( ) ) ;
    }
    else if ( pItemEvent.getSource ( ).equals (
        this.abstractSyntaxTreeUI.getJCheckBoxBindings ( ) ) )
    {
      AbstractSyntaxTreeNode
          .setShowBindings ( pItemEvent.getStateChange ( ) == ItemEvent.SELECTED ) ;
      this.abstractSyntaxTreeUI.getASTTreeSelectionListener ( ).update (
          this.abstractSyntaxTreeUI.getJAbstractSyntaxTree ( )
              .getSelectionPath ( ) ) ;
    }
  }
}
