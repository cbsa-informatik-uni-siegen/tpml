package de.unisiegen.tpml.core.smallstep;

import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;

/**
 * Default implementation of the <code>SmallStepProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.smallstep.SmallStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode
 */
final class DefaultSmallStepProofNode extends AbstractInterpreterProofNode implements SmallStepProofNode {
  //
  // Constructor (package)
  //
  
  /**
   * Convenience wrapper for {@link #DefaultSmallStepProofNode(Expression, Store)}, which passes an
   * empty {@link Store} for the <code>store</code> parameter.
   * 
   * @param expression the {@link Expression} for this node.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   */
  DefaultSmallStepProofNode(Expression expression) {
    this(expression, new DefaultStore());
  }
  
  /**
   * Allocates a new <code>DefaultSmallStepProofNode</code> with the specified
   * <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   * 
   * @throws NullPointerException if either <code>expression</code> or <code>store</code>
   *                              is <code>null</code>.
   */
  DefaultSmallStepProofNode(Expression expression, Store store) {
    super(expression, store);
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRules()
   */
  public List<SmallStepProofRule> getRules() {
    List<SmallStepProofRule> rules = new LinkedList<SmallStepProofRule>();
    for (ProofStep step : getSteps()) {
      rules.add((SmallStepProofRule)step.getRule());
    }
    return rules;
  }

  

  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven() {
    // check if any axiom was applied
    for (SmallStepProofRule rule : getRules()) {
      if (rule.isAxiom()) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
   */
  @Override
  public DefaultSmallStepProofNode getChildAt(int childIndex) {
    return (DefaultSmallStepProofNode)super.getChildAt(childIndex);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
   */
  @Override
  public DefaultSmallStepProofNode getParent() {
    return (DefaultSmallStepProofNode)super.getParent();
  }
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
   */
  @Override
  public DefaultSmallStepProofNode getRoot() {
    return (DefaultSmallStepProofNode)super.getRoot();
  }
  
  
  
  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstChild()
   */
  @Override
  public DefaultSmallStepProofNode getFirstChild() {
    return (DefaultSmallStepProofNode)super.getFirstChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
   */
  @Override
  public DefaultSmallStepProofNode getLastChild() {
    return (DefaultSmallStepProofNode)super.getLastChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultSmallStepProofNode getChildAfter(TreeNode aChild) {
    return (DefaultSmallStepProofNode)super.getChildAfter(aChild);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultSmallStepProofNode getChildBefore(TreeNode aChild) {
    return (DefaultSmallStepProofNode)super.getChildBefore(aChild);
  }
  
  
  
  //
  // Leaf Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstLeaf()
   */
  @Override
  public DefaultSmallStepProofNode getFirstLeaf() {
    return (DefaultSmallStepProofNode)super.getFirstLeaf();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
   */
  @Override
  public DefaultSmallStepProofNode getLastLeaf() {
    return (DefaultSmallStepProofNode)super.getLastLeaf();
  }
}
