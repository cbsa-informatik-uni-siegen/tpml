package de.unisiegen.tpml.core.languages;


import java.util.ArrayList;

import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException;
import de.unisiegen.tpml.core.exceptions.LanguageParserReplaceException;
import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.Duplication;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Inherit;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.util.BoundRenaming;


/**
 * A helper class for the parser.
 * 
 * @author Christian Fehler
 */
public class MultipleIdentifier
{

  /**
   * Throws a {@link LanguageParserMultiException} if the {@link Inherit} has
   * two or more {@link Attribute} with the same {@link Identifier}.
   * 
   * @param pBody The {@link Inherit} which should be checked.
   */
  public static void check ( Inherit pBody )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ();
    Identifier [] bodyIdentifiers = pBody.getIdentifiers ();
    for ( int i = 0 ; i < bodyIdentifiers.length ; i++ )
    {
      negativeIdentifiers.clear ();
      for ( int j = i + 1 ; j < bodyIdentifiers.length ; j++ )
      {
        if ( bodyIdentifiers [ i ].equals ( bodyIdentifiers [ j ] ) )
        {
          negativeIdentifiers.add ( bodyIdentifiers [ j ] );
        }
      }
      if ( negativeIdentifiers.size () > 0 )
      {
        negativeIdentifiers.add ( bodyIdentifiers [ i ] );
        LanguageParserMultiException.throwExceptionBody ( negativeIdentifiers );
      }
    }
    /*
     * Rename the duplicated attribute Identifier.
     */
    ArrayList < Identifier > replaceIdentifiers = new ArrayList < Identifier > ();
    // For all Identifier in the Inherit
    for ( Identifier bodyId : bodyIdentifiers )
    {
      // Clear both lists for the next Identifier in the Inherit
      negativeIdentifiers.clear ();
      replaceIdentifiers.clear ();
      // For all Identifier in the domainA from the body or row of the body
      for ( Identifier domAId : ( pBody.getBody () ).getDomA () )
      {
        // If the Identifiers are equal, something
        if ( bodyId.equals ( domAId ) )
        {
          /*
           * Rename the Identifier and all Identifier which are bound to him, if
           * the parent of the domA Identifier is an Attribute.
           */
          if ( ( domAId.getParent () != null )
              && ( domAId.getParent () instanceof Attribute ) )
          {
            Attribute attribute = ( Attribute ) domAId.getParent ();
            replaceIdentifiers.add ( domAId );
            replaceIdentifiers.addAll ( attribute.getIdentifiersBound ().get (
                0 ) );
          }
          /*
           * Add the Identifier to the negative list.
           */
          else
          {
            negativeIdentifiers.add ( domAId );
          }
        }
        if ( replaceIdentifiers.size () > 0 )
        {
          negativeIdentifiers.add ( bodyId );
          BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ();
          boundRenaming.add ( bodyIdentifiers );
          boundRenaming.add ( pBody.getBody ().getDomA () );
          LanguageParserReplaceException.throwExceptionBody (
              negativeIdentifiers, replaceIdentifiers, boundRenaming
                  .newIdentifier ( bodyId ).toString () );
        }
        else if ( negativeIdentifiers.size () > 0 )
        {
          negativeIdentifiers.add ( bodyId );
          LanguageParserMultiException
              .throwExceptionBody ( negativeIdentifiers );
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
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ();
    Expression [] rowExpressions = pRow.getExpressions ();
    for ( int i = 0 ; i < rowExpressions.length ; i++ )
    {
      if ( rowExpressions [ i ] instanceof Attribute )
      {
        negativeIdentifiers.clear ();
        Attribute attribute1 = ( Attribute ) rowExpressions [ i ];
        for ( int j = i + 1 ; j < rowExpressions.length ; j++ )
        {
          if ( rowExpressions [ j ] instanceof Attribute )
          {
            Attribute attribute2 = ( Attribute ) rowExpressions [ j ];
            if ( attribute1.getId ().equals ( attribute2.getId () ) )
            {
              negativeIdentifiers.add ( attribute2.getId () );
            }
          }
        }
        if ( negativeIdentifiers.size () > 0 )
        {
          negativeIdentifiers.add ( attribute1.getId () );
          LanguageParserMultiException.throwExceptionRow ( negativeIdentifiers );
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
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ();
    Identifier [] identifier = pDuplication.getIdentifiers ();
    for ( int i = 0 ; i < identifier.length ; i++ )
    {
      negativeIdentifiers.clear ();
      for ( int j = i + 1 ; j < identifier.length ; j++ )
      {
        if ( identifier [ i ].equals ( identifier [ j ] ) )
        {
          negativeIdentifiers.add ( identifier [ j ] );
        }
      }
      if ( negativeIdentifiers.size () > 0 )
      {
        negativeIdentifiers.add ( identifier [ i ] );
        LanguageParserMultiException
            .throwExceptionDuplication ( negativeIdentifiers );
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
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ();
    Identifier [] identifier = pRowType.getIdentifiers ();
    for ( int i = 0 ; i < identifier.length ; i++ )
    {
      negativeIdentifiers.clear ();
      for ( int j = i + 1 ; j < identifier.length ; j++ )
      {
        if ( identifier [ i ].equals ( identifier [ j ] ) )
        {
          negativeIdentifiers.add ( identifier [ j ] );
        }
      }
      if ( negativeIdentifiers.size () > 0 )
      {
        negativeIdentifiers.add ( identifier [ i ] );
        LanguageParserMultiException
            .throwExceptionRowType ( negativeIdentifiers );
      }
    }
  }
}
