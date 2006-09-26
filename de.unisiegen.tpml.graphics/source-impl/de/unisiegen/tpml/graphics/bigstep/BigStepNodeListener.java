package de.unisiegen.tpml.graphics.bigstep;

import java.util.EventListener;

public interface BigStepNodeListener extends EventListener {

	public void nodeChanged (BigStepNodeComponent node);
	
}
