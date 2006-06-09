package ui.newgui;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id: EditorAction.java 137 2006-05-09 17:51:32Z benny $
 */
public class DefaultEditorAction implements EditorAction{
	private String title;
	private int group;
	private Icon icon;
	private boolean enabled;
	private ActionListener actionlistener;
  public ActionListener getActionlistener() {
		return actionlistener;
	}

	public void setActionlistener(ActionListener actionlistener) {
		this.actionlistener = actionlistener;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public void setTitle(String title) {
		this.title = title;
	}

/**
   * Adds a <code>PropertyChangeListener</code> for the properties
   * of this action.
   * 
   * @param listener the <code>PropertyChangeListener</code> to add.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener){};
  
  /**
   * Removes a <code>PropertyChangeListener</code> for the properties
   * of this action.
   *  
   * @param listener the <code>PropertyChangeListener</code> to remove.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener){};
  
  /**
   * Returns <code>true</code> if the action can be performed, that is,
   * if the action widget should be sensitive.
   * 
   * @return <code>true</code> if enabled.
   */
  public boolean isEnabled(){
	  return enabled;
  };

  /**
   * Returns the grouping id of this action. Actions with similar group
   * ids should be displayed together, while spacing should be inserted
   * between actions with different group ids.
   * 
   * @return the grouping id.
   */
  public int getGroup(){return group;};
  
  /**
   * Returns the {@link Icon} for this action.
   * 
   * @return the {@link Icon} for this action.
   */
  public Icon getIcon(){
	  return icon;
  };
  
  /**
   * Returns the title (or tooltip) for this action.
   *
   * @return the title (or tooltip) for this action.
   */
  public String getTitle(){return title;}

public ActionListener getActionListener() {
	return actionlistener;
};
}
