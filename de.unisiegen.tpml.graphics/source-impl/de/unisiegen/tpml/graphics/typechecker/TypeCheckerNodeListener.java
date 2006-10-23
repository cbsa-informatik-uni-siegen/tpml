package de.unisiegen.tpml.graphics.typechecker;

import java.util.EventListener;

public interface TypeCheckerNodeListener extends EventListener {

	/**
	 * Called when the given node has changed.
	 * 
	 * @param node The node that has changed.
	 */
	public void nodeChanged (TypeCheckerNodeComponent node);
	
	/**
	 * Called when the user has selected the "enter type" from the
	 * menu.
	 * @param node
	 */
	public void requestTypeEnter (TypeCheckerNodeComponent node);
}
