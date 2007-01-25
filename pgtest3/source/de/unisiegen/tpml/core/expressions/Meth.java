package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public class Meth extends Expression
{
  private String identifier ;


  private Expression expression ;


  private Row parentRow ;


  public Meth ( String pIdentifier , Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "meth is null" ) ;
    }
    this.identifier = pIdentifier ;
    this.expression = pExpression ;
  }


  public void parentRow ( Row pRow )
  {
    this.parentRow = pRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Method" ; //$NON-NLS-1$
  }


  public Expression getE ( )
  {
    return this.expression ;
  }


  public String getIdentifier ( )
  {
    return this.identifier ;
  }


  @ Override
  public Meth clone ( )
  {
    return new Meth ( this.identifier , this.expression.clone ( ) ) ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Meth )
    {
      Meth other = ( Meth ) pObject ;
      return ( ( this.identifier.equals ( other.identifier ) ) && ( this.expression
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
    return this.identifier.hashCode ( ) + this.expression.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    return new Meth ( this.identifier , this.expression.substitute ( pID ,
        pExpression ) ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_METH ) ;
    builder.addKeyword ( "meth" ) ;
    builder.addText ( " " ) ;
    builder.addIdentifier ( this.identifier ) ;
    builder.addText ( " = " ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_METH_E ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( ";" ) ;
    return builder ;
  }


  public Row returnParentRow ( )
  {
    return this.parentRow ;
  }
}
