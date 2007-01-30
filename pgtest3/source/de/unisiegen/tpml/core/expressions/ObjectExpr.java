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
public final class ObjectExpr extends Expression
{
  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @see #getIdentifier()
   */
  private String identifier ;


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pExpression TODO
   */
  public ObjectExpr ( String pIdentifier , Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    if ( ! ( pExpression instanceof Row ) )
    {
      throw new IllegalArgumentException ( "The expression must be a Row" ) ; //$NON-NLS-1$
    }
    this.identifier = pIdentifier ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.identifier , this.expression.clone ( ) ) ;
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
      if ( ! this.expression.equals ( other.expression ) )
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
    return this.identifier == null ? this.expression.hashCode ( )
        : this.identifier.hashCode ( ) + this.expression.hashCode ( ) ;
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
  public ObjectExpr substitute ( String pID , Expression pExpression )
  {
    if ( "self".equals ( pID ) ) //$NON-NLS-1$
    {
      return this ;
    }
    return new ObjectExpr ( this.identifier , this.expression.substitute ( pID ,
        pExpression ) ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public ObjectExpr substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression tmp = this.expression.substitute ( pTypeSubstitution ) ;
    return ( this.expression.equals ( tmp ) ) ? this : new ObjectExpr (
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
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_OBJECTEXPR_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "end" ) ; //$NON-NLS-1$
    return builder ;
  }
}
