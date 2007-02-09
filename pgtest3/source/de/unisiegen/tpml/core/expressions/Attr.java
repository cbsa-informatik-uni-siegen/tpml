package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public class Attr extends Expression
{
  /**
   * TODO
   * 
   * @see #getId()
   */
  private String identifier ;


  /**
   * TODO
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public Attr ( String pIdentifier , MonoType pTau , Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    this.identifier = pIdentifier ;
    this.tau = pTau ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Attr clone ( )
  {
    return new Attr ( this.identifier , this.tau , this.expression.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Attr )
    {
      Attr other = ( Attr ) pObject ;
      return ( ( this.identifier.equals ( other.identifier ) )
          && ( this.expression.equals ( other.expression ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Attribute" ; //$NON-NLS-1$
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
  public String getId ( )
  {
    return this.identifier ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #tau
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifier.hashCode ( ) + this.expression.hashCode ( )
        + ( this.tau == null ? 0 : this.tau.hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expression.isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Attr substitute ( String pID , Expression pExpression )
  {
    return new Attr ( this.identifier , this.tau , this.expression.substitute (
        pID , pExpression ) ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Attr substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType tmp = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new Attr ( this.identifier , tmp , this.expression
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_ATTR ) ;
    builder.addKeyword ( "val" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifier ) ;
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.tau
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ATTR_TAU ) ;
    }
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ATTR_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ";" ) ; //$NON-NLS-1$
    return builder ;
  }
}
