package de.unisiegen.tpml.graphics.outline.ui ;


import java.util.TimerTask ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;


/**
 * Invokes the execute method in the {@link DefaultOutline}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineTimerTask extends TimerTask
{
  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * Initilizes the {@link OutlineTimerTask}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineTimerTask ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * Invokes the execute method in the {@link DefaultOutline}.
   * 
   * @see TimerTask#run()
   */
  @ Override
  public final void run ( )
  {
    this.defaultOutline.execute ( ) ;
  }
}
