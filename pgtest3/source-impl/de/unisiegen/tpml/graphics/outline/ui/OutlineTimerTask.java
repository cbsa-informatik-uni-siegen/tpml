package de.unisiegen.tpml.graphics.outline.ui ;


import java.util.TimerTask ;
import de.unisiegen.tpml.graphics.outline.AbstractOutline ;


/**
 * Invokes the execute method in the <code>Outline</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineTimerTask extends TimerTask
{
  /**
   * The <code>AbstractOutline</code>.
   */
  private AbstractOutline abstractOutline ;


  /**
   * Initilizes the <code>OutlineTimerTask</code>.
   * 
   * @param pAbstractOutline The <code>AbstractOutline</code>.
   */
  public OutlineTimerTask ( AbstractOutline pAbstractOutline )
  {
    this.abstractOutline = pAbstractOutline ;
  }


  /**
   * Invokes the execute method in the <code>AbstractOutline</code>.
   * 
   * @see TimerTask#run()
   */
  @ Override
  public void run ( )
  {
    this.abstractOutline.execute ( ) ;
  }
}
