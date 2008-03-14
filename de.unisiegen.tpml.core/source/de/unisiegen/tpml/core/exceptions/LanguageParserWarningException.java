package de.unisiegen.tpml.core.exceptions;


import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.types.Type;


/**
 * This {@link LanguageParserWarningException} is used, if an
 * {@link  Expression} or {@link Type} is noz complete.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see LanguageParserException
 */
public final class LanguageParserWarningException extends
    LanguageParserException
{

  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = 4128074579334924253L;


  /**
   * The text, which should be inserted.
   */
  private String insertText;


  /**
   * Initializes the exception.
   * 
   * @param pMessages The message.
   * @param pParserStartOffset The parser start offset.
   * @param pParserEndOffset The parser end offset.
   */
  public LanguageParserWarningException ( String pMessages,
      int pParserStartOffset, int pParserEndOffset )
  {
    this ( pMessages, pParserStartOffset, pParserEndOffset, "" ); //$NON-NLS-1$
  }


  /**
   * Initializes the exception.
   * 
   * @param pMessages The message.
   * @param pParserStartOffset The parser start offset.
   * @param pParserEndOffset The parser end offset.
   * @param pInsertText The text, which should be inserted.
   */
  public LanguageParserWarningException ( String pMessages,
      int pParserStartOffset, int pParserEndOffset, String pInsertText )
  {
    super ( pMessages, pParserStartOffset, pParserEndOffset );
    this.insertText = pInsertText;
  }


  /**
   * Returns the insertText.
   * 
   * @return The insertText.
   * @see #insertText
   */
  public String getInsertText ()
  {
    return this.insertText;
  }
}
