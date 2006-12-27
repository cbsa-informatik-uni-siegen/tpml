package de.unisiegen.tpml.graphics.outline.ui ;


import javax.swing.tree.DefaultMutableTreeNode ;


/**
 * Displays the tree in the <code>OutlineUI</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineDisplayTree implements Runnable
{
  /**
   * The <code>OutlineUI</code>.
   */
  private OutlineUI outlineUI ;


  /**
   * The root node.
   */
  private DefaultMutableTreeNode rootNode ;


  /**
   * Initilizes the <code>OutlineDisplayTree</code>.
   * 
   * @param pOutlineUI The <code>OutlineUI</code>.
   * @param pRootNode The root node.
   */
  public OutlineDisplayTree ( OutlineUI pOutlineUI ,
      DefaultMutableTreeNode pRootNode )
  {
    this.outlineUI = pOutlineUI ;
    this.rootNode = pRootNode ;
  }


  /**
   * Displays the tree in the <code>OutlineUI</code>.
   * 
   * @see Runnable#run()
   */
  public void run ( )
  {
    this.outlineUI.setRootNode ( this.rootNode ) ;
  }
}
