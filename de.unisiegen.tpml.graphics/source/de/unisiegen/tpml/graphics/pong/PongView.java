package de.unisiegen.tpml.graphics.pong;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;

import de.unisiegen.tpml.graphics.Messages;


/**
 * no dokumentation for this stuff
 * 
 * @author Marcell Fischbach
 * @author Michael
 * @version $Id$
 * @see de.unisiegen.tpml.graphics.pong.Pong
 */
public class PongView extends JWindow
{

  //
  // Constants
  //

  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 83888488905679466L;


  //
  // Constructor
  //

  /**
   * @param owner
   * @throws NullPointerException if <code>owner</code> is <code>null</code>.
   */
  public PongView ( Frame owner )
  {
    // super(owner, true);
    super ( owner );

    if ( owner == null )
    {
      throw new NullPointerException ( "owner is null" ); //$NON-NLS-1$
    }

    // setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout ( new BorderLayout () );

    JPanel pongPanel = new JPanel ( new BorderLayout () );
    pongPanel.add ( new Pong ( this ), BorderLayout.CENTER );
    pongPanel.setBorder ( BorderFactory.createLoweredBevelBorder () );
    add ( pongPanel, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 6, 6 ) );
    add ( buttonPanel, BorderLayout.SOUTH );

    JButton closeButton = new JButton ( Messages.getString ( "PongView.0" ) ); //$NON-NLS-1$
    closeButton.setMnemonic ( Messages.getString ( "PongView.1" ).charAt ( 0 ) ); //$NON-NLS-1$
    buttonPanel.add ( closeButton );
    closeButton.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( @SuppressWarnings ( "unused" )
      ActionEvent e )
      {
        dispose ();
      }
    } );
    closeButton.addKeyListener ( new KeyAdapter ()
    {

      @Override
      public void keyPressed ( KeyEvent e )
      {
        if ( ( ( e.getKeyCode () == KeyEvent.VK_ESCAPE )
            || ( e.getKeyCode () == KeyEvent.VK_SPACE )
            || ( e.getKeyCode () == KeyEvent.VK_END ) || ( e.getKeyCode () == KeyEvent.VK_ENTER ) ) )
        {
          dispose ();
        }
      }
    } );

    // setTitle("Pong"); //$NON-NLS-1$
    // GraphicsEnvironment ge =
    // GraphicsEnvironment.getLocalGraphicsEnvironment();
    // GraphicsDevice gs = ge.getDefaultScreenDevice();
    // gs.setFullScreenWindow(this);
    setAlwaysOnTop ( true );
    validate ();
    // setSize(640, 480);
    Dimension fullscreen = java.awt.Toolkit.getDefaultToolkit ()
        .getScreenSize ();
    fullscreen.height += 4;
    fullscreen.width += 4;
    setSize ( fullscreen );
    setLocationRelativeTo ( owner );
    Point location = getLocation ();
    location.y -= 2;
    location.x -= 2;
    setLocation ( location );
  }
}
