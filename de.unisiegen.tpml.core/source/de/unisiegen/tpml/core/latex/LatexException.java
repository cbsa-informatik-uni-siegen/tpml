package de.unisiegen.tpml.core.latex;


/**
 * This {@link LatexException} is used, if something in the latex export does
 * not work.
 * 
 * @author Christian Fehler
 */
public final class LatexException extends Exception
{

  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = -2551220796746494338L;


  /**
   * Initializes the exception.
   * 
   * @param pText The detail message.
   */
  public LatexException ( String pText )
  {
    super ( pText );
  }
}
