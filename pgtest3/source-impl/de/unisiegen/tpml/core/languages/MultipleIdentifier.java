package de.unisiegen.tpml.core.languages ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Body ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.types.RowType ;


/**
 * A helper class for the parser.
 * 
 * @author Christian Fehler
 */
public class MultipleIdentifier
{
  /**
   * Throws a {@link LanguageParserMultiException} if the {@link Body} has two
   * or more {@link Attribute} with the same {@link Identifier}.
   * 
   * @param pBody The {@link Body} which should be checked.
   */
  public static void check ( Body pBody )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    Identifier [ ] attributeIdentifiers = pBody.getIdentifiers ( ) ;
    for ( int i = 0 ; i < attributeIdentifiers.length ; i ++ )
    {
      negativeIdentifiers.clear ( ) ;
      for ( int j = i + 1 ; j < attributeIdentifiers.length ; j ++ )
      {
        if ( attributeIdentifiers [ i ].equals ( attributeIdentifiers [ j ] ) )
        {
          negativeIdentifiers.add ( attributeIdentifiers [ j ] ) ;
        }
      }
      if ( negativeIdentifiers.size ( ) > 0 )
      {
        negativeIdentifiers.add ( attributeIdentifiers [ i ] ) ;
        LanguageParserMultiException.throwExceptionBody ( negativeIdentifiers ) ;
      }
    }
    if ( pBody.getBodyOrRow ( ) instanceof Row )
    {
      Row row = ( Row ) pBody.getBodyOrRow ( ) ;
      for ( Identifier a : attributeIdentifiers )
      {
        negativeIdentifiers.clear ( ) ;
        for ( Expression e : row.getExpressions ( ) )
        {
          if ( e instanceof Attribute )
          {
            Attribute attribute = ( Attribute ) e ;
            if ( a.equals ( attribute.getId ( ) ) )
            {
              negativeIdentifiers.add ( attribute.getId ( ) ) ;
            }
          }
        }
        if ( negativeIdentifiers.size ( ) > 0 )
        {
          negativeIdentifiers.add ( a ) ;
          LanguageParserMultiException
              .throwExceptionBody ( negativeIdentifiers ) ;
        }
      }
    }
  }


  /**
   * Throws a {@link LanguageParserMultiException} if the {@link Row} has two or
   * more {@link Attribute}s with the same {@link Identifier}.
   * 
   * @param pRow The {@link Row} which should be checked.
   */
  public static void check ( Row pRow )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    Expression [ ] rowExpressions = pRow.getExpressions ( ) ;
    for ( int i = 0 ; i < rowExpressions.length ; i ++ )
    {
      if ( rowExpressions [ i ] instanceof Attribute )
      {
        negativeIdentifiers.clear ( ) ;
        Attribute attribute1 = ( Attribute ) rowExpressions [ i ] ;
        for ( int j = i + 1 ; j < rowExpressions.length ; j ++ )
        {
          if ( rowExpressions [ j ] instanceof Attribute )
          {
            Attribute attribute2 = ( Attribute ) rowExpressions [ j ] ;
            if ( attribute1.getId ( ).equals ( attribute2.getId ( ) ) )
            {
              negativeIdentifiers.add ( attribute2.getId ( ) ) ;
            }
          }
        }
        if ( negativeIdentifiers.size ( ) > 0 )
        {
          negativeIdentifiers.add ( attribute1.getId ( ) ) ;
          LanguageParserMultiException.throwExceptionRow ( negativeIdentifiers ) ;
        }
      }
    }
  }


  /**
   * Throws a {@link LanguageParserMultiException} if the {@link Duplication}
   * has two or more {@link Identifier}s with the same name.
   * 
   * @param pDuplication The {@link Duplication} which should be checked.
   */
  public static void check ( Duplication pDuplication )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    Identifier [ ] identifier = pDuplication.getIdentifiers ( ) ;
    for ( int i = 0 ; i < identifier.length ; i ++ )
    {
      negativeIdentifiers.clear ( ) ;
      for ( int j = i + 1 ; j < identifier.length ; j ++ )
      {
        if ( identifier [ i ].equals ( identifier [ j ] ) )
        {
          negativeIdentifiers.add ( identifier [ j ] ) ;
        }
      }
      if ( negativeIdentifiers.size ( ) > 0 )
      {
        negativeIdentifiers.add ( identifier [ i ] ) ;
        LanguageParserMultiException
            .throwExceptionDuplication ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * Throws a {@link LanguageParserMultiException} if the {@link RowType} has
   * two or more {@link Identifier}s with the same name.
   * 
   * @param pRowType The {@link RowType} which should be checked.
   */
  public static void check ( RowType pRowType )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    Identifier [ ] identifier = pRowType.getIdentifiers ( ) ;
    for ( int i = 0 ; i < identifier.length ; i ++ )
    {
      negativeIdentifiers.clear ( ) ;
      for ( int j = i + 1 ; j < identifier.length ; j ++ )
      {
        if ( identifier [ i ].equals ( identifier [ j ] ) )
        {
          negativeIdentifiers.add ( identifier [ j ] ) ;
        }
      }
      if ( negativeIdentifiers.size ( ) > 0 )
      {
        negativeIdentifiers.add ( identifier [ i ] ) ;
        LanguageParserMultiException
            .throwExceptionRowType ( negativeIdentifiers ) ;
      }
    }
  }
}
