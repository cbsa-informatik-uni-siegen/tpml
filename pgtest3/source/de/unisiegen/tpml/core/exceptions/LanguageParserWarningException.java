package de.unisiegen.tpml.core.exceptions ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * This {@link LanguageParserWarningException} is used, if an
 * {@link  Expression} or {@link Type} is noz complete.
 * 
 * @author Christian Fehler
 * @see LanguageParserException
 */
public final class LanguageParserWarningException extends
    LanguageParserException
{
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = 4128074579334924253L ;


  /**
   * Initializes the exception.
   * 
   * @param pMessages The message.
   * @param pParserStartOffset The parser start offset.
   * @param pParserEndOffset The parser end offset.
   */
  public LanguageParserWarningException ( String pMessages ,
      int pParserStartOffset , int pParserEndOffset )
  {
    super ( pMessages , pParserStartOffset , pParserEndOffset ) ;
  }
}
