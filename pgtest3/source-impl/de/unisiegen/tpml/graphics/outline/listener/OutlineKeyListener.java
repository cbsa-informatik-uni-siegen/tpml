package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.KeyEvent ;
import java.awt.event.KeyListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlineClipboard ;


/**
 * This class listens for key events. It can copy the selected node as a string
 * into the clipboard.
 * 
 * @author Christian Fehler
 */
public class OutlineKeyListener implements KeyListener
{
  /**
   * The Outline UI.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the OutlineKeyListener.
   * 
   * @param pOutlineUI The Outline UI.
   */
  public OutlineKeyListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
  }


  /**
   * Copies the selection into the clipboard.
   */
  public void copy ( )
  {
    DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) this.outlineUI
        .getJTreeAbstractSyntaxTree ( ).getSelectionPath ( )
        .getLastPathComponent ( ) ;
    if ( node != null )
    {
      OutlineClipboard.getInstance ( ).copy (
          ( ( OutlineNode ) node.getUserObject ( ) ).getExpressionString ( ) ) ;
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
        this.outlineUI.getJTreeAbstractSyntaxTree ( ) ) )
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
