package de.unisiegen.tpml.graphics.components;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.ToListenForMouseContainer;


/**
 * this class renders the coumpoundexpression
 * 
 * @version $Id: CompoundExpression.java 2796 2008-03-14 19:13:11Z fehler $
 */
public class CompoundExpressionBigStepClosure extends JComponent
{

  /**
   * 
   */
  private static final long serialVersionUID = -7653329118052555176L;


  /**
   * Renderer that is used to render the expression
   */
  private PrettyStringRenderer expressionRenderer;


  /**
   * The current expression.
   */
  private Closure closure;


  /**
   * The expression that will be underlined during the rendering.
   */
  private Expression underlineExpression;


  /**
   * Whether the expression should be wrapped if there is not enough space to
   * render it in on line.<br>
   * Actualy this is only used with the result of the BigStepGUI.
   */
  private boolean noLineWrapping;


  /**
   * If this color is given all colors of the {@link AbstractRenderer} are
   * ignored and only this color is used.
   */
  private Color alternativeColor;


  /**
   * The arrow symbol that is used between the environment and the expression
   * when used within the
   * {@link de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent}
   */
  private static String arrowStr = " \u22b3 "; //$NON-NLS-1$


  /**
   * Initialises the CompoundExpression with the default values.<br>
   * <br>
   * The braces have a size of 10 pixes, no underlining and the color of the
   * {@link AbstractRenderer} are ignored.
   */
  private ShowBonds bonds;


  /**
   * the list of points where the mouseovereffect will be react
   */
  private ToListenForMouseContainer toListenForMouse;


  /**
   * the constructor
   */
  public CompoundExpressionBigStepClosure ()
  {
    super ();
    this.bonds = new ShowBonds ();
    this.toListenForMouse = new ToListenForMouseContainer ();
    this.alternativeColor = null;
    this.underlineExpression = null;
    addMouseMotionListener ( new MouseMotionAdapter ()
    {

      @Override
      public void mouseMoved ( MouseEvent event )
      {
        handleMouseMoved ( event );
      }
    } );
    addMouseListener ( new MouseAdapter ()
    {

      @Override
      public void mouseExited ( @SuppressWarnings ( "unused" )
      MouseEvent e )
      {
        CompoundExpressionBigStepClosure.this.getToListenForMouse ().reset ();
        CompoundExpressionBigStepClosure.this.getToListenForMouse ().setMark (
            false );
        CompoundExpressionBigStepClosure.this.repaint ();
      }
    } );
  }


  /**
   * Sets an alternative color.<br>
   * <br>
   * Both renderers, the {@link PrettyStringRenderer} is updated with this
   * color.
   * 
   * @param color The alternative color.
   */
  public void setAlternativeColor ( Color color )
  {
    this.alternativeColor = color;
    if ( this.expressionRenderer != null )
      this.expressionRenderer.setAlternativeColor ( color );
  }


  /**
   * Sets the expression, that should be underlined.
   * 
   * @param pUnderlineExpression
   */
  public void setUnderlineExpression ( Expression pUnderlineExpression )
  {
    boolean needsRepaint = this.underlineExpression != pUnderlineExpression;
    this.underlineExpression = pUnderlineExpression;
    if ( this.expressionRenderer != null )
    {
      this.expressionRenderer
          .setUndelinePrettyPrintable ( this.underlineExpression );
    }
    if ( needsRepaint )
    {
      repaint ();
    }
  }


  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ()
  {
    if ( this.expressionRenderer != null )
    {
      this.expressionRenderer.checkLinewraps ();
    }
  }


  /**
   * Handles whether a ToolTip should be displayed for the environment, if some
   * parts of it are collapsed.
   * 
   * @param event
   */
  void handleMouseMoved ( MouseEvent event )
  {

    // tell the PrettyStringRenderer where the mouse pointer is
    this.toListenForMouse.setHereIam ( event.getX (), event.getY () );

    // first, we do not want to mark anything, we are waiting for mouse pointer
    // is over one bounded id
    this.toListenForMouse.setMark ( false );
    CompoundExpressionBigStepClosure.this.repaint ();

    // note if to mark or not to mark
    boolean mark = false;

    // walk throu the postions where to mark
    for ( int t = 0 ; t < this.toListenForMouse.size () ; t++ )
    {
      // get position of pointer, these are rectangles. These positions are made
      // by the PrettyStringRenderer
      Rectangle r = this.toListenForMouse.get ( t );
      int pX = r.x;
      int pX1 = r.x + r.width;
      int pY = r.y;
      int pY1 = r.y + r.height;

      // fnde out if pointer is on one of the chars to mark
      if ( ( event.getX () >= pX ) && ( event.getX () <= pX1 )
          && ( event.getY () >= pY ) && ( event.getY () <= pY1 ) )
      // if ( ( event.getX ( ) >= pX ) && ( event.getX ( ) <= pX1 ) )
      {
        // just note it
        mark = true;
      }
    }

    // if the pointer is on one of the bounded chars
    if ( mark )
    {
      // we want to habe marked
      this.toListenForMouse.setMark ( true );
      this.repaint ();
    }
    else
    {
      // we do not want to see anything marked
      this.toListenForMouse.setMark ( false );
      this.toListenForMouse.reset ();
      this.repaint ();
    }
  }


  /**
   * Sets whether the expression should be wrapped or not.
   * 
   * @param pNoLineWrapping
   */
  public void setNoLineWrapping ( boolean pNoLineWrapping )
  {
    this.noLineWrapping = pNoLineWrapping;
  }


  /**
   * Sets the closure that should rendered.
   * 
   * @param pClosure
   */
  public void setClosure ( final Closure pClosure )
  {
    // check if we have a new closure
    if ( this.closure == pClosure )
      return;

    this.closure = pClosure;

    // because of the bounds are cached we need a new one. The expression
    // might change by translating in coresyntax
    // check what to do with the renderer
    if ( this.closure == null )
    {
      this.expressionRenderer = null;
      this.bonds = null;
    }
    else
    {
      this.bonds = new ShowBonds ();
      this.bonds.load ( this.closure.getExpression () ); // FIXME: is this
                                                          // right?

      if ( this.expressionRenderer == null )
      {
        this.expressionRenderer = new PrettyStringRenderer ();
        this.expressionRenderer.setAlternativeColor ( this.alternativeColor );
      }
      this.expressionRenderer.setPrettyString ( this.closure.toPrettyString () );
      // reset the underlineExpression
      setUnderlineExpression ( this.underlineExpression );
    }
    // be sure to schedule a repaint
    repaint ();
  }


  /**
   * Calculates the size needed to propperly render the compoundExpression
   * 
   * @param pMaxWidth
   * @return TODO
   */
  public Dimension getNeededSize ( final int pMaxWidth )
  {
    int maxWidth = pMaxWidth;
    Dimension result = new Dimension ( 0, 0 );
    if ( ( this.closure == null ) || ( this.expressionRenderer == null ) )
      return result;

    Dimension neededSize = this.expressionRenderer.getNeededSize ( maxWidth );
    result.width = neededSize.width;
    result.height = neededSize.height;

    if ( this.noLineWrapping )
    {
      // to guaranty that no line wrapping should be performed
      // set the maxWidth = MAX_INT
      maxWidth = Integer.MAX_VALUE;
    }

    result.width += AbstractRenderer.getTextFontMetrics ().stringWidth (
        CompoundExpressionBigStepClosure.arrowStr );

    return result;
  }


  /**
   * The actualy rendering method.
   * 
   * @param gc The Graphics object that will be used to render the stuff.
   */
  @Override
  protected void paintComponent ( Graphics gc )
  {
    // make sure that we have an expression renderer
    if ( this.expressionRenderer == null )
      return;
    // assuming the size of the component will suffice, no testing
    // of any sizes will happen.
    /*
     * just to get reminded: no environment: expression storeenvironment:
     * (expression [env]) typeenvironment: [env] |> expression
     */
    
    int posX = 0;
    int posY = 0;

    gc.setFont ( AbstractRenderer.getTextFont () );
    gc.setColor ( AbstractRenderer.getTextColor () );
    gc.drawString ( CompoundExpressionBigStepClosure.arrowStr, posX, posY
        + AbstractRenderer.getFontAscent () );
    posX += AbstractRenderer.getTextFontMetrics ().stringWidth (
        CompoundExpressionBigStepClosure.arrowStr );
    // draw the expression at the last position.

    this.expressionRenderer.render ( posX, posY, getWidth (), getHeight (), gc,
        this.bonds, this.toListenForMouse );
    // this.expressionRenderer.render ( posX , posY , getWidth()
    // ,AbstractRenderer.getAbsoluteHeight (), gc , bonds , toListenForMouse )
    // ;

    // TODO DEbugging
    // gc.setColor (Color.YELLOW);
    // gc.drawRect(0, 0, getWidth () - 1, getHeight () - 1);
  }


  /**
   * returns the current expression.
   * 
   * @return the expression
   */
  public Closure getClosure ()
  {
    return this.closure;
  }


  /**
   * @return the toListenForMouse
   */
  public ToListenForMouseContainer getToListenForMouse ()
  {
    return this.toListenForMouse;
  }

}
