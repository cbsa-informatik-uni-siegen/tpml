package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent or expressions, printed as
 * <code>e1 || e2</code>. This is syntactic sugar for
 * <code>if e1 then true else e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see And
 * @see Condition
 * @see Condition1
 * @see Expression
 */
public final class Or extends Expression
{
  /**
   * The left side expression.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The right side expression.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  /**
   * Allocates a new <code>Or</code> instance with the specified
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression1 the left side expression.
   * @param pExpression2 the right side expression.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public Or ( Expression pExpression1 , Expression pExpression2 )
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
   */
  @ Override
  public String getCaption ( )
  {
    return "Or" ; //$NON-NLS-1$
  }


  /**
   * Returns the left side expression.
   * 
   * @return the e1.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the right side expression.
   * 
   * @return the e2.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Or clone ( )
  {
    return new Or ( this.e1.clone ( ) , this.e2.clone ( ) ) ;
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
    return ( this.e1 == newE1 && this.e2 == newE2 ) ? this : new Or ( newE1 ,
        newE2 ) ;
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
    return ( this.e1 == newE1 && this.e2 == newE2 ) ? this : new Or ( newE1 ,
        newE2 ) ;
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
        this , PRIO_OR ) ;
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_OR_E1 ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "||" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_OR_E2 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Or )
    {
      Or other = ( Or ) pObject ;
      return ( this.e1.equals ( other.e1 ) && this.e2.equals ( other.e2 ) ) ;
    }
    return false ;
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
}
