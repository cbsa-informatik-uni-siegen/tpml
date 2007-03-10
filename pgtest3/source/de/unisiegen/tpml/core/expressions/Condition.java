package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Represents the conditional evaluation in the expression hierarchy. The string
 * representation for conditions is <code>if e0 then e1 else e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Condition extends Expression
{
  /**
   * The condition.
   * 
   * @see #getE0()
   */
  private Expression e0 ;


  /**
   * The first expression, which is evaluated if <code>e0</code> evaluates to
   * <code>true</code>.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The second expression, which is evaluated if <code>e0</code> evaluates to
   * <code>false</code>.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  /**
   * Allocates a new <code>Condition</code> with the specified <code>e0</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression0 the condition.
   * @param pExpression1 the <code>true</code> case.
   * @param pExpression2 the <code>false</code> case.
   * @throws NullPointerException if <code>e0</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Condition ( Expression pExpression0 , Expression pExpression1 ,
      Expression pExpression2 )
  {
    if ( pExpression0 == null )
    {
      throw new NullPointerException ( "e0 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    this.e0 = pExpression0 ;
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Condition clone ( )
  {
    return new Condition ( this.e0.clone ( ) , this.e1.clone ( ) , this.e2
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Condition )
    {
      Condition other = ( Condition ) pObject ;
      return ( ( this.e0.equals ( other.e0 ) )
          && ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Condition" ; //$NON-NLS-1$
  }


  /**
   * Returns the conditional expression.
   * 
   * @return the conditional expression.
   */
  public Expression getE0 ( )
  {
    return this.e0 ;
  }


  /**
   * Returns the expression that is evaluated if <code>e0</code> evaluates to
   * <code>true</code>.
   * 
   * @return the <code>true</code> case.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the expression that is evaluated if <code>e0</code> evaluates to
   * <code>false</code>.
   * 
   * @return the <code>false</code> case.
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
    return this.e0.hashCode ( ) + this.e1.hashCode ( ) + this.e2.hashCode ( ) ;
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
  public Condition substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE0 = this.e0.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE1 = this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE2 = this.e2.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    return new Condition ( newE0 , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Condition substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE0 = this.e0.substitute ( pTypeSubstitution ) ;
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return new Condition ( newE0 , newE1 , newE2 ) ;
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
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "else" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e2
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CONDITION_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
