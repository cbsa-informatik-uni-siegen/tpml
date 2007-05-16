package de.unisiegen.tpml.core.exceptions ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.types.RowType ;


/**
 * This {@link LanguageParserException} is used, if more than one error should
 * be highlighted in the source code view.
 * 
 * @author Christian Fehler
 * @see LanguageParserException
 */
public final class LanguageParserMultiException extends LanguageParserException
{
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 4717084402322482294L ;


  /**
   * The string for variable identifiers.
   */
  private static final String VARIABLE = "Variable" ; //$NON-NLS-1$


  /**
   * The string for attribute identifiers.
   */
  private static final String ATTRIBUTE = "Attribute" ; //$NON-NLS-1$


  /**
   * The string for message identifiers.
   */
  private static final String MESSAGE = "Message" ; //$NON-NLS-1$


  /**
   * The string for self identifiers.
   */
  private static final String SELF = "Self" ; //$NON-NLS-1$


  /**
   * Returns the set of the {@link Identifier} as a string.
   * 
   * @param pIdentifier The input {@link Identifier}.
   * @return The set of the {@link Identifier} as a string.
   */
  private static String getIdSet ( Identifier pIdentifier )
  {
    switch ( pIdentifier.getSet ( ) )
    {
      case VARIABLE :
      {
        return VARIABLE ;
      }
      case ATTRIBUTE :
      {
        return ATTRIBUTE ;
      }
      case MESSAGE :
      {
        return MESSAGE ;
      }
      case SELF :
      {
        return SELF ;
      }
    }
    return null ;
  }


  /**
   * Throws a <code>LanguageParserMultiException</code> if the sets of the
   * {@link Identifier}s are not disjunct.
   * 
   * @param pNegativeIdentifiers The input list of {@link Identifier}s.
   */
  public static void throwExceptionDisjunction (
      ArrayList < Identifier > pNegativeIdentifiers )
  {
    if ( pNegativeIdentifiers.size ( ) > 1 )
    {
      String [ ] message = new String [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] startOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] endOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      for ( int j = 0 ; j < pNegativeIdentifiers.size ( ) ; j ++ )
      {
        message [ j ] = MessageFormat.format (
            Messages.getString ( "Parser.3" ) , getIdSet ( pNegativeIdentifiers //$NON-NLS-1$
                .get ( j ) ) , pNegativeIdentifiers.get ( j ) ) ;
        startOffset [ j ] = pNegativeIdentifiers.get ( j )
            .getParserStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getParserEndOffset ( ) ;
      }
      throw new LanguageParserMultiException ( message , startOffset ,
          endOffset ) ;
    }
  }


  /**
   * Throws a <code>LanguageParserMultiException</code> if the
   * {@link Duplication} consist of {@link Identifier}s with the same name.
   * 
   * @param pNegativeIdentifiers The input list of {@link Identifier}s.
   */
  public static void throwExceptionDuplication (
      ArrayList < Identifier > pNegativeIdentifiers )
  {
    if ( pNegativeIdentifiers.size ( ) > 1 )
    {
      String [ ] message = new String [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] startOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] endOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      for ( int j = 0 ; j < pNegativeIdentifiers.size ( ) ; j ++ )
      {
        message [ j ] = MessageFormat.format (
            Messages.getString ( "Parser.4" ) , pNegativeIdentifiers.get ( j ) ) ; //$NON-NLS-1$
        startOffset [ j ] = pNegativeIdentifiers.get ( j )
            .getParserStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getParserEndOffset ( ) ;
      }
      throw new LanguageParserMultiException ( message , startOffset ,
          endOffset ) ;
    }
  }


  /**
   * Throws a <code>LanguageParserMultiException</code> if the {@link Row}
   * consist of {@link Attribute}s with the same {@link Identifier}.
   * 
   * @param pNegativeIdentifiers The input list of {@link Identifier}s.
   */
  public static void throwExceptionRow (
      ArrayList < Identifier > pNegativeIdentifiers )
  {
    if ( pNegativeIdentifiers.size ( ) > 1 )
    {
      String [ ] message = new String [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] startOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] endOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      for ( int j = 0 ; j < pNegativeIdentifiers.size ( ) ; j ++ )
      {
        message [ j ] = MessageFormat.format (
            Messages.getString ( "Parser.2" ) , pNegativeIdentifiers.get ( j ) ) ; //$NON-NLS-1$
        startOffset [ j ] = pNegativeIdentifiers.get ( j )
            .getParserStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getParserEndOffset ( ) ;
      }
      throw new LanguageParserMultiException ( message , startOffset ,
          endOffset ) ;
    }
  }


  /**
   * Throws a <code>LanguageParserMultiException</code> if the {@link RowType}
   * consist of {@link Method} names with the same {@link Identifier}.
   * 
   * @param pNegativeIdentifiers The input list of {@link Identifier}s.
   */
  public static void throwExceptionRowType (
      ArrayList < Identifier > pNegativeIdentifiers )
  {
    if ( pNegativeIdentifiers.size ( ) > 1 )
    {
      String [ ] message = new String [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] startOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      int [ ] endOffset = new int [ pNegativeIdentifiers.size ( ) ] ;
      for ( int j = 0 ; j < pNegativeIdentifiers.size ( ) ; j ++ )
      {
        message [ j ] = MessageFormat.format (
            Messages.getString ( "Parser.5" ) , pNegativeIdentifiers.get ( j ) ) ; //$NON-NLS-1$
        startOffset [ j ] = pNegativeIdentifiers.get ( j )
            .getParserStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getParserEndOffset ( ) ;
      }
      throw new LanguageParserMultiException ( message , startOffset ,
          endOffset ) ;
    }
  }


  /**
   * The array of shown messages.
   */
  private String [ ] messages ;


  /**
   * The array of parser start offsets.
   */
  private int [ ] parserStartOffset ;


  /**
   * The array of parser end offsets.
   */
  private int [ ] parserEndOffset ;


  /**
   * Initializes the exception.
   * 
   * @param pMessages The array of shown messages.
   * @param pParserStartOffset The array of parser start offsets.
   * @param pParserEndOffset The array of parser end offsets.
   */
  public LanguageParserMultiException ( String [ ] pMessages ,
      int [ ] pParserStartOffset , int [ ] pParserEndOffset )
  {
    super ( pMessages [ 0 ] , pParserStartOffset [ 0 ] , pParserEndOffset [ 0 ] ) ;
    this.messages = pMessages ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Returns the messages.
   * 
   * @return The messages.
   * @see #messages
   */
  public String [ ] getMessages ( )
  {
    return this.messages ;
  }


  /**
   * Returns the endOffset.
   * 
   * @return The endOffset.
   * @see #parserEndOffset
   */
  public int [ ] getParserEndOffset ( )
  {
    return this.parserEndOffset ;
  }


  /**
   * Returns the startOffset.
   * 
   * @return The startOffset.
   * @see #parserStartOffset
   */
  public int [ ] getParserStartOffset ( )
  {
    return this.parserStartOffset ;
  }
}
