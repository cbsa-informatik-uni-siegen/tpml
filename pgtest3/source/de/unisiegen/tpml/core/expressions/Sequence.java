package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent statements for sequential execution of
 * commands in the expression hierarchy. The string representation for
 * <code>Sequence</code>s is <code>e1;e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Sequence extends Expression
{
  /**
   * The first statement.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The second statement.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  /**
   * Allocates a new <code>Sequence</code> with the given expressions
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression1 the first statement.
   * @param pExpression2 the second statement.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public Sequence ( Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Sequence clone ( )
  {
    return new Sequence ( this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Sequence )
    {
      Sequence other = ( Sequence ) pObject ;
      return ( ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Sequence" ; //$NON-NLS-1$
  }


  /**
   * Returns the first statement.
   * 
   * @return the first statement.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second statement.
   * 
   * @return the second statement.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.e1.hashCode ( ) + this.e2.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE1 = this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE2 = this.e2.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    return new Sequence ( newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Expression substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return new Sequence ( newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_SEQUENCE ) ;
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_SEQUENCE_E1 ) ;
    builder.addText ( "; " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_SEQUENCE_E2 ) ;
    return builder ;
  }
}
