package de.unisiegen.tpml.graphics.components;


import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;


/**
 * An item that can be added to the menu of the MenuButton.<br>
 * This item provides the entry text to enter a type.<br>
 * This one is only used within the type checker.
 * 
 * @author marcell
 * @version $Id$
 */
public class MenuEnterTypeItem extends JMenuItem
{

  /**
   * 
   */
  private static final long serialVersionUID = -5464136166948202537L;


  /**
   * TODO
   */
  public MenuEnterTypeItem ()
  {
    super ();

    init ( Messages.getString ( "MenuEnterTypeItem.0" ), null ); //$NON-NLS-1$
  }

}
