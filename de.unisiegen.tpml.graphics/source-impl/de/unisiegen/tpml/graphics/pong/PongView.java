/**
 * 
 */
package de.unisiegen.tpml.graphics.pong;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * @author marcell
 *
 */
public class PongView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 83888488905679466L;
	
	
	private Pong	pong;
	
	public PongView () {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout (new BorderLayout());
		
		this.pong = new Pong ();
		add (this.pong, BorderLayout.CENTER);
	
		setSize (640, 480);
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed (WindowEvent event) {
				System.exit (0);
			}
		});
	}
	
}
