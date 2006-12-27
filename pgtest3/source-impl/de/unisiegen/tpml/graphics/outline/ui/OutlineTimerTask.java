package de.unisiegen.tpml.graphics.outline.ui ;


import java.util.TimerTask ;
import de.unisiegen.tpml.graphics.outline.AbstractOutline ;


/**
 * Invokes the execute method in the {@link AbstractOutline}.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineTimerTask extends TimerTask
{
  /**
   * The {@link AbstractOutline}.
   */
  private AbstractOutline abstractOutline ;


  /**
   * Initilizes the {@link OutlineTimerTask}.
   * 
   * @param pAbstractOutline The {@link AbstractOutline}.
   */
  public OutlineTimerTask ( AbstractOutline pAbstractOutline )
  {
    this.abstractOutline = pAbstractOutline ;
  }


  /**
   * Invokes the execute method in the {@link AbstractOutline}.
   * 
   * @see TimerTask#run()
   */
  @ Override
  public void run ( )
  {
    this.abstractOutline.execute ( ) ;
  }
}
