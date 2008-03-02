package de.unisiegen.tpml.graphics.components;


import javax.swing.JMenuItem;

import de.unisiegen.tpml.graphics.Messages;


/**
 * An item that can be added to the menu of the MenuButton.<br>
 * This item provides the entry text to translate the expression into core
 * syntax.<br>
 * 
 * @author marcell
 */

public class MenuTranslateItem extends JMenuItem
{

  /**
   * 
   */
  private static final long serialVersionUID = -874935881714839506L;


  /**
   * TODO
   */
  public MenuTranslateItem ()
  {
    super ();
    init ( Messages.getString ( "MenuTranslateItem.0" ), null ); //$NON-NLS-1$
  }
}
