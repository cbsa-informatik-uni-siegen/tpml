/**
 * 
 */
package de.unisiegen.tpml.graphics.pong;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;


/**
 * no comments for this stuff
 * 
 * @author marcell
 * @author Michael
 */
@SuppressWarnings ( "all" )
public class Pong extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = 6688203129068640177L;


  private static final int WINPOINTS = 5;


  private class Bat
  {

    public float height;


    public float width;


    public float position;


    public float oldPosition;


    public float movementOldPos;


    public int points;


    public Bat ( float height, float width, float position )
    {
      this.height = height;
      this.width = width;
      this.position = position;
      this.oldPosition = position;
      this.points = 0;
    }


    public void save ()
    {
      this.oldPosition = this.position;
    }


    public void saveMovement ()
    {
      this.movementOldPos = this.position;
    }


    public void setPosition ( float position )
    {
      if ( position < ( this.height / 2f ) )
      {
        position = this.height / 2f;
      }
      else if ( position > ( 1f - this.height / 2f ) )
      {
        position = ( 1f - this.height / 2f );
      }
      this.position = position;
    }
  }


  private class Ball
  {

    public float posX;


    public float posY;


    public float oldPosX;


    public float oldPosY;


    public float directionX;


    public float directionY;


    public float speed;


    public float radius;


    public Ball ()
    {
      this.directionX = -1.0f / ( float ) Math.sqrt ( 2 );

      this.radius = 0.02f;

    }


    public void move ()
    {
      this.posX += this.directionX * this.speed;
      this.posY += this.directionY * this.speed;
    }


    public void save ()
    {
      this.oldPosX = this.posX;
      this.oldPosY = this.posY;
    }


    public void restart ( Bat pcBat )
    {
      this.posX = 0.9f - this.radius;
      this.posY = pcBat.position;
      this.oldPosX = this.posX;
      this.oldPosY = this.posY;

      this.speed = 0.015f;

      this.directionX = -this.directionX;
      this.directionY = 1.0f / ( float ) Math.sqrt ( 2 );
    }
  }


  private class NPC
  {

    public float maxSpeed;


    public float speed;


    public float dest;


    public Bat bat;


    public void act ()
    {
      if ( this.bat.position > this.dest )
      {
        this.bat.setPosition ( this.bat.position - this.speed );

        if ( this.bat.position < this.dest )
        {
          this.bat.setPosition ( this.dest );
        }
      }

      if ( this.bat.position < this.dest )
      {
        this.bat.setPosition ( this.bat.position + this.speed );

        if ( this.bat.position > this.dest )
        {
          this.bat.setPosition ( this.dest );
        }
      }
    }
  }


  private Bat pcBat;


  private Bat npcBat;


  private Ball ball;


  private NPC npc;


  private boolean renderAll;


  private Timer timer;


  private Random random;


  private Font font;


  private boolean onTheRun;


  private boolean paused;


  private enum GameState
  {
    GameStateHold, GameStateFlow,
  }


  private GameState gameState;


  private JWindow parent;


  public Pong ( JWindow p )
  {
    parent = p;

    this.pcBat = new Bat ( 0.1f, 0.01f, 0.5f );
    this.npcBat = new Bat ( 0.1f, 0.01f, 0.5f );

    this.ball = new Ball ();
    this.ball.restart ( this.pcBat );

    this.npc = new NPC ();
    this.npc.maxSpeed = this.ball.speed;
    this.npc.bat = this.npcBat;
    this.npc.dest = this.npc.bat.position;

    this.random = new Random ();

    this.onTheRun = false;

    this.gameState = GameState.GameStateHold;

    this.font = new JLabel ().getFont ();

    this.font = this.font.deriveFont ( this.font.getSize2D () * 3.0f );
    this.addMouseMotionListener ( new MouseMotionAdapter ()
    {

      @Override
      public void mouseMoved ( MouseEvent event )
      {
        Pong.this.handleMouseMoved ( event );
      }
    } );

    this.addComponentListener ( new ComponentAdapter ()
    {

      @Override
      public void componentResized ( ComponentEvent event )
      {
        Pong.this.handleResize ();
      }
    } );

    this.timer = new Timer ();
    this.timer.schedule ( new TimerTask ()
    {

      @Override
      public void run ()
      {
        if ( !Pong.this.paused )
        {
          Pong.this.handleTimerEvent ();
        }
      }
    }, 0, 20 );

    // setup an invisible cursor
    Image cursorImage = Toolkit.getDefaultToolkit ()
        .createImage (
            getClass ().getResource (
                "/de/unisiegen/tpml/graphics/pong/empty.gif" ) );
    setCursor ( Toolkit.getDefaultToolkit ().createCustomCursor ( cursorImage,
        new Point ( 0, 0 ), "empty" ) );

    // pause the game if the mouse is outside the component
    this.addMouseListener ( new MouseAdapter ()
    {

      @Override
      public void mouseEntered ( MouseEvent e )
      {
        Pong.this.paused = false;
      }


      @Override
      public void mouseExited ( MouseEvent e )
      {
        // Pong.this.paused = true;
      }


      @Override
      public void mouseClicked ( MouseEvent e )
      {
        Pong.this.handleMouseClicked ();
      }
    } );

    setDoubleBuffered ( true );
    setOpaque ( true );
    this.renderAll = true;
  }


  private void handleResize ()
  {
    this.renderAll = true;
  }


  private void handleMouseMoved ( MouseEvent event )
  {
    float height = ( float ) event.getY () / ( float ) getHeight ();

    // save the old state
    this.pcBat.save ();

    // and set the new one
    this.pcBat.setPosition ( height );

    if ( this.gameState == GameState.GameStateHold )
    {
      this.ball.posX = 0.9f - this.ball.radius;
      this.ball.posY = this.pcBat.position;
    }

    repaint ();
  }


  private void handleMouseClicked ()
  {
    if ( this.gameState == GameState.GameStateFlow )
    {
      return;
    }

    this.ball.restart ( this.pcBat );

    this.ball.directionX = -0.4f;
    this.ball.directionY = 0.5f - this.pcBat.position;

    float length = ( float ) Math.sqrt ( this.ball.directionX
        * this.ball.directionX + this.ball.directionY * this.ball.directionY );
    this.ball.directionX /= length;
    this.ball.directionY /= length;

    this.gameState = GameState.GameStateFlow;
    this.onTheRun = false;
    recalcNPCDest ();
  }


  private void handleTimerEvent ()
  {

    if ( this.gameState == GameState.GameStateHold )
    {
      return;
    }

    // do the npc movement
    this.npc.act ();

    // this.ball.speed += 0.00001;
    this.ball.speed += 0.000005;

    // check the bat movement of the player
    float pcBatMovement = this.pcBat.position - this.pcBat.movementOldPos;
    this.pcBat.saveMovement ();

    float npcBatMovement = this.npcBat.position - this.npcBat.movementOldPos;
    this.npcBat.saveMovement ();

    // save the old state
    this.ball.save ();

    // and set the new one
    this.ball.move ();

    if ( this.ball.posY >= 1.0f || this.ball.posY <= 0.0f )
    {
      this.ball.directionY *= -1.0f;
    }

    if ( this.ball.posX >= 1.0f )
    {
      this.gameState = GameState.GameStateHold;
      this.npcBat.points++ ;
      if ( this.npcBat.points >= WINPOINTS
          && this.npcBat.points >= this.pcBat.points + 2 )
      {
        String message = "Computer wins!";
        String title = "Computer wins!";
        parent.setAlwaysOnTop ( false );
        parent.setVisible ( false );
        JOptionPane.showMessageDialog ( parent, message, title,
            JOptionPane.INFORMATION_MESSAGE );
        parent.dispose ();
      }
      this.onTheRun = false;
      this.ball.restart ( this.pcBat );
    }

    if ( this.ball.posX <= 0.0f )
    {
      this.gameState = GameState.GameStateHold;
      this.pcBat.points++ ;
      if ( this.pcBat.points >= WINPOINTS
          && this.pcBat.points >= this.npcBat.points + 2 )
      {
        String message = "Congratulations! You win!";
        String title = "You win!";
        parent.setAlwaysOnTop ( false );
        parent.setVisible ( false );
        JOptionPane.showMessageDialog ( parent, message, title,
            JOptionPane.INFORMATION_MESSAGE );
        parent.dispose ();
      }
      this.onTheRun = false;
      this.ball.restart ( this.pcBat );
    }

    recalcNPCDest ();

    if ( this.ball.posX >= ( 0.9f - this.pcBat.width / 2 )
        && this.ball.oldPosX <= ( 0.9f - this.pcBat.width / 2 ) )
    {
      // check whether the pc bat hits the ball
      if ( this.ball.posY >= ( this.pcBat.position - this.pcBat.height / 2 )
          && this.ball.posY <= ( this.pcBat.position + this.pcBat.height / 2 ) )
      {

        if ( this.ball.directionX > pcBatMovement )
        {
          this.ball.directionX -= pcBatMovement;
          this.ball.directionY -= pcBatMovement * 3;
        }

        float length = ( float ) Math.sqrt ( this.ball.directionX
            * this.ball.directionX + this.ball.directionY
            * this.ball.directionY );
        this.ball.directionX /= length;
        this.ball.directionY /= length;

        this.ball.directionX *= -1.0f;

        this.onTheRun = false;
      }
    }

    if ( this.ball.posX <= ( 0.1f + this.npcBat.width / 2 )
        && this.ball.oldPosX >= ( 0.1f + this.npcBat.width / 2 ) )
    {
      // check whether the npc bat hits the ball
      if ( this.ball.posY >= ( this.npcBat.position - this.npcBat.height / 2 )
          && this.ball.posY <= ( this.npcBat.position + this.npcBat.height / 2 ) )
      {

        if ( this.ball.directionX < npcBatMovement )
        {
          this.ball.directionX += npcBatMovement;
          this.ball.directionY -= npcBatMovement * 3;
        }

        float length = ( float ) Math.sqrt ( this.ball.directionX
            * this.ball.directionX + this.ball.directionY
            * this.ball.directionY );
        this.ball.directionX /= length;
        this.ball.directionY /= length;
        this.ball.directionX *= -1.0f;

        this.onTheRun = false;
      }
    }

    repaint ();

  }


  private void recalcNPCDest ()
  {

    float Px = this.ball.posX;
    float Py = this.ball.posY;

    float Qx = this.ball.directionX;
    float Qy = this.ball.directionY;

    if ( Qx > 0.0f )
    {
      return;
    }

    if ( Qy == 0.0f )
    {
      return;
    }
    float topX = Px - ( Py / Qy ) * Qx;
    float bottomX = Px + ( ( 1.0f - Py ) / Qy ) * Qx;

    if ( topX < 0.1 || bottomX < 0.1 )
    {
      if ( this.onTheRun == false )
      {
        this.onTheRun = true;

        this.npc.dest = Py + ( ( 0.1f - Px ) / Qx ) * Qy;
        float add = ( this.random.nextFloat () - 0.5f ) * this.npcBat.height
            * this.ball.speed * 70;
        this.npc.dest += add;

        float dist = this.npc.dest - this.npc.bat.position;

        float time = ( this.ball.posX - 0.1f )
            / ( this.ball.speed * this.ball.directionX );

        this.npc.speed = dist / time;
        this.npc.speed *= Math.signum ( this.npc.speed );
        if ( this.npc.speed > this.npc.maxSpeed )
        {
          this.npc.speed = this.npc.maxSpeed;
        }
      }
      return;
    }

    this.onTheRun = false;

    if ( this.ball.directionX < 0.0f )
    {
      this.npc.speed = this.npc.maxSpeed;
      this.npc.dest = this.ball.posY;
    }

  }


  @Override
  public void paint ( Graphics gc )
  {

    gc.setColor ( Color.BLACK );

    if ( this.renderAll || true )
    {
      // clear the background
      gc.fillRect ( 0, 0, getWidth (), getHeight () );
      this.renderAll = false;
    }

    gc.setColor ( Color.GREEN );

    // draw the old pc and the old npc bat
    // npc old
    int centerX = ( int ) ( getWidth () * 0.1f );
    int centerY = ( int ) ( getHeight () * this.npcBat.position );

    int width = ( int ) ( this.npcBat.width * getWidth () );
    int height = ( int ) ( this.npcBat.height * getHeight () );

    gc.fillRect ( centerX - width / 2, centerY - height / 2, width, height );

    // pc old
    centerX = ( int ) ( getWidth () * 0.9f );
    centerY = ( int ) ( getHeight () * this.pcBat.position );

    width = ( int ) ( this.pcBat.width * getWidth () );
    height = ( int ) ( this.pcBat.height * getHeight () );

    gc.fillRect ( centerX - width / 2, centerY - height / 2, width, height );

    // draw the old ball
    centerX = ( int ) ( this.ball.posX * getWidth () );
    centerY = ( int ) ( this.ball.posY * getHeight () );

    width = ( int ) ( this.ball.radius * getWidth () );
    height = ( int ) ( this.ball.radius * getHeight () );

    gc.fillOval ( centerX - width / 2, centerY - height / 2, width, height );

    height = ( int ) ( 0.9f * getHeight () );
    width = ( int ) ( this.pcBat.width * getWidth () );
    centerX = ( int ) ( 0.5f * getWidth () );
    centerY = ( int ) ( 0.5f * getHeight () );

    gc.fillRect ( centerX - width / 2, centerY - height / 2, width, height );

    String points = "" + this.npcBat.points;
    FontMetrics fm = getFontMetrics ( this.font );
    int posX = getWidth () / 4 - fm.stringWidth ( points ) / 2;
    int posY = fm.getHeight ();
    gc.setFont ( this.font );
    gc.drawString ( points, posX, posY );

    points = "" + this.pcBat.points;
    posX = getWidth () * 3 / 4 - fm.stringWidth ( points ) / 2;
    gc.drawString ( points, posX, posY );

  }

}
