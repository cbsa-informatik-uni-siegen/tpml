package de.unisiegen.tpml.graphics.outline.listener ;


import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * Sets the divider location, if it was not set before. The divider location can
 * not be set when the JSplitPane is created, so this is done here.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlinePropertyChangeListener implements PropertyChangeListener
{
  /**
   * The JSplitPane.
   */
  private JSplitPane jSplitPane ;


  /**
   * Used to indicate that the divider location was set before.
   */
  private boolean setDivider ;


  /**
   * The Outline.
   */
  private Outline outline ;


  /**
   * Initializes the OutlinePropertyChangeListener.
   * 
   * @param pJSplitPane The JSplitPane.
   * @param pOutline The Outline.
   */
  public OutlinePropertyChangeListener ( JSplitPane pJSplitPane ,
      Outline pOutline )
  {
    this.jSplitPane = pJSplitPane ;
    this.outline = pOutline ;
    this.setDivider = false ;
  }


  /**
   * Sets the divider location, if it was not set before. The divider location
   * can not be set when the JSplitPane is created, so this is done here.
   * 
   * @param pPropertyChangeEvent The property change event.
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange ( PropertyChangeEvent pPropertyChangeEvent )
  {
    if ( pPropertyChangeEvent.getSource ( ) instanceof AbstractProofView )
    {
      if ( ! this.setDivider )
      {
        this.setDivider = true ;
        this.jSplitPane.setDividerLocation ( this.outline
            .getOutlinePreferences ( ).getDividerLocation ( ) ) ;
      }
    }
  }
}
