package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;

/**
 * The proof context for the type checker. The proof context acts as an interface to the proof model
 * for the type rules.
 *
 * @author Benedikt Meurer
 * @version $Rev:1309 $
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
public interface MinimalTypingProofContext {
  //
  // Primitives
  //
  
  /**
   * Adds a new child node below the <code>node</code>, to prove the
   * type safety of the <code>expression</code> in the specified
   * <code>environment</code>. The <code>type</code> is either a
   * type variable or a concrete type, that is the expected type for
   * the <code>expression</code> (used during the unification).
   * 
   * @param node the parent node.
   * @param environment the new {@link TypeEnvironment} for the child node.
   * @param expression the expression for the child node.
   * @param type the expected type for the <code>expression</code>.
   * 
   * @throws IllegalArgumentException if the <code>node</code> is invalid for the proof model that is associated
   *                                  with this proof context.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void addProofNode(MinimalTypingProofNode node, TypeEnvironment environment, Expression expression);
  
  public void addProofNode(MinimalTypingProofNode node, MonoType type, MonoType type2);
  
  public void setNodeType(MinimalTypingProofNode node, MonoType type);
  
  /**
   * Returns the {@link Type} for the given <code>expression</code> if possible, i.e. <b>(BOOL)</b> if
   * <code>expression</code> is an instance of {@link de.unisiegen.tpml.core.expressions.BooleanConstant}.
   * 
   * @param expression an {@link Expression}.
   * 
   * @return the {@link Type} for the <code>expression</code>.
   * 
   * @throws IllegalArgumentException if <code>expression</code> is not a simple expression, like a
   *                                  constant for which it is possible to determine the type out of the box.
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   */
  public Type getTypeForExpression(Expression expression);
  
  
}
