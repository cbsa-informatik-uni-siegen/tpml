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
public class Attr extends Expression
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
   * @see #parentRow(Row) ;
   * @see #returnParentRow() ;
   */
  private Row parentRow ;


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pExpression TODO
   */
  public Attr ( String pIdentifier , Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    this.identifier = pIdentifier ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Attr clone ( )
  {
    return new Attr ( this.identifier , this.expression.clone ( ) ) ;
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
      return ( ( this.identifier.equals ( other.identifier ) ) && ( this.expression
          .equals ( other.expression ) ) ) ;
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
    return this.identifier.hashCode ( ) + this.expression.hashCode ( ) ;
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
  public Attr substitute ( String pID , Expression pExpression )
  {
    return new Attr ( this.identifier , this.expression.substitute ( pID ,
        pExpression ) ) ;
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
    Expression tmp = this.expression.substitute ( pTypeSubstitution ) ;
    return ( this.expression.equals ( tmp ) ) ? this : new Attr (
        this.identifier , tmp ) ;
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
    builder.addKeyword ( "attr" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifier ) ;
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ATTR_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ";" ) ; //$NON-NLS-1$
    return builder ;
  }
}
