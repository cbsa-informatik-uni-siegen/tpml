package de.unisiegen.tpml.core.subtyping;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class SubTypingProofModel extends AbstractProofModel {

	//
	// Constants
	//
	/**
	 * The {@link Logger} for this class.
	 * 
	 * @see Logger
	 */
	private static final Logger logger = Logger
			.getLogger ( SubTypingProofModel.class );

	//
	// Attributes
	//

	/**
	 * The current proof index, which indicates the number of steps that
	 * have been performed on the proof model so far (starting with one),
	 * and is used to generate new unique type variables in the associated
	 * contexts.
	 * 
	 * @see #getIndex()
	 * @see TypeCheckerProofContext#newTypeVariable()
	 * @see types.TypeVariable
	 */

	/**
	 * TODO
	 *
	 * @param root
	 * @param ruleSet
	 */
	public SubTypingProofModel ( MonoType type, MonoType type2,
			AbstractProofRuleSet ruleSet ) {
		super ( new DefaultSubTypingProofNode ( type, type2 ), ruleSet );
	}

	/**
	 * TODO
	 *
	 * @param node
	 * @throws ProofGuessException
	 * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void guess ( ProofNode pNode ) throws ProofGuessException {
		DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode;
		guessInternal ( node );
	}

	/**
	 * TODO
	 *
	 * @param rule
	 * @param node
	 * @throws ProofRuleException
	 * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule, de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void prove ( ProofRule rule, ProofNode pNode )
			throws ProofRuleException {

		if ( !this.ruleSet.contains ( rule ) ) {
			throw new IllegalArgumentException ( "The rule is invalid for the model" ); //$NON-NLS-1$
		}
		if ( !this.root.isNodeRelated ( pNode ) ) {
			throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
		}
		if ( pNode.getRules ( ).length > 0 ) {
			throw new IllegalArgumentException ( "The node is already completed" ); //$NON-NLS-1$
		}
		DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode;

		// try to apply the rule to the specified node
		applyInternal ( ( SubTypingProofRule ) rule, node );

	}

	//
	// Rule application
	//

	/**
	 * Applies the specified proof <code>rule</code> to the given <code>node</code>
	 * in this proof model.
	 * 
	 * @param rule the type proof rule to apply.
	 * @param node the type proof node to which to apply the <code>rule</code>.
	 * @param type the type the user guessed for the <code>node</code> or <code>null</code>
	 *             if the user didn't enter a type.
	 * 
	 * @throws ProofRuleException if the application of the <code>rule</code> to
	 *                            the <code>node</code> failed.
	 * 
	 * @see #guess(ProofNode)
	 * @see #prove(ProofRule, ProofNode)
	 */
	private void applyInternal ( SubTypingProofRule rule,
			DefaultSubTypingProofNode node ) throws ProofRuleException {

		// allocate a new TypeCheckerContext
		DefaultSubTypingProofContext context = new DefaultSubTypingProofContext (
				this, node );

		try {
			context.apply ( rule, node );

			//	 check if we are finished
			final DefaultSubTypingProofNode root = ( DefaultSubTypingProofNode ) getRoot ( );
			context.addRedoAction ( new Runnable ( ) {
				public void run ( ) {
					setFinished ( root.isFinished ( ) );
				}
			} );
			context.addUndoAction ( new Runnable ( ) {
				public void run ( ) {
					setFinished ( false );
				}
			} );
			// determine the redo and undo actions from the context
			final Runnable redoActions = context.getRedoActions ( );
			final Runnable undoActions = context.getUndoActions ( );

			// record the undo edit action for this proof step
			addUndoableTreeEdit ( new UndoableTreeEdit ( ) {
				public void redo ( ) {
					redoActions.run ( );
				}

				public void undo ( ) {
					undoActions.run ( );
				}
			} );
		}

		catch ( ProofRuleException e ) {
			// revert the actions performed so far
			context.revert ( );

			// re-throw the exception
			throw e;
		} catch ( SubTypingException e ) {
			// revert the actions performed so far
			context.revert ( );

			// re-throw the exception as proof rule exception
			throw new ProofRuleException ( node, rule, e );
		} catch ( RuntimeException e ) {
			// revert the actions performed so far
			context.revert ( );

			// re-throw the exception
			throw e;
		}
	}

	/**
	 * Implementation of the {@link #guess(ProofNode)} and {@link #guessWithType(ProofNode, MonoType)}
	 * methods.
	 * 
	 * @param node the proof node for which to guess the next step.
	 * @param type the type that the user entered for this <code>node</code> or <code>null</code> to
	 *             let the type inference algorithm guess the type.
	 * 
	 * @throws IllegalArgumentException if the <code>node</code> is invalid for this model.             
	 * @throws IllegalStateException if for some reason <code>node</code> cannot be proven.
	 * @throws NullPointerException if <code>node</code> is <code>null</code>.
	 * @throws ProofGuessException if the next proof step could not be guessed.
	 *
	 * @see #guess(ProofNode)
	 * @see #guessWithType(ProofNode, MonoType)
	 */
	private void guessInternal ( DefaultSubTypingProofNode node )
			throws ProofGuessException {

		if ( node == null ) {
			throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
		}
		if ( node.getSteps ( ).length > 0 ) {
			throw new IllegalArgumentException ( "The node is already completed" ); //$NON-NLS-1$
		}

		if ( !this.root.isNodeRelated ( node ) ) {
			throw new IllegalArgumentException ( "The node is invalid for the model" ); //$NON-NLS-1$
		}
		// try to guess the next rule
		logger.debug ( "Trying to guess a rule for " + node ); //$NON-NLS-1$
		for ( ProofRule rule : this.ruleSet.getRules ( ) ) {
			try {
				// try to apply the rule to the specified node
				applyInternal ( ( SubTypingProofRule ) rule, node );
				// remember that the user cheated
				setCheating ( true );

				// yep, we did it
				logger.debug ( "Successfully applied (" + rule + ") to " + node ); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			} catch ( ProofRuleException e ) {
				// rule failed to apply... so, next one, please
				logger.debug ( "Failed to apply (" + rule + ") to " + node, e ); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}
		}

		// unable to guess next step
		logger.debug ( "Failed to find rule to apply to " + node ); //$NON-NLS-1$
		throw new ProofGuessException ( node );
	}

	//
	// Proof context support
	//

	/**
	 * Adds a new child proof node below the <code>node</code> using the <code>context</code>, for the
	 * <code>environment</code>, <code>expression</code> and <code>type</code>.
	 * 
	 * @param context the <code>TypeCheckerProofContext</code> on which the action is to be performed.
	 * @param node the parent <code>DefaultTypeInferenceProofNode</code>.
	 * @param environment the <code>TypeEnvironment</code> for the child node.
	 * @param expression the <code>Expression</code> for the child node.
	 * @param type the type variable or the concrete type for the child node, used for the unification.
	 * 
	 * @throws IllegalArgumentException if <code>node</code> is invalid for this tree.
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	void contextAddProofNode ( final DefaultSubTypingProofContext context,
			final SubTypingProofNode pNode, final MonoType type, final MonoType type2 ) {

		final DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode;

		final DefaultSubTypingProofNode child = new DefaultSubTypingProofNode (
				type, type2 );
		final ProofStep[] oldSteps = node.getSteps ( );

		// add redo and undo options
		context.addRedoAction ( new Runnable ( ) {
			public void run ( ) {
				node.add ( child );
				nodesWereInserted ( node, new int[] { node.getIndex ( child ) } );
			}
		} );

		context.addUndoAction ( new Runnable ( ) {
			public void run ( ) {
				int index = node.getIndex ( child );
				node.remove ( index );
				nodesWereRemoved ( node, new int[] { index }, new Object[] { child } );
			}
		} );
	}

	/**
	 * Used to implement the {@link DefaultTypeInferenceProofContext#apply(TypeCheckerProofRule, TypeInferenceProofNode)}
	 * method of the {@link DefaultTypeInferenceProofContext} class.
	 * 
	 * @param context the type inference proof context.
	 * @param node the type inference node.
	 * @param rule the type checker rule.
	 * 
	 * @see DefaultTypeCheckerProofContext#apply(TypeCheckerProofRule, TypeCheckerProofNode)
	 */
	void contextSetProofNodeRule ( DefaultSubTypingProofContext context,
			final DefaultSubTypingProofNode node, final SubTypingProofRule rule ) {
		final ProofStep[] oldSteps = node.getSteps ( );

		context.addRedoAction ( new Runnable ( ) {
			public void run ( ) {
				node.setSteps ( new ProofStep[] { new ProofStep ( node.getType ( ),
						node.getType2 ( ), rule ) } );
				ProofRule[] rules = new ProofRule[1];
				rules[0] = rule;
				node.setRules ( rules );
				nodeChanged ( node );
			}
		} );

		context.addUndoAction ( new Runnable ( ) {
			public void run ( ) {
				node.setSteps ( oldSteps );
				ProofRule[] rules = null;
				node.setRules ( rules );
				nodeChanged ( node );
			}
		} );
	}

	/**
	 * get the rules of the actual proof rule set
	 *
	 * @return ProofRuleSet[] with all rules
	 * @see de.unisiegen.tpml.core.AbstractProofModel#getRules()
	 */
	@Override
	public ProofRule[] getRules ( ) {
		return this.ruleSet.getRules ( );
	}
}
