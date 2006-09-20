import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class TestDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8927517104220650542L;
	private JButton		undoButton;
	private JButton		redoButton;
	private JButton		guess;
	

	public TestDialog () {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	
		setLayout (new BorderLayout());
		
		JPanel panel = new JPanel ();
		panel.setLayout(new FlowLayout ());
		
		this.undoButton = new JButton("Undo");
		this.redoButton = new JButton("Redo");
		this.guess = new JButton ("Guess");

		panel.add (this.undoButton);
		panel.add (this.redoButton);
		panel.add (this.guess);

		this.add(panel, BorderLayout.SOUTH);
		
	}
	
	public void setContent (JComponent content) {
		this.add(content, BorderLayout.CENTER);
		
	}
}
