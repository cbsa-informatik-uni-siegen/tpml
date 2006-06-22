package ui.newgui;

import java.util.List;
import java.awt.Component;


/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id: EditorPane.java 137 2006-05-09 17:51:32Z benny $
 */
public interface EditorComponent {  
  /**
   * Returns the {@link DefaultEditorAction}s for this editor pane.
   * 
   * @return the {@link DefaultEditorAction}s.
   */
  public List<EditorAction> getActions();
  
  /**
   * Returns the title for this editor pane, i.e. <tt>"Source"</tt>
   * in case of the source pane.
   * 
   * @return the title for this editor pane.
   */
  public String getTitle();
  /**
   * Sets the enabled status of the specified action.
   * @param action the name of the action
   * @param enabled the enabled status to be set
   */
  public void setActionStatus (String action, boolean enabled);
  
  public EditorAction getAction (String action);
  
  public void handleUndo();
  
  public void handleRedo();
    
//  public Component getDisplay();
}
