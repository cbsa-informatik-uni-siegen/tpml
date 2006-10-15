package de.unisiegen.tpml.graphics.components;

import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;

public class MenuGuessItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1526168055763966007L;

	public MenuGuessItem () {
		super ();
		
		init (Messages.getString("MenuGuessItem.0"), null); //$NON-NLS-1$
	}
}
