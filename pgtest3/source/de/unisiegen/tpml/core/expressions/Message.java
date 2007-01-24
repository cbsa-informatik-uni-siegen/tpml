package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class Message extends Expression
{
  private String identifier ;


  private Expression expression ;


  public Message ( Expression pExpression , String pIdentifier )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "expression is null" ) ;
    }
    this.expression = pExpression ;
    this.identifier = pIdentifier ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Message" ; //$NON-NLS-1$
  }


  public String getIdentifier ( )
  {
    return this.identifier ;
  }


  public Expression getE ( )
  {
    return this.expression ;
  }


  @ Override
  public Message clone ( )
  {
    return new Message ( this.expression.clone ( ) , new String (
        this.identifier ) ) ;
  }


  @ Override
  public Message substitute ( String pID , Expression pExpression )
  {
    return new Message ( this.expression.substitute ( pID , pExpression ) ,
        new String ( this.identifier ) ) ;
  }


  @ Override
  public boolean isValue ( )
  {
    return false ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_MESSAGE ) ;
    builder
        .addBuilder ( this.expression
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_MESSAGE_E ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( "#" ) ;
    builder.addText ( " " ) ;
    builder.addIdentifier ( this.identifier ) ;
    return builder ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Message )
    {
      Message other = ( Message ) pObject ;
      return ( this.expression.equals ( other.expression ) ) ;
    }
    return false ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.expression.hashCode ( ) ;
  }
}
