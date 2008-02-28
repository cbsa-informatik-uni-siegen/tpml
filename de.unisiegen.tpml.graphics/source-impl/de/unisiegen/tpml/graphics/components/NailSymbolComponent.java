package de.unisiegen.tpml.graphics.components;


import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.util.Theme;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;


/**
 * this class renders the Nail Symbol
 * 
 * @author Benjamin Mies
 */
public class NailSymbolComponent extends JComponent
{

  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = -1108849991978877212L;


  /**
   * Renderer that is used to render the label
   */
  private PrettyStringRenderer renderer;


  /**
   * the constructor
   */
  public NailSymbolComponent ()
  {
    super ();
    this.renderer = new PrettyStringRenderer ();
  }


  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ()
  {
    if ( this.renderer != null )
    {
      this.renderer.checkLinewraps ();
    }
  }


  /**
   * Calculates the size needed to propperly render the labelComponent
   * 
   * @param maxWidth
   * @return the Dimension needed to render the label
   */
  public Dimension getNeededSize ( @SuppressWarnings ( "unused" )
  int maxWidth )
  {
    Dimension result = new Dimension ( 0, 0 );
    result.width += AbstractRenderer.getTextFontMetrics ().stringWidth ( "--" ); //$NON-NLS-1$
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
    // make sure that we have a type to renderer
    if ( this.renderer == null )
    {
      return;
    }
    // assuming the size of the component will suffice, no testing
    // of any sizes will happen.
    /*
     * just to get reminded: no environment: expression storeenvironment:
     * (expression [env]) typeenvironment: [env] |> expression
     */
    int posX = 0;
    // draw the arrow character in the vertical center
    int centerV = getHeight () / 2;
    centerV += AbstractRenderer.getTextFontMetrics ().getAscent () / 2;
    gc.setFont ( AbstractRenderer.getTextFont () );
    FontMetrics fm = AbstractRenderer.getTextFontMetrics ();
    gc.setColor ( Theme.currentTheme ().getExpressionColor () );
    // The nail as Polygon
    Polygon polygon = new Polygon ();
    // polygon.addPoint ( posX, ( posY - AbstractRenderer.fontHeight / 2 +
    // fontDescent )
    // + AbstractRenderer.fontHeight / 5 );
    // polygon.addPoint ( posX, ( posY - AbstractRenderer.fontHeight / 2 +
    // fontDescent )
    // - AbstractRenderer.fontHeight / 5 );
    // polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2 +
    // fontDescent );
    // polygon.addPoint ( posX + AbstractRenderer.keywordFontMetrics.stringWidth
    // ( "--" ), posY
    // - AbstractRenderer.fontHeight / 2 + fontDescent );
    // polygon.addPoint ( posX, posY - AbstractRenderer.fontHeight / 2 +
    // fontDescent );
    polygon.addPoint ( posX, ( fm.getAscent () - fm.getHeight () / 2 + fm
        .getDescent () )
        + fm.getHeight () / 5 );
    polygon.addPoint ( posX, ( fm.getAscent () - fm.getHeight () / 2 + fm
        .getDescent () )
        - fm.getHeight () / 5 );
    polygon.addPoint ( posX, fm.getAscent () - fm.getHeight () / 2
        + fm.getDescent () );
    polygon.addPoint ( posX + fm.stringWidth ( "--" ), fm.getAscent () //$NON-NLS-1$
        - fm.getHeight () / 2 + fm.getDescent () );
    polygon.addPoint ( posX, fm.getAscent () - fm.getHeight () / 2
        + fm.getDescent () );
    // render it
    gc.drawPolygon ( polygon );
  }
}
