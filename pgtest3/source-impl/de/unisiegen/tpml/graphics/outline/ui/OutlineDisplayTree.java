package de.unisiegen.tpml.graphics.outline.ui ;


import javax.swing.tree.DefaultMutableTreeNode ;


/**
 * Displays the tree in the GUI.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineDisplayTree implements Runnable
{
  /**
   * The AbstractOutline UI.
   */
  private OutlineUI outlineUI ;


  /**
   * The root node.
   */
  private DefaultMutableTreeNode rootNode ;


  /**
   * Initilizes the OutlineDisplayTree.
   * 
   * @param pASTUI The AbstractOutline UI.
   * @param pRootNode The root node.
   */
  public OutlineDisplayTree ( OutlineUI pASTUI , DefaultMutableTreeNode pRootNode )
  {
    this.outlineUI = pASTUI ;
    this.rootNode = pRootNode ;
  }


  /**
   * Displays the tree in the GUI.
   * 
   * @see java.lang.Runnable#run()
   */
  public void run ( )
  {
    this.outlineUI.setRootNode ( this.rootNode ) ;
  }
}
