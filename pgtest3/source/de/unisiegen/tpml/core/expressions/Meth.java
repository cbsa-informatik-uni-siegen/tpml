package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1067 $
 */
public class Meth extends Expression
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
   * @see #parentRow(Row) ;
   * @see #returnParentRow() ;
   */
  private Row parentRow ;


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public Meth ( String pIdentifier , MonoType pTau , Expression pExpression )
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
  public Meth clone ( )
  {
    return new Meth ( this.identifier , this.tau , this.expression.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Meth )
    {
      Meth other = ( Meth ) pObject ;
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
    return "Method" ; //$NON-NLS-1$
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
   * TODO
   * 
   * @param pRow TODO
   * @see #parentRow
   * @see #returnParentRow()
   */
  public void parentRow ( Row pRow )
  {
    this.parentRow = pRow ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #parentRow
   * @see #parentRow(Row)
   */
  public Row returnParentRow ( )
  {
    return this.parentRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Meth substitute ( String pID , Expression pExpression )
  {
    return new Meth ( this.identifier , this.tau , this.expression.substitute (
        pID , pExpression ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Meth substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType tmp = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new Meth ( this.identifier , tmp , this.expression
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
        this , PRIO_METH ) ;
    builder.addKeyword ( "method" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifier ) ;
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.tau
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_METH_TAU ) ;
    }
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_METH_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ";" ) ; //$NON-NLS-1$
    return builder ;
  }
}
