/**
 * TODO
 */
package de.unisiegen.tpml.core.subtyping;

import java.util.LinkedList;

import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class DefaultSubTypingProofContext implements SubTypingProofContext {
	
	private SubTypingProofModel model;
	
	private DefaultSubTypingProofNode node;
	
  /**
   * The list of redoable actions on the proof model.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getRedoActions()
   */
  private LinkedList < Runnable > redoActions = new LinkedList < Runnable > ( ) ;


  /**
   * The list of undoable actions on the proof model.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  private LinkedList < Runnable > undoActions = new LinkedList < Runnable > ( ) ;

	
	
	public DefaultSubTypingProofContext(SubTypingProofModel pModel, SubTypingProofNode pNode){
		model = pModel;
		node = (DefaultSubTypingProofNode) pNode;
	}


	public void apply ( SubTypingProofRule rule, DefaultSubTypingProofNode pNode ) throws ProofRuleException, SubTypingException {
		
		model.contextSetProofNodeRule ( this, pNode, rule );
//	 try to apply the rule to the node
		rule.apply ( this, pNode );
	}


	public void addProofNode ( SubTypingProofNode pNode, MonoType type, MonoType type2 ) {
		model.contextAddProofNode ( this, pNode, type, type2 );
		
	}
	
  /**
   * Invokes all previously registered undo actions and clears the list of undo
   * actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  void revert ( )
  {
    // undo all already performed changes
    for ( Runnable undoAction : this.undoActions )
    {
      undoAction.run ( ) ;
    }
    this.undoActions.clear ( ) ;
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
  public void addRedoAction ( Runnable redoAction )
  {
    if ( redoAction == null )
    {
      throw new NullPointerException ( "undoAction is null" ) ;
    }
    // perform the action
    redoAction.run ( ) ;
    // record the action
    this.redoActions.add ( redoAction ) ;
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
  void addUndoAction ( Runnable undoAction )
  {
    if ( undoAction == null )
    {
      throw new NullPointerException ( "undoAction is null" ) ;
    }
    // record the action
    this.undoActions.add ( 0 , undoAction ) ;
  }


  /**
   * Returns a <code>Runnable</code>, which performs all the previously
   * recorded redoable actions, added via {@link #addRedoAction(Runnable)}.
   * 
   * @return a <code>Runnable</code> for all recorded redoable actions.
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  Runnable getRedoActions ( )
  {
    return new Runnable ( )
    {
      public void run ( )
      {
        for ( Runnable redoAction : DefaultSubTypingProofContext.this.redoActions )
        {
          redoAction.run ( ) ;
        }
      }
    } ;
  }


  /**
   * Returns a <code>Runnable</code>, which performs all the previously
   * recorded undoable actions, added via {@link #addUndoAction(Runnable)}.
   * 
   * @return a <code>Runnable</code> for all recorded undoable actions.
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  Runnable getUndoActions ( )
  {
    return new Runnable ( )
    {
      public void run ( )
      {
        for ( Runnable undoAction : DefaultSubTypingProofContext.this.undoActions )
        {
          undoAction.run ( ) ;
        }
      }
    } ;
  }
	
}