package de.unisiegen.tpml.ui;

import javax.swing.JOptionPane;

public class JOpFrame
{
 public JOpFrame(String title, String message)
 {
  JOptionPane jpP = new JOptionPane();
	jpP.showMessageDialog(null, message, title, jpP.ERROR_MESSAGE);
	System.exit(1);
 }
}
