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
public final class CheckDisjunctionException extends LanguageParserException
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
      default :
      {
        return VARIABLE ;
      }
    }
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
        startOffset [ j ] = pNegativeIdentifiers.get ( j ).getStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getEndOffset ( ) ;
      }
      throw new CheckDisjunctionException ( message , startOffset , endOffset ) ;
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
        startOffset [ j ] = pNegativeIdentifiers.get ( j ).getStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getEndOffset ( ) ;
      }
      throw new CheckDisjunctionException ( message , startOffset , endOffset ) ;
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
        startOffset [ j ] = pNegativeIdentifiers.get ( j ).getStartOffset ( ) ;
        endOffset [ j ] = pNegativeIdentifiers.get ( j ).getEndOffset ( ) ;
      }
      throw new CheckDisjunctionException ( message , startOffset , endOffset ) ;
    }
  }


  /**
   * TODO
   */
  public String [ ] message ;


  /**
   * TODO
   */
  public int [ ] startOffset ;


  /**
   * TODO
   */
  public int [ ] endOffset ;


  /**
   * TODO
   * 
   * @param pMessage TODO
   * @param pStartOffset TODO
   * @param pEndOffset TODO
   */
  private CheckDisjunctionException ( String [ ] pMessage ,
      int [ ] pStartOffset , int [ ] pEndOffset )
  {
    super ( pMessage [ 0 ] , pStartOffset [ 0 ] , pEndOffset [ 0 ] ) ;
    this.message = pMessage ;
    this.startOffset = pStartOffset ;
    this.endOffset = pEndOffset ;
  }
}
