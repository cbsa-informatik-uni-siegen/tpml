package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class ObjectExpr extends Expression
{
  private String [ ] identifiers ;


  private Expression [ ] expressions ;


  private String method ;


  public ObjectExpr ( String [ ] pIdentifiers , Expression [ ] pExpressions )
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
    this.method = null ;
  }


  public ObjectExpr ( String [ ] pIdentifiers , Expression [ ] pExpressions ,
      String pMethod )
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
    this.method = pMethod ;
  }


  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  public String getIdentifiers ( int n )
  {
    return this.identifiers [ n ] ;
  }


  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  public String getMethod ( )
  {
    return this.method ;
  }


  @ Override
  public ObjectExpr clone ( )
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
    if ( this.method == null )
    {
      return new ObjectExpr ( tmpI , tmpE ) ;
    }
    return new ObjectExpr ( tmpI , tmpE , new String ( this.method ) ) ;
  }


  @ Override
  public ObjectExpr substitute ( String pID , Expression pExpression )
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
    return new ObjectExpr ( tmpI , tmpE ) ;
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
    if ( this.method != null ) 
    {
      return false ;
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
    int index = 0 ;
    while ( index < this.expressions.length )
    {
      builder.addText ( " " ) ;
      builder.addBreak ( ) ;
      builder.addKeyword ( "method" ) ;
      builder.addText ( " " ) ;
      builder.addIdentifier ( this.identifiers [ index ] ) ;
      builder.addText ( " = " ) ;
      builder.addBuilder ( this.expressions [ index ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECT_E ) ;
      index ++ ;
    }
    builder.addText ( " " ) ;
    builder.addKeyword ( "end" ) ;
    if ( this.method != null )
    {
      builder.addText ( " " ) ;
      builder.addKeyword ( "#" ) ;
      builder.addText ( " " ) ;
      builder.addIdentifier ( this.method ) ;
    }
    return builder ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectExpr )
    {
      ObjectExpr other = ( ObjectExpr ) pObject ;
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
