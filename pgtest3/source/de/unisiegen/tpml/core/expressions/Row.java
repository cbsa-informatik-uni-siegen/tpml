package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class Row extends Expression
{
  private Expression [ ] expressions ;


  public Row ( Expression [ ] pExpressions )
  {
    this.expressions = pExpressions ;
    for ( Expression e : this.expressions )
    {
      if ( e instanceof Attr )
      {
        ( ( Attr ) e ).parentRow ( this ) ;
      }
      if ( e instanceof Meth )
      {
        ( ( Meth ) e ).parentRow ( this ) ;
      }
      if ( e instanceof CurriedMeth )
      {
        ( ( CurriedMeth ) e ).parentRow ( this ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Row" ; //$NON-NLS-1$
  }


  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  @ Override
  public Row clone ( )
  {
    return new Row ( this.expressions.clone ( ) ) ;
  }


  @ Override
  public boolean isValue ( )
  {
    for ( Expression e : this.expressions )
    {
      if ( ( e instanceof Attr ) && ( ! ( ( Attr ) e ).isValue ( ) ) )
      {
        return false ;
      }
    }
    return true ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Row )
    {
      Row other = ( Row ) pObject ;
      return this.expressions.equals ( other.expressions ) ;
    }
    return false ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = this.expressions.clone ( ) ;
    for ( int i = 0 ; i < tmp.length ; i ++ )
    {
      Expression e = tmp [ i ] ;
      if ( e instanceof Attr )
      {
        // TODO "with a new name ... not only append '"
        Attr attr = ( Attr ) e ;
        // first case id == id'
        if ( attr.getIdentifier ( ).equals ( pID ) )
        {
          tmp [ i ] = new Attr ( attr.getIdentifier ( ) , attr.getE ( )
              .substitute ( pID , pExpression ) ) ;
          break ;
        }
        // second case id != id'
        tmp [ i ] = new Attr ( attr.getIdentifier ( ) + "'" , attr.getE ( )
            .substitute ( pID , pExpression ) ) ;
        for ( int j = i + 1 ; j < tmp.length ; j ++ )
        {
          tmp [ j ] = tmp [ j ].substitute ( attr.getIdentifier ( ) ,
              new Identifier ( attr.getIdentifier ( ) + "'" ) ) ;
        }
      }
      else if ( e instanceof Meth )
      {
        Meth meth = ( Meth ) e ;
        tmp [ i ] = new Meth ( meth.getIdentifier ( ) , meth.getE ( )
            .substitute ( pID , pExpression ) ) ;
      }
      else if ( e instanceof CurriedMeth )
      {
        CurriedMeth curriedMeth = ( CurriedMeth ) e ;
        tmp [ i ] = new CurriedMeth ( curriedMeth.getIdentifiers ( ) ,
            curriedMeth.getE ( ).substitute ( pID , pExpression ) ) ;
      }
    }
    return new Row ( tmp ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_ROW ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ROW_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( " " ) ;
        builder.addBreak ( ) ;
      }
    }
    return builder ;
  }
}
