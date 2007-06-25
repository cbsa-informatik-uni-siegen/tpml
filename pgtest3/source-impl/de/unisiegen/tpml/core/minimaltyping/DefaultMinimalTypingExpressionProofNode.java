package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;

public class DefaultMinimalTypingExpressionProofNode extends
		AbstractMinimalTypingProofNode implements MinimalTypingExpressionProofNode {
	
  /**
   * The type environment for this type checker proof node.
   * 
   * @see #getEnvironment()
   * @see #setEnvironment(TypeEnvironment)
   */
  protected TypeEnvironment environment;
  
  /**
   * The type for this type node, which is either a type variable or a monorphic type.
   * 
   * @see #getType()
   * @see #setType(MonoType)
   */
  	protected MonoType type;

    /**
     * Allocates a new <code>DefaultMinimalTypingExpressionProofNode</code> with the specified <code>environment</code>,
     * <code>expression</code> and <code>type</code>.
     * 
     * @param environment the {@link TypeEnvironment} for this node.
     * @param expression the {@link Expression} for this node.
     * @param type the {@link de.unisiegen.tpml.core.types.TypeVariable} or concrete type for this node.
     * 
     * @throws NullPointerException if <code>environment</code>, <code>expression</code> or <code>type</code>
     *                              is <code>null</code>.
     */
    public DefaultMinimalTypingExpressionProofNode(TypeEnvironment environment, Expression expression) {
    	super(expression);
      setEnvironment(environment);
      
    }
	
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getEnvironment()
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
   * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getType()
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
  public void setType(MonoType type) {
    this.type = type;
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
