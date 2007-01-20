package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public class Meth extends Expression
{
  private String name ;


  private Expression expression ;


  public Meth ( String pName , Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "meth is null" ) ;
    }
    this.name = pName ;
    this.expression = pExpression ;
  }


  public Expression getE ( )
  {
    return this.expression ;
  }


  public String getName ( )
  {
    return this.name ;
  }


  @ Override
  public Meth clone ( )
  {
    return new Meth ( this.name , this.expression.clone ( ) ) ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Meth )
    {
      Meth other = ( Meth ) pObject ;
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
    return new Meth ( this.name , this.expression.substitute ( pID ,
        pExpression ) ) ;
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
    builder.addText ( " = " ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_OBJECT_E ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( ";" ) ;
    return builder ;
  }
}
