package de.unisiegen.tpml.graphics.components;

import java.util.EventListener;

import javax.swing.JMenuItem;

public interface MenuButtonListener extends EventListener {

	public void menuItemActivated (MenuButton source, JMenuItem item);
	
	public void menuClosed (MenuButton source);
}
