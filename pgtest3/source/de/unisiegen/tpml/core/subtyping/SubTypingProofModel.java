package de.unisiegen.tpml.core.subtyping;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * The heart of the subtyping algorithm. Subtyping proof rules are supplied via an
 * {@link de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet}
 * that is passed to the constructor.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofContext
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode
 */
public class SubTypingProofModel extends AbstractProofModel implements SubTypingModel {

	//
	// Constants
	//
	/**
	 * The {@link Logger} for this class.
	 * 
	 * @see Logger
	 */
	private static final Logger logger = Logger.getLogger ( SubTypingProofModel.class );

	private boolean mode = true;

	AbstractSubTypingProofRuleSet ruleSet;

	//
	// Constructor
	//

	/**
	 * Allocates a new <code>SubTypingProofModel</code> with the specified <code>type</code> and
	 * <code>type2</type> as its root node.
	 * 
	 * @param type the first {@link MonoType} for the root node.
	 * @param type2 the second {@link MonoType} for the root node.
	 * @param ruleSet the available type rules for the model.
	 * @param mode the chosen mode (Advanced or Beginner)
	 * 
	 * @throws NullPointerException if either one <code>type</code> or <code>ruleSet</code> is
	 *                              <code>null</code>.
	 *
	 * @see AbstractProofModel#AbstractProofModel(AbstractProofNode, AbstractProofRuleSet)
	 */
	public SubTypingProofModel ( MonoType type, MonoType type2, AbstractSubTypingProofRuleSet ruleSet, boolean mode ) {
		super ( new DefaultSubTypingProofNode ( type, type2 ), ruleSet );
		this.ruleSet = ruleSet;
		this.mode = mode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void guess ( ProofNode pNode ) throws ProofGuessException {
		DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode;
		guessInternal ( node );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule, de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void prove ( ProofRule rule, ProofNode pNode ) throws ProofRuleException {

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
	private void applyInternal ( SubTypingProofRule rule, DefaultSubTypingProofNode node ) throws ProofRuleException {

		// allocate a new TypeCheckerContext
		DefaultSubTypingProofContext context = new DefaultSubTypingProofContext ( this, node );

		try {
			context.apply ( rule, node );

			//	 check if we are finished
			final DefaultSubTypingProofNode modelRoot = getRoot ( );
			context.addRedoAction ( new Runnable ( ) {
				@SuppressWarnings ( "synthetic-access" )
				public void run ( ) {
					setFinished ( modelRoot.isFinished ( ) );
				}
			} );
			context.addUndoAction ( new Runnable ( ) {
				@SuppressWarnings ( "synthetic-access" )
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
			throw new ProofRuleException ( e.getMessage ( ), node, rule, null );
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
	private void guessInternal ( DefaultSubTypingProofNode node ) throws ProofGuessException {
		if ( node == null ) {
			throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
		}
		if ( node.getSteps ( ).length > 0 ) {
			throw new IllegalArgumentException ( MessageFormat.format (
					Messages.getString ( "IllegalArgumentException.0" ), node ) ); //$NON-NLS-1$
		}

		if ( !this.root.isNodeRelated ( node ) ) {
			throw new IllegalArgumentException ( MessageFormat.format (
					Messages.getString ( "IllegalArgumentException.1" ), node ) ); //$NON-NLS-1$
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

		throw new ProofGuessException (
				MessageFormat.format ( Messages.getString ( "ProofGuessException.0" ), node ), node ); //$NON-NLS-1$
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
	void contextAddProofNode ( final DefaultSubTypingProofContext context, final SubTypingProofNode pNode,
			final MonoType type, final MonoType type2 ) {

		final DefaultSubTypingProofNode node = ( DefaultSubTypingProofNode ) pNode;

		final DefaultSubTypingProofNode child = new DefaultSubTypingProofNode ( type, type2 );

		// add redo and undo options
		context.addRedoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.add ( child );
				nodesWereInserted ( node, new int[] { node.getIndex ( child ) } );
			}
		} );

		context.addUndoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
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
	void contextSetProofNodeRule ( DefaultSubTypingProofContext context, final DefaultSubTypingProofNode node,
			final SubTypingProofRule rule ) {
		final ProofStep[] oldSteps = node.getSteps ( );

		context.addRedoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				node.setSteps ( new ProofStep[] { new ProofStep ( node.getType ( ), node.getType2 ( ), rule ) } );
				ProofRule[] rules = new ProofRule[1];
				rules[0] = rule;
				node.setRules ( rules );
				nodeChanged ( node );
			}
		} );

		context.addUndoAction ( new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
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

	/**
	 * TODO
	 *
	 * @return
	 * @see de.unisiegen.tpml.core.AbstractProofModel#getRoot()
	 */
	@Override
	public DefaultSubTypingProofNode getRoot ( ) {
		return ( DefaultSubTypingProofNode ) super.getRoot ( );
	}

	/**
	 * 
	 * set a new root for this model
	 *
	 * @param type first MonoType of the new root
	 * @param type2 second MonoType of the new root
	 */
	public void setRoot ( MonoType type, MonoType type2 ) {
		this.root = new DefaultSubTypingProofNode ( type, type2 );
	}

	/**
	 * 
	 * Set the mode (Beginner, Advanced) of choosen by the user
	 *
	 * @param mode boolean, true means advanced, false beginner mode
	 */
	public void setMode ( boolean mode ) {
		if ( this.mode != mode ) {
			this.mode = mode;
			if ( this.ruleSet.getLanguage ( ).getName ( ).equalsIgnoreCase ( "l2o" ) ) { //$NON-NLS-1$

				if ( mode ) {
					this.ruleSet.unregister ( "TRANS" ); //$NON-NLS-1$
					this.ruleSet.unregister ( "OBJECT-WIDTH" ); //$NON-NLS-1$
					this.ruleSet.unregister ( "OBJECT-DEPTH" ); //$NON-NLS-1$
					this.ruleSet.unregister ( "REFL" ); //$NON-NLS-1$

					this.ruleSet.registerByMethodName ( L2OLanguage.L2O, "OBJECT", "applyObject" ); //$NON-NLS-1$ //$NON-NLS-2$
					this.ruleSet.registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					this.ruleSet.unregister ( "OBJECT" ); //$NON-NLS-1$
					this.ruleSet.unregister ( "REFL" ); //$NON-NLS-1$

					this.ruleSet.registerByMethodName ( L2OLanguage.L2O, "TRANS", "applyTrans" ); //$NON-NLS-1$ //$NON-NLS-2$
					this.ruleSet.registerByMethodName ( L2OLanguage.L2O, "OBJECT-WIDTH", "applyObjectWidth" ); //$NON-NLS-1$ //$NON-NLS-2$
					this.ruleSet.registerByMethodName ( L2OLanguage.L2O, "OBJECT-DEPTH", "applyObjectDepth" ); //$NON-NLS-1$ //$NON-NLS-2$
					this.ruleSet.registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}
}
