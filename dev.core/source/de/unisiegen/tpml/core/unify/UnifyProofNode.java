package de.unisiegen.tpml.core.unify;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker;
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
  public TypeEquationListTypeChecker getTypeEquationList ();


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
}
