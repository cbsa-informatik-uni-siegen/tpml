package ui;

import java.util.EventListener;
import java.util.EventObject;

public interface DialogListener extends EventListener {
	
	public void dialogOk(EventObject o);
	
	public void dialogCancel(EventObject o);

}
