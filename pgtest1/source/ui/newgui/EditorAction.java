package ui.newgui;

import java.awt.event.ActionListener;

import javax.swing.Icon;

import common.beans.Bean;

public interface EditorAction extends Bean {
	/**
	 * Returns <code>true</code> if the action is visible in the small
	 * menu within the editorwindow
	 * @return true if visible
	 */
	public boolean isVisible();
  /**
   * Returns <code>true</code> if the action can be performed, that is,
   * if the action widget should be sensitive.
   * 
   * @return <code>true</code> if enabled.
   */
  public boolean isEnabled();
  
  /**
   * Returns the grouping id of this action. Actions with similar group
   * ids should be displayed together, while spacing should be inserted
   * between actions with different group ids.
   * 
   * @return the grouping id.
   */
  public int getGroup();
  
  /**
   * Returns the {@link Icon} for this action.
   * 
   * @return the {@link Icon} for this action.
   */
  public Icon getIcon();
  
  /**
   * Returns the title (or tooltip) for this action.
   *
   * @return the title (or tooltip) for this action.
   */
  public String getTitle();
  /**
   * Returns the key to hold for shortcuts. Example:
   *  Ctrl + X, this functions would define Ctrl.
   * @return the key to be held.
   */
  public int getAccelModifiers();
  /**
   * Returns the key to be pressed for shortcuts. Example:
   *  Ctrl + X, this would define the X. If no keyHeld is
   *  defined only this key has to be pressed to trigger the
   *  actio. Example: F12
   * @return the key to be pressed.
   */
  public int getAccelKey();

  /**
   * FIXME
   */
  public ActionListener getActionListener();
}
