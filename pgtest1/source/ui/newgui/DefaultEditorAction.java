package ui.newgui;

import java.awt.event.ActionListener;

import javax.swing.Icon;

import common.beans.AbstractBean;

/**
 * TODO Add documentation here.
 * 
 * @author Benedikt Meurer
 * @version $Id: EditorAction.java 137 2006-05-09 17:51:32Z benny $
 */
public class DefaultEditorAction extends AbstractBean implements EditorAction {
	//
	// Attributes
	//

	private boolean visible;
	
	private String title;

	private int group;

	private Icon icon;

	private boolean enabled;

	private ActionListener actionlistener;
	
	private int accelModifiers;
	
	private int accelKey;

	//
	// Primitives
	//

	public ActionListener getActionlistener() {
		return actionlistener;
	}

	public void setActionlistener(ActionListener actionlistener) {
		ActionListener oldActionlistener = this.actionlistener;
		this.actionlistener = actionlistener;
		firePropertyChange("actionlistener", oldActionlistener, actionlistener);
	}

	public void setEnabled(boolean enabled) {
		boolean oldEnabled = this.enabled;
		this.enabled = enabled;
		firePropertyChange("enabled", oldEnabled, enabled);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setGroup(int group) {
		int oldGroup = this.group;
		this.group = group;
		firePropertyChange("group", oldGroup, group);
	}

	public void setIcon(Icon icon) {
		Icon oldIcon = this.icon;
		this.icon = icon;
		firePropertyChange("icon", oldIcon, icon);
	}

	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		firePropertyChange("title", oldTitle, title);
	}

	/**
	 * Returns <code>true</code> if the action can be performed, that is, if
	 * the action widget should be sensitive.
	 * 
	 * @return <code>true</code> if enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	};
	

	
	public void setAccelModifiers(int keyHeld) {
		this.accelModifiers = keyHeld;
	}

	public void setAccelKey(int keyPressed) {
		this.accelKey = keyPressed;
	}

	/**
	 * Returns the grouping id of this action. Actions with similar group ids
	 * should be displayed together, while spacing should be inserted between
	 * actions with different group ids.
	 * 
	 * @return the grouping id.
	 */
	public int getGroup() {
		return group;
	};

	/**
	 * Returns the {@link Icon} for this action.
	 * 
	 * @return the {@link Icon} for this action.
	 */
	public Icon getIcon() {
		return icon;
	};

	/**
	 * Returns the title (or tooltip) for this action.
	 * 
	 * @return the title (or tooltip) for this action.
	 */
	public String getTitle() {
		return title;
	}

	public ActionListener getActionListener() {
		return actionlistener;
	}

	public int getAccelModifiers() {
		return accelModifiers;
	}

	public int getAccelKey() {
		return accelKey;
	};
}
