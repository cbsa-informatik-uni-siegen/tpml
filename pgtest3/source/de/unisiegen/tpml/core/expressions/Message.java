package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Message extends Expression
{
  /**
   * TODO
   * 
   * @see #getIdentifier()
   */
  private String identifier ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @param pExpression TODO
   * @param pIdentifier TODO
   */
  public Message ( Expression pExpression , String pIdentifier )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    this.expression = pExpression ;
    this.identifier = pIdentifier ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Message clone ( )
  {
    return new Message ( this.expression.clone ( ) , new String (
        this.identifier ) ) ;
  }


  /**
   * {@inheritDoc}
   */
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


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Message" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expression
   */
  public Expression getE ( )
  {
    return this.expression ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #identifier
   */
  public String getIdentifier ( )
  {
    return this.identifier ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expression.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Message substitute ( String pID , Expression pExpression )
  {
    return new Message ( this.expression.substitute ( pID , pExpression ) ,
        this.identifier ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Message substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression tmp = this.expression.substitute ( pTypeSubstitution ) ;
    return ( this.expression.equals ( tmp ) ) ? this : new Message ( tmp ,
        this.identifier ) ;
  }


  /**
   * {@inheritDoc}
   */
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
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( "#" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifier ) ;
    return builder ;
  }
}
