package de.unisiegen.tpml.graphics.subtyping;

import java.util.EventListener;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.graphics.bigstep.BigStepNodeComponent;

public interface SubTypingNodeListener extends EventListener
{

	/**
	 * Called when ever the node changed and the layout needs to
	 * get updated. 
	 *  	 
	 * @param node
	 */
	public void nodeChanged (SubTypingNodeComponent node);
	
	/**
	 * Callend when an error occured during guess or an guess tree.
	 * 
	 * @param node The node where the component should jumpt to
	 */
	public void requestJumpToNode (ProofNode node);


}
