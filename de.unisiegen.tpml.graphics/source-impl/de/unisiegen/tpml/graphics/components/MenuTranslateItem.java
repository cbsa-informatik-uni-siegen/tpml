package de.unisiegen.tpml.graphics.components;

import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;

public class MenuTranslateItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -874935881714839506L;

	public MenuTranslateItem () {
		super ();
		init (Messages.getString("MenuTranslateItem.0"), null); //$NON-NLS-1$
	}
}
