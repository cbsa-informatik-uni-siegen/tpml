package de.unisiegen.tpml.core.minimaltyping;


import de.unisiegen.tpml.core.typechecker.TypeEnvironment;


/**
 * This node type represents the normal expression proof nodes in the Minimal
 * Typing Algorithm. The node contains an {@link TypeEnvironment}, an
 * {@link de.unisiegen.tpml.core.expressions.Expression}, and a
 * {@link de.unisiegen.tpml.core.types.MonoType}.
 * 
 * @author Benjamin Mies
 */
public interface MinimalTypingExpressionProofNode extends
    MinimalTypingProofNode
{

  /**
   * Returns the type environment for this type node, that is, the environment
   * in which the type of the expression was determined.
   * 
   * @return the type environment for this type node.
   */
  public TypeEnvironment getEnvironment ();
}
