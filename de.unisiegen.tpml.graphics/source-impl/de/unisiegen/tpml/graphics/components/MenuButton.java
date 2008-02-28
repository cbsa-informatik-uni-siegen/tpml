package de.unisiegen.tpml.graphics.components;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class MenuButton extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 3324315568161015639L;


  /**
   * A popup menu that will be displayed when the MenuButton is opend.
   */
  private JPopupMenu menu = null;


  /**
   * The action listener.
   */
  private ActionListener listener = null;


  /**
   * The text that will be displayed.
   */
  private String text;


  /**
   * The color that will be used to render the text.
   */
  private Color textColor;


  /**
   * The <i>dimension</i> is the width and height of the boxed arrow drawn by
   * the MenuButton.
   */
  private int dimension;


  public MenuButton ()
  {

    // set the default font... it will be the font from a combo box
    setFont ( new JComboBox ().getFont () );

    // the dimension of the drawn icon will be calculated by the height
    // of the text, that would be used in an JComboBox

    FontMetrics fm = getFontMetrics ( getFont () );
    this.dimension = fm.getHeight ();

    // initially the text will be empty
    this.text = "";
    this.textColor = Color.BLACK;

    /*
     * create the listener that will be connected to every element that is in
     * the menu item tree.
     */
    this.listener = new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        // just delegate the event to a method of the surrounding class.
        MenuButton.this.fireMenuItemActivated ( ( JMenuItem ) event
            .getSource () );
      }
    };

    this.addMouseListener ( new MouseAdapter ()
    {

      @Override
      public void mousePressed ( MouseEvent event )
      {
        MenuButton.this.handleMousePressed ( event );
      }
    } );

  }


  public void addMenuButtonListener ( MenuButtonListener listener )
  {
    this.listenerList.add ( MenuButtonListener.class, listener );
  }


  public void removeMenuButtonListener ( MenuButtonListener listener )
  {
    this.listenerList.remove ( MenuButtonListener.class, listener );
  }


  /**
   * Sets the Popupmenu for the MenuButton.
   * 
   * @param menu
   */
  public void setMenu ( JPopupMenu menu )
  {
    if ( this.menu != null )
    {
      uninstallMenuEventListener ( this.menu );
    }
    this.menu = menu;

    // the application needs to know when the menu is closed
    this.menu.addPopupMenuListener ( new PopupMenuListener ()
    {

      public void popupMenuCanceled ( PopupMenuEvent event )
      {
      }


      public void popupMenuWillBecomeVisible ( PopupMenuEvent event )
      {
      }


      public void popupMenuWillBecomeInvisible ( PopupMenuEvent event )
      {
        MenuButton.this.fireMenuClosed ();
      }
    } );

    installMenuEventListener ( this.menu );
  }


  /**
   * Sets the text that should be shown.
   * 
   * @param text
   */
  public void setText ( String text )
  {
    this.text = text;
  }


  /**
   * Sets the color
   * 
   * @param color
   */
  public void setTextColor ( Color color )
  {
    this.textColor = color;
  }


  /**
   * Calculates and returns the dimension this menu button needs to do a propper
   * rendering.
   * 
   * @return The needed size for this MenuButton
   */
  public Dimension getNeededSize ()
  {
    Dimension result = new Dimension ( 0, 0 );

    // normaly there is no text in the button so only the
    // sizes of the boxed arrow would be of interest.

    result.width = this.dimension;
    result.height = this.dimension;

    if ( this.text != null && this.text.length () != 0 )
    {

      FontMetrics fm = getFontMetrics ( getFont () );

      // when text should be visible, add the half dimension as
      // an additional size and add the size of the text
      result.width += this.dimension / 2;
      result.width += fm.stringWidth ( this.text );

      // check whether the text of the dimension for the
      // boxed arrow is bigger
      result.height = Math.max ( result.height, fm.getHeight () );
    }

    return result;
  }


  /**
   * Paints the component
   * 
   * @param gc The graphic context
   */
  @Override
  protected void paintComponent ( Graphics gc )
  {

    // find the vertical centering position of menubutton
    int centerV = getHeight () / 2;

    int posX = 0;
    int posY = centerV - this.dimension / 2;

    // render the boxed around the arrow at the front of the component
    gc.setColor ( Color.LIGHT_GRAY );
    gc.drawRect ( posX, posY, this.dimension - 1, this.dimension - 1 );

    // draw the arrow within the box
    Polygon polygon = new Polygon ();
    polygon.addPoint ( posX + this.dimension * 3 / 12, posY + this.dimension
        * 3 / 12 );
    polygon.addPoint ( posX + this.dimension * 9 / 12, posY + this.dimension
        * 3 / 12 );
    polygon.addPoint ( posX + this.dimension / 2, posY + this.dimension * 9
        / 12 );
    gc.fillPolygon ( polygon );

    if ( this.text != null && this.text.length () != 0 )
    {
      FontMetrics fm = getFontMetrics ( getFont () );
      posX += this.dimension + this.dimension / 2;
      posY = centerV + fm.getAscent () / 3;
      gc.setFont ( getFont () );
      gc.setColor ( this.textColor );
      gc.drawString ( this.text, posX, posY );
    }
  }


  /*
   * Implementation of the event handling of the menu item tree
   */

  /**
   * Handle the mousePress event
   * 
   * @param event
   */
  private void handleMousePressed ( MouseEvent event )
  {
    if ( this.menu == null )
    {
      return;
    }

    this.menu.show ( this, event.getX (), event.getY () );
  }


  /**
   * Fires the menuItemActivated function from MenuButtonListener.
   * 
   * @param item the item that has been activated
   */
  private void fireMenuItemActivated ( JMenuItem item )
  {
    Object [] listeners = this.listenerList.getListenerList ();
    for ( int i = 0 ; i < listeners.length ; i += 2 )
    {
      // find the correct listener object
      if ( listeners [ i ] != MenuButtonListener.class )
      {
        continue;
      }

      // perform the listener
      ( ( MenuButtonListener ) listeners [ i + 1 ] ).menuItemActivated ( this,
          item );

    }
  }


  /**
   * Fires the menuClosed function from MenuButtonListener
   */
  private void fireMenuClosed ()
  {
    Object [] listeners = this.listenerList.getListenerList ();
    for ( int i = 0 ; i < listeners.length ; i += 2 )
    {
      // find the correct listener object
      if ( listeners [ i ] != MenuButtonListener.class )
      {
        continue;
      }

      // perform the listener
      ( ( MenuButtonListener ) listeners [ i + 1 ] ).menuClosed ( this );

    }
  }


  /**
   * Removes all previously install menu item listeners from every JMenuItem
   * within the menu tree.
   * 
   * @param element The element which listeners should be removed
   */
  private void uninstallMenuEventListener ( MenuElement element )
  {

    if ( element instanceof JMenuItem )
    {

      // if this is just a leaf object remove the listener
      JMenuItem item = ( JMenuItem ) element;

      item.removeActionListener ( this.listener );

    }
    else
    {

      // if this is a submenuitem proceed with all children elements
      MenuElement [] subElements = element.getSubElements ();
      for ( MenuElement e : subElements )
      {
        uninstallMenuEventListener ( e );
      }
    }
  }


  /**
   * Installs a menu item listener to every JMenuItem within the menu tree.
   * 
   * @param element The element where a listener should be installed.
   */
  private void installMenuEventListener ( MenuElement element )
  {
    if ( element instanceof JMenuItem )
    {

      // if this is just a leaf object remove the listener
      JMenuItem item = ( JMenuItem ) element;

      item.addActionListener ( this.listener );

    }
    else
    {

      // if this is a submenuitem proceed with all children elements
      MenuElement [] subElements = element.getSubElements ();
      for ( MenuElement e : subElements )
      {
        installMenuEventListener ( e );
      }
    }
  }
}
