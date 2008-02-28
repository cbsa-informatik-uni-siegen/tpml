package de.unisiegen.tpml.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Michael Oeste
 * this class will be used instead of JOptionPane(); because of the compatibility to old versions
 * This class should be compiled with target 1.1                   
 */
public class MsgFrame extends Dialog implements ActionListener
{
	/**
	 * displays a simpel message like a JOptionPane. After OK is pressed the programm will exit with errorcode 1
	 *
	 * @param titel		the titel of the window
	 * @param message	the message that should displayed
	 */
	public MsgFrame (String titel, String message)
	{
		super(new Frame(), true);
		this.setTitle(titel);
		setLayout(new BorderLayout());
		setBackground(Color.lightGray);
		

		// Label:
		Label msg = new Label(message);
		msg.setBackground(Color.lightGray);
		add("North", msg);
		msg.setAlignment(Label.CENTER);

		Panel panelButton = new Panel();

		// CloseButton
		Button closeButton = new Button("Close");
		closeButton.addActionListener(this);

		panelButton.add(closeButton);

		add("South", panelButton);

		// enable close button:
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				dispose();
				System.exit(1);
			}
		});
		pack();
		//find the middel of screen
		Dimension dimFrame = getSize();
		Dimension dimSystem = Toolkit.getDefaultToolkit().getScreenSize();
		int y = (dimSystem.height - dimFrame.height) /2;
		int x = (dimSystem.width - dimFrame.width) /2;
		setLocation(x,y);
		
		
		// to be compatible with early java versions
		// setVisible(true);
		show();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Close"))
		{
			dispose();
			System.exit(1);
		}
	}
}
