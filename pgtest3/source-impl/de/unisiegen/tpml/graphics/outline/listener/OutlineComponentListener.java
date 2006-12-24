package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ComponentEvent ;
import java.awt.event.ComponentListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.graphics.outline.Outline;


/**
 * This class listens for component events. It saves the divider location in the
 * OutlinePreferences when the component was resized.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineComponentListener implements ComponentListener
{
  /**
   * The JSplitPane.
   */
  private JSplitPane jSplitPane ;


  /**
   * The AbstractOutline.
   */
  private Outline abstractSyntaxTree ;


  /**
   * Initializes the OutlineComponentListener.
   * 
   * @param pJSplitPane The JSplitPane.
   * @param pAbstractSyntaxTree The AbstractOutline.
   */
  public OutlineComponentListener ( JSplitPane pJSplitPane ,
      Outline pAbstractSyntaxTree )
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
  public void componentResized ( ComponentEvent pComponentEvent )
  {
    if ( pComponentEvent.getSource ( ).equals (
        this.abstractSyntaxTree.getASTUI ( ).getJPanelMain ( ) ) )
    {
      this.abstractSyntaxTree.getASTPreferences ( ).setDividerLocation (
          this.jSplitPane.getDividerLocation ( ) ) ;
    }
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
