package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.KeyEvent ;
import java.awt.event.KeyListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTClipboard ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * This class listens for key events. It can copy the selected node as a string
 * into the clipboard.
 * 
 * @author Christian Fehler
 */
public class ASTKeyListener implements KeyListener
{
  /**
   * The AbstractSyntaxTree UI.
   */
  private ASTUI aSTUI ;


  /**
   * Initializes the ASTKeyListener.
   * 
   * @param pASTUI The AbstractSyntaxTree UI.
   */
  public ASTKeyListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  /**
   * Copies the selection into the clipboard.
   */
  public void copy ( )
  {
    DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) this.aSTUI
        .getJTreeAbstractSyntaxTree ( ).getSelectionPath ( )
        .getLastPathComponent ( ) ;
    if ( node != null )
    {
      ASTClipboard.getInstance ( ).copy (
          ( ( ASTNode ) node.getUserObject ( ) ).getExpressionString ( ) ) ;
    }
  }


  /**
   * Handles the key event.
   * 
   * @param pKeyEvent The key event.
   */
  private void handleKeyEvent ( KeyEvent pKeyEvent )
  {
    /*
     * jTreeAbstractSyntaxTree
     */
    if ( pKeyEvent.getSource ( ).equals (
        this.aSTUI.getJTreeAbstractSyntaxTree ( ) ) )
    {
      /*
       * Copy the selected node as a string into the clipboard.
       */
      if ( ( pKeyEvent.isControlDown ( ) )
          && ( pKeyEvent.getKeyCode ( ) == KeyEvent.VK_C ) )
      {
        copy ( ) ;
      }
    }
    pKeyEvent.consume ( ) ;
  }


  /**
   * This method is invoked if a key is pressed.
   * 
   * @param pKeyEvent The key event.
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
  public void keyPressed ( KeyEvent pKeyEvent )
  {
    handleKeyEvent ( pKeyEvent ) ;
  }


  /**
   * This method is invoked if a key is released.
   * 
   * @param pKeyEvent The key event.
   * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
   */
  public void keyReleased ( KeyEvent pKeyEvent )
  {
    handleKeyEvent ( pKeyEvent ) ;
  }


  /**
   * This method is invoked if a key is typed.
   * 
   * @param pKeyEvent The key event.
   */
  public void keyTyped ( @ SuppressWarnings ( "unused" )
  KeyEvent pKeyEvent )
  {
    // Do nothing.
  }
}
