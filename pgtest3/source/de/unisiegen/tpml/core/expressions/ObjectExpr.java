package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class ObjectExpr extends Expression
{
  private Row row ;


  private String identifier ;


  public ObjectExpr ( Row pRow )
  {
    if ( pRow == null )
    {
      throw new NullPointerException ( "row is null" ) ;
    }
    this.row = pRow ;
    this.identifier = null ;
  }


  public ObjectExpr ( String pIdentifier , Row pRow )
  {
    if ( pRow == null )
    {
      throw new NullPointerException ( "row is null" ) ;
    }
    this.identifier = pIdentifier ;
    this.row = pRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Object" ; //$NON-NLS-1$
  }


  public String getIdentifier ( )
  {
    return this.identifier ;
  }


  public Expression getE ( )
  {
    return this.row ;
  }


  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.identifier , this.row.clone ( ) ) ;
  }


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


  @ Override
  public boolean isValue ( )
  {
    return this.row.isValue ( ) ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.identifier == null ? this.row.hashCode ( ) : this.identifier
        .hashCode ( )
        + this.row.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    if ( "self".equals ( pID ) )
    {
      return this ;
    }
    return new ObjectExpr ( this.identifier , ( Row ) this.row.substitute (
        pID , pExpression ) ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECTEXPR ) ;
    builder.addKeyword ( "object" ) ;
    builder.addText ( " " ) ;
    if ( this.identifier != null )
    {
      builder.addText ( "( " ) ;
      builder.addIdentifier ( this.identifier ) ;
      builder.addText ( " ) " ) ;
    }
    builder.addBreak ( ) ;
    builder.addBuilder ( this.row
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_OBJECTEXPR_E ) ;
    builder.addText ( " " ) ;
    builder.addBreak ( ) ;
    builder.addKeyword ( "end" ) ;
    return builder ;
  }
}
