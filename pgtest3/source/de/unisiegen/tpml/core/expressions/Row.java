package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import de.unisiegen.tpml.core.identifiers.BoundedIdentifiers ;
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
public final class Row extends Expression implements BoundedIdentifiers
{
  /**
   * TODO
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * The bound identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private Identifier [ ] identifiers ;


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
    this.identifiers = new Identifier [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( this.expressions [ i ] instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        this.identifiers [ i ] = attribute.getId ( ) ;
      }
      else if ( this.expressions [ i ] instanceof Method )
      {
        Method method = ( Method ) this.expressions [ i ] ;
        this.identifiers [ i ] = method.getId ( ) ;
      }
      else if ( this.expressions [ i ] instanceof CurriedMethod )
      {
        CurriedMethod curriedMethod = ( CurriedMethod ) this.expressions [ i ] ;
        this.identifiers [ i ] = curriedMethod.getIdentifiers ( 0 ) ;
      }
      else
      {
        throw new IllegalArgumentException (
            "A child Expression is not an instance of Attribute, Method or CurriedMethod" ) ; //$NON-NLS-1$
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
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > newBounded = new ArrayList < Identifier > ( ) ;
      for ( Expression expr : this.expressions )
      {
        ArrayList < Identifier > freeCurrent = new ArrayList < Identifier > ( ) ;
        freeCurrent.addAll ( expr.free ( ) ) ;
        while ( freeCurrent.removeAll ( newBounded ) )
        {
          // Remove all Identifiers with the same name
        }
        this.free.addAll ( freeCurrent ) ;
        if ( expr instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) expr ;
          newBounded.add ( attribute.getId ( ) ) ;
        }
      }
    }
    return this.free ;
  }


  /**
   * Returns a list of lists of in this {@link Expression} bounded
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bounded
   *         {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getBoundedIdentifiers ( )
  {
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        ArrayList < Identifier > boundedId = new ArrayList < Identifier > ( ) ;
        if ( this.expressions [ i ] instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) this.expressions [ i ] ;
          for ( int j = i + 1 ; j < this.expressions.length ; j ++ )
          {
            Expression child = this.expressions [ j ] ;
            ArrayList < Identifier > boundedE = child.free ( ) ;
            for ( Identifier freeId : boundedE )
            {
              if ( attribute.getId ( ).equals ( freeId ) )
              {
                freeId.setBoundedToExpression ( attribute ) ;
                freeId.setBoundedToIdentifier ( attribute.getId ( ) ) ;
                boundedId.add ( freeId ) ;
              }
            }
            if ( ( child instanceof Attribute )
                && ( ( ( Attribute ) child ).getId ( ).equals ( attribute
                    .getId ( ) ) ) )
            {
              break ;
            }
          }
        }
        this.boundedIdentifiers.add ( boundedId ) ;
      }
    }
    return this.boundedIdentifiers ;
  }


  /**
   * Returns the <code>pIndex</code>th list of in this {@link Expression}
   * bounded {@link Identifier}s.
   * 
   * @param pIndex The index of the list of {@link Identifier}s to return.
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
  public ArrayList < Identifier > getBoundedIdentifiers ( int pIndex )
  {
    if ( this.boundedIdentifiers == null )
    {
      return getBoundedIdentifiers ( ).get ( pIndex ) ;
    }
    return this.boundedIdentifiers.get ( pIndex ) ;
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
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   * @see #expressions
   * @see #getExpressions()
   */
  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * Returns the identifiers for the tuple items.
   * 
   * @return the identifiers for the tuple items.
   * @see #getIdentifiers(int)
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns the <code>n</code>th identifier.
   * 
   * @param pIndex the index of the identifier to return.
   * @return the <code>n</code>th identifier.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getIdentifiers()
   */
  public Identifier getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
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
    ArrayList < Identifier > attributeIdList = new ArrayList < Identifier > ( ) ;
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
        /*
         * If there are Attributes with the same id, this Row is not a value.
         */
        if ( attributeIdList.contains ( attribute.getId ( ) ) )
        {
          return false ;
        }
        attributeIdList.add ( attribute.getId ( ) ) ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Row substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    if ( this.free ( ).contains ( pId ) )
    {
      for ( int i = 0 ; i < newExpressions.length ; i ++ )
      {
        if ( newExpressions [ i ] instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) newExpressions [ i ] ;
          if ( pId.equals ( attribute.getId ( ) ) )
          {
            newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
                pExpression , pAttributeRename ) ;
            break ;
          }
          BoundRenaming boundRenaming = new BoundRenaming ( ) ;
          for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
          {
            boundRenaming.add ( newExpressions [ j ].free ( ) ) ;
          }
          boundRenaming.remove ( attribute.getId ( ) ) ;
          boundRenaming.add ( pExpression.free ( ) ) ;
          boundRenaming.add ( pId ) ;
          Identifier newId = boundRenaming.newId ( attribute.getId ( ) ) ;
          if ( ! attribute.getId ( ).equals ( newId ) )
          {
            for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
            {
              newExpressions [ j ] = newExpressions [ j ].substitute (
                  attribute.getId ( ) , newId , pAttributeRename ) ;
            }
          }
          newExpressions [ i ] = new Attribute ( newId ,
              attribute.getTau ( ) == null ? null : attribute.getTau ( )
                  .clone ( ) , attribute.getE ( ).substitute ( pId ,
                  pExpression , pAttributeRename ) ) ;
        }
        else
        {
          newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
              pExpression , pAttributeRename ) ;
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
