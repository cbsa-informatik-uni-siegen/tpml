package de.unisiegen.tpml.core;


import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.expressions.Expression;


/**
 * A proof step represents an application of a
 * {@link de.unisiegen.tpml.core.ProofRule} to an
 * {@link de.unisiegen.tpml.core.entities.TypeEquationList}.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.ProofRule
 */
public final class UnifyProofStep
{

  //
  // Attributes
  //
  /**
   * The {@link TypeEquationList} to which the {@link ProofRule} was applied.
   * 
   * @see #getEquationList()
   */
  private TypeEquationList equations;


  /**
   * The {@link ProofRule} that was applied to an {@link Expression} to advance
   * in the proof.
   * 
   * @see #getRule()
   */
  private ProofRule rule;


  //
  // Constructor
  //
  /**
   * Allocates a new proof step with the given <code>equations</code> and the
   * specified <code>rule</code>.
   * 
   * @param equations the {@link Expression}.
   * @param pRule the {@link ProofRule} that was applied.
   * @throws NullPointerException if <code>equations</code> or
   *           <code>rule</code> is <code>null</code>.
   */
  public UnifyProofStep ( TypeEquationList equations, ProofRule pRule )
  {
    if ( pRule == null )
    {
      throw new NullPointerException ( "rule is null" ); //$NON-NLS-1$
    }
    if ( equations == null )
    {
      throw new NullPointerException ( "expression is null" ); //$NON-NLS-1$
    }
    this.equations = equations;
    this.rule = pRule;
  }


  //
  // Primitives
  //
  /**
   * Returns the {@link TypeEquationList} to which the {@link ProofRule} was applied
   * to advance in the proof.
   * 
   * @return the expression.
   */
  public TypeEquationList getEquationList ()
  {
    return this.equations;
  }


  /**
   * Returns the {@link ProofRule} that was applied to an {@link Expression} to
   * advance in the proof.
   * 
   * @return the proof rule that was applied in this step.
   * @see #getEquationList()
   */
  public ProofRule getRule ()
  {
    return this.rule;
  }


  //
  // Base methods
  //
  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof UnifyProofStep )
    {
      UnifyProofStep other = ( UnifyProofStep ) obj;
      return ( this.equations.equals ( other.equations ) || this.rule
          .equals ( other.rule ) );
    }
    return false;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode ()
  {
    return this.equations.hashCode () + this.rule.hashCode ();
  }
}
