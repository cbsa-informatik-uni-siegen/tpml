package de.unisiegen.tpml.graphics.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.util.Environment;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.ToListenForMouseContainer;

public class CompoundExpressionSubTyping extends JComponent
{
	/**
   * 
   */
  private static final long serialVersionUID = - 7653329118052555176L ;


  /**
   * Renderer that is used to render the first Type
   */
  private PrettyStringRenderer type1Renderer ;
  
  /**
   * Renderer that is used to render the second Type
   */
  private PrettyStringRenderer type2Renderer ;


  /**
   * The current expression.
   */
  //private Expression expression ;
  
  
  /**
   * The first Type
   */
  private MonoType type1;
  
  /**
   * The second Type
   */
  private MonoType type2;


  /**
   * The expression that will be underlined during the rendering.
   */
  private Expression underlineExpression ;


  /**
   * The size of the type1.
   */
  private Dimension type1Size ;
  
  /**
   * The size of the type2.
   */
  private Dimension type2Size ;

 
  /**
   * The size of the environment.
   */
  private Dimension environmentSize ;


  /**
   * The width of the braces around the expression and the environment.
   */
  private int braceSize ;


  /**
   * Whether the expression should be wrapped if there is not enough space to
   * render it in on line.<br>
   * Actualy this is only used with the result of the BigStepGUI.
   */
  private boolean noLineWrapping ;


  /**
   * If this color is given all colors of the {@link AbstractRenderer} are
   * ignored and only this color is used.
   */
  private Color alternativeColor ;


  /**
   * The arrow symbol that is used between the environment and the expression
   * when used within the
   * {@link de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent}
   */
  private static String arrowStr = " \u22b3 " ;


  /**
   * Initialises the CompoundExpression with the default values.<br>
   * <br>
   * The braces have a size of 10 pixes, no underlining and the color of the
   * {@link AbstractRenderer} are ignored.
   */
  //private ShowBonds bonds ;


  // private ToListenForMouseContainer toListenForMouse = new
  // ToListenForMouseContainer();
  /**
   * the list of points where the mouseovereffect will be react
   */
  private ToListenForMouseContainer toListenForMouse ;


  /**
   * the constructor
   *
   */
  public CompoundExpressionSubTyping ( )
  {
    super ( ) ;
    this.toListenForMouse = new ToListenForMouseContainer ( ) ;
    this.alternativeColor = null ;
    this.braceSize = 10 ;
    this.underlineExpression = null ;
    this.addMouseMotionListener ( new MouseMotionAdapter ( )
    {
      @ Override
      public void mouseMoved ( MouseEvent event )
      {
        handleMouseMoved ( event ) ;
      }
    } ) ;
    // CHANGE CHRISTIAN
    //this.addMouseListener ( new OutlineMouseListener ( this ) ) ;
    //TODO muss die Outline auf hier reagieren können?
    // CHANGE CHRISTIAN END
    this.addMouseListener ( new MouseAdapter ( )
    {
      @ Override
      public void mouseExited ( MouseEvent e )
      {
        // TODO hier sollte eigentlich das Ereignis sein, dass die Maus den
        // Ausdruck verl�sst, und der dann neu gemalt wird, funktioniert aber
        // nicht
        // System.err.println("Ladidal");
        // ToListenForMouseContainer.getInstanceOf().reset();
        //if ( ! toListenForMouse.setExpression ( expression ) )
        //{
        //  Debug.out.print ( "Schei�e, es ist ein anderer Ausdruck: "
        //      + expression.toPrettyString ( ).toString ( ) , "Feivel" ) ;
        //}
        
        toListenForMouse.reset();
          toListenForMouse.setRightList(-1);
        toListenForMouse.setMark ( false ) ;
        CompoundExpressionSubTyping.this.repaint();
        
      }
    } ) ;
  }


  /**
   * Sets an alternative color.<br>
   * <br>
   * Both renderers, the {@link PrettyStringRenderer} and the
   * {@link EnvironmentRenderer}, are updated with this color.
   * 
   * @param color The alternative color.
   */
  public void setAlternativeColor ( Color color )
  {
    this.alternativeColor = color ;
    if ( this.type1Renderer != null )
    {
      this.type1Renderer.setAlternativeColor ( color ) ;
    }
    
    if ( this.type2Renderer != null )
    {
      this.type2Renderer.setAlternativeColor ( color ) ;
    }
  }


  /**
   * Sets the expression, that should be underlined.
   * 
   * @param underlineExpression
   */
  public void setUnderlineExpression ( Expression underlineExpression )
  {
    boolean needsRepaint = this.underlineExpression != underlineExpression ;
    this.underlineExpression = underlineExpression ;
    if ( this.type1Renderer != null )
    {
      this.type1Renderer
          .setUndelinePrettyPrintable ( this.underlineExpression ) ;
    }
    if ( needsRepaint )
    {
      repaint ( ) ;
    }
  }


  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ( )
  {
    if ( this.type1Renderer != null )
    {
      this.type1Renderer.checkLinewraps ( ) ;
    }
    
    if ( this.type2Renderer != null )
    {
      this.type2Renderer.checkLinewraps  ( )  ;
    }
  }


  /**
   * Handles whether a ToolTip should be displayed for the environment, if some
   * parts of it are collapsed.
   * 
   * @param event
   */
  private void handleMouseMoved ( MouseEvent event )
  {
    
    //tell the PrettyStringRenderer where the mouse pointer is
    toListenForMouse.setHereIam ( event.getX ( ) , event.getY ( ) ) ;

    //first, we do not want to mark anything, we are waiting for mouse pointer is over one bounded id
    toListenForMouse.setMark ( false ) ;
    CompoundExpressionSubTyping.this.repaint ( ) ;
    
    //note if to mark or not to mark
    boolean mark = false;
    
    //walk throu the postions where to mark
    for ( int t = 0 ; t < toListenForMouse.size ( ) ; t = t + 4 )
    {
      //get position of pointer, these are rectangles. These positions are made by the PrettyStringRenderer
      int pX = toListenForMouse.get ( t ) ;
      int pX1 = toListenForMouse.get ( t + 1 ) ;
      int pY = toListenForMouse.get(t+2);
      int pY1 = toListenForMouse.get(t+3);
    
      // fnde out if pointer is on one of the chars to mark
      if ((event.getX() >= pX) && (event.getX() <= pX1) && (event.getY() >= pY) && (event.getY() <= pY1)) 
      //if ( ( event.getX ( ) >= pX ) && ( event.getX ( ) <= pX1 ) )
      {
        //just note it
        mark = true;
      }
    }
    
    //if the pointer is on one of the bounded chars
    if (mark)
    {
      //we want to habe marked
      toListenForMouse.setMark ( true ) ;
      CompoundExpressionSubTyping.this.repaint ( ) ;
    }
    else
    {
      //we do not want to see anything marked
     toListenForMouse.setMark ( false ) ;
     toListenForMouse.reset();
     toListenForMouse.setRightList(-1);
     toListenForMouse.reset();
     CompoundExpressionSubTyping.this.repaint ( ) ;
    }
   }


  /**
   * Sets whether the expression should be wrapped or not.
   * 
   * @param noLineWrapping
   */
  public void setNoLineWrapping ( boolean noLineWrapping )
  {
    this.noLineWrapping = noLineWrapping ;
  }


  /**
   * Sets the expression that should rendered.
   * 
   * @param expression
   */
  public void setType1 ( MonoType type1P )
  {
    // check if we have a new type1
    if ( this.type1 != type1P )
    {
      // update to the new type1P
      this.type1 = type1P ;
        
      // check what to do with the renderer
      if ( this.type1 == null )
      {
        this.type1Renderer = null ;
      }
      else
      {
        if ( this.type1Renderer == null )
        {
          this.type1Renderer = new PrettyStringRenderer ( ) ;
          this.type1Renderer.setAlternativeColor ( this.alternativeColor ) ;
        }
        this.type1Renderer.setPrettyString ( this.type1.toPrettyString ( ) ) ;
        // reset the underlineExpression
        setUnderlineExpression ( this.underlineExpression ) ;
      }
      // be sure to schedule a repaint
      repaint ( ) ;
    }
  }
  
  /**
   * Sets the expression that should rendered.
   * 
   * @param expression
   */
  public void setType2 ( MonoType type2P )
  {
    // check if we have a new type1
    if ( this.type2 != type2P )
    {
      // update to the new type2P
      this.type2 = type2P ;
        
      // check what to do with the renderer
      if ( this.type2 == null )
      {
        this.type2Renderer = null ;
      }
      else
      {
        if ( this.type2Renderer == null )
        {
          this.type2Renderer = new PrettyStringRenderer ( ) ;
          this.type2Renderer.setAlternativeColor ( this.alternativeColor ) ;
        }
        this.type2Renderer.setPrettyString ( this.type2.toPrettyString ( ) ) ;
        // reset the underlineExpression
        setUnderlineExpression ( this.underlineExpression ) ;
      }
      // be sure to schedule a repaint
      repaint ( ) ;
    }
  }


  /**
   * Sets the environment taht should be rendered.
   * 
   * @param environment
   */


  /**
   * Calculates the size needed to propperly render the compoundExpression
   * 
   * @param maxWidth
   * @return
   */
  public Dimension getNeededSize ( int maxWidth )
  {
    Dimension result = new Dimension ( 0 , 0 ) ;
    if ( this.noLineWrapping )
    {
      // to guaranty that no line wrapping should be performed
      // set the maxWidth = MAX_INT
      maxWidth = Integer.MAX_VALUE ;
    }
    // check whether there is an environment.
   
    
    if ( this.type1 != null && this.type1Renderer != null && this.type2 != null && this.type2Renderer != null)
    {
      // now check the size still available for the expression
    	//TODO Test for printing
    	//this.expressionSize = this.expressionRenderer.getNeededSizeAll_ ( maxWidth ) ;
      this.type1Size = this.type1Renderer.getNeededSize ( maxWidth ) ;
      this.type2Size = this.type2Renderer.getNeededSize ( maxWidth );
      //TODO wir brauchen noch das doofe <: Zeichen
      result.width += AbstractRenderer.getTextFontMetrics().stringWidth(" <: ");
      result.width += this.type1Size.width ;
      result.width += this.type2Size.width ;
      result.height = Math.max ( result.height , this.type1Size.height ) ;
      result.height = Math.max ( result.height , this.type2Size.height ) ;
    }
    return result ;
  }


  /**
   * The actualy rendering method.
   * 
   * @param gc The Graphics object that will be used to render the stuff.
   */
  @ Override
  protected void paintComponent ( Graphics gc )
  {
    //TODO Only for test to make yompoundexpression visible
  	//it also displays how often the exptresso is rednerd while srolling...
    //gc.setColor (Color.yellow);
  	//--------------------------------
    //Color [] test = new Color [10];
    //test[0] = Color.yellow;
    //test[1] = Color.red;
    //test[2] = Color.green;
    //test[3] = Color.cyan;
    //test[4] = Color.green;
    //test[5] = Color.lightGray;
    //test[6] = Color.blue;
    //test[7] = Color.gray;
    //test[8] = Color.magenta;
    //test[9] = Color.orange;
    
    //double get = Math.random();
    //int getR = (int) (get*10);
    //gc.setColor (test[getR]);
    //gc.fillRect(0, 0, getWidth () - 1, getHeight () - 1);
  	//--------------------------------
    
    // make sure that we have an expression renderer
    if ( this.type1Renderer == null )
    {
      return ;
    }
    // assuming the size of the component will suffice, no testing
    // of any sizes will happen.
    /*
     * just to get reminded: no environment: expression storeenvironment:
     * (expression [env]) typeenvironment: [env] |> expression
     */
    int posX = 0 ;
    int posY = 0 ;
    ShowBonds bound = new ShowBonds();
		bound.setExpression(null);
		
    //type1Renderer.render(posX, posY -(type1Size.height / 2) - AbstractRenderer.getFontAscent() / 2, type1Size.height, gc, bound, new ToListenForMouseContainer());
		type1Renderer.render(posX, posY, type1Size.height, gc, bound, new ToListenForMouseContainer());
    posX += type1Size.width;
    
    gc.setColor(Color.BLACK);
    gc.setFont(new JComboBox().getFont());
        
    //gc.drawString(" <: ", posX, 0 );
    int centerV = getHeight ( ) / 2 ;
    centerV += AbstractRenderer.getTextFontMetrics ( ).getAscent ( ) / 2 ;
    gc.setFont ( AbstractRenderer.getTextFont ( ) ) ;
    gc.setColor ( AbstractRenderer.getTextColor ( ) ) ;
    gc.drawString ( " <: " , posX , centerV ) ;
    posX += AbstractRenderer.getTextFontMetrics ( ).stringWidth (
        " <: " ) ;
    
    
    //posX += AbstractRenderer.getTextFontMetrics().stringWidth(" <: ");
    //type2Renderer.render(posX, posY-(type2Size.height / 2) - AbstractRenderer.getFontAscent() / 2, type2Size.height, gc, bound, new ToListenForMouseContainer());
    type2Renderer.render(posX, posY, type2Size.height, gc, bound, new ToListenForMouseContainer());
    // if there is an environment and it is of type Store
    // draw the braces around the entire expression with environment

    // if there is no environment or the environment is of type
    // Store, the entire expression (with environment) will begin
    // with the expression

    
    //gc.setColor (Color.YELLOW);
    //gc.fillRect(0, 0, getWidth () - 1, getHeight () - 1);
  }


  public MonoType getType1 ( )
  {
    return type1 ;
  }
  
  public MonoType getType2 ( )
  {
    return type2 ;
  }

}
