package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SmallStepListener implements ActionListener {

	Mainwindow mywindow;
	EditorWindow panel;
	SmallStepGUI mysmallstep;
	JButton mybutton;
	public SmallStepListener (EditorWindow ipanel, SmallStepGUI igui, JButton imybutton)
	{
		panel=ipanel;
		mysmallstep = igui;
		mybutton = imybutton;
		//TODO clean this up...
		(mysmallstep.getButtonClose()).addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
	String event = e.getActionCommand();
	if (event.equals("SmallStep")){
		panel.remove(panel.getShownComponent());
		panel.add(mysmallstep, BorderLayout.CENTER);
		panel.setShownComponent(mysmallstep);
		panel.repaint();
	}
	if (event.equals("Close")){
		panel.getMenu().remove(mybutton);
		panel.remove(mysmallstep);
		panel.repaint();
		//panel.removeShownComponent();
	}
}

}
