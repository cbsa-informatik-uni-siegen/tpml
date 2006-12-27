package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ComponentEvent ;
import java.awt.event.ComponentListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * This class listens for component events. It saves the
 * <code>DividerLocation</code> in the <code>OutlinePreferences</code> when
 * the component was resized.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineComponentListener implements ComponentListener
{
  /**
   * The <code>JSplitPane</code>.
   */
  private JSplitPane jSplitPane ;


  /**
   * The <code>Outline</code>.
   */
  private Outline outline ;


  /**
   * Initializes the <code>OutlineComponentListener</code>.
   * 
   * @param pJSplitPane The <code>JSplitPane</code>.
   * @param pOutline The <code>Outline</code>.
   */
  public OutlineComponentListener ( JSplitPane pJSplitPane , Outline pOutline )
  {
    this.jSplitPane = pJSplitPane ;
    this.outline = pOutline ;
  }


  /**
   * Component was hidden.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentHidden(ComponentEvent)
   */
  public void componentHidden ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * Component was moved.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentMoved(ComponentEvent)
   */
  public void componentMoved ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * Component was resized.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentResized(ComponentEvent)
   */
  public void componentResized ( ComponentEvent pComponentEvent )
  {
    if ( pComponentEvent.getSource ( ).equals (
        this.outline.getJPanelOutline ( ) ) )
    {
      this.outline.getOutlinePreferences ( ).setDividerLocation (
          this.jSplitPane.getDividerLocation ( ) ) ;
    }
  }


  /**
   * Component was shown.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentShown(ComponentEvent)
   */
  public void componentShown ( @ SuppressWarnings ( "unused" )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }
}
