package de.unisiegen.tpml.ui;


import javax.swing.JOptionPane;


/**
 * This Class is only used by the Start.java. This class displays a normal
 * JOptionPane as Errormessage and exists the programm with errorcode 1. It is
 * neccesary to make an own class because of the compatibility to java-versions
 * less then 1.2. This file must be compild with source and target 1.2
 * 
 * @author Feivel
 * @version $Id$
 */
public class JOpFrame
{

  /**
   * displays the Optionpane as ERROR_MESSAGE
   * 
   * @param title the error titel
   * @param message the error message
   */
  public JOpFrame ( String title, String message )
  {
    JOptionPane.showMessageDialog ( null, message, title,
        JOptionPane.ERROR_MESSAGE );
    System.exit ( 1 );
  }
}
