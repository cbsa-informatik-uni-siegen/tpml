package de.unisiegen.tpml.graphics.outline.util ;


import java.awt.Toolkit ;
import java.awt.datatransfer.Clipboard ;
import java.awt.datatransfer.ClipboardOwner ;
import java.awt.datatransfer.DataFlavor ;
import java.awt.datatransfer.StringSelection ;
import java.awt.datatransfer.Transferable ;
import java.awt.datatransfer.UnsupportedFlavorException ;
import java.io.IOException ;


/**
 * Can copy a text into the clipboard and can read the <code>String</code>,
 * which is current saved in the clipboard.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class OutlineClipboard implements ClipboardOwner
{
  /**
   * An empty <code>String</code>.
   */
  private static final String EMPTY = "" ; //$NON-NLS-1$


  /**
   * The unused <code>String</code> for the <code>SuppressWarnings</code>.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The clipboard.
   */
  private Clipboard clipboard ;


  /**
   * The single object of <code>OutlineClipboard</code>.
   */
  private static OutlineClipboard outlineClipboard = null ;


  /**
   * Initializes the <code>OutlineClipboard</code>.
   */
  private OutlineClipboard ( )
  {
    this.clipboard = Toolkit.getDefaultToolkit ( ).getSystemClipboard ( ) ;
  }


  /**
   * Returns the single object of <code>OutlineClipboard</code>.
   * 
   * @return The single object of <code>OutlineClipboard</code>.
   */
  public final static OutlineClipboard getInstance ( )
  {
    if ( outlineClipboard == null )
    {
      outlineClipboard = new OutlineClipboard ( ) ;
    }
    return outlineClipboard ;
  }


  /**
   * Copies the given text into the clipboard.
   * 
   * @param pText The text, which should be copied into the clipboard.
   */
  public final void copy ( String pText )
  {
    StringSelection stringSelection = new StringSelection ( pText ) ;
    this.clipboard.setContents ( stringSelection , this ) ;
  }


  /**
   * This method is invoked if the ownership is lost.
   * 
   * @param pClipboard The <code>Clipboard</code>.
   * @param pContents The <code>Contests</code>.
   * @see ClipboardOwner#lostOwnership(Clipboard, Transferable)
   */
  public final void lostOwnership ( @ SuppressWarnings ( UNUSED )
  Clipboard pClipboard , @ SuppressWarnings ( UNUSED )
  Transferable pContents )
  {
    // Do Nothing
  }


  /**
   * Returns the <code>String</code>, which is current saved in the
   * clipboard.
   * 
   * @return The <code>String</code>, which is current saved in the
   *         clipboard.
   */
  public final String paste ( )
  {
    Transferable transfer = this.clipboard.getContents ( null ) ;
    try
    {
      return ( String ) transfer.getTransferData ( DataFlavor.stringFlavor ) ;
    }
    catch ( UnsupportedFlavorException e )
    {
      // Do Nothing
    }
    catch ( IOException e )
    {
      // Do Nothing
    }
    return EMPTY ;
  }
}
