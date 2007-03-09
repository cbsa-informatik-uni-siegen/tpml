package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent and expressions, printed as
 * <code>e1 &amp;&amp; e2</code>. This is syntactic sugar for
 * <code>if e1 then e2 else false</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Condition
 * @see Condition1
 * @see Expression
 * @see Or
 */
public final class And extends Expression
{
  /**
   * The left-side expression.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The right-side expression.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  /**
   * Allocates a new <code>And</code> instance with the specified
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression1 the left side expression.
   * @param pExpression2 the right side expression.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public And ( Expression pExpression1 , Expression pExpression2 )
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
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public And clone ( )
  {
    return new And ( this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof And )
    {
      And other = ( And ) pObject ;
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
    return "And" ; //$NON-NLS-1$
  }


  /**
   * Returns the left side expression.
   * 
   * @return the left side expression.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the right side expression.
   * 
   * @return the right side expression.
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
    return new And ( newE1 , newE2 ) ;
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
    return new And ( newE1 , newE2 ) ;
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
        this , PRIO_AND ) ;
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_AND_E1 ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "&&" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_AND_E2 ) ;
    return builder ;
  }
}
