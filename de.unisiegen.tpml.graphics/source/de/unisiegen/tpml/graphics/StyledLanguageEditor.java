package de.unisiegen.tpml.graphics;


import java.awt.event.MouseEvent;

import javax.swing.JEditorPane;
import javax.swing.ToolTipManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledEditorKit;


/**
 * An {@link javax.swing.JEditorPane} that works on
 * {@link de.unisiegen.tpml.graphics.StyledLanguageDocument}s and displays
 * tooltips for parser and lexer errors detected by the document.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.graphics.StyledLanguageDocument
 */
public final class StyledLanguageEditor extends JEditorPane
{

  //
  // Constants
  //
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = -7613995810851080677L;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>StyledLanguageEditor</code> instance.
   * 
   * @see javax.swing.JEditorPane#JEditorPane()
   */
  public StyledLanguageEditor ()
  {
    super ();
    setEditorKit ( new StyledEditorKit () );
    ToolTipManager.sharedInstance ().registerComponent ( this );
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.JEditorPane#getScrollableTracksViewportWidth()
   */
  @Override
  public boolean getScrollableTracksViewportWidth ()
  {
    return true;
  }


  //
  // Event handling
  //
  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.text.JTextComponent#getToolTipText(java.awt.event.MouseEvent)
   */
  @Override
  public String getToolTipText ( MouseEvent event )
  {
    // determine the character index in the model
    int index = viewToModel ( event.getPoint () );
    if ( index < getDocument ().getLength () )
    {
      // determine the character attribute set at the index
      StyledLanguageDocument document = ( StyledLanguageDocument ) getDocument ();
      AttributeSet set = document.getCharacterElement ( index )
          .getAttributes ();
      // check if we have an error here
      Object exception = set.getAttribute ( "exception" ); //$NON-NLS-1$
      if ( exception != null && exception instanceof Exception )
      {
        return ( ( Exception ) exception ).getMessage ();
      }
    }
    // fallback to parent's implementation
    return super.getToolTipText ( event );
  }
}
