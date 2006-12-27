package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.KeyEvent ;
import java.awt.event.KeyListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlineClipboard ;


/**
 * This class listens for <code>KeyEvents</code>. It can copy the selected
 * node as a <code>String</code> into the clipboard.
 * 
 * @author Christian Fehler
 */
public class OutlineKeyListener implements KeyListener
{
  /**
   * The <code>OutlineUI</code>.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the <code>OutlineKeyListener</code>.
   * 
   * @param pOutlineUI The <code>OutlineUI</code>.
   */
  public OutlineKeyListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
  }


  /**
   * Copies the <code>Expression</code> as a <code>String</code> of the
   * selected node into the clipboard using the <code>OutlineClipboard</code>.
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
   * Handles the <code>KeyEvent</code>.
   * 
   * @param pKeyEvent The <code>KeyEvent</code>.
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
   * @param pKeyEvent The <code>KeyEvent</code>.
   * @see KeyListener#keyPressed(KeyEvent)
   */
  public void keyPressed ( KeyEvent pKeyEvent )
  {
    handleKeyEvent ( pKeyEvent ) ;
  }


  /**
   * This method is invoked if a key is released.
   * 
   * @param pKeyEvent The <code>KeyEvent</code>.
   * @see KeyListener#keyReleased(KeyEvent)
   */
  public void keyReleased ( KeyEvent pKeyEvent )
  {
    handleKeyEvent ( pKeyEvent ) ;
  }


  /**
   * This method is invoked if a key is typed.
   * 
   * @param pKeyEvent The <code>KeyEvent</code>.
   * @see KeyListener#keyTyped(KeyEvent)
   */
  public void keyTyped ( @ SuppressWarnings ( "unused" )
  KeyEvent pKeyEvent )
  {
    // Do nothing.
  }
}
