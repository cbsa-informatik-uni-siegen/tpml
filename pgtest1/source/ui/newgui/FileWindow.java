package ui.newgui;

import java.util.List;


/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id: EditorPane.java 137 2006-05-09 17:51:32Z benny $
 */
public interface FileWindow {  
  /**
   * Returns the {@link DefaultEditorAction}s for this editor pane.
   * 
   * @return the {@link DefaultEditorAction}s.
   */
  public List<EditorAction> getFileActions();
  
  public List<EditorAction> getToolbarActions();
  
  public List<EditorAction> getEditActions();
  
  public List<EditorAction> getRunActions();
  
  /**
   * Returns the title for this editor pane, i.e. <tt>"Source"</tt>
   * in case of the source pane.
   * 
   * @return the title for this editor pane.
   */
  public String getTitle();
}
