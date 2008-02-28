package de.unisiegen.tpml.graphics.outline.listener;


import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JSplitPane;

import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences;


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
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * The <code>JSplitPane</code>.
   */
  private JSplitPane jSplitPane;


  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline;


  /**
   * Initializes the {@link OutlineComponentListener}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineComponentListener ( DefaultOutline pDefaultOutline )
  {
    this.jSplitPane = null;
    this.defaultOutline = pDefaultOutline;
  }


  /**
   * Initializes the {@link OutlineComponentListener}.
   * 
   * @param pJSplitPane The <code>JSplitPane</code>.
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineComponentListener ( JSplitPane pJSplitPane,
      DefaultOutline pDefaultOutline )
  {
    this.jSplitPane = pJSplitPane;
    this.defaultOutline = pDefaultOutline;
  }


  /**
   * Component was hidden.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentHidden(ComponentEvent)
   */
  public final void componentHidden ( @SuppressWarnings ( UNUSED )
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
  public final void componentMoved ( @SuppressWarnings ( UNUSED )
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
  public final void componentResized ( ComponentEvent pComponentEvent )
  {
    if ( ( pComponentEvent.getSource ()
        .equals ( this.defaultOutline.getTree () ) )
        && ( this.jSplitPane != null ) )
    {
      this.defaultOutline.getPreferences ().setDividerLocation (
          this.jSplitPane.getDividerLocation () );
    }
    else if ( pComponentEvent.getSource ().equals (
        this.defaultOutline.getPanel () ) )
    {
      this.defaultOutline.updateBreaks ();
    }
  }


  /**
   * Component was shown.
   * 
   * @param pComponentEvent The <code>ComponentEvent</code>.
   * @see ComponentListener#componentShown(ComponentEvent)
   */
  public final void componentShown ( @SuppressWarnings ( UNUSED )
  ComponentEvent pComponentEvent )
  {
    // Do nothing
  }
}
