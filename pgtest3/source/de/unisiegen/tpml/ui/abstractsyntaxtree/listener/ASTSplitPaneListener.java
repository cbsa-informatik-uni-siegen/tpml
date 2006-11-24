package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ComponentEvent ;
import java.awt.event.ComponentListener ;
import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTSplitPaneListener implements PropertyChangeListener ,
    ComponentListener
{
  /**
   * TODO
   */
  private JSplitPane jSplitPane ;


  /**
   * TODO
   */
  private boolean setDivider = false ;


  /**
   * TODO
   */
  private AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * TODO
   * 
   * @param pJSplitPane
   * @param pAbstractSyntaxTree
   */
  public ASTSplitPaneListener ( JSplitPane pJSplitPane ,
      AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.jSplitPane = pJSplitPane ;
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  /**
   * TODO
   * 
   * @param pPropertyChangeEvent
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange ( @ SuppressWarnings ( "unused" )
  PropertyChangeEvent pPropertyChangeEvent )
  {
    if ( ! this.setDivider )
    {
      this.setDivider = true ;
      this.jSplitPane.setDividerLocation ( this.abstractSyntaxTree
          .getASTPreferences ( ).getDividerLocation ( ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pComponentEvent
   * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
   */
  public void componentHidden ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * TODO
   * 
   * @param pComponentEvent
   * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
   */
  public void componentMoved ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * TODO
   * 
   * @param pComponentEvent
   * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
   */
  public void componentResized ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    this.abstractSyntaxTree.getASTPreferences ( ).setDividerLocation (
        this.jSplitPane.getDividerLocation ( ) ) ;
  }


  /**
   * TODO
   * 
   * @param pComponentEvent
   * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
   */
  public void componentShown ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }
}
