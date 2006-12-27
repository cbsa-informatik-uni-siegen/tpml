package de.unisiegen.tpml.graphics.outline.listener ;


import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * Sets the <code>DividerLocation</code>, if it was not set before. The
 * <code>DividerLocation</code> can not be set when the
 * <code>JSplitPane</code> is created, so this is done here.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlinePropertyChangeListener implements PropertyChangeListener
{
  /**
   * The <code>JSplitPane</code>.
   */
  private JSplitPane jSplitPane ;


  /**
   * Used to indicate that the <code>DividerLocation</code> was set before.
   */
  private boolean setDivider ;


  /**
   * The <code>Outline</code>.
   */
  private Outline outline ;


  /**
   * Initializes the <code>OutlinePropertyChangeListener</code>.
   * 
   * @param pJSplitPane The <code>JSplitPane</code>.
   * @param pOutline The <code>Outline</code>.
   */
  public OutlinePropertyChangeListener ( JSplitPane pJSplitPane ,
      Outline pOutline )
  {
    this.jSplitPane = pJSplitPane ;
    this.outline = pOutline ;
    this.setDivider = false ;
  }


  /**
   * Sets the <code>DividerLocation</code>, if it was not set before. The
   * <code>DividerLocation</code> can not be set when the
   * <code>JSplitPane</code> is created, so this is done here.
   * 
   * @param pPropertyChangeEvent The <code>PropertyChangeEvent</code>.
   * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
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
