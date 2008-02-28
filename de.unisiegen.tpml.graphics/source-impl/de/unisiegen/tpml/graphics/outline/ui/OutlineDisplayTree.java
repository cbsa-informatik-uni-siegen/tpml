package de.unisiegen.tpml.graphics.outline.ui;


import de.unisiegen.tpml.graphics.outline.DefaultOutline;


/**
 * Displays the tree in the {@link OutlineUI}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineDisplayTree implements Runnable
{

  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline;


  /**
   * Initilizes the {@link OutlineDisplayTree}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineDisplayTree ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline;
  }


  /**
   * Displays the tree in the {@link OutlineUI}.
   * 
   * @see Runnable#run()
   */
  public final void run ()
  {
    this.defaultOutline.setRootNode ();
  }
}
