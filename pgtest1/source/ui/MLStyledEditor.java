package ui;

import java.awt.event.MouseEvent;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class MLStyledEditor extends JEditorPane {
  //
  // Constants
  //
  
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = -694036770161511153L;

  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>MLStyledEditor</code>.
   */
  public MLStyledEditor() {
    super();
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
  public String getToolTipText(MouseEvent event) {
    // determine the character index in the model
    int index = viewToModel(event.getPoint());
    
    // determine the character attribute set at the index
    MLStyledDocument document = (MLStyledDocument)getDocument();
    AttributeSet set = document.getCharacterElement(index).getAttributes();
    
    // check if we have an error here
    Object exception = set.getAttribute("exception");
    if (exception != null && exception instanceof Exception) {
      return ((Exception)exception).getLocalizedMessage();
    }
    
    // fallback to parent's implementation
    return super.getToolTipText(event);
  }
}
