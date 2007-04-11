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
public final class Send extends Expression
{
  /**
   * TODO
   * 
   * @see #getId()
   */
  private Identifier id ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression e ;


  /**
   * TODO
   * 
   * @param pExpression TODO
   * @param pIdentifier TODO
   */
  public Send ( Expression pExpression , Identifier pIdentifier )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    this.e = pExpression ;
    this.id = pIdentifier ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Send clone ( )
  {
    return new Send ( this.e.clone ( ) , this.id.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Send )
    {
      Send other = ( Send ) pObject ;
      return ( ( this.e.equals ( other.e ) ) && ( this.id.equals ( other.id ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Send" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #e
   */
  public Expression getE ( )
  {
    return this.e ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #id
   */
  public Identifier getId ( )
  {
    return this.id ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.e.hashCode ( ) + this.id.hashCode ( ) ;
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
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Send substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Send substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE = this.e.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Send ( newE , this.id.clone ( ) ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Send substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.e.substitute ( pTypeSubstitution ) ;
    return new Send ( newE , this.id.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_SEND ) ;
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_SEND_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "#" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.id
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
    }
    return this.prettyStringBuilder ;
  }
}
