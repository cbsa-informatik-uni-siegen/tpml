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
	 * TODO Add documentation here.
	 * 
	 * @param source
	 * @param item
	 */
	public void menuItemActivated (MenuButton source, JMenuItem item);
	
	/**
	 * TODO Add documentation here.
	 * 
	 * @param source
	 */
	public void menuClosed (MenuButton source);
}
