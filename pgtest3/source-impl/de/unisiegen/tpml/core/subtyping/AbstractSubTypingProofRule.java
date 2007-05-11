/**
 * TODO
 */
package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public abstract class AbstractSubTypingProofRule extends AbstractProofRule
		implements SubTypingProofRule {

	/**
	 * TODO
	 *
	 * @param group
	 * @param name
	 */
	public AbstractSubTypingProofRule ( int group, String name ) {
		super ( group, name );
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO
	 *
	 * @param context
	 * @param node
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofRule#apply(de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofContext, de.unisiegen.tpml.core.subtyping.DefaultSubTypingProofNode)
	 */
	public void apply ( DefaultSubTypingProofContext context,
			DefaultSubTypingProofNode node ) throws ProofRuleException {
		if ( node == null ) {
			throw new NullPointerException ( "node is null" );
		}
		if ( context == null ) {
			throw new NullPointerException ( "context is null" );
		}
		try {
			applyInternal ( context, node );
		} catch ( ProofRuleException e ) {
			throw e;
		} catch ( Exception e ) {
			// check if e contains a usable error message
			for ( Throwable t = e; t != null; t = t.getCause ( ) ) {
				if ( t instanceof IllegalArgumentException ) {
					throw new ProofRuleException ( t.getMessage ( ), node, this, e );
				}
			}
			throw new ProofRuleException ( node, this, e );
		}
	}

	/**
	 * TODO
	 *
	 * @param context
	 * @param node
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofRule#update(de.unisiegen.tpml.core.subtyping.SubTypingProofContext, de.unisiegen.tpml.core.subtyping.SubTypingProofNode)
	 */
	public void update ( SubTypingProofContext context, SubTypingProofNode node ) {
		if ( node == null ) {
			throw new NullPointerException ( "node is null" );
		}
		if ( context == null ) {
			throw new NullPointerException ( "context is null" );
		}
		try {
			updateInternal ( context, node );
		} catch ( RuntimeException e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RuntimeException ( e );
		}
	}

	//
	// Abstract methods
	//

	/**
	 * Abstract internal apply method, implemented by the {@link AbstractTypeCheckerProofRuleSet} class
	 * while registering new proof rules.
	 * 
	 * @param context see {@link #apply(TypeCheckerProofContext, TypeCheckerProofNode)}.
	 * @param node see {@link #apply(TypeCheckerProofContext, TypeCheckerProofNode)}.
	 * 
	 * @throws Exception if an error occurs while applying the rule to the <code>node</code> using
	 *                   the <code>context</code>.
	 *                   
	 * @see #apply(TypeCheckerProofContext, TypeCheckerProofNode)
	 */
	protected abstract void applyInternal ( SubTypingProofContext context,
			SubTypingProofNode node ) throws Exception;

	/**
	 * Abstract internal update method, implemented by the {@link AbstractTypeCheckerProofRuleSet} class
	 * while registering new proof rules.
	 * 
	 * @param context see {@link #update(TypeCheckerProofContext, TypeCheckerProofNode)}.
	 * @param node see {@link #update(TypeCheckerProofContext, TypeCheckerProofNode)}.
	 * 
	 * @throws Exception if an error occurs while updating the <code>node</code> using the 
	 *                   <code>context</code>.
	 */
	protected abstract void updateInternal ( SubTypingProofContext context,
			SubTypingProofNode node ) throws Exception;
}
