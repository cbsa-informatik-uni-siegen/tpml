package de.unisiegen.tpml.core.smallstep;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.interpreters.InterpreterProofNode;


/**
 * Base interface to nodes in a
 * {@link de.unisiegen.tpml.core.smallstep.SmallStepProofModel}.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofModel
 */
public interface SmallStepProofNode extends InterpreterProofNode
{

  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public SmallStepProofNode getChildAt ( int childIndex );


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public SmallStepProofNode getParent ();


  //
  // Tree Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getRoot()
   */
  public SmallStepProofNode getRoot ();


  //
  // Child Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public SmallStepProofNode getFirstChild ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public SmallStepProofNode getLastChild ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public SmallStepProofNode getChildAfter ( TreeNode aChild );


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public SmallStepProofNode getChildBefore ( TreeNode aChild );


  //
  // Leaf Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public SmallStepProofNode getFirstLeaf ();


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public SmallStepProofNode getLastLeaf ();
}
