package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent conditions without an else block in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Condition
 * @see Expression
 */
public final class Condition1 extends Expression
{
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


  /**
   * Allocates a new <code>Condition1</code> with the specified
   * <code>e0</code> and <code>e1</code>.
   * 
   * @param pExpression0 the conditional block.
   * @param pExpression1 the <code>true</code> case.
   * @throws NullPointerException if <code>e0</code> or <code>e1</code> is
   *           <code>null</code>.
   */
  public Condition1 ( Expression pExpression0 , Expression pExpression1 )
  {
    if ( pExpression0 == null )
    {
      throw new NullPointerException ( "e0 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    this.e0 = pExpression0 ;
    this.e1 = pExpression1 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Condition1 clone ( )
  {
    return new Condition1 ( this.e0.clone ( ) , this.e1.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Condition1 )
    {
      Condition1 other = ( Condition1 ) pObject ;
      return ( ( this.e0.equals ( other.e0 ) ) && ( this.e1.equals ( other.e1 ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Condition-1" ; //$NON-NLS-1$
  }


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


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.e0.hashCode ( ) + this.e1.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Condition1 substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Condition1 substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE0 = this.e0.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE1 = this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    return new Condition1 ( newE0 , newE1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Condition1 substitute ( TypeSubstitution substitution )
  {
    Expression newE0 = this.e0.substitute ( substitution ) ;
    Expression newE1 = this.e1.substitute ( substitution ) ;
    return new Condition1 ( newE0 , newE1 ) ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_CONDITION ) ;
      this.prettyStringBuilder.addKeyword ( "if" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e0
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CONDITION_E0 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "then" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e1
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CONDITION_E1 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
