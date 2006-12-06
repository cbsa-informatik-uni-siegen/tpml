package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Toolkit ;
import java.awt.datatransfer.Clipboard ;
import java.awt.datatransfer.ClipboardOwner ;
import java.awt.datatransfer.StringSelection ;
import java.awt.datatransfer.Transferable ;


/**
 * Copies the a text into the clipboard.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTClipboard implements ClipboardOwner
{
  /**
   * The clipboard.
   */
  private Clipboard clipboard ;


  /**
   * Initializes the ASTClipboard.
   */
  public ASTClipboard ( )
  {
    this.clipboard = Toolkit.getDefaultToolkit ( ).getSystemClipboard ( ) ;
  }


  /**
   * Copies the given text into the clipboard.
   * 
   * @param pText The text, which should be copied into the clipboard.
   */
  public void copy ( String pText )
  {
    StringSelection stringSelection = new StringSelection ( pText ) ;
    this.clipboard.setContents ( stringSelection , this ) ;
  }


  /**
   * This method is invoked if the ownership is lost.
   * 
   * @param pClipboard The clipboard.
   * @param pContents The contests.
   * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard,
   *      java.awt.datatransfer.Transferable)
   */
  public void lostOwnership ( @ SuppressWarnings ( "unused" )
  Clipboard pClipboard , @ SuppressWarnings ( "unused" )
  Transferable pContents )
  {
    // Do Nothing
  }
}
