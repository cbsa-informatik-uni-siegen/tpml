package de.unisiegen.tpml.core.languages ;


/**
 * This exception is thrown whenever a parse error occurs in the
 * {@link LanguageParser}.
 * 
 * @author Benedikt Meurer
 * @version $Rev:291M $
 * @see de.unisiegen.tpml.core.languages.LanguageScannerException
 */
public class LanguageParserException extends LanguageScannerException
{
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = - 5267613216858473920L ;


  /**
   * Allocates a new <code>LanguageParserException</code> with the specified
   * parameters.
   * 
   * @param message the error message.
   * @param left the left character offset.
   * @param right the right character offset.
   * @see LanguageScannerException#getLeft()
   * @see LanguageScannerException#getRight()
   */
  public LanguageParserException ( String message , int left , int right )
  {
    super ( left , right , message ) ;
  }
}
