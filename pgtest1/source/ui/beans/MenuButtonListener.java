package ui.beans;

import java.util.EventListener;

import javax.swing.JMenuItem;

public interface MenuButtonListener extends EventListener {

	public void menuItemActivated (MenuButton source, JMenuItem item); 
}
