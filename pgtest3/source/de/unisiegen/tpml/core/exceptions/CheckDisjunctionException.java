package de.unisiegen.tpml.core.exceptions ;


import java.util.ArrayList ;
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
  public CheckDisjunctionException ( String pMessage , int [ ] pStartOffset ,
      int [ ] pEndOffset )
  {
    super ( pMessage , 0 , 0 ) ;
    this.startOffset = pStartOffset ;
    this.endOffset = pEndOffset ;
  }


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pNegativeIdentifiers TODO
   * @param pMessage TODO
   */
  public static void throwException ( Identifier pIdentifier ,
      ArrayList < Identifier > pNegativeIdentifiers , String pMessage )
  {
    if ( pNegativeIdentifiers.size ( ) > 0 )
    {
      int [ ] startOffset = new int [ pNegativeIdentifiers.size ( ) + 1 ] ;
      int [ ] endOffset = new int [ pNegativeIdentifiers.size ( ) + 1 ] ;
      startOffset [ 0 ] = pIdentifier.getStartOffset ( ) ;
      endOffset [ 0 ] = pIdentifier.getEndOffset ( ) ;
      for ( int j = 0 ; j < pNegativeIdentifiers.size ( ) ; j ++ )
      {
        startOffset [ j + 1 ] = pNegativeIdentifiers.get ( j )
            .getStartOffset ( ) ;
        endOffset [ j + 1 ] = pNegativeIdentifiers.get ( j ).getEndOffset ( ) ;
      }
      throw new CheckDisjunctionException ( pMessage , startOffset , endOffset ) ;
    }
  }
}
