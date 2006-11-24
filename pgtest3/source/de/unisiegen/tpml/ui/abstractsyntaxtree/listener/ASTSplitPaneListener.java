package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ComponentEvent ;
import java.awt.event.ComponentListener ;
import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


public class ASTSplitPaneListener implements PropertyChangeListener ,
    ComponentListener
{
  private JSplitPane jSplitPane ;


  private boolean setDivider = false ;


  private AbstractSyntaxTree abstractSyntaxTree ;


  public ASTSplitPaneListener ( JSplitPane pJSplitPane ,
      AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.jSplitPane = pJSplitPane ;
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  public void propertyChange ( PropertyChangeEvent pPropertyChangeEvent )
  {
    if ( ( pPropertyChangeEvent.getPropertyName ( ).equals ( "advanced" ) )
        && ( ! this.setDivider ) )
    {
      this.setDivider = true ;
      this.jSplitPane.setDividerLocation ( this.abstractSyntaxTree
          .getASTPreferences ( ).getDividerLocation ( ) ) ;
    }
  }


  public void componentHidden ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  public void componentMoved ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  public void componentResized ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    this.abstractSyntaxTree.getASTPreferences ( ).setDividerLocation (
        this.jSplitPane.getDividerLocation ( ) ) ;
  }


  public void componentShown ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }
}
