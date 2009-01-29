package de.unisiegen.tpml.graphics.components;


import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.graphics.renderer.AbstractRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.ToListenForMouseContainer;


/**
 * TODO
 */
public class EnvironmentLabelBigStepClosure extends JComponent
{

  public void setEnvironments ( final ArrayList < PrettyString > envs )
  {
    this.envs = envs;
    repaint ();
  }


  public Dimension getNeededSize ()
  {
    Dimension result = new Dimension ( 0, this.envs.size ()
        * AbstractRenderer.getTextFontMetrics ().getHeight () );

    for ( PrettyString str : this.envs )
      result.width = Math.max ( result.width, AbstractRenderer
          .getTextFontMetrics ().stringWidth ( str.toString () + 20 ) ); //FIXME:
                                                                         // why
                                                                         // is
                                                                         // the
                                                                         // length
                                                                         // calculated
                                                                         // here
                                                                         // to
                                                                         // sufficient
                                                                         // ?

    return result;
  }


  @Override
  protected void paintComponent ( Graphics gc )
  {
    int posX = 0;
    int posY = 0;

    gc.setFont ( AbstractRenderer.getTextFont () );
    gc.setColor ( AbstractRenderer.getTextColor () );

    for ( PrettyString str : this.envs )
    {
      this.expressionRenderer.setPrettyString ( str );
      this.expressionRenderer.render ( posX, posY, getWidth (), getHeight (),
          gc, this.bonds, this.toListenForMouse );
      posY += AbstractRenderer.getTextFontMetrics ().getHeight ();
    }
  }


  private PrettyStringRenderer expressionRenderer = new PrettyStringRenderer ();


  private ShowBonds bonds = new ShowBonds ();


  private ArrayList < PrettyString > envs;


  private ToListenForMouseContainer toListenForMouse = new ToListenForMouseContainer ();
}
