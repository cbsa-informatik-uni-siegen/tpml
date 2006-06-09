package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import ui.newgui.EditorComponent;
import ui.smallstep.SmallStepGUI;

public class EditorComponentListener implements ActionListener {

	EditorWindow panel;
	EditorComponent mycomponent;
	JToggleButton mybutton;
	public EditorComponentListener (EditorWindow ipanel, EditorComponent gui, JToggleButton imybutton)
	{
		panel=ipanel;
		mycomponent = gui;
		mybutton = imybutton;
		//TODO clean this up...
	}
	
	public void actionPerformed(ActionEvent e) {
		panel.remove(panel.getShownComponent());
		panel.add(mycomponent.getDisplay(), BorderLayout.CENTER);
		panel.setShownComponent(mycomponent.getDisplay());
		panel.deselectButtons();
		mybutton.setSelected(true);		
		panel.repaint();
		panel.updateActions(mycomponent);
}

}
