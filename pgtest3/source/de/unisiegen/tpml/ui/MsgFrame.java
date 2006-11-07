package de.unisiegen.tpml.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
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
 * @author Feivel
 * this class will be used instead of JOptionPane(); because of the compatibility to old versions
 * you can give a titel and the message as strings with constructor
 * if the button ist pushed the programm will be exit                   
 */
public class MsgFrame extends Frame implements ActionListener
{
	public MsgFrame (String titel, String message)
	{
		super(titel);
		setLayout(new BorderLayout());
		setBackground(Color.lightGray);
		
		//TODO zentrieren
		// setSize(100,80);

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
				System.exit(0);
			}
		});
		pack();
		//find the middel of screen
		Dimension dimFrame = getSize();
		Dimension dimSystem = Toolkit.getDefaultToolkit().getScreenSize();
		int y = (dimSystem.height - dimFrame.height) /2;
		int x = (dimSystem.width - dimFrame.width) /2;
		setLocation(x,y);
		
		
		//TODO Muss noch gekärt werden, warum show() nicht verwendet werden sollte.
		show();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Close"))
		{
			dispose();
			System.exit(0);
		}
	}
}
