package de.unisiegen.tpml.graphics.pong;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.unisiegen.tpml.graphics.Messages;

/**
 * TODO Add documentation here.
 * 
 * @version $Rev$
 * @author Marcell Fischbach
 *
 * @see de.unisiegen.tpml.graphics.pong.Pong
 */
public class PongView extends JDialog {
	//
	// Constants
	//
	
	/**
	 * The unique serialization identifier for this class.
	 */
	private static final long serialVersionUID = 83888488905679466L;
	
	
	
	//
	// Constructor
	//
	
	/**
	 * TODO Add documentation here.
	 * 
	 * @param owner
	 * 
	 * @throws NullPointerException if <code>owner</code> is <code>null</code>.
	 */
	public PongView (Frame owner) {
		super(owner, true);
		
		if (owner == null) {
			throw new NullPointerException("owner is null"); //$NON-NLS-1$
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JPanel pongPanel = new JPanel(new BorderLayout());
		pongPanel.add(new Pong(), BorderLayout.CENTER);
		pongPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		add(pongPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
		add(buttonPanel, BorderLayout.SOUTH);
		
		JButton closeButton = new JButton(Messages.getString("PongView.0")); //$NON-NLS-1$
		closeButton.setMnemonic(Messages.getString("PongView.1").charAt(0)); //$NON-NLS-1$
		buttonPanel.add(closeButton);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	
		setTitle("Pong"); //$NON-NLS-1$
		setSize(640, 480);
		setLocationRelativeTo(owner);
	}
}
