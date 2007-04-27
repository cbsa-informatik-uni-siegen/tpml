package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.RowSubstitutionException ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Row extends Expression implements ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [ ] indicesE ;


  /**
   * TODO
   * 
   * @see #getExpressions()
   */
  private Expression [ ] expressions ;


  /**
   * TODO
   * 
   * @param pExpressions TODO
   */
  public Row ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "Expressions is null" ) ; //$NON-NLS-1$
    }
    this.expressions = pExpressions ;
    this.indicesE = new int [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      this.indicesE [ i ] = i + 1 ;
      if ( this.expressions [ i ].getParent ( ) != null )
      {
        // this.expressions [ i ] = this.expressions [ i ].clone ( ) ;
      }
      this.expressions [ i ].setParent ( this ) ;
      if ( this.expressions [ i ] instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        attribute.setParent ( this ) ;
      }
      else if ( ( ! ( this.expressions [ i ] instanceof Method ) )
          && ( ! ( this.expressions [ i ] instanceof CurriedMethod ) ) )
      {
        throw new IllegalArgumentException (
            "One child Expression of the Row is not an Attribute, Method or CurriedMethod." ) ; //$NON-NLS-1$
      }
    }
  }


  /**
   * TODO
   * 
   * @param pExpressions TODO
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   */
  public Row ( Expression [ ] pExpressions , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pExpressions ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * TODO
   */
  public void checkDisjunction ( )
  {
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    ArrayList < Identifier > allIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( this.expressions [ i ] instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        allIdentifiers.clear ( ) ;
        negativeIdentifiers.clear ( ) ;
        for ( int j = i + 1 ; j < this.expressions.length ; j ++ )
        {
          allIdentifiers.addAll ( this.expressions [ j ].getIdentifiersAll ( ) ) ;
        }
        for ( Identifier allId : allIdentifiers )
        {
          if ( ( attribute.getId ( ).equals ( allId ) )
              && ( ! ( allId.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) ) ) )
          {
            negativeIdentifiers.add ( allId ) ;
          }
        }
        negativeIdentifiers.add ( attribute.getId ( ) ) ;
        LanguageParserMultiException
            .throwExceptionDisjunction ( negativeIdentifiers ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new Row ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Row )
    {
      Row other = ( Row ) pObject ;
      return Arrays.equals ( this.expressions , other.expressions ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Row" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expressions
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( )
  {
    return this.indicesE ;
  }


  /**
   * TODO
   * 
   * @param pAttribute TODO
   * @return TODO
   */
  public ArrayList < Identifier > getIdentifiersBound ( Attribute pAttribute )
  {
    ArrayList < Identifier > boundId = new ArrayList < Identifier > ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( pAttribute == this.expressions [ i ] )
      {
        Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        for ( int j = i + 1 ; j < this.expressions.length ; j ++ )
        {
          Expression child = this.expressions [ j ] ;
          ArrayList < Identifier > freeIdentifiers = child
              .getIdentifiersFree ( ) ;
          for ( Identifier freeId : freeIdentifiers )
          {
            if ( attribute.getId ( ).equals ( freeId ) )
            {
              freeId.setBoundTo ( attribute , attribute.getId ( ) ) ;
              boundId.add ( freeId ) ;
            }
          }
        }
        return boundId ;
      }
    }
    return boundId ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > newBound = new ArrayList < Identifier > ( ) ;
      for ( Expression expr : this.expressions )
      {
        ArrayList < Identifier > freeCurrent = new ArrayList < Identifier > ( ) ;
        freeCurrent.addAll ( expr.getIdentifiersFree ( ) ) ;
        while ( freeCurrent.removeAll ( newBound ) )
        {
          // Remove all Identifiers with the same name
        }
        this.identifiersFree.addAll ( freeCurrent ) ;
        if ( expr instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) expr ;
          newBound.add ( attribute.getId ( ) ) ;
        }
      }
    }
    return this.identifiersFree ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  @ Override
  public String getPrefix ( )
  {
    return this.isValue ( ) ? PREFIX_ROW_VALUE : PREFIX_ROW ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    for ( Expression expr : this.expressions )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        /*
         * If an Attribute is not a value, this Row is not a value.
         */
        if ( ! attribute.isValue ( ) )
        {
          return false ;
        }
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( Identifier pId , Expression pExpression )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ] ;
    }
    if ( this.getIdentifiersFree ( ).contains ( pId ) )
    {
      for ( int i = 0 ; i < newExpressions.length ; i ++ )
      {
        if ( newExpressions [ i ] instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) newExpressions [ i ] ;
          if ( pId.equals ( attribute.getId ( ) ) )
          {
            newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
                pExpression ) ;
            break ;
          }
          BoundRenaming boundRenaming = new BoundRenaming ( ) ;
          for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
          {
            boundRenaming.add ( newExpressions [ j ].getIdentifiersFree ( ) ) ;
          }
          boundRenaming.remove ( attribute.getId ( ) ) ;
          boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
          boundRenaming.add ( pId ) ;
          Identifier newId = boundRenaming.newId ( attribute.getId ( ) ) ;
          if ( ! attribute.getId ( ).equals ( newId ) )
          {
            for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
            {
              newExpressions [ j ] = newExpressions [ j ].substitute (
                  attribute.getId ( ) , newId ) ;
            }
          }
          newExpressions [ i ] = new Attribute ( newId , attribute.getTau ( ) ,
              attribute.getE ( ).substitute ( pId , pExpression ) ) ;
        }
        else
        {
          newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
              pExpression ) ;
        }
      }
    }
    return new Row ( newExpressions ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Row substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpr [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return new Row ( newExpr ) ;
  }


  /**
   * TODO
   * 
   * @param pId TODO
   * @param pExpression TODO
   * @return TODO
   */
  public Row substituteRow ( Identifier pId , Expression pExpression )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    boolean found = false ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      if ( newExpressions [ i ] instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) newExpressions [ i ] ;
        if ( pId.equals ( attribute.getId ( ) ) )
        {
          newExpressions [ i ] = new Attribute ( attribute.getId ( ) ,
              attribute.getTau ( ) , pExpression ) ;
          found = true ;
          break ;
        }
      }
    }
    if ( ! found )
    {
      throw new RowSubstitutionException (
          "The Identifier is in the domain of the Row" ) ; //$NON-NLS-1$
    }
    return new Row ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_ROW ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.prettyStringBuilder
            .addBuilder ( this.expressions [ i ]
                .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
                PRIO_ROW_E ) ;
        if ( i != this.expressions.length - 1 )
        {
          this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      if ( this.expressions.length == 0 )
      {
        this.prettyStringBuilder.addText ( "\u03B5" ) ; //$NON-NLS-1$
      }
    }
    return this.prettyStringBuilder ;
  }
}
