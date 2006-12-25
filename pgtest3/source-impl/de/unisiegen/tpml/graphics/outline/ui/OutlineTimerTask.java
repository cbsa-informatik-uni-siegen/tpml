package de.unisiegen.tpml.graphics.outline.ui ;


import java.util.TimerTask ;
import de.unisiegen.tpml.graphics.outline.AbstractOutline;


/**
 * Invokes the execute method in the AbstractOutline.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineTimerTask extends TimerTask
{
  /**
   * The AbstractOutline.
   */
  private AbstractOutline abstractOutline ;


  /**
   * Initilizes the OutlineTimerTask.
   * 
   * @param pAbstractOutline The AbstractOutline.
   */
  public OutlineTimerTask ( AbstractOutline pAbstractOutline )
  {
    this.abstractOutline = pAbstractOutline ;
  }


  /**
   * Invokes the execute method in the AbstractOutline.
   * 
   * @see java.util.TimerTask#run()
   */
  @ Override
  public void run ( )
  {
    this.abstractOutline.execute ( ) ;
  }
}
