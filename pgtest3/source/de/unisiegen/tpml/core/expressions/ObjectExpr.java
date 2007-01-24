package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public final class ObjectExpr extends Expression
{
  private Row row ;


  public ObjectExpr ( Row pRow )
  {
    if ( pRow == null )
    {
      throw new NullPointerException ( "row is null" ) ;
    }
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


  public Expression getE ( )
  {
    return this.row ;
  }


  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.row.clone ( ) ) ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectExpr )
    {
      ObjectExpr other = ( ObjectExpr ) pObject ;
      return this.row.equals ( other.row ) ;
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
    return this.row.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    return new ObjectExpr ( ( Row ) this.row.substitute ( pID , pExpression ) ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECTEXPR ) ;
    builder.addKeyword ( "object" ) ;
    builder.addText ( " " ) ;
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