package de.unisiegen.tpml.core.unify;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.UnifyProofStep;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * base interface to nodes in a UnifyProofModel
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see de.unisiegen.tpml.core.ProofNode
 */
public interface UnifyProofNode extends ProofNode
{

  /**
   * Returns the {@link UnifyProofStep}s which were already performed on this
   * proof node. The steps represent the {@link ProofRule}s that were applied
   * to this node already and the associated expressions (which may be sub
   * expressions of the expression associated with this proof node), to which
   * the rules were applied.
   * 
   * @return the {@link UnifyProofStep}s or an empty array if no rules were
   *         applied to this node yet.
   * @see #getRules()
   */
  public UnifyProofStep [] getSteps ();


  /**
   * gets the list of type substitutions
   * 
   * @return the list of type substitutions for this node
   */
  public TypeSubstitutionList getTypeSubstitutions ();


  /**
   * gets the list of type equations
   * 
   * @return the list of type equations for this node
   */
  public TypeEquationList getTypeEquationList ();


  /**
   * Returns <code>true</code> if this node and all subnodes are finished. A
   * node is finished if a {@link de.unisiegen.tpml.core.ProofRule} was applied.
   * 
   * @return <code>true</code> if finished.
   */
  public boolean isFinished ();


  /**
   * Returns <code>true</code> if this node is provable
   * 
   * @return true if this node is provable, false otherwise
   */
  public boolean isProvable ();


  /**
   * sets the provable state of this node
   * 
   * @param provable indicates wheather this node is provable or not
   */
  public void setProvable ( boolean provable );


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public UnifyProofNode getChildAfter ( TreeNode child );


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public UnifyProofNode getChildAt ( int childIndex );


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public UnifyProofNode getChildBefore ( TreeNode child );


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public UnifyProofNode getFirstChild ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public UnifyProofNode getFirstLeaf ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public UnifyProofNode getLastChild ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public UnifyProofNode getLastLeaf ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public UnifyProofNode getParent ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getRoot()
   */
  public UnifyProofNode getRoot ();


  /**
   * get the rules applied to this node
   * 
   */
  public UnifyProofRule getRule ();

}
