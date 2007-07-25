package de.unisiegen.tpml.core.typechecker;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractExpressionProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Default implementation of the <code>TypeCheckerProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Rev:878M $
 *
 * @see de.unisiegen.tpml.core.AbstractExpressionProofNode
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode
 */
public abstract class AbstractTypeCheckerProofNode extends AbstractExpressionProofNode implements TypeCheckerProofNode {
	//
	// Attributes
	//

	/**
	 * The type for this type node, which is either a type variable or a monorphic type.
	 * 
	 * @see #getType()
	 * @see #setType(MonoType)
	 */
	protected MonoType type;

	//
	// Constructor (package)
	//

	/**
	 * Allocates a new <code>DefaultTypeCheckerProofNode</code> with the specified <code>environment</code>,
	 * <code>expression</code> and <code>type</code>.
	 * 
	 * @param pExpression the {@link Expression} for this node.
	 * 
	 * @throws NullPointerException if <code>environment</code>, <code>expression</code> or <code>type</code>
	 *                              is <code>null</code>.
	 */
	public AbstractTypeCheckerProofNode ( Expression pExpression ) {
		super ( pExpression );
	}

	//
	// Accessors
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#isFinished()
	 */
	public boolean isFinished ( ) {
		if ( !isProven ( ) ) {
			return false;
		}
		for ( int n = 0; n < getChildCount ( ); ++n ) {
			if ( !getChildAt ( n ).isFinished ( ) ) {
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
	public MonoType getType ( ) {
		return this.type;
	}

	/**
	 * Sets the type of this proof node to <code>type</code>.
	 * 
	 * @param pType the new type for this proof node.
	 * 
	 * @throws NullPointerException if <code>type</code> is <code>null</code>.
	 * 
	 * @see #getType()
	 */
	void setType ( MonoType pType ) {
		if ( pType == null ) {
			throw new NullPointerException ( "type is null" ); //$NON-NLS-1$
		}
		this.type = pType;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#getRule()
	 */
	public TypeCheckerProofRule getRule ( ) {
		ProofStep[] steps = getSteps ( );
		if ( steps.length > 0 ) {
			return ( TypeCheckerProofRule ) steps[0].getRule ( );
		}
		return null;
	}

	//
	// Primitives
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.ProofNode#isProven()
	 */
	public boolean isProven ( ) {
		return ( getSteps ( ).length > 0 );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
	 */
	@Override
	public AbstractTypeCheckerProofNode getChildAt ( int childIndex ) {
		return ( AbstractTypeCheckerProofNode ) super.getChildAt ( childIndex );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
	 */
	@Override
	public AbstractTypeCheckerProofNode getParent ( ) {
		return ( AbstractTypeCheckerProofNode ) super.getParent ( );
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
	public AbstractTypeCheckerProofNode getRoot ( ) {
		return ( AbstractTypeCheckerProofNode ) super.getRoot ( );
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
	public AbstractTypeCheckerProofNode getFirstChild ( ) {
		return ( AbstractTypeCheckerProofNode ) super.getFirstChild ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
	 */
	@Override
	public AbstractTypeCheckerProofNode getLastChild ( ) {
		return ( AbstractTypeCheckerProofNode ) super.getLastChild ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
	 */
	@Override
	public AbstractTypeCheckerProofNode getChildAfter ( TreeNode aChild ) {
		return ( AbstractTypeCheckerProofNode ) super.getChildAfter ( aChild );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
	 */
	@Override
	public AbstractTypeCheckerProofNode getChildBefore ( TreeNode aChild ) {
		return ( AbstractTypeCheckerProofNode ) super.getChildBefore ( aChild );
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
	public AbstractTypeCheckerProofNode getFirstLeaf ( ) {
		return ( AbstractTypeCheckerProofNode ) super.getFirstLeaf ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
	 */
	@Override
	public AbstractTypeCheckerProofNode getLastLeaf ( ) {
		return ( AbstractTypeCheckerProofNode ) super.getLastLeaf ( );
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
	public abstract String toString ( );
	/* {
	 StringBuilder builder = new StringBuilder();
	 builder.append(this.environment);
	 builder.append(" \u22b3 "); //$NON-NLS-1$
	 builder.append(this.expression);
	 builder.append(" :: "); //$NON-NLS-1$
	 builder.append(this.type);
	 if (getRule() != null) {
	 builder.append(" (" + getRule() + ")");  //$NON-NLS-1$//$NON-NLS-2$
	 }
	 return builder.toString();
	 }*/
}
