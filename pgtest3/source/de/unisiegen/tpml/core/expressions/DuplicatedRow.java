package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class DuplicatedRow extends Expression
{
  private Expression [ ] expressions ;


  protected String [ ] identifiers ;


  public DuplicatedRow ( String [ ] pIdentifiers , Expression [ ] pExpressions )
  {
    ArrayList < String > id = new ArrayList < String > ( ) ;
    ArrayList < Expression > expr = new ArrayList < Expression > ( ) ;
    for ( int i = pIdentifiers.length - 1 ; i >= 0 ; i -- )
    {
      if ( ! id.contains ( pIdentifiers [ i ] ) )
      {
        id.add ( 0 , pIdentifiers [ i ] ) ;
        expr.add ( 0 , pExpressions [ i ] ) ;
      }
    }
    this.identifiers = new String [ id.size ( ) ] ;
    this.expressions = new Expression [ expr.size ( ) ] ;
    for ( int i = 0 ; i < id.size ( ) ; i ++ )
    {
      this.identifiers [ i ] = id.get ( i ) ;
      this.expressions [ i ] = expr.get ( i ) ;
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Duplicated-Row" ; //$NON-NLS-1$
  }


  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  @ Override
  public DuplicatedRow clone ( )
  {
    Expression [ ] tmpE = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmpE [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new DuplicatedRow ( this.identifiers , tmpE ) ;
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
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DuplicatedRow )
    {
      DuplicatedRow other = ( DuplicatedRow ) pObject ;
      return ( this.expressions.equals ( other.expressions ) && this.identifiers
          .equals ( other.identifiers ) ) ;
    }
    return false ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expressions.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int n = 0 ; n < tmp.length ; ++ n )
    {
      tmp [ n ] = this.expressions [ n ].substitute ( pID , pExpression ) ;
    }
    return new DuplicatedRow ( this.identifiers , tmp ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECT ) ;
    builder.addKeyword ( "{" ) ;
    builder.addKeyword ( "<" ) ;
    builder.addText ( " " ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addIdentifier ( this.identifiers [ i ] ) ;
      builder.addText ( " = " ) ;
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECT_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( ", " ) ;
        builder.addBreak ( ) ;
      }
    }
    builder.addText ( " " ) ;
    builder.addKeyword ( ">" ) ;
    builder.addKeyword ( "}" ) ;
    return builder ;
  }
}
