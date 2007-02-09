package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEquation;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;

public interface TypeInferenceProofContext {

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
	  public void addProofNode(TypeInferenceProofNode node, TypeEnvironment environment, Expression expression, MonoType type);
	  
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
	  
	  /**
	   * Instantiates the <code>type</code> using the context information. That said, if <code>type</code>
	   * is a {@link de.unisiegen.tpml.core.types.PolyType} the {@link #newTypeVariable()} method is used
	   * to allocate new type variables for the quantified variables of the polymorphic type, while if
	   * <code>type</code> is a {@link MonoType} this method simply returns the <code>type</code>.
	   * 
	   * @param type the {@link Type} to instantiate.
	   * 
	   * @return the instantiated monomorphic type.
	   * 
	   * @throws NullPointerException if <code>type</code> is <code>null</code>.
	   * 
	   * @see MonoType
	   * @see de.unisiegen.tpml.core.types.PolyType
	   */
	  public MonoType instantiate(Type type);
	  
	  /**
	   * Allocates a new <code>TypeVariable</code> that is not already used in the type checker proof
	   * model associated with this type checker proof context.
	   * 
	   * @return a new, unique <code>TypeVariable</code>.
	   */
	  public TypeVariable newTypeVariable();
}
