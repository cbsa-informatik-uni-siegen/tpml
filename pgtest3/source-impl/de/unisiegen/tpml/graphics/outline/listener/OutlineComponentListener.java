package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ComponentEvent ;
import java.awt.event.ComponentListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;


/**
 * This class listens for <code>ComponentEvents</code>. It saves the
 * <code>DividerLocation</code> in the {@link OutlinePreferences} when the
 * component was resized.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineComponentListener implements ComponentListener
{
  /**
   * The unused <code>String</code> for the <code>SuppressWarnings</code>.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The <code>JSplitPane</code>.
   */
  private JSplitPane jSplitPane ;


  /**
   * The {@link Outline}.
   */
  private Outline outline ;


  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * Initializes the {@link OutlineComponentListener}.
   * 
   * @param pJSplitPane The <code>JSplitPane</code>.
   * @param pOutline The {@link Outline}.
   */
  public OutlineComponentListener ( final JSplitPane pJSplitPane ,
      final Outline pOutline )
  {
    this.jSplitPane = pJSplitPane ;
    this.outline = pOutline ;
    this.defaultOutline = null ;
  }


  /**
   * Initializes the {@link OutlineComponentListener}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineComponentListener ( final DefaultOutline pDefaultOutline )
  {
    this.jSplitPane = null ;
    this.outline = null ;
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * Component was hidden.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentHidden(ComponentEvent)
   */
  public final void componentHidden (
      @ SuppressWarnings ( OutlineComponentListener.UNUSED )
      final ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * Component was moved.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentMoved(ComponentEvent)
   */
  public final void componentMoved (
      @ SuppressWarnings ( OutlineComponentListener.UNUSED )
      final ComponentEvent pComponentEvent )
  {
    // Do nothing
  }


  /**
   * Component was resized.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentResized(ComponentEvent)
   */
  public final void componentResized ( final ComponentEvent pComponentEvent )
  {
    if ( this.outline != null )
    {
      if ( pComponentEvent.getSource ( ).equals (
          this.outline.getJPanelOutline ( ) ) )
      {
        this.outline.getOutlinePreferences ( ).setDividerLocation (
            this.jSplitPane.getDividerLocation ( ) ) ;
      }
    }
    if ( this.defaultOutline != null )
    {
      if ( pComponentEvent.getSource ( ).equals (
          this.defaultOutline.getJPanelOutline ( ) ) )
      {
        this.defaultOutline.updateBreaks ( ) ;
      }
    }
  }


  /**
   * Component was shown.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentShown(ComponentEvent)
   */
  public final void componentShown (
      @ SuppressWarnings ( OutlineComponentListener.UNUSED )
      final ComponentEvent pComponentEvent )
  {
    // Do nothing
  }
}
