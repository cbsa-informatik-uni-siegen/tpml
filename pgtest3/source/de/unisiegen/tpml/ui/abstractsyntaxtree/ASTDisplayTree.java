package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import javax.swing.tree.DefaultMutableTreeNode ;


/**
 * Displays the tree in the GUI.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTDisplayTree implements Runnable
{
  /**
   * The AbstractSyntaxTree UI.
   */
  private ASTUI aSTUI ;


  /**
   * The root node.
   */
  private DefaultMutableTreeNode rootNode ;


  /**
   * Initilizes the ASTDisplayTree.
   * 
   * @param pASTUI The AbstractSyntaxTree UI.
   * @param pRootNode The root node.
   */
  public ASTDisplayTree ( ASTUI pASTUI , DefaultMutableTreeNode pRootNode )
  {
    this.aSTUI = pASTUI ;
    this.rootNode = pRootNode ;
  }


  /**
   * Displays the tree in the GUI.
   * 
   * @see java.lang.Runnable#run()
   */
  public void run ( )
  {
    this.aSTUI.setRootNode ( this.rootNode ) ;
  }
}
