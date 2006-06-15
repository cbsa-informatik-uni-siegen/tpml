package ui.newgui;

import java.awt.event.ActionListener;

import javax.swing.Icon;

import common.beans.Bean;

public interface EditorAction extends Bean {
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
   * FIXME
   */
  public ActionListener getActionListener();
}
