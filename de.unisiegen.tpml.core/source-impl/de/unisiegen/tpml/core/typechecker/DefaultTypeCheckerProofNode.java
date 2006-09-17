package de.unisiegen.tpml.core.typechecker;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Default implementation of the <code>TypeCheckerProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode
 */
final class DefaultTypeCheckerProofNode extends AbstractProofNode implements TypeCheckerProofNode {
  //
  // Attributes
  //
  
  /**
   * The type environment for this type checker proof node.
   * 
   * @see #getEnvironment()
   */
  private TypeEnvironment environment;
  
  /**
   * The type for this type node, which is either a type variable or a monorphic type.
   * 
   * @see #getType()
   */
  private MonoType type;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultTypeCheckerProofNode</code> with the specified <code>environment</code>,
   * <code>expression</code> and <code>type</code>.
   * 
   * @param environment the {@link TypeEnvironment} for this node.
   * @param expression the {@link Expression} for this node.
   * @param type the {@link de.unisiegen.tpml.core.types.TypeVariable} or concrete type for this node.
   * 
   * @throws NullPointerException if <code>environment</code>, <code>expression</code> or <code>type</code>
   *                              is <code>null</code>.
   */
  DefaultTypeCheckerProofNode(TypeEnvironment environment, Expression expression, MonoType type) {
    super(expression);
    if (environment == null) {
      throw new NullPointerException("environment is null");
    }
    if (type == null) {
      throw new NullPointerException("type is null");
    }
    this.environment = environment;
    this.type = type;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#getEnvironment()
   */
  public TypeEnvironment getEnvironment() {
    return this.environment;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#getType()
   */
  public MonoType getType() {
    return this.type;
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
    return (getSteps().length > 0);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
   */
  @Override
  public DefaultTypeCheckerProofNode getChildAt(int childIndex) {
    return (DefaultTypeCheckerProofNode)super.getChildAt(childIndex);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
   */
  @Override
  public DefaultTypeCheckerProofNode getParent() {
    return (DefaultTypeCheckerProofNode)super.getParent();
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
  public DefaultTypeCheckerProofNode getRoot() {
    return (DefaultTypeCheckerProofNode)super.getRoot();
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
  public DefaultTypeCheckerProofNode getFirstChild() {
    return (DefaultTypeCheckerProofNode)super.getFirstChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
   */
  @Override
  public DefaultTypeCheckerProofNode getLastChild() {
    return (DefaultTypeCheckerProofNode)super.getLastChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultTypeCheckerProofNode getChildAfter(TreeNode aChild) {
    return (DefaultTypeCheckerProofNode)super.getChildAfter(aChild);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultTypeCheckerProofNode getChildBefore(TreeNode aChild) {
    return (DefaultTypeCheckerProofNode)super.getChildBefore(aChild);
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
  public DefaultTypeCheckerProofNode getFirstLeaf() {
    return (DefaultTypeCheckerProofNode)super.getFirstLeaf();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
   */
  @Override
  public DefaultTypeCheckerProofNode getLastLeaf() {
    return (DefaultTypeCheckerProofNode)super.getLastLeaf();
  }
}
