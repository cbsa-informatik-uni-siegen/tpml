package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
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
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * TODO
   * 
   * @param pExpressions TODO
   */
  public Row ( final Expression [ ] pExpressions )
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
        this.expressions [ i ] = this.expressions [ i ].clone ( ) ;
      }
      this.expressions [ i ].setParent ( this ) ;
      if ( this.expressions [ i ] instanceof Attribute )
      {
        final Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        attribute.setParent ( this ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row clone ( )
  {
    final Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
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
  public boolean equals ( final Object pObject )
  {
    if ( pObject instanceof Row )
    {
      final Row other = ( Row ) pObject ;
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
      final ArrayList < Identifier > newBounded = new ArrayList < Identifier > ( ) ;
      for ( final Expression expr : this.expressions )
      {
        final ArrayList < Identifier > freeCurrent = new ArrayList < Identifier > ( ) ;
        freeCurrent.addAll ( expr.free ( ) ) ;
        while ( freeCurrent.removeAll ( newBounded ) )
        {
          // Remove all Identifiers with the same name
        }
        this.free.addAll ( freeCurrent ) ;
        if ( expr instanceof Attribute )
        {
          final Attribute attribute = ( Attribute ) expr ;
          newBounded.add ( attribute.getId ( ) ) ;
        }
      }
    }
    return this.free ;
  }


  /**
   * TODO
   * 
   * @param pAttribute TODO
   * @return TODO
   */
  public ArrayList < Identifier > getBoundedIdentifiers (
      final Attribute pAttribute )
  {
    final ArrayList < Identifier > boundedId = new ArrayList < Identifier > ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( pAttribute == this.expressions [ i ] )
      {
        final Attribute attribute = ( Attribute ) this.expressions [ i ] ;
        for ( int j = i + 1 ; j < this.expressions.length ; j ++ )
        {
          final Expression child = this.expressions [ j ] ;
          final ArrayList < Identifier > boundedE = child.free ( ) ;
          for ( final Identifier freeId : boundedE )
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
            return boundedId ;
          }
        }
        return boundedId ;
      }
    }
    return boundedId ;
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
  public Expression getExpressions ( final int pIndex )
  {
    return this.expressions [ pIndex ] ;
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
   * @return TODO
   */
  @ Override
  public String getPrefix ( )
  {
    return this.isValue ( ) ? Expression.PREFIX_ROW_VALUE
        : Expression.PREFIX_ROW ;
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
    final ArrayList < Identifier > attributeIdList = new ArrayList < Identifier > ( ) ;
    for ( final Expression expr : this.expressions )
    {
      if ( expr instanceof Attribute )
      {
        final Attribute attribute = ( Attribute ) expr ;
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
  public Row substitute ( final Identifier pId , final Expression pExpression )
  {
    return this.substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( final Identifier pId , final Expression pExpression ,
      final boolean pAttributeRename )
  {
    final Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
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
          final Attribute attribute = ( Attribute ) newExpressions [ i ] ;
          if ( pId.equals ( attribute.getId ( ) ) )
          {
            newExpressions [ i ] = newExpressions [ i ].substitute ( pId ,
                pExpression , pAttributeRename ) ;
            break ;
          }
          final BoundRenaming boundRenaming = new BoundRenaming ( ) ;
          for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
          {
            boundRenaming.add ( newExpressions [ j ].free ( ) ) ;
          }
          boundRenaming.remove ( attribute.getId ( ) ) ;
          boundRenaming.add ( pExpression.free ( ) ) ;
          boundRenaming.add ( pId ) ;
          final Identifier newId = boundRenaming.newId ( attribute.getId ( ) ) ;
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
  public Row substitute ( final TypeSubstitution pTypeSubstitution )
  {
    final Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
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
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_ROW ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.expressions [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PrettyPrintPriorities.PRIO_ROW_E ) ;
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
