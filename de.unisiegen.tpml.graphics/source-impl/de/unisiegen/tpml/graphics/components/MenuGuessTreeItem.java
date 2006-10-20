package de.unisiegen.tpml.graphics.components;

import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.graphics.components.MenuGuessItem
 */
public final class MenuGuessTreeItem extends JMenuItem {
	//
	// Constants
	//

	/**
	 * The unique serialization identifier for this class.
	 */
	private static final long serialVersionUID = -358409782100968334L;
	
	
	
	//
	// Constructor
	//
	
	/**
	 * Allocates a new <code>MenuGuessTreeItem</code> instance.
	 */
	public MenuGuessTreeItem() {
		super(Messages.getString("MenuGuessTreeItem.0")); //$NON-NLS-1$
	}
}
