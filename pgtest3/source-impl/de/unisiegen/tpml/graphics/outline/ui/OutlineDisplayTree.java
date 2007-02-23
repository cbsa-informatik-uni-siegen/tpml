package de.unisiegen.tpml.graphics.outline.ui ;


import de.unisiegen.tpml.graphics.outline.AbstractOutline ;


/**
 * Displays the tree in the {@link OutlineUI}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineDisplayTree implements Runnable
{
  /**
   * The {@link AbstractOutline}.
   */
  private AbstractOutline abstractOutline ;


  /**
   * Initilizes the {@link OutlineDisplayTree}.
   * 
   * @param pAbstractOutline The {@link AbstractOutline}.
   */
  public OutlineDisplayTree ( AbstractOutline pAbstractOutline )
  {
    this.abstractOutline = pAbstractOutline ;
  }


  /**
   * Displays the tree in the {@link OutlineUI}.
   * 
   * @see Runnable#run()
   */
  public final void run ( )
  {
    this.abstractOutline.setRootNode ( ) ;
  }
}
