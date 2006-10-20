package de.unisiegen.tpml.graphics.smallstep;

import java.util.EventListener;

public interface SmallStepNodeListener extends EventListener {

	public void nodeChanged (SmallStepNodeComponent node);
	
	public void repaintAll ();
	
	public void nodeGuessed ();
}
