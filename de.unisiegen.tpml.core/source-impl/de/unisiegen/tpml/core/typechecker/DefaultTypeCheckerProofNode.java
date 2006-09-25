package de.unisiegen.tpml.core.typechecker;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Default implementation of the <code>TypeCheckerProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
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
   * @see #setEnvironment(TypeEnvironment)
   */
  private TypeEnvironment environment;
  
  /**
   * The type for this type node, which is either a type variable or a monorphic type.
   * 
   * @see #getType()
   * @see #setType(MonoType)
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
    setEnvironment(environment);
    setType(type);
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
   * Sets the type environment for this proof node to <code>environment</code>.
   * 
   * @param environment the new type environment for this node.
   * 
   * @throws NullPointerException if <code>environment</code> is <code>null</code>.
   * 
   * @see #getEnvironment()
   */
  void setEnvironment(TypeEnvironment environment) {
    if (environment == null) {
      throw new NullPointerException("environment is null");
    }
    this.environment = environment;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#isFinished()
   */
  public boolean isFinished() {
    if (!isProven()) {
      return false;
    }
    for (int n = 0; n < getChildCount(); ++n) {
      if (!getChildAt(n).isFinished()) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#getType()
   */
  public MonoType getType() {
    return this.type;
  }
  
  /**
   * Sets the type of this proof node to <code>type</code>.
   * 
   * @param type the new type for this proof node.
   * 
   * @throws NullPointerException if <code>type</code> is <code>null</code>.
   * 
   * @see #getType()
   */
  void setType(MonoType type) {
    if (type == null) {
      throw new NullPointerException("type is null");
    }
    this.type = type;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#getRule()
   */
  public TypeCheckerProofRule getRule() {
    ProofStep[] steps = getSteps();
    if (steps.length > 0) {
      return (TypeCheckerProofRule)steps[0].getRule();
    }
    else {
      return null;
    }
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
  
  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   * 
   * Mainly useful for debugging purposes.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.environment);
    builder.append(" \u22b3 ");
    builder.append(this.expression);
    builder.append(" :: ");
    builder.append(this.type);
    if (getRule() != null) {
      builder.append(" (" + getRule() + ")");
    }
    return builder.toString();
  }
}
