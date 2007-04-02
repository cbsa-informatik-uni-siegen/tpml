package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.KeyEvent ;
import java.awt.event.KeyListener ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlineClipboard ;


/**
 * This class listens for <code>KeyEvents</code>. It can copy the selected
 * node as a <code>String</code> into the clipboard.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineKeyListener implements KeyListener
{
  /**
   * The unused <code>String</code> for the <code>SuppressWarnings</code>.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The {@link OutlineUI}.
   */
  private OutlineUI outlineUI ;


  /**
   * Initializes the {@link OutlineKeyListener}.
   * 
   * @param pOutlineUI The {@link OutlineUI}.
   */
  public OutlineKeyListener ( OutlineUI pOutlineUI )
  {
    this.outlineUI = pOutlineUI ;
  }


  /**
   * Copies the {@link Expression} as a <code>String</code> of the selected
   * node into the clipboard using the {@link OutlineClipboard}.
   */
  public final void copy ( )
  {
    OutlineNode outlineNode = ( OutlineNode ) this.outlineUI.getJTreeOutline ( )
        .getSelectionPath ( ).getLastPathComponent ( ) ;
    if ( outlineNode != null )
    {
      OutlineClipboard.getInstance ( ).copy ( outlineNode.getNodeString ( ) ) ;
    }
  }


  /**
   * Handles the <code>KeyEvent</code>.
   * 
   * @param pKeyEvent The <code>KeyEvent</code>.
   */
  private final void handleKeyEvent ( KeyEvent pKeyEvent )
  {
    /*
     * jTreeAbstractSyntaxTree
     */
    if ( pKeyEvent.getSource ( ).equals ( this.outlineUI.getJTreeOutline ( ) ) )
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
  public final void keyPressed ( KeyEvent pKeyEvent )
  {
    handleKeyEvent ( pKeyEvent ) ;
  }


  /**
   * This method is invoked if a key is released.
   * 
   * @param pKeyEvent The <code>KeyEvent</code>.
   * @see KeyListener#keyReleased(KeyEvent)
   */
  public final void keyReleased ( KeyEvent pKeyEvent )
  {
    handleKeyEvent ( pKeyEvent ) ;
  }


  /**
   * This method is invoked if a key is typed.
   * 
   * @param pKeyEvent The <code>KeyEvent</code>.
   * @see KeyListener#keyTyped(KeyEvent)
   */
  public final void keyTyped ( @ SuppressWarnings ( UNUSED )
  KeyEvent pKeyEvent )
  {
    // Do nothing.
  }
}
