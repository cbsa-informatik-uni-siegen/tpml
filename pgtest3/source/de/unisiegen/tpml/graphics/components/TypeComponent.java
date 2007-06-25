package de.unisiegen.tpml.graphics.components ;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.ToListenForMouseContainer;


/**
 * this class renders the coumpoundexpression

 */
public class TypeComponent extends JComponent
{
  /**
   * 
   */
  private static final long serialVersionUID = - 7653329118052555176L ;


  /**
   * Renderer that is used to render the type
   */
  private PrettyStringRenderer typeRenderer ;


  /**
   * The current type.
   */
  private Type type ;



  /**
   * The size of the type.
   */
  private Dimension typeSize ;



  /**
   * If this color is given all colors of the {@link AbstractRenderer} are
   * ignored and only this color is used.
   */
  private Color alternativeColor ;


  /**
   * Initialises the CompoundExpression with the default values.<br>
   * <br>
   * The braces have a size of 10 pixes, no underlining and the color of the
   * {@link AbstractRenderer} are ignored.
   */
  private ShowBonds bonds ;


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
  public TypeComponent ( )
  {

    super ( ) ;
    this.bonds = new ShowBonds ( ) ;
    this.toListenForMouse = new ToListenForMouseContainer ( ) ;
    this.alternativeColor = null ;

    this.addMouseMotionListener ( new MouseMotionAdapter ( )
    {
      @ Override
      public void mouseMoved ( MouseEvent event )
      {
        handleMouseMoved ( event ) ;
      }
    } ) ;
    this.addMouseListener (new MouseAdapter ()
    {
      @Override
      public void mouseExited (MouseEvent e)
      {
        toListenForMouse.reset ();
        toListenForMouse.setMark (false);
        TypeComponent.this.repaint ();
      }
    });
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
    if ( this.typeRenderer != null )
    {
      this.typeRenderer.setAlternativeColor ( color ) ;
    }
  }

  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ( )
  {
    if ( this.typeRenderer != null )
    {
      this.typeRenderer.checkLinewraps ( ) ;
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
    
    
    //TODO das brauchen wir hier nicht...
    //if the mouse is over the expression, let the showbound know it
    //bonds.setExpression(this.expression);
   

    //first, we do not want to mark anything, we are waiting for mouse pointer is over one bounded id
    toListenForMouse.setMark ( false ) ;
    TypeComponent.this.repaint ( ) ;
    
    //note if to mark or not to mark
    boolean mark = false;
    
    //walk throu the postions where to mark
    for ( int t = 0 ; t < toListenForMouse.size ( ) ; t++ )
    {
      //get position of pointer, these are rectangles. These positions are made by the PrettyStringRenderer
      Rectangle r =  toListenForMouse.get ( t ) ;
      int pX = r.x;
      int pX1 = r.x+r.width ;
      int pY = r.y;
      int pY1 = r.y+r.height;
    
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
      TypeComponent.this.repaint ( ) ;
    }
    else
    {
      //we do not want to see anything marked
     toListenForMouse.setMark ( false ) ;
     toListenForMouse.reset();
     TypeComponent.this.repaint ( ) ;
    }
  }


  /**
   * Sets the type that should rendered.
   * @param typeP the type to render
   * 
   * @param expression
   */
  public void setType ( Type typeP )
  {
    // check if we have a new expression
    if ( this.type != typeP )
    {
      // update to the new expression
      this.type = typeP ;

     bonds = new ShowBonds(); 
     bonds.setType ( this.type ) ;
      
      // check what to do with the renderer
      if ( this.type == null )
      {
        this.typeRenderer = null ;
      }
      else
      {
        if ( this.typeRenderer == null )
        {
          
          // bound = new ShowBonds();
          // CHANGE MICHAEL
          // with ervery new expression renderd by the PrettyStringRenderer the
          // elements listen by mouse will be resetet
          // toListenForMouse.reset();
          // CHANGE MICHAEL
          
          this.typeRenderer = new PrettyStringRenderer ( ) ;
          this.typeRenderer.setAlternativeColor ( this.alternativeColor ) ;
        }
        this.typeRenderer.setPrettyString ( this.type
            .toPrettyString ( ) ) ;
      }
      // be sure to schedule a repaint
      repaint ( ) ;
    }
  }


  /**
   * Calculates the size needed to propperly render the typeComponent
   * Note: Till now the types are never wraped so the maxWidth is ignored 
   * Ther is no way to print the typechecker
   * TODO getNeededSizeAll einbauen
   * 
   * @param maxWidth
   * @return the Dimension needed to render the type
   */
  public Dimension getNeededSize ( int maxWidth )
  {
    Dimension result = new Dimension ( 0 , 0 ) ;
    result.width += AbstractRenderer.getTextFontMetrics ( ).stringWidth(" :: ");
    if (true ) //no linwraping
    {
      // to guaranty that no line wrapping should be performed
      // set the maxWidth = MAX_INT
      maxWidth = Integer.MAX_VALUE ;
    }
    // check whether there is an environment.
  
    if ( this.type != null && this.typeRenderer != null )
    {
      // now check the size still available for the expression
      //TODO Test for printing
      this.typeSize = this.typeRenderer.getNeededSizeAll_ ( maxWidth ) ;
      //this.expressionSize = this.expressionRenderer.getNeededSize ( maxWidth ) ;
      result.width += this.typeSize.width ;
      result.height = Math.max ( result.height , this.typeSize.height ) ;
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
    
    // make sure that we have a type to renderer
    if ( this.typeRenderer == null )
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
    // if there is an environment and it is of type Store
    // draw the braces around the entire expression with environment
  
    // if there is no environment or the environment is of type
    // Store, the entire expression (with environment) will begin
    // with the expression


   
      // draw the arrow character in the vertical center
      int centerV = getHeight ( ) / 2 ;
      centerV += AbstractRenderer.getTextFontMetrics ( ).getAscent ( ) / 2 ;
      gc.setFont ( AbstractRenderer.getTextFont ( ) ) ;
      gc.setColor ( AbstractRenderer.getTextColor ( ) ) ;
      gc.drawString ( " :: " , posX , centerV ) ;
      posX += AbstractRenderer.getTextFontMetrics ( ).stringWidth (
          " :: ") ;
      // draw the expression at the last position.
      //TODO Testausgabe
      //System.out.println(getHeight());
      this.typeRenderer.render ( posX , posY , getWidth() ,getHeight ( ) , gc ,
          bonds , toListenForMouse ) ;
    
    
    //TODO nur testen
    //gc.setColor (Color.YELLOW);
    //gc.drawRect(0, 0, getWidth () - 1, getHeight () - 1);
  }


  public Type getType ( )
  {
    return type ;
  }
 
}
