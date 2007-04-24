package de.unisiegen.tpml.core.exceptions ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public final class LanguageParserMultiException extends LanguageParserException
{
  /**
   * TODO
   */
  private static final long serialVersionUID = 4717084402322482294L ;


  /**
   * TODO
   */
  private static final String VARIABLE = "Variable" ; //$NON-NLS-1$


  /**
   * TODO
   */
  private static final String ATTRIBUTE = "Attribute" ; //$NON-NLS-1$


  /**
   * TODO
   */
  private static final String MESSAGE = "Message" ; //$NON-NLS-1$


  /**
   * TODO
   */
  private static final String SELF = "Self" ; //$NON-NLS-1$


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @return TODO
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
   * TODO
   * 
   * @param pNegativeIdentifiers TODO
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
   * TODO
   * 
   * @param pNegativeIdentifiers TODO
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
   * TODO
   * 
   * @param pNegativeIdentifiers TODO
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
   * TODO
   * 
   * @param pNegativeIdentifiers TODO
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
   * TODO
   */
  private String [ ] messages ;


  /**
   * TODO
   */
  private int [ ] parserStartOffset ;


  /**
   * TODO
   */
  private int [ ] parserEndOffset ;


  /**
   * TODO
   * 
   * @param pMessages TODO
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
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
