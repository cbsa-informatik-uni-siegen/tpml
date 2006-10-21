package de.unisiegen.tpml.graphics.bigstep;

import java.util.EventListener;

/**
 * 
 * @author marcell
 *
 */
public interface BigStepNodeListener extends EventListener {

	/**
	 * Called when ever the node changed and the layout needs to
	 * get updated. 
	 *  	 
	 * @param node
	 */
	public void nodeChanged (BigStepNodeComponent node);
}
