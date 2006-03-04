package ui;

import java.util.EventListener;
import java.util.EventObject;

public interface SmallStepEventListener extends EventListener {

	public void contentsChanged(EventObject o);
	
	public void stepEvaluated(EventObject o);
	
}

