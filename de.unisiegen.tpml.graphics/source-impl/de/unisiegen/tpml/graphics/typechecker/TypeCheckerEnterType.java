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

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.graphics.Messages;


/**
 * Component providing a label with a text enter field, where the User is able
 * to enter a Type, that will be used to proof a node using the
 * {@link TypeCheckerProofModel} The Component that will be displayed when the
 * user selectes Enter Type.
 * 
 * @author marcell
 */
public class TypeCheckerEnterType extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 63257034534910804L;


  /**
   * The Panel where everything is layed out
   */
  private JPanel panel;


  /**
   * The Textfield where the user is able to enter the type
   */
  private JTextField textField;


  /**
   * Label that informs the user what to do: "Enter type: "
   */
  private JLabel label;


  /**
   * The {@link TypeCheckerNodeComponent} can determine whether the
   * TypeCheckerEnterType-GUI is active. It will need the information to do a
   * propper layouting.
   */
  private boolean active;


  /**
   * The {@link ComponentAdapter} that will be used to determine when the
   * component gets shown. When the component gets shown a
   * {@link #requestTextFocus()} will be called.
   */
  private ComponentAdapter componentAdapter;


  public TypeCheckerEnterType ()
  {
    super ();

    setLayout ( new BorderLayout () );

    this.panel = new JPanel ();
    add ( this.panel, BorderLayout.CENTER );
    this.panel.setLayout ( new BorderLayout () );

    this.textField = new JTextField ();
    this.panel.add ( this.textField, BorderLayout.CENTER );

    this.label = new JLabel ( Messages.getString ( "TypeCheckerEnterType.0" ) ); //$NON-NLS-1$
    this.panel.add ( this.label, BorderLayout.WEST );
    this.panel.setBorder ( new BevelBorder ( BevelBorder.RAISED ) );

    // calc some space for the textField
    FontMetrics fm = getFontMetrics ( this.textField.getFont () );
    int width = fm.stringWidth ( "int -> int -> int -> int -> int" ); //$NON-NLS-1$

    Dimension size = getPreferredSize ();
    setPreferredSize ( new Dimension ( size.width + width, size.height ) );
    this.active = false;

    this.textField.addKeyListener ( new KeyAdapter ()
    {

      @Override
      public void keyReleased ( KeyEvent event )
      {
        TypeCheckerEnterType.this.keyReleased ( event );
      }
    } );

    this.textField.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent event )
      {
        TypeCheckerEnterType.this.actionPerformed ( event );
      }
    } );

    this.componentAdapter = new ComponentAdapter ()
    {

      @Override
      public void componentShown ( ComponentEvent event )
      {
        TypeCheckerEnterType.this.requestTextFocus ();
      }
    };
  }


  /**
   * Clears the text of the textField
   */
  public void clear ()
  {
    this.textField.setText ( "" ); //$NON-NLS-1$
  }


  /**
   * Selects all text in the textField.
   */
  public void selectAll ()
  {
    this.textField.selectAll ();
  }


  public void addTypeCheckerTypeEnterListener (
      TypeCheckerTypeEnterListener listener )
  {
    this.listenerList.add ( TypeCheckerTypeEnterListener.class, listener );
  }


  public void removeTypeCheckerTypeEnterListener (
      TypeCheckerTypeEnterListener listener )
  {
    this.listenerList.remove ( TypeCheckerTypeEnterListener.class, listener );
  }


  private void fireTypeEntered ( String type )
  {
    Object [] listeners = this.listenerList.getListenerList ();
    for ( int i = 0 ; i < listeners.length ; i++ )
    {
      if ( listeners [ i ] == TypeCheckerTypeEnterListener.class )
      {
        ( ( TypeCheckerTypeEnterListener ) listeners [ i + 1 ] )
            .typeEntered ( type );
      }
    }
  }


  private void fireCanceled ()
  {
    Object [] listeners = this.listenerList.getListenerList ();
    for ( int i = 0 ; i < listeners.length ; i++ )
    {
      if ( listeners [ i ] == TypeCheckerTypeEnterListener.class )
      {
        ( ( TypeCheckerTypeEnterListener ) listeners [ i + 1 ] ).canceled ();
      }
    }
  }


  /**
   * Sets this component active and adds the componentAdapter.
   * 
   * @param active
   */
  public void setActive ( boolean active )
  {
    this.active = active;

    if ( this.active )
    {
      addComponentListener ( this.componentAdapter );
    }
  }


  /**
   * Requests the keyboard focus for the TextField and removes the
   * componentAdapter from the Component.
   */
  public void requestTextFocus ()
  {
    this.textField.requestFocus ();
    removeComponentListener ( this.componentAdapter );
  }


  /**
   * Returns the {@link #active}-Flag
   * 
   * @return Returns the {@link #active}-Flag
   */
  public boolean isActive ()
  {
    return this.active;
  }


  /**
   * Checks whether the uses has released the "Escape" key.<br>
   * <br>
   * If so, the cancel event will be fired using the
   * {@link TypeCheckerTypeEnterListener#canceled()}
   * 
   * @param event The keyboard event from Java
   */
  private void keyReleased ( KeyEvent event )
  {
    if ( event.getKeyCode () == KeyEvent.VK_ESCAPE )
    {
      fireCanceled ();
    }
  }


  /**
   * Causes the {@link TypeCheckerTypeEnterListener#typeEntered(String)} to be
   * called with the type string entered.
   * 
   * @param event
   */
  private void actionPerformed ( ActionEvent event )
  {
    fireTypeEntered ( this.textField.getText () );
  }

}
