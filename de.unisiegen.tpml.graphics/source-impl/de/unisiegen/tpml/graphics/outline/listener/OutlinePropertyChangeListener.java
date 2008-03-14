package de.unisiegen.tpml.graphics.outline.listener;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSplitPane;

import de.unisiegen.tpml.core.util.Theme;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.editor.TextEditorPanel;
import de.unisiegen.tpml.graphics.editor.TypeEditorPanel;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;


/**
 * Sets the <code>DividerLocation</code>, if it was not set before. The
 * <code>DividerLocation</code> can not be set when the
 * <code>JSplitPane</code> is created, so this is done here.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class OutlinePropertyChangeListener implements
    PropertyChangeListener
{

  /**
   * The <code>JSplitPane</code>.
   */
  private JSplitPane jSplitPane;


  /**
   * Used to indicate that the <code>DividerLocation</code> was set before.
   */
  private boolean setDivider;


  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline;


  /**
   * Initializes the {@link OutlinePropertyChangeListener}.
   * 
   * @param pJSplitPane The <code>JSplitPane</code>.
   * @param pDefaultOutline The {@link Outline}.
   */
  public OutlinePropertyChangeListener ( JSplitPane pJSplitPane,
      DefaultOutline pDefaultOutline )
  {
    this.jSplitPane = pJSplitPane;
    this.defaultOutline = pDefaultOutline;
    this.setDivider = false;
  }


  /**
   * Initializes the {@link OutlinePropertyChangeListener}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlinePropertyChangeListener ( DefaultOutline pDefaultOutline )
  {
    this.jSplitPane = null;
    this.defaultOutline = pDefaultOutline;
    this.setDivider = false;
  }


  /**
   * Sets the <code>DividerLocation</code>, if it was not set before. The
   * <code>DividerLocation</code> can not be set when the
   * <code>JSplitPane</code> is created, so this is done here.
   * 
   * @param pPropertyChangeEvent The <code>PropertyChangeEvent</code>.
   * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
   */
  public final void propertyChange ( PropertyChangeEvent pPropertyChangeEvent )
  {
    if ( ( pPropertyChangeEvent.getSource () instanceof AbstractProofView )
        || ( pPropertyChangeEvent.getSource () instanceof TextEditorPanel )
        || ( pPropertyChangeEvent.getSource () instanceof TypeEditorPanel ) )
    {
      if ( !this.setDivider )
      {
        this.setDivider = true;
        this.jSplitPane.setDividerLocation ( this.defaultOutline
            .getPreferences ().getDividerLocation () );
      }
    }
    else if ( pPropertyChangeEvent.getSource () instanceof Theme )
    {
      if ( pPropertyChangeEvent.getPropertyName ().endsWith ( "Color" ) )//$NON-NLS-1$
      {
        this.defaultOutline.propertyChanged ();
      }
    }
  }
}
