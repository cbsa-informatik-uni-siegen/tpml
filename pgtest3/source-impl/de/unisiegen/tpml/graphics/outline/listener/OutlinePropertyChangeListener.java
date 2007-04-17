package de.unisiegen.tpml.graphics.outline.listener ;


import java.beans.PropertyChangeEvent ;
import java.beans.PropertyChangeListener ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.ui.editor.TextEditorPanel ;


/**
 * Sets the <code>DividerLocation</code>, if it was not set before. The
 * <code>DividerLocation</code> can not be set when the
 * <code>JSplitPane</code> is created, so this is done here.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlinePropertyChangeListener implements
    PropertyChangeListener
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
   * The {@link Outline}.
   */
  private Outline outline ;


  /**
   * Initializes the {@link OutlinePropertyChangeListener}.
   * 
   * @param pJSplitPane The <code>JSplitPane</code>.
   * @param pOutline The {@link Outline}.
   */
  public OutlinePropertyChangeListener ( final JSplitPane pJSplitPane ,
      final Outline pOutline )
  {
    this.jSplitPane = pJSplitPane ;
    this.outline = pOutline ;
    this.setDivider = false ;
  }


  /**
   * Initializes the {@link OutlinePropertyChangeListener}.
   * 
   * @param pOutline The {@link Outline}.
   */
  public OutlinePropertyChangeListener ( final Outline pOutline )
  {
    this.jSplitPane = null ;
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
  public final void propertyChange (
      final PropertyChangeEvent pPropertyChangeEvent )
  {
    if ( ( pPropertyChangeEvent.getSource ( ) instanceof AbstractProofView )
        || ( pPropertyChangeEvent.getSource ( ) instanceof TextEditorPanel ) )
    {
      if ( ! this.setDivider )
      {
        this.setDivider = true ;
        this.jSplitPane.setDividerLocation ( this.outline
            .getOutlinePreferences ( ).getDividerLocation ( ) ) ;
      }
    }
    else if ( pPropertyChangeEvent.getSource ( ) instanceof Theme )
    {
      if ( pPropertyChangeEvent.getPropertyName ( ).endsWith ( "Color" ) )//$NON-NLS-1$
      {
        this.outline.propertyChanged ( ) ;
      }
    }
  }
}
