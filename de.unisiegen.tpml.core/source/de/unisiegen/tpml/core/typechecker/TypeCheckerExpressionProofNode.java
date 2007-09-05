package de.unisiegen.tpml.core.typechecker;

/**
 * The public interface for the normal type checker proof node in the type
 * checker algorithm, containing an evironment. an expression and a type.
 * 
 * @author Benjamin Mies
 */
public interface TypeCheckerExpressionProofNode extends TypeCheckerProofNode {
	/**
	 * Returns the type environment for this type node, that is, the environment
	 * in which the type of the expression was determined.
	 * 
	 * @return the type environment for this type node.
	 */
	public TypeEnvironment getEnvironment ( );
}
