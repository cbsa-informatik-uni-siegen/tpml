package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;


/**
 * Base interface to nodes in a
 * {@link de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel}.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.ProofNode
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel
 */
public interface TypeInferenceProofNode extends ProofNode
{
  //
  // Accessors
  //
  /**
   * Returns <code>true</code> if this node and all subnodes are finished. A
   * node is finished if a {@link de.unisiegen.tpml.core.ProofRule} was applied
   * and thereby a proper type is known.
   * 
   * @return <code>true</code> if finished.
   */
  public boolean isFinished ( ) ;


  /**
   * Convenience wrapper for the getRule method, which returns the
   * <code>TypeCheckerProofRule</code> that was applied to this node or
   * <code>null</code> if no rule was applied to this node so far.
   * 
   * @return the type proof rule that was applied to this node, or
   *         <code>null</code> if no rule was applied to this node.
   * @see TypeCheckerProofRule
   * @see de.unisiegen.tpml.core.ProofStep
   */
  public TypeCheckerProofRule getRule ( ) ;


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public TypeInferenceProofNode getChildAt ( int childIndex ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public TypeInferenceProofNode getParent ( ) ;


  /**
   * get the proof steps of this node
   * 
   * @return ProofStep[] steps
   */
  public ProofStep [ ] getSteps ( ) ;


  //
  // Tree Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public TypeInferenceProofNode getRoot ( ) ;


  //
  // Child Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public TypeInferenceProofNode getFirstChild ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public TypeInferenceProofNode getLastChild ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public TypeInferenceProofNode getChildAfter ( TreeNode aChild ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public TypeInferenceProofNode getChildBefore ( TreeNode aChild ) ;


  //
  // Leaf Queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public TypeInferenceProofNode getFirstLeaf ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public TypeInferenceProofNode getLastLeaf ( ) ;


  /**
   * get the type substitution list of this node
   * 
   * @return TypeSubstitutionList substitutions
   */
  public ArrayList < TypeSubstitution > getSubstitution ( ) ;


  /**
   * get the first type formula of this type formula list
   * 
   * @return TypeFormula first in list
   */
  public TypeFormula getFirstFormula ( ) ;


  /**
   * get the type formula list of this node
   * 
   * @return ArrayList with all type formulas
   */
  public ArrayList < TypeFormula > getAllFormulas ( ) ;
}
