package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * The proof context for the type checker. The proof context acts as an interface to the proof model
 * for the type rules.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
public interface TypeCheckerProofContext {
  //
  // Primitives
  //
  
  /**
   * Adds a type equation to the list of type equations that serve as input for the unification
   * algorithm.
   * 
   * @param left the monomorphic type on the left side.
   * @param right the monomorphic type on the right side.
   * 
   * @see TypeEquation
   */
  public void addEquation(MonoType left, MonoType right);
  
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
  public void addProofNode(TypeCheckerProofNode node, TypeEnvironment environment, Expression expression, MonoType type);
  
  /**
   * Allocates a new <code>TypeVariable</code> that is not already used in the type checker proof
   * model associated with this type checker proof context.
   * 
   * @return a new, unique <code>TypeVariable</code>.
   */
  public TypeVariable newTypeVariable();
}
