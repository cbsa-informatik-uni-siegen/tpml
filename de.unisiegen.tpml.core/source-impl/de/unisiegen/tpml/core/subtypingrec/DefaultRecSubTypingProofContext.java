package de.unisiegen.tpml.core.subtypingrec;

import java.util.LinkedList;

import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Default implementation of the <code>RecSubTypingProofContext</code> interface.
 *
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext
 */
public class DefaultRecSubTypingProofContext implements RecSubTypingProofContext {

	/**
	 * The rec subtyping proof model with which this proof context is associated.
	 * 
	 */
	private RecSubTypingProofModel model;

	/**
	 * The rec sub typing proof node of this proof context
	 */
	@SuppressWarnings("unused")
	private DefaultRecSubTypingProofNode node;

	/**
	 * List with the already seen types
	 */
	private SeenTypes < DefaultSubType > seenTypes = new SeenTypes < DefaultSubType > ( );

	/**
	 * The list of redoable actions on the proof model.
	 * 
	 * @see #addRedoAction(Runnable)
	 * @see #getRedoActions()
	 */
	private LinkedList < Runnable > redoActions = new LinkedList < Runnable > ( );

	/**
	 * The list of undoable actions on the proof model.
	 * 
	 * @see #addUndoAction(Runnable)
	 * @see #getUndoActions()
	 */
	private LinkedList < Runnable > undoActions = new LinkedList < Runnable > ( );

	/**
	 * Allocates a new proof step with the given types.
	 * @param pModel the subtyping proof model with which the context is associated.
	 * @param pNode the actual subtyping proof node
	 * 
	 */
	public DefaultRecSubTypingProofContext ( RecSubTypingProofModel pModel, RecSubTypingProofNode pNode ) {
		this.model = pModel;
		this.node = ( DefaultRecSubTypingProofNode ) pNode;
	}

	//
	// Rule application
	//

	/**
	 * Applies the specified proof <code>rule</code> to the given <code>node</code>.
	 * 
	 * @param rule the proof rule to apply to the <code>node</code>.
	 * @param pNode the proof node to which to apply the <code>rule</code>.
	 * 
	 * @throws NullPointerException if <code>rule</code> or <code>node</code> is <code>null</code>.
	 * @throws ProofRuleException if the application of the <code>rule</code> to the
	 *                            <code>node</code> failed for some reason.
	 */
	public void apply ( RecSubTypingProofRule rule, DefaultRecSubTypingProofNode pNode ) throws ProofRuleException {

		this.seenTypes.addAll ( pNode.getSeenTypes ( ) );

		this.model.contextSetProofNodeRule ( this, pNode, rule );
		//	 try to apply the rule to the node
		rule.apply ( this, pNode );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext#addProofNode(de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode, 
	 * de.unisiegen.tpml.core.types.MonoType, de.unisiegen.tpml.core.types.MonoType)
	 */
	public void addProofNode ( RecSubTypingProofNode pNode, MonoType type, MonoType type2 ) {
		this.model.contextAddProofNode ( this, pNode, type, type2, this.seenTypes );

	}

	/**
	 *{@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext#addSeenType(de.unisiegen.tpml.core.types.MonoType, de.unisiegen.tpml.core.types.MonoType)
	 */
	public void addSeenType ( MonoType type, MonoType type2 ) {
		DefaultSubType subtype = new DefaultSubType ( type, type2 );
		this.seenTypes.add ( subtype );
	}

	/**
	 * Invokes all previously registered undo actions and clears the list of undo
	 * actions.
	 * 
	 * @see #addUndoAction(Runnable)
	 * @see #getUndoActions()
	 */
	void revert ( ) {
		// undo all already performed changes
		for ( Runnable undoAction : this.undoActions ) {
			undoAction.run ( );
		}
		this.undoActions.clear ( );
	}

	//
	// Context action handling
	//
	/**
	 * Adds the specified <code>redoAction</code> to the internal list of
	 * redoable actions, and runs the <code>redoAction</code>. This method
	 * should be called before adding the matching undo action via
	 * {@link #addUndoAction(Runnable)}.
	 * 
	 * @param redoAction the redoable action.
	 * @see #addUndoAction(Runnable)
	 * @see #getRedoActions()
	 * @throws NullPointerException if <code>redoAction</code> is
	 *           <code>null</code>.
	 */
	public void addRedoAction ( Runnable redoAction ) {
		if ( redoAction == null ) {
			throw new NullPointerException ( "undoAction is null" ); //$NON-NLS-1$
		}
		// perform the action
		redoAction.run ( );
		// record the action
		this.redoActions.add ( redoAction );
	}

	/**
	 * Adds the specified <code>undoAction</code> to the internal list of
	 * undoable actions, and runs the <code>undoActions</code>. This method
	 * should be called after adding the matching redo action via
	 * {@link #addRedoAction(Runnable)}.
	 * 
	 * @param undoAction the undoable action.
	 * @see #addRedoAction(Runnable)
	 * @see #getUndoActions()
	 * @throws NullPointerException if <code>undoAction</code> is
	 *           <code>null</code>.
	 */
	void addUndoAction ( Runnable undoAction ) {
		if ( undoAction == null ) {
			throw new NullPointerException ( "undoAction is null" ); //$NON-NLS-1$
		}
		// record the action
		this.undoActions.add ( 0, undoAction );
	}

	/**
	 * Returns a <code>Runnable</code>, which performs all the previously
	 * recorded redoable actions, added via {@link #addRedoAction(Runnable)}.
	 * 
	 * @return a <code>Runnable</code> for all recorded redoable actions.
	 * @see #addRedoAction(Runnable)
	 * @see #getUndoActions()
	 */
	Runnable getRedoActions ( ) {
		return new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				for ( Runnable redoAction : DefaultRecSubTypingProofContext.this.redoActions ) {
					redoAction.run ( );
				}
			}
		};
	}

	/**
	 * Returns a <code>Runnable</code>, which performs all the previously
	 * recorded undoable actions, added via {@link #addUndoAction(Runnable)}.
	 * 
	 * @return a <code>Runnable</code> for all recorded undoable actions.
	 * @see #addUndoAction(Runnable)
	 * @see #getRedoActions()
	 */
	Runnable getUndoActions ( ) {
		return new Runnable ( ) {
			@SuppressWarnings ( "synthetic-access" )
			public void run ( ) {
				for ( Runnable undoAction : DefaultRecSubTypingProofContext.this.undoActions ) {
					undoAction.run ( );
				}
			}
		};
	}

}
