package de.unisiegen.tpml.ui;

import javax.swing.UIManager;

/**
 * The main starter class for the TPML project.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.ui.MainWindow
 */
public final class Main {
	/**
	 * The main entry point for the TPML project, which allocates and runs a new {@link MainWindow}.
	 * 
	 * This method also sets up native look and feel for the platform if possible.
	 * 
	 * @param args the command line arguments.
	 * 
	 * @see MainWindow
	 */
	public static void main(String args[]) {
		try {
			// try to setup native look and feel for the platform
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			// just ignore the exception here
		}
		
		// run a new MainWindow
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainWindow().setVisible(true);
			}
		});
	}
}
