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
        .arraycopy ( this.expressions , 0 , tmp , 0 , this.expressions.length ) ;
    tmp [ this.expressions.length ] = pAttr ;
    this.expressions = tmp ;
  }


  public void addMeth ( Meth pMeth )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length + 1 ] ;
    System
        .arraycopy ( this.expressions , 0 , tmp , 0 , this.expressions.length ) ;
    tmp [ this.expressions.length ] = pMeth ;
    this.expressions = tmp ;
  }


  @ Override
  public Row clone ( )
  {
    Row tmp = new Row ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( this.expressions [ i ] instanceof Attr )
      {
        tmp.addAttr ( ( ( Attr ) this.expressions [ i ] ).clone ( ) ) ;
      }
      else
      {
        tmp.addMeth ( ( ( Meth ) this.expressions [ i ] ).clone ( ) ) ;
      }
    }
    return tmp ;
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
      builder.addText ( " " ) ;
    }
    return builder ;
  }
}
