package de.unisiegen.tpml.core.minimaltyping;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofRuleException;

/**
 * Abstract base class for implementations of the
 * <code>MinimalTypingProofRule</code> interface.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofRule
 * @see de.unisiegen.tpml.core.AbstractProofRule
 */
public abstract class AbstractMinimalTypingProofRule extends AbstractProofRule
		implements MinimalTypingProofRule {
	//
	// Constructor (package)
	//
	/**
	 * Allocates a new <code>AbstractMinimalTypingProofRule</code> of the
	 * specified <code>name</code>.
	 * 
	 * @param group the group id of the type rule, see the description of the
	 *          {@link AbstractProofRule#getGroup()} method for details.
	 * @param name the name of the type rule to allocate.
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 */
	AbstractMinimalTypingProofRule ( int group, String name ) {
		super ( group, name );
	}

	//
	// Primitives
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofRule#apply(de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext,
	 *      de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode)
	 */
	public void apply ( MinimalTypingProofContext context,
			MinimalTypingProofNode node ) throws ProofRuleException {
		if ( node == null ) {
			throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
		}
		if ( context == null ) {
			throw new NullPointerException ( "context is null" ); //$NON-NLS-1$
		}
		try {
			applyInternal ( context, node );
		} catch ( ProofRuleException e ) {
			throw e;
		} catch ( InvocationTargetException e ) {
			if ( e.getTargetException ( ) instanceof ClassCastException ) {
				throw new ProofRuleException ( MessageFormat.format ( Messages
						.getString ( "ProofRuleException.0" ), this, node ), node, //$NON-NLS-1$
						this, e.getTargetException ( ) );
			}
			else if ( e.getTargetException ( ) instanceof IllegalArgumentException ) {
				throw new ProofRuleException ( MessageFormat.format ( Messages
						.getString ( "ProofRuleException.0" ), this, node ), node, //$NON-NLS-1$
						this, e.getTargetException ( ) );
		}
			else if ( e.getCause ( ) instanceof RuntimeException ) {
				throw new RuntimeException ( e.getTargetException ( ).getMessage ( ) );
			}
			else if ( e.getTargetException ( ).getMessage ( ) == null ) {
				throw new ProofRuleException ( node, this, e.getTargetException ( ) );
			}
			throw new ProofRuleException (
					e.getTargetException ( ).getMessage ( ), node, this, e
							.getTargetException ( ) );
		} catch ( Exception e ) {
			
			 // check if e contains a usable error message
			 for ( Throwable t = e ; t != null ; t = t.getCause ( ) )
			 {
			 if ( t instanceof IllegalArgumentException )
			 {
			 throw new ProofRuleException ( t.getMessage ( ) , node , this , e ) ;
			 }
			 }
			 throw new ProofRuleException ( node , this , e ) ;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofRule#update(de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext,
	 *      de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode)
	 */
	public void update ( MinimalTypingProofContext context,
			MinimalTypingProofNode node ) {
		if ( node == null ) {
			throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
		}
		if ( context == null ) {
			throw new NullPointerException ( "context is null" ); //$NON-NLS-1$
		}
		try {
			updateInternal ( context, node );
		} catch ( RuntimeException e ) {
			throw e;
		} catch ( InvocationTargetException e ) {
			throw new RuntimeException ( e.getTargetException ( ).getMessage ( ) );
		} catch ( Exception e ) {
			throw new RuntimeException ( e );

		}
	}

	//
	// Abstract methods
	//
	/**
	 * Abstract internal apply method, implemented by the
	 * {@link AbstractMinimalTypingProofRuleSet} class while registering new proof
	 * rules.
	 * 
	 * @param context see
	 *          {@link #apply(MinimalTypingProofContext, MinimalTypingProofNode)}.
	 * @param node see
	 *          {@link #apply(MinimalTypingProofContext, MinimalTypingProofNode)}.
	 * @throws Exception if an error occurs while applying the rule to the
	 *           <code>node</code> using the <code>context</code>.
	 * @see #apply(MinimalTypingProofContext, MinimalTypingProofNode)
	 */
	protected abstract void applyInternal ( MinimalTypingProofContext context,
			MinimalTypingProofNode node ) throws Exception;

	/**
	 * Abstract internal update method, implemented by the
	 * {@link AbstractMinimalTypingProofRuleSet} class while registering new proof
	 * rules.
	 * 
	 * @param context see
	 *          {@link #update(MinimalTypingProofContext, MinimalTypingProofNode)}.
	 * @param node see
	 *          {@link #update(MinimalTypingProofContext, MinimalTypingProofNode)}.
	 * @throws Exception if an error occurs while updating the <code>node</code>
	 *           using the <code>context</code>.
	 */
	protected abstract void updateInternal ( MinimalTypingProofContext context,
			MinimalTypingProofNode node ) throws Exception;
}
