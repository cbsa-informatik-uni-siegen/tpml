package de.unisiegen.tpml.graphics.components;

import java.util.EventListener;

import javax.swing.JMenuItem;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.graphics.components.MenuButton
 */
public interface MenuButtonListener extends EventListener {
	/**
	 * Called when an item from the menu was selected.
	 * 
	 * @param source The menu button who caused the event
	 * @param item   The item that was selected
	 */
	public void menuItemActivated (MenuButton source, JMenuItem item);
	
	/**
	 * Called when the menu was closed.
	 * 
	 * @param source The menu button who caused the event.
	 */
	public void menuClosed (MenuButton source);
}
