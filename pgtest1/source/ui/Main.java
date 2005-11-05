package ui;

import java.awt.event.*;
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mainwindow mw = new Mainwindow();
		mw.addWindowListener(new WindowAdapter () {
			public void windowClosing(WindowEvent e) { System.exit(0); };
		});
		mw.setVisible(true);
	}

}
