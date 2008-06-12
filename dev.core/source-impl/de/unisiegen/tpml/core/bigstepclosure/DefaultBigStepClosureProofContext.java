package de.unisiegen.tpml.core.bigstepclosure;

import java.util.LinkedList;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.DefaultClosureEnvironment;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;

final class DefaultBigStepClosureProofContext implements BigStepClosureProofContext
{
  public DefaultBigStepClosureProofContext(BigStepClosureProofModel model)
  {
    this.model = model;
  }
  
  public BigStepClosureProofRule newNoopRule(String name)
  {
    return null;
  }
  
  public void setProofNodeResult(BigStepClosureProofNode node, Expression expression)
  {
    setProofNodeResult(node, new BigStepClosureProofResult(new DefaultStore(), new Closure(expression, new DefaultClosureEnvironment())));
  }
  
  public void setProofNodeResult(BigStepClosureProofNode node, BigStepClosureProofResult result)
  {
    this.model.contextSetProofNodeResult ( this,
        ( DefaultBigStepClosureProofNode ) node, result );
  }
  
  public void setProofNodeResult(BigStepClosureProofNode node, Expression expression, Store store)
  {
    setProofNodeResult(node, new BigStepClosureProofResult(store, new Closure(expression, new DefaultClosureEnvironment())));
  }
 
  public void setProofNodeResult(BigStepClosureProofNode node, Closure closure)
  {
    setProofNodeResult(node, new BigStepClosureProofResult(new DefaultStore(), closure));
  }
  
  public void setProofNodeRule(BigStepClosureProofNode node, BigStepClosureProofRule rule)
  {
    this.model.contextSetProofNodeRule ( this,
        ( DefaultBigStepClosureProofNode ) node, rule );
  }
 
  public boolean isMemoryEnabled()
  {
    return false;
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression)
  {
    addProofNode(
        node,
        expression,
        getDefaultStore(node));
  }
  
  public void addProofNode(BigStepClosureProofNode node, Closure closure)
  {
    addProofNode(
        node,
        closure,
        getDefaultStore(node));
  }
  
  private Store getDefaultStore(BigStepClosureProofNode node)
  {
    System.err.println (node.getChildCount ());
    System.err.println (node.getChildCount() > 0
    ? node.getFirstChild ().getStore()
    : node.getStore());
    return node.getChildCount() > 0
    ? node.getFirstChild ().getStore()
    : node.getStore();
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression, Store store)
  {
    addProofNode(node, new Closure(expression, new DefaultClosureEnvironment()), store);
  }
  
  public void addProofNode(
      BigStepClosureProofNode node,
      Closure closure,
      Store store)
  {
    this.model.contextAddProofNode ( this, (DefaultBigStepClosureProofNode)node,
        new DefaultBigStepClosureProofNode ( closure, store) );
  }
  
  void apply ( BigStepClosureProofRule rule, BigStepClosureProofNode node )
    throws ProofRuleException
  {
    // record the proof step
    setProofNodeRule ( node, rule );
    // try to apply the rule to the node
    rule.apply ( this, node );
    // update all (unproven) super nodes
    BigStepClosureProofNode newNode = node;
    for ( ; ; )
    {
      // determine the parent node
      newNode = newNode.getParent ();
      if ( newNode == null )
      {
        break;
      }
      // update the parent node
      updateNode ( newNode );
    }
  }
  
  void updateNode ( BigStepClosureProofNode node )
  {
    // skip the node if its already proven
    /*if ( node.isFinished() )
    {
      return;
    }*/
    // check if all child nodes are finished...
    boolean childrenFinished = node.isFinished (); //true;
    /*for ( int n = 0 ; childrenFinished && n < node.getChildCount () ; ++n )
    {
      childrenFinished = ( childrenFinished && node.getChildAt ( n )
          .isFinished () );
    }*/
    // ...and if so, check if any resulted in an exception
    BigStepClosureProofNode nodeWithExn = null;
    if ( childrenFinished )
    {
      for ( int n = 0 ; n < node.getChildCount () ; ++n )
      {
        nodeWithExn = node.getChildAt ( n );
        if ( nodeWithExn.getResult ().getValue ().isException () )
          break;
        nodeWithExn = null;
      }
    }
    // determine the rule that was applied to the node
    AbstractBigStepClosureProofRule rule = ( AbstractBigStepClosureProofRule ) node
        .getRule ();
    // check if child node resulted in an exception
    if ( nodeWithExn != null )
    {
      // generate an exception rule for the node
      setProofNodeRule ( node, rule.toExnRule ( node.getIndex ( nodeWithExn ) ) );
      // forward the exception value
      setProofNodeResult ( node, nodeWithExn.getResult () );
    }
    else
    {
      // use the rule's update() mechanism
      rule.update ( this, node );
    }
  }
  
  public void revert()
  {
    
  }
  
  void addRedoAction ( Runnable redoAction )
  {
    // perform the action
    redoAction.run ();
    // record the action
    this.redoActions.add ( redoAction );
  }


  /**
   * Prepends the <code>undoAction</code> to the list of undoable actions.
   * 
   * @param undoAction the undo action to add.
   * @throws NullPointerException if <code>undoAction</code> is
   *           <code>null</code>.
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
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
   * Returns a single <code>Runnable</code> that runs all previously
   * registered redo actions.
   * 
   * @return a single <code>Runnable</code> to run all redo actions.
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
        for ( Runnable redoAction : DefaultBigStepClosureProofContext.this.redoActions )
        {
          redoAction.run ();
        }
      }
    };
  }


  /**
   * Returns a single <code>Runnable</code> that runs all previously
   * registered undo actions.
   * 
   * @return a single <code>Runnable</code> to run all undo actions.
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
        for ( Runnable undoAction : DefaultBigStepClosureProofContext.this.undoActions )
        {
          undoAction.run ();
        }
      }
    };
  }
  
  private BigStepClosureProofModel model;
  
  private LinkedList < Runnable > redoActions = new LinkedList < Runnable > ();

  private LinkedList < Runnable > undoActions = new LinkedList < Runnable > ();
}
