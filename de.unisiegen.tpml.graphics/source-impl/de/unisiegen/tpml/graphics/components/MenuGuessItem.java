package de.unisiegen.tpml.graphics.components;


import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;


/**
 * An item that can be added to the menu of the MenuButton.<br>
 * This item provides the entry text to guess the node.<br>
 * 
 * @author marcell
 * @version $Id$
 */
public class MenuGuessItem extends JMenuItem
{

  /**
   * 
   */
  private static final long serialVersionUID = -1526168055763966007L;


  /**
   * TODO
   */
  public MenuGuessItem ()
  {
    super ();

    init ( Messages.getString ( "MenuGuessItem.0" ), null ); //$NON-NLS-1$
  }
}
