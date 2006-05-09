package ui;

import java.util.List;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface EditorPane {  
  /**
   * Returns the {@link EditorAction}s for this editor pane.
   * 
   * @return the {@link EditorAction}s.
   */
  public List<EditorAction> getActions();
  
  /**
   * Returns the title for this editor pane, i.e. <tt>"Source"</tt>
   * in case of the source pane.
   * 
   * @return the title for this editor pane.
   */
  public String getTitle();
}
