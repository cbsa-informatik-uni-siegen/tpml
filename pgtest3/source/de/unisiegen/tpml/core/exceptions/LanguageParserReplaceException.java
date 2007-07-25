package de.unisiegen.tpml.core.exceptions ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Body ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;


/**
 * This {@link LanguageParserReplaceException} is used, if more than one error
 * should be highlighted in the source code view and some {@link Attribute}
 * {@link Identifier}s should be replaced.
 * 
 * @author Christian Fehler
 * @see LanguageParserException
 */
public final class LanguageParserReplaceException extends
    LanguageParserException
{
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 2588539895990502861L ;


  /**
   * Throws a <code>LanguageParserReplaceException</code> if the {@link Body}
   * consist of {@link Attribute}s with the same {@link Identifier}.
   * 
   * @param pNegativeIdentifiers The input list of negative {@link Identifier}s.
   * @param pReplaceIdentifiers The input list of {@link Identifier}s which
   *          should be replaced.
   * @param pReplaceText The new text of the {@link Identifier}s.
   */
  public static void throwExceptionBody (
      ArrayList < Identifier > pNegativeIdentifiers ,
      ArrayList < Identifier > pReplaceIdentifiers , String pReplaceText )
  {
    String [ ] messageNegative = new String [ pNegativeIdentifiers.size ( ) ] ;
    int [ ] startOffsetNegative = new int [ pNegativeIdentifiers.size ( ) ] ;
    int [ ] endOffsetNegative = new int [ pNegativeIdentifiers.size ( ) ] ;
    for ( int j = 0 ; j < pNegativeIdentifiers.size ( ) ; j ++ )
    {
      messageNegative [ j ] = MessageFormat.format ( Messages
          .getString ( "Parser.20" ) , pNegativeIdentifiers.get ( j ) ) ; //$NON-NLS-1$
      startOffsetNegative [ j ] = pNegativeIdentifiers.get ( j )
          .getParserStartOffset ( ) ;
      endOffsetNegative [ j ] = pNegativeIdentifiers.get ( j )
          .getParserEndOffset ( ) ;
    }
    String [ ] messageReplace = new String [ pReplaceIdentifiers.size ( ) ] ;
    int [ ] startOffsetReplace = new int [ pReplaceIdentifiers.size ( ) ] ;
    int [ ] endOffsetReplace = new int [ pReplaceIdentifiers.size ( ) ] ;
    for ( int j = 0 ; j < pReplaceIdentifiers.size ( ) ; j ++ )
    {
      messageReplace [ j ] = MessageFormat.format ( Messages
          .getString ( "Parser.21" ) , pReplaceIdentifiers.get ( j ) ) ; //$NON-NLS-1$
      startOffsetReplace [ j ] = pReplaceIdentifiers.get ( j )
          .getParserStartOffset ( ) ;
      endOffsetReplace [ j ] = pReplaceIdentifiers.get ( j )
          .getParserEndOffset ( ) ;
    }
    throw new LanguageParserReplaceException ( messageNegative ,
        startOffsetNegative , endOffsetNegative , messageReplace ,
        startOffsetReplace , endOffsetReplace , pReplaceText ) ;
  }


  /**
   * The array of shown negative {@link Identifier} messages.
   */
  private String [ ] messagesNegative ;


  /**
   * The array of negative {@link Identifier} parser start offsets.
   */
  private int [ ] parserStartOffsetNegative ;


  /**
   * The array of negative {@link Identifier} parser end offsets.
   */
  private int [ ] parserEndOffsetNegative ;


  /**
   * The array of shown replace {@link Identifier} messages.
   */
  private String [ ] messagesReplace ;


  /**
   * The array of replace {@link Identifier} parser start offsets.
   */
  private int [ ] parserStartOffsetReplace ;


  /**
   * The array of replace {@link Identifier} parser end offsets.
   */
  private int [ ] parserEndOffsetReplace ;


  /**
   * The new text of the {@link Identifier}s.
   */
  private String replaceText ;


  /**
   * Initializes the exception.
   * 
   * @param pMessagesRename The array of shown messages.
   * @param pParserStartOffsetRename The array of parser start offsets.
   * @param pParserEndOffsetRename The array of parser end offsets.
   * @param pReplaceText The new text of the {@link Identifier}s.
   */
  public LanguageParserReplaceException ( String pMessagesRename ,
      int pParserStartOffsetRename , int pParserEndOffsetRename ,
      String pReplaceText )
  {
    super ( null , - 1 , - 1 ) ;
    this.messagesReplace = new String [ ]
    { pMessagesRename } ;
    this.parserStartOffsetReplace = new int [ ]
    { pParserStartOffsetRename } ;
    this.parserEndOffsetReplace = new int [ ]
    { pParserEndOffsetRename } ;
    this.replaceText = pReplaceText ;
  }


  /**
   * Initializes the exception.
   * 
   * @param pMessagesNegative The array of shown negative {@link Identifier}
   *          messages.
   * @param pParserStartOffsetNegative
   * @param pParserEndOffsetNegative
   * @param pMessagesReplace The array of shown replace {@link Identifier}
   *          messages.
   * @param pParserStartOffsetReplace The array of parser start offsets.
   * @param pParserEndOffsetReplace The array of parser end offsets.
   * @param pReplaceText The new text of the {@link Identifier}s.
   */
  public LanguageParserReplaceException ( String [ ] pMessagesNegative ,
      int [ ] pParserStartOffsetNegative , int [ ] pParserEndOffsetNegative ,
      String [ ] pMessagesReplace , int [ ] pParserStartOffsetReplace ,
      int [ ] pParserEndOffsetReplace , String pReplaceText )
  {
    super ( null , - 1 , - 1 ) ;
    this.messagesNegative = pMessagesNegative ;
    this.parserStartOffsetNegative = pParserStartOffsetNegative ;
    this.parserEndOffsetNegative = pParserEndOffsetNegative ;
    this.messagesReplace = pMessagesReplace ;
    this.parserStartOffsetReplace = pParserStartOffsetReplace ;
    this.parserEndOffsetReplace = pParserEndOffsetReplace ;
    this.replaceText = pReplaceText ;
  }


  /**
   * Returns the messagesNegative.
   * 
   * @return The messagesNegative.
   * @see #messagesNegative
   */
  public String [ ] getMessagesNegative ( )
  {
    return this.messagesNegative ;
  }


  /**
   * Returns the messagesReplace.
   * 
   * @return The messagesReplace.
   * @see #messagesReplace
   */
  public String [ ] getMessagesReplace ( )
  {
    return this.messagesReplace ;
  }


  /**
   * Returns the parserEndOffsetNegative.
   * 
   * @return The parserEndOffsetNegative.
   * @see #parserEndOffsetNegative
   */
  public int [ ] getParserEndOffsetNegative ( )
  {
    return this.parserEndOffsetNegative ;
  }


  /**
   * Returns the parserEndOffsetReplace.
   * 
   * @return The parserEndOffsetReplace.
   * @see #parserEndOffsetReplace
   */
  public int [ ] getParserEndOffsetReplace ( )
  {
    return this.parserEndOffsetReplace ;
  }


  /**
   * Returns the parserStartOffsetNegative.
   * 
   * @return The parserStartOffsetNegative.
   * @see #parserStartOffsetNegative
   */
  public int [ ] getParserStartOffsetNegative ( )
  {
    return this.parserStartOffsetNegative ;
  }


  /**
   * Returns the parserStartOffsetReplace.
   * 
   * @return The parserStartOffsetReplace.
   * @see #parserStartOffsetReplace
   */
  public int [ ] getParserStartOffsetReplace ( )
  {
    return this.parserStartOffsetReplace ;
  }


  /**
   * Returns the replaceText.
   * 
   * @return The replaceText.
   * @see #replaceText
   */
  public String getReplaceText ( )
  {
    return this.replaceText ;
  }
}
