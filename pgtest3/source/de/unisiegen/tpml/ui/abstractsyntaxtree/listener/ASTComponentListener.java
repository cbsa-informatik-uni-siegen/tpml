package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.ComponentEvent ;
import java.awt.event.ComponentListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


/**
 * This class listens for component events. It saves the divider location in the
 * ASTPreferences when the component was resized.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTComponentListener implements ComponentListener
{
  /**
   * The JSplitPane.
   */
  private JSplitPane jSplitPane ;


  /**
   * The AbstractSyntaxTree.
   */
  private AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * Initializes the ASTComponentListener.
   * 
   * @param pJSplitPane The JSplitPane.
   * @param pAbstractSyntaxTree The AbstractSyntaxTree.
   */
  public ASTComponentListener ( JSplitPane pJSplitPane ,
      AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.jSplitPane = pJSplitPane ;
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  /**
   * Component was hidden.
   * 
   * @param pComponentEvent The component event.
   * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
   */
  public void componentHidden ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * Component was moved.
   * 
   * @param pComponentEvent The component event.
   * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
   */
  public void componentMoved ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * Component was resized.
   * 
   * @param pComponentEvent The component event.
   * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
   */
  public void componentResized ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    this.abstractSyntaxTree.getASTPreferences ( ).setDividerLocation (
        this.jSplitPane.getDividerLocation ( ) ) ;
  }


  /**
   * Component was shown.
   * 
   * @param pComponentEvent The component event.
   * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
   */
  public void componentShown ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }
}
