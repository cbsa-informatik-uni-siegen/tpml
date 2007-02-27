package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Represents the <b>(APP)</b> expression in the expression hierarchy. The
 * string representation for applications is <code>e1 e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Application extends Expression
{
  /**
   * The first, left-side expression.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The second, right-side expression.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  /**
   * Allocates a new application of <code>e1</code> to <code>e2</code>.
   * 
   * @param pExpression1 the first expression (the operation).
   * @param pExpression2 the second expression (the operand).
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public Application ( Expression pExpression1 , Expression pExpression2 )
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
    return "Application" ; //$NON-NLS-1$
  }


  /**
   * Returns the first expression of the application.
   * 
   * @return the first expression of the application.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second expression of the application.
   * 
   * @return the second expression of the application.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Application clone ( )
  {
    return new Application ( this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * An <code>Application</code> can be a value if it consists of a binary
   * operator and a value, or if it consists of a <code>UnaryCons</code>
   * operator and a value.
   * 
   * @return <code>true</code> if the application consists of a binary
   *         operator and a value.
   * @see Expression#isValue()
   */
  @ Override
  public boolean isValue ( )
  {
    return ( ( this.e1 instanceof BinaryOperator || this.e1 instanceof UnaryCons ) && this.e2
        .isValue ( ) ) ;
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
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @ Override
  public Application substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return ( this.e1 == newE1 && this.e2 == newE2 ) ? this : new Application (
        newE1 , newE2 ) ;
  }


  /**
   * Substitutes <code>e</code> for <code>id</code> in the two sub
   * expressions of the application.
   * 
   * @param pId the identifier for which to substitute.
   * @param pExpression the expression to substitute for <code>id</code>.
   * @return the resulting expression.
   * @see #getE1()
   * @see #getE2()
   * @see Expression#substitute(String, Expression , boolean )
   */
  @ Override
  public Application substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE1 = this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE2 = this.e2.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    return ( this.e1 == newE1 && this.e2 == newE2 ) ? this : new Application (
        newE1 , newE2 ) ;
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
        this , PRIO_APPLICATION ) ;
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_APPLICATION_E1 ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_APPLICATION_E2 ) ;
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
    if ( pObject instanceof Application )
    {
      Application other = ( Application ) pObject ;
      return ( ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) ) ;
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
