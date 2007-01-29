package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class ObjectExpr extends Expression
{
  /**
   * TODO
   * 
   * @see #getE()
   */
  private Row row ;


  /**
   * TODO
   * 
   * @see #getIdentifier()
   */
  private String identifier ;


  /**
   * TODO
   * 
   * @param pRow TODO
   */
  public ObjectExpr ( Row pRow )
  {
    if ( pRow == null )
    {
      throw new NullPointerException ( "Row is null" ) ; //$NON-NLS-1$
    }
    this.row = pRow ;
    this.identifier = null ;
  }


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pRow TODO
   */
  public ObjectExpr ( String pIdentifier , Row pRow )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Identifier is null" ) ; //$NON-NLS-1$
    }
    if ( pRow == null )
    {
      throw new NullPointerException ( "Row is null" ) ; //$NON-NLS-1$
    }
    this.identifier = pIdentifier ;
    this.row = pRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.identifier , this.row.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectExpr )
    {
      ObjectExpr other = ( ObjectExpr ) pObject ;
      if ( ! this.row.equals ( other.row ) )
      {
        return false ;
      }
      if ( ( this.identifier == null ) && other.identifier != null )
      {
        return false ;
      }
      if ( ( this.identifier != null ) && other.identifier == null )
      {
        return false ;
      }
      if ( ( this.identifier == null ) && other.identifier == null )
      {
        return true ;
      }
      return this.identifier.equals ( other.identifier ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Object" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #row
   */
  public Expression getE ( )
  {
    return this.row ;
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
    return this.identifier == null ? this.row.hashCode ( ) : this.identifier
        .hashCode ( )
        + this.row.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.row.isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    if ( "self".equals ( pID ) ) //$NON-NLS-1$
    {
      return this ;
    }
    return new ObjectExpr ( this.identifier , ( Row ) this.row.substitute (
        pID , pExpression ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECTEXPR ) ;
    builder.addKeyword ( "object" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    if ( this.identifier != null )
    {
      builder.addText ( "( " ) ; //$NON-NLS-1$
      builder.addIdentifier ( this.identifier ) ;
      builder.addText ( " ) " ) ; //$NON-NLS-1$
    }
    builder.addBreak ( ) ;
    builder.addBuilder ( this.row
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_OBJECTEXPR_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "end" ) ; //$NON-NLS-1$
    return builder ;
  }
}
