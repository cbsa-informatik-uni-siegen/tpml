package ui;

import java.util.EventListener;
import java.util.EventObject;

public interface SmallStepEventListener extends EventListener {

	public void smallStepResized(EventObject o);
	
	public void smallStepResolved(EventObject o);
	
	public void mouseFocusEvent(SmallStepEvent e);
}
