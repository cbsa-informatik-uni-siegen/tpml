package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
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
public final class Row extends Expression
{
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
  public Row ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "Expressions is null" ) ; //$NON-NLS-1$
    }
    this.expressions = pExpressions ;
    for ( Expression expr : this.expressions )
    {
      if ( ! ( ( expr instanceof Attribute ) || ( expr instanceof Method ) || ( expr instanceof CurriedMethod ) ) )
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
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      TreeSet < String > bounded = new TreeSet < String > ( ) ;
      for ( Expression expr : this.expressions )
      {
        TreeSet < String > freeCurrent = new TreeSet < String > ( ) ;
        freeCurrent.addAll ( expr.free ( ) ) ;
        freeCurrent.removeAll ( bounded ) ;
        this.free.addAll ( freeCurrent ) ;
        if ( expr instanceof Attribute )
        {
          Attribute attribute = ( Attribute ) expr ;
          bounded.add ( attribute.getId ( ) ) ;
        }
      }
    }
    return this.free ;
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
    ArrayList < String > attributeIdList = new ArrayList < String > ( ) ;
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Row substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression [ ] newExpressions = this.expressions.clone ( ) ;
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
          String newId = boundRenaming.newIdentifier ( attribute.getId ( ) ) ;
          if ( ! attribute.getId ( ).equals ( newId ) )
          {
            for ( int j = i + 1 ; j < newExpressions.length ; j ++ )
            {
              newExpressions [ j ] = newExpressions [ j ].substitute (
                  attribute.getId ( ) , new Identifier ( newId ) ,
                  pAttributeRename ) ;
            }
          }
          newExpressions [ i ] = new Attribute ( newId , attribute.getTau ( ) ,
              attribute.getE ( ).substitute ( pId , pExpression ,
                  pAttributeRename ) ) ;
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
        this.prettyStringBuilder.addKeyword ( "\u03B5" ) ; //$NON-NLS-1$
      }
    }
    return this.prettyStringBuilder ;
  }
}
