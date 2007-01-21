package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public class CurriedMeth extends Expression
{
  private String name ;


  private String [ ] identifiers ;


  private Expression expression ;


  public CurriedMeth ( String pName , String [ ] pIdentifiers ,
      Expression pExpression )
  {
    if ( pName == null )
    {
      throw new NullPointerException ( "name is null" ) ;
    }
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ;
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "expression is null" ) ;
    }
    this.name = pName ;
    this.identifiers = pIdentifiers ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Method" ; //$NON-NLS-1$
  }


  public Expression getE ( )
  {
    return this.expression ;
  }


  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  public String getName ( )
  {
    return this.name ;
  }


  @ Override
  public CurriedMeth clone ( )
  {
    return new CurriedMeth ( this.name , this.identifiers.clone ( ) ,
        this.expression.clone ( ) ) ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof CurriedMeth )
    {
      CurriedMeth other = ( CurriedMeth ) pObject ;
      return ( ( this.name.equals ( other.name ) ) && ( this.expression
          .equals ( other.expression ) ) ) ;
    }
    return false ;
  }


  @ Override
  public boolean isValue ( )
  {
    return this.expression.isValue ( ) ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) + this.expression.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    return new CurriedMeth ( this.name , this.identifiers.clone ( ) ,
        this.expression.substitute ( pID , pExpression ) ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECT ) ;
    builder.addKeyword ( "meth" ) ;
    builder.addText ( " " ) ;
    builder.addIdentifier ( this.name ) ;
    for ( String id : this.identifiers )
    {
      builder.addText ( " " ) ;
      builder.addIdentifier ( id ) ;
    }
    builder.addText ( " = " ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_OBJECT_E ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( ";" ) ;
    return builder ;
  }
}
