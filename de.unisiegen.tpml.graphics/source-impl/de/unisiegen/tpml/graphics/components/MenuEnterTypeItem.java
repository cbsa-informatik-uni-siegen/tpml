package de.unisiegen.tpml.graphics.components;

import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;

public class MenuEnterTypeItem extends JMenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5464136166948202537L;

	public MenuEnterTypeItem () {
		super ();
		
		init (Messages.getString("MenuEnterTypeItem.0"), null); //$NON-NLS-1$
	}

}
