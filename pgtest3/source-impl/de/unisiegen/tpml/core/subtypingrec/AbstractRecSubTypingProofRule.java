package de.unisiegen.tpml.core.subtypingrec;

import java.lang.reflect.InvocationTargetException;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;

/**
 * Abstract base class for implementations of the <code>SubTypingProofRule</code> interface.
 *
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofRule
 * @see de.unisiegen.tpml.core.AbstractProofRule
 */
public abstract class AbstractRecSubTypingProofRule extends AbstractProofRule implements RecSubTypingProofRule {

	/**
	 * Allocates a new <code>AbstractSubTypingProofRule</code> of the specified <code>name</code>.
	 * 
	 * @param group the group id of the type rule, see the description of the
	 *              {@link AbstractProofRule#getGroup()} method for details.
	 * @param name the name of the type rule to allocate.
	 *
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 * 
	 * @see AbstractProofRule#AbstractProofRule(String)
	 */
	public AbstractRecSubTypingProofRule ( int group, String name ) {
		super ( group, name );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofRule#apply(de.unisiegen.tpml.core.subtyping.SubTypingProofContext, de.unisiegen.tpml.core.subTyping.RecSubTypingProofNode)
	 */
	public void apply ( DefaultRecSubTypingProofContext context, DefaultRecSubTypingProofNode node )
			throws ProofRuleException {
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
			if ( e.getTargetException ( ) instanceof RuntimeException )
				throw new ProofRuleException ( node, this, e );
			throw new ProofRuleException ( e.getTargetException ( ).getMessage ( ), node, this, e );
		} catch ( Exception e ) {
			// check if e contains a usable error message
			for ( Throwable t = e; t != null; t = t.getCause ( ) ) {
				if ( t instanceof IllegalArgumentException ) {
					throw new ProofRuleException ( t.getMessage ( ), node, this, e );
				}
			}
			if ( e instanceof ProofRuleException )
				throw new ProofRuleException ( e.getMessage ( ), node, this, e );
			throw new ProofRuleException ( node, this, e );

		}
	}

	/*
	 /**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofRule#update(de.unisiegen.tpml.core.subtyping.SubTypingProofContext, de.unisiegen.tpml.core.subtyping.SubTypingProofNode)
	 /*
	 public void update ( SubTypingProofContext context, SubTypingProofNode node ) {
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
	 } catch ( Exception e ) {
	 throw new RuntimeException ( e );
	 }
	 }*/

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
	protected abstract void applyInternal ( RecSubTypingProofContext context, RecSubTypingProofNode node )
			throws Exception, SubTypingException;

	/*
	 /**
	 * Abstract internal update method, implemented by the {@link AbstractTypeCheckerProofRuleSet} class
	 * while registering new proof rules.
	 * 
	 * @param context see {@link #update(TypeCheckerProofContext, TypeCheckerProofNode)}.
	 * @param node see {@link #update(TypeCheckerProofContext, TypeCheckerProofNode)}.
	 * 
	 * @throws Exception if an error occurs while updating the <code>node</code> using the 
	 *                   <code>context</code>.
	 *
	 protected abstract void updateInternal ( SubTypingProofContext context,
	 SubTypingProofNode node ) throws Exception;
	 */
}
