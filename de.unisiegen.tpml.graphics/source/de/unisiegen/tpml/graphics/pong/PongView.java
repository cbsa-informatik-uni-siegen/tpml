/**
 * 
 */
package de.unisiegen.tpml.graphics.pong;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;

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
	// Attributes
	//
	
	/**
	 * The {@link Pong} component.
	 * 
	 * @see Pong
	 */
	private Pong pong;
	
	
	
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
			throw new NullPointerException("owner is null");
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout (new BorderLayout());
		
		this.pong = new Pong ();
		add (this.pong, BorderLayout.CENTER);
	
		setSize (640, 480);
		setLocationRelativeTo(owner);
	}
}
