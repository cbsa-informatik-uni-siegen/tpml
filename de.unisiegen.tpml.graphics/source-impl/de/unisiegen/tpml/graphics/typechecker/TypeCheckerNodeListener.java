package de.unisiegen.tpml.graphics.typechecker;

import java.util.EventListener;

public interface TypeCheckerNodeListener extends EventListener {

	public void nodeChanged (TypeCheckerNodeComponent node);
	
	public void requestTypeEnter (TypeCheckerNodeComponent node);
	
	public void nodeGuessed ();
}
