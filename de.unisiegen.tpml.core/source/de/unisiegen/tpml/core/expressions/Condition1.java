package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent conditions without an else block in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Condition
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Condition1 extends Expression
{
  //
  // Attributes
  //
  /**
   * The conditional block.
   * 
   * @see #getE0()
   */
  private Expression e0 ;


  /**
   * The <code>true</code> block.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>Condition1</code> with the specified
   * <code>e0</code> and <code>e1</code>.
   * 
   * @param e0 the conditional block.
   * @param e1 the <code>true</code> case.
   * @throws NullPointerException if <code>e0</code> or <code>e1</code> is
   *           <code>null</code>.
   */
  public Condition1 ( Expression e0 , Expression e1 )
  {
    if ( e0 == null )
    {
      throw new NullPointerException ( "e0 is null" ) ;
    }
    if ( e1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ;
    }
    this.e0 = e0 ;
    this.e1 = e1 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Condition1" ; //$NON-NLS-1$
  }


  //
  // Accessors
  //
  /**
   * Returns the conditional part.
   * 
   * @return the conditional block
   */
  public Expression getE0 ( )
  {
    return this.e0 ;
  }


  /**
   * Returns the <code>true</code> case.
   * 
   * @return the <code>true</code> block.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Condition1 clone ( )
  {
    return new Condition1 ( this.e0.clone ( ) , this.e1.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @ Override
  public Expression substitute ( TypeSubstitution substitution )
  {
    Expression e0 = this.e0.substitute ( substitution ) ;
    Expression e1 = this.e1.substitute ( substitution ) ;
    return ( this.e0 == e0 && this.e1 == e1 ) ? this
        : new Condition1 ( e0 , e1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Expression substitute ( String id , Expression e )
  {
    Expression e0 = this.e0.substitute ( id , e ) ;
    Expression e1 = this.e1.substitute ( id , e ) ;
    return ( this.e0 == e0 && this.e1 == e1 ) ? this
        : new Condition1 ( e0 , e1 ) ;
  }


  //
  // Pretty printing
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_CONDITION ) ;
    builder.addKeyword ( "if" ) ;
    builder.addText ( " " ) ;
    builder.addBuilder ( this.e0.toPrettyStringBuilder ( factory ) ,
        PRIO_CONDITION_E0 ) ;
    builder.addBreak ( ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( "then" ) ;
    builder.addText ( " " ) ;
    builder.addBuilder ( this.e1.toPrettyStringBuilder ( factory ) ,
        PRIO_CONDITION_E1 ) ;
    return builder ;
  }


  //
  // Base methods
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof Condition1 )
    {
      Condition1 other = ( Condition1 ) obj ;
      return ( this.e0.equals ( other.e0 ) && this.e1.equals ( other.e1 ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.e0.hashCode ( ) + this.e1.hashCode ( ) ;
  }
}
