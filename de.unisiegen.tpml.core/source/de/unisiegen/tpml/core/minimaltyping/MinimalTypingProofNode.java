package de.unisiegen.tpml.core.minimaltyping ;


import javax.swing.tree.TreeNode ;
import de.unisiegen.tpml.core.ExpressionProofNode ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Base interface to nodes in a
 * {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 * 
 * @author Benedikt Meurer
 * @version $Rev:878 $
 * @see de.unisiegen.tpml.core.ExpressionProofNode
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
public interface MinimalTypingProofNode extends ExpressionProofNode
{
  //
  // Accessors
  //
  /**
   * Returns the type for this type node, which is either a type variable or a
   * concrete type.
   * 
   * @return the monomorphic type for this type node.
   */
  public MonoType getType ( ) ;


  /**
   * Returns <code>true</code> if this node and all subnodes are finished. A
   * node is finished if a {@link de.unisiegen.tpml.core.ProofRule} was applied
   * and thereby a proper type is known.
   * 
   * @return <code>true</code> if finished.
   */
  public boolean isFinished ( ) ;


  /**
   * Convenience wrapper for the {@link ExpressionProofNode#getSteps()} method,
   * which returns the <code>TypeCheckerProofRule</code> that was applied to
   * this node or <code>null</code> if no rule was applied to this node so
   * far.
   * 
   * @return the type proof rule that was applied to this node, or
   *         <code>null</code> if no rule was applied to this node.
   * @see MinimalTypingProofRule
   * @see de.unisiegen.tpml.core.ProofStep
   */
  public MinimalTypingProofRule getRule ( ) ;


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public MinimalTypingProofNode getChildAt ( int childIndex ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public MinimalTypingProofNode getParent ( ) ;


  //
  // Tree Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getRoot()
   */
  public MinimalTypingProofNode getRoot ( ) ;


  //
  // Child Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public MinimalTypingProofNode getFirstChild ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public MinimalTypingProofNode getLastChild ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public MinimalTypingProofNode getChildAfter ( TreeNode aChild ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public MinimalTypingProofNode getChildBefore ( TreeNode aChild ) ;


  //
  // Leaf Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public MinimalTypingProofNode getFirstLeaf ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public MinimalTypingProofNode getLastLeaf ( ) ;


  /**
   * Get the type environment for this node
   * 
   * @return the type environment of this node
   */
  public TypeEnvironment getEnvironment ( ) ;

}
