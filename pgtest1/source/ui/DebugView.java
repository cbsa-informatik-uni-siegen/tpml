package ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DebugView extends JDialog {

	
	public DebugView(JFrame owner, String title, boolean modal, JComponent component) {
		super(owner, title, modal);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout ());
		this.getContentPane().add(component);
	}
}
