/**
 * 
 */
package de.unisiegen.tpml.graphics.typechecker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * @author marcell
 *
 */
public class TypeCheckerEnterType extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 63257034534910804L;

	private JPanel						panel;
	
	private JTextField				textField;
	
	private JLabel						label;
	
	private boolean						active;
	
	private ComponentAdapter	componentAdapter;
	
	public TypeCheckerEnterType () {
		super ();
	
		setLayout (new BorderLayout());
		
		this.panel = new JPanel();
		add (this.panel, BorderLayout.CENTER);
		this.panel.setLayout(new BorderLayout ());
		
		
		this.textField = new JTextField ();
		this.panel.add (this.textField, BorderLayout.CENTER);
		
		this.label = new JLabel ("Type: ");
		this.panel.add(this.label, BorderLayout.WEST);
		this.panel.setBorder(new BevelBorder (BevelBorder.RAISED));

		
		// calc some space for the textField
		FontMetrics fm = getFontMetrics(this.textField.getFont());
		int width = fm.stringWidth("int -> int -> int -> int -> int");
		
		Dimension size = getPreferredSize ();
		setPreferredSize (new Dimension (size.width + width, size.height));
		this.active = false;
		
		this.textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased (KeyEvent event) {
				TypeCheckerEnterType.this.keyReleased(event);
			}
		});
		
		this.textField.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent event) {
				TypeCheckerEnterType.this.actionPerformed (event);
			}
		});
		
		
		this.componentAdapter = new ComponentAdapter() {
			@Override
			public void componentShown (ComponentEvent event) {
				TypeCheckerEnterType.this.requestTextFocus ();
			}
		};
	}
	
	public void clear () {
		this.textField.setText("");
	}
	
	public void selectAll() {
		this.textField.selectAll();
	}
	
	public void addTypeCheckerTypeEnterListener (TypeCheckerTypeEnterListener listener) {
		this.listenerList.add(TypeCheckerTypeEnterListener.class, listener);
	}
	
	public void removeTypeCheckerTypeEnterListener (TypeCheckerTypeEnterListener listener) {
		this.listenerList.remove(TypeCheckerTypeEnterListener.class, listener);
	}
	
	private void fireTypeEntered (String type) {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i=0; i<listeners.length; i++) {
			if (listeners[i] == TypeCheckerTypeEnterListener.class) {
				((TypeCheckerTypeEnterListener)listeners[i+1]).typeEntered(type);
			}
		}
	}
	
	private void fireCanceled () {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i=0; i<listeners.length; i++) {
			if (listeners[i] == TypeCheckerTypeEnterListener.class) {
				((TypeCheckerTypeEnterListener)listeners[i+1]).canceled();
			}
		}
	}
		
	public void setActive (boolean active) {
		this.active = active;
		
		if (this.active) {
			addComponentListener(this.componentAdapter);
		}
	}
	
	public void requestTextFocus () {
		this.textField.requestFocus();
		removeComponentListener(this.componentAdapter);
	}
	
	public boolean isActive () {
		return this.active;
	}
	
	private void keyReleased (KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			fireCanceled ();
		}
	}
	
	private void actionPerformed (ActionEvent event) {
		fireTypeEntered (this.textField.getText());
	}
	
}


