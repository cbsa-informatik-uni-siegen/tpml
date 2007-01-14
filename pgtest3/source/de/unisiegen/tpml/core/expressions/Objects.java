package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class Objects extends Expression
{
  protected String [ ] identifiers ;


  private Expression [ ] expressions ;


  public Objects ( String [ ] pIdentifiers , Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "expressions is null" ) ;
    }
    if ( pExpressions.length == 0 )
    {
      throw new IllegalArgumentException ( "expressions is empty" ) ;
    }
    this.identifiers = pIdentifiers ;
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


  @ Override
  public Objects clone ( )
  {
    String [ ] tmpI = new String [ this.identifiers.length ] ;
    Expression [ ] tmpE = new Expression [ this.expressions.length ] ;
    for ( int n = 0 ; n < tmpI.length ; ++ n )
    {
      tmpI [ n ] = this.identifiers [ n ] ;
    }
    for ( int n = 0 ; n < tmpE.length ; ++ n )
    {
      tmpE [ n ] = this.expressions [ n ].clone ( ) ;
    }
    return new Objects ( tmpI , tmpE ) ;
  }


  @ Override
  public Objects substitute ( String pID , Expression pExpression )
  {
    String [ ] tmpI = new String [ this.identifiers.length ] ;
    Expression [ ] tmpE = new Expression [ this.expressions.length ] ;
    for ( int n = 0 ; n < tmpI.length ; ++ n )
    {
      tmpI [ n ] = this.identifiers [ n ] ;
    }
    for ( int n = 0 ; n < tmpE.length ; ++ n )
    {
      tmpE [ n ] = this.expressions [ n ].substitute ( pID , pExpression ) ;
    }
    return new Objects ( tmpI , tmpE ) ;
  }


  @ Override
  public boolean isValue ( )
  {
    for ( Expression e : this.expressions )
    {
      if ( ! e.isValue ( ) )
      {
        return false ;
      }
    }
    return true ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECT ) ;
    builder.addKeyword ( "object" ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( "method" ) ;
    int index = 0 ;
    while ( index < this.expressions.length )
    {
      builder.addText ( " " ) ;
      builder.addIdentifier ( this.identifiers [ index ] ) ;
      builder.addText ( "=" ) ;
      builder.addBuilder ( this.expressions [ index ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECT_E ) ;
      index ++ ;
    }
    builder.addText ( " " ) ;
    builder.addKeyword ( "end" ) ;
    return builder ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Objects )
    {
      Objects other = ( Objects ) pObject ;
      return ( this.expressions.equals ( other.expressions ) ) ;
    }
    return false ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }
}
