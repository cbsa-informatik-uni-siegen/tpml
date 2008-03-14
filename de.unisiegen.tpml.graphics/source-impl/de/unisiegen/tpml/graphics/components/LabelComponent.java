package de.unisiegen.tpml.graphics.components;


import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.unisiegen.tpml.core.util.Theme;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;


/**
 * this class renders JLabels
 * 
 * @author Benjamin Mies
 * @version $Id$
 */
public class LabelComponent extends JComponent
{

  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 1365792225448341074L;


  /**
   * Renderer that is used to render the label
   */
  private PrettyStringRenderer typeRenderer;


  /**
   * The current label.
   */
  private JLabel label;


  /**
   * The size of the type.
   */
  private Dimension labelSize;


  /**
   * the constructor
   */
  public LabelComponent ()
  {
    super ();
    this.typeRenderer = new PrettyStringRenderer ();
  }


  /**
   * Causes the PrettyStringRenderer to recheck the linewraps
   */
  public void reset ()
  {
    if ( this.typeRenderer != null )
    {
      this.typeRenderer.checkLinewraps ();
    }
  }


  /**
   * Sets the label that should rendered.
   * 
   * @param pLabel the type to render
   */
  public void setLabel ( JLabel pLabel )
  {
    // check if we have a new expression
    if ( this.label != pLabel )
    {
      // update to the new label
      this.label = pLabel;
      // be sure to schedule a repaint
      repaint ();
    }
  }


  /**
   * Calculates the size needed to propperly render the labelComponent
   * 
   * @param maxWidth
   * @return the Dimension needed to render the label
   */
  public Dimension getNeededSize ( int maxWidth )
  {
    Dimension result = new Dimension ( 0, 0 );
    result.width += AbstractRenderer.getTextFontMetrics ().stringWidth (
        this.label.toString () );
    if ( ( this.label != null ) && ( this.typeRenderer != null ) )
    {
      this.labelSize = this.typeRenderer.getNeededSizeAll_ ( maxWidth );
      result.width += this.labelSize.width;
      result.height = Math.max ( result.height, this.labelSize.height );
    }
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
    if ( this.typeRenderer == null )
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
    gc.setColor ( Theme.currentTheme ().getExpressionColor () );
    gc.drawString ( this.label.getText (), posX, centerV );
    posX += AbstractRenderer.getTextFontMetrics ().stringWidth (
        this.label.toString () );
  }


  /**
   * Get the label of this component
   * 
   * @return label the active JLabel
   */
  public JLabel getLabel ()
  {
    return this.label;
  }
}
