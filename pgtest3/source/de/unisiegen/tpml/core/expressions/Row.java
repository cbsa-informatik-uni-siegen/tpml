package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class Row extends Expression
{
  private Expression [ ] expressions ;


  public Row ( )
  {
    this.expressions = new Expression [ 0 ] ;
  }


  public Row ( Expression [ ] pExpressions )
  {
    this.expressions = pExpressions ;
  }


  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  public void addAttr ( Attr pAttr )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length + 1 ] ;
    System
        .arraycopy ( this.expressions , 0 , tmp , 1 , this.expressions.length ) ;
    tmp [ 0 ] = pAttr ;
    this.expressions = tmp ;
  }


  public void addMeth ( Meth pMeth )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length + 1 ] ;
    System
        .arraycopy ( this.expressions , 0 , tmp , 1 , this.expressions.length ) ;
    tmp [ 0 ] = pMeth ;
    this.expressions = tmp ;
  }


  public void addCurriedMeth ( CurriedMeth pCurriedMeth )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length + 1 ] ;
    System
        .arraycopy ( this.expressions , 0 , tmp , 1 , this.expressions.length ) ;
    tmp [ 0 ] = pCurriedMeth ;
    this.expressions = tmp ;
  }


  @ Override
  public Row clone ( )
  {
    Row tmp = new Row ( ) ;
    for ( Expression e : this.expressions )
    {
      if ( e instanceof Attr )
      {
        tmp.addAttr ( ( ( Attr ) e ).clone ( ) ) ;
      }
      else
      {
        tmp.addMeth ( ( ( Meth ) e ).clone ( ) ) ;
      }
    }
    return tmp ;
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
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int n = 0 ; n < tmp.length ; ++ n )
    {
      tmp [ n ] = this.expressions [ n ].substitute ( pID , pExpression ) ;
    }
    return new Row ( tmp ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECT ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECT_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( " " ) ;
        builder.addBreak ( ) ;
      }
    }
    return builder ;
  }
}
