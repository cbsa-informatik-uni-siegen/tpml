package de.unisiegen.tpml.graphics.outline.ui ;


import javax.swing.tree.DefaultMutableTreeNode ;


/**
 * Displays the tree in the {@link OutlineUI}.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineDisplayTree implements Runnable
{
  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * The root node.
   */
  private DefaultMutableTreeNode rootNode ;


  /**
   * Initilizes the {@link OutlineDisplayTree}.
   * 
   * @param pOutlineUI The {@link OutlineUI}.
   * @param pRootNode The root node.
   */
  public OutlineDisplayTree ( OutlineUI pOutlineUI ,
      DefaultMutableTreeNode pRootNode )
  {
    this.outlineUI = pOutlineUI ;
    this.rootNode = pRootNode ;
  }


  /**
   * Displays the tree in the {@link OutlineUI}.
   * 
   * @see Runnable#run()
   */
  public void run ( )
  {
    this.outlineUI.setRootNode ( this.rootNode ) ;
  }
}
