package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * The normal type checker proof node in the type checker algorithm, containing
 * an evironment. an expression and a type.
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeCheckerExpressionProofNode extends AbstractTypeCheckerProofNode implements
		TypeCheckerExpressionProofNode {

	//
	// Attributes
	//

	/**
	 * The type environment for this type checker proof node.
	 * 
	 * @see #getEnvironment()
	 * @see #setEnvironment(TypeEnvironment)
	 */
	protected TypeEnvironment environment;

	/**
	 * 
	 * Allocates a new <code>DefaultTypeCheckerExpressionProofNode</code>
	 *
	 * @param pEnvironment the type environment of this proof node
	 * @param pExpression the expression of this proof node
	 * @param pType the type of this proof node
	 */
	public DefaultTypeCheckerExpressionProofNode ( TypeEnvironment pEnvironment, Expression pExpression, MonoType pType ) {
		super ( pExpression );
		this.environment = pEnvironment;
		this.type = pType;
	}

	//
	// Accessors
	//

	/**
	 * Get the type environment of this proof node
	 * 
	 * @return the type environment of this proof node
	 */
	public TypeEnvironment getEnvironment ( ) {
		return this.environment;
	}

	/**
	 * Sets the type environment for this proof node to <code>environment</code>.
	 * 
	 * @param pEnvironment the new type environment for this node.
	 * 
	 * @throws NullPointerException if <code>environment</code> is <code>null</code>.
	 * 
	 * @see #getEnvironment()
	 */
	void setEnvironment ( TypeEnvironment pEnvironment ) {
		if ( pEnvironment == null ) {
			throw new NullPointerException ( "environment is null" ); //$NON-NLS-1$
		}
		this.environment = pEnvironment;
	}

	/*	  public DefaultTypeCheckerExpressionProofNode getChildAt(int childIndex){
	 return (DefaultTypeCheckerExpressionProofNode) super.getChildAt ( childIndex );
	 }*/

	/**
	 * {@inheritDoc}
	 * 
	 * Mainly useful for debugging purposes.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {
		StringBuilder builder = new StringBuilder ( );
		builder.append ( this.environment );
		builder.append ( " \u22b3 " ); //$NON-NLS-1$
		builder.append ( this.expression );
		builder.append ( " :: " ); //$NON-NLS-1$
		builder.append ( this.type );
		if ( getRule ( ) != null ) {
			builder.append ( " (" + getRule ( ) + ")" ); //$NON-NLS-1$//$NON-NLS-2$
		}
		return builder.toString ( );
	}

}