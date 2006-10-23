package de.unisiegen.tpml.graphics.smallstep;

import java.util.EventListener;

public interface SmallStepNodeListener extends EventListener {

	/**
	 * Called when a node has changed.
	 * 
	 * @param node The node that has changed.
	 */
	public void nodeChanged (SmallStepNodeComponent node);
	
	/**
	 * Called when a entire repaint is requested.
	 *
	 */
	public void repaintAll ();
}
