package de.unisiegen.tpml.core.unify;


import java.util.LinkedList;

import de.unisiegen.tpml.core.entities.DefaultTypeEquationList;
import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * Default implementation of the <code>UnifyProofContext</code> interface.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 */
public class DefaultUnifyProofContext implements UnifyProofContext
{

  /**
   * List of type equations we've collected so far.
   */
  private TypeEquationList equations = new DefaultTypeEquationList ();


  /**
   * The UnifyProofModel with which this context is associated
   */
  private UnifyProofModel model;


  /**
   * The list of redoable actions on the proof model.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getRedoActions()
   */
  private LinkedList < Runnable > redoActions = new LinkedList < Runnable > ();


  /**
   * The list of undoable actions on the proof model.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  private LinkedList < Runnable > undoActions = new LinkedList < Runnable > ();


  /**
   * Allocates a new <code>DefaultUnifyProofContext</code> with the specified
   * <code>model</code>. This constructor automatically increments the index
   * of the <code>model</code> using a redo/undo pair, via the
   * {@link UnifyProofModel#setIndex(int)} method.
   * 
   * @param pUnifyProofModel the type checker proof model with which the context
   *          is associated.
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   * @see TypeCheckerProofModel#setIndex(int)
   */
  public DefaultUnifyProofContext ( final UnifyProofModel pUnifyProofModel )
  {
    if ( pUnifyProofModel == null )
    {
      throw new NullPointerException ( "Model is null" ); //$NON-NLS-1$
    }
    this.model = pUnifyProofModel;
    // increment the model index
    final int index = pUnifyProofModel.getIndex ();
    addRedoAction ( new Runnable ()
    {

      public void run ()
      {
        pUnifyProofModel.setIndex ( index + 1 );
      }
    } );
    addUndoAction ( new Runnable ()
    {

      public void run ()
      {
        pUnifyProofModel.setIndex ( index );
      }
    } );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofContext#addEquation(de.unisiegen.tpml.core.entities.TypeEquation)
   */
  public void addEquation ( TypeEquation eqn )
  {
    this.equations = this.equations.extend ( eqn );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofContext#addProofNode(
   *      de.unisiegen.tpml.core.unify.UnifyProofNode,
   *      de.unisiegen.tpml.core.typeinference.TypeSubstitutionList,
   *      de.unisiegen.tpml.core.entities.TypeEquationList)
   */
  public void addProofNode ( UnifyProofNode node, TypeSubstitutionList substs,
      TypeEquationList eqns )
  {
    this.model.contextAddProofNode ( this, ( AbstractUnifyProofNode ) node,
        substs, eqns );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofContext#addProofNode(
   *      de.unisiegen.tpml.core.unify.UnifyProofNode,
   *      de.unisiegen.tpml.core.typeinference.TypeSubstitutionList)
   */
  public void addProofNode ( UnifyProofNode node, TypeSubstitutionList substs )
  {
    this.model.contextAddProofNode ( this, ( AbstractUnifyProofNode ) node,
        substs );
  }


  /**
   * Invokes all previously registered undo actions and clears the list of undo
   * actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  void revert ()
  {
    // undo all already performed changes
    for ( Runnable undoAction : this.undoActions )
    {
      undoAction.run ();
    }
    this.undoActions.clear ();
  }


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
      throw new NullPointerException ( "redoAction is null" ); //$NON-NLS-1$
    }
    // perform the action
    redoAction.run ();
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
  void addUndoAction ( Runnable undoAction )
  {
    if ( undoAction == null )
    {
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
  Runnable getRedoActions ()
  {
    return new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        for ( Runnable redoAction : DefaultUnifyProofContext.this.redoActions )
        {
          redoAction.run ();
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
  Runnable getUndoActions ()
  {
    return new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        for ( Runnable undoAction : DefaultUnifyProofContext.this.undoActions )
        {
          undoAction.run ();
        }
      }
    };
  }
}
