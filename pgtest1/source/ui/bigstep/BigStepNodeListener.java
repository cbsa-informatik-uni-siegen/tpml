package ui.bigstep;

import java.util.EventListener;
import common.ProofNode;

public interface BigStepNodeListener extends EventListener{

	public void aboutToProve (ProofNode node);
	
}
