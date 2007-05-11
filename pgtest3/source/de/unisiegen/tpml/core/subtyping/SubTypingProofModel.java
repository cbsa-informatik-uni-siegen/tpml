package de.unisiegen.tpml.core.subtyping;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.CannotRedoException;
import de.unisiegen.tpml.core.CannotUndoException;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
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
	private int index = 1;

	/**
	 * TODO
	 *
	 * @param root
	 * @param ruleSet
	 */
	public SubTypingProofModel ( MonoType type, MonoType type2,
			AbstractProofRuleSet ruleSet ) {
		super ( new DefaultSubTypingProofNode(type, type2), ruleSet );
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
		DefaultSubTypingProofNode node = (DefaultSubTypingProofNode) pNode;
	guessInternal (node);
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
	public void prove ( ProofRule rule, ProofNode node )
			throws ProofRuleException {
	// TODO Auto-generated method stub

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
		context.apply(rule,node);
		

		// think about what to do here
		//TODO add try catch and revert in catch block
		
		
		
		
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
	private void guessInternal ( DefaultSubTypingProofNode node) throws ProofGuessException {

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
				applyInternal ( ( SubTypingProofRule ) rule, node  );
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
			final SubTypingProofNode pNode,
			final MonoType type,
			final MonoType type2 ) {
		
		final DefaultSubTypingProofNode node = (DefaultSubTypingProofNode) pNode;

		final DefaultSubTypingProofNode child = new DefaultSubTypingProofNode (
				type, type2 );
		final ProofStep[] oldSteps = node.getSteps ( );

		// add redo and undo options
		addUndoableTreeEdit ( new UndoableTreeEdit ( ) {

			public void redo ( ) {

				setFinished ( ( ( DefaultSubTypingProofNode ) root ).isFinished ( ) );

				node.add ( child );
				nodesWereInserted ( node, new int[] { node.getIndex ( child ) } );
				nodeChanged ( node );
			}

			public void undo ( ) {

				// update the "finished" state
				setFinished ( false );

				// remove the child and revert the steps
				int[] indices = { pNode.getIndex ( child ) };
				node.removeAllChildren ( );
				nodesWereRemoved ( pNode, indices, new Object[] { child } );
				node.setSteps ( oldSteps );
				nodeChanged ( pNode );

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
			 DefaultSubTypingProofNode node,
			final SubTypingProofRule rule ) {

		node
				.setSteps ( new ProofStep[] { new ProofStep (node.getType ( ), node.getType2 ( ), rule ) } );
		node.setRules ( new ProofRule[] { rule } );
		nodeChanged ( node );
	}

	//
	// Undo/Redo
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofModel#addUndoableTreeEdit(de.unisiegen.tpml.core.AbstractProofModel.UndoableTreeEdit)
	 */
	@Override
	public void addUndoableTreeEdit ( UndoableTreeEdit edit ) {

		// perform the redo of the edit
		edit.redo ( );

		// add to the undo history
		super.addUndoableTreeEdit ( edit );
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

	@Override
	public void undo ( ) throws CannotUndoException {
		super.undo ( );
		index-- ;
	}

	@Override
	public void redo ( ) throws CannotRedoException {
		super.redo ( );
		index++ ;
	}

	public void setFinished ( ) {
		this.setFinished ( true );
	}

}
