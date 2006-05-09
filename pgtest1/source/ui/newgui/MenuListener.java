package ui.newgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuListener implements ActionListener {
	private MainWindow window; 

	public MenuListener( MainWindow iwindow){
		window = iwindow;
	}
	
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "New")
          window.handleNew();
        else if (e.getActionCommand() == "Save")
          window.handleSave();
        else if (e.getActionCommand() == "Save as")
          window.handleSaveAs();
        else if (e.getActionCommand() == "Close")
          window.handleClose();
        else if (e.getActionCommand() == "Exit")
          window.handleExit();       
        else if (e.getActionCommand() == "SmallStep")
              window.handleSmallStep();
        
      }

}
