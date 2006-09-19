package de.unisiegen.tpml.core.bigstep;

import java.util.LinkedList;

import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;

/**
 * Default implementation of the <code>BigStepProofContext</code> interface. The big step proof
 * context is used in the big step proof rules (and thereby the rule sets) to communicate with
 * the proof model. For the proof model, the context is used to register redo and undo actions
 * during a single proof step and collect the actions later.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRuleSet
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext
 */
final class DefaultBigStepProofContext implements BigStepProofContext {
  //
  // Attributes
  //
  
  /**
   * The big step proof model to which this context is connected.
   */
  private BigStepProofModel model;
  
  /**
   * The list of registered redo actions.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getRedoActions()
   */
  private LinkedList<Runnable> redoActions = new LinkedList<Runnable>();
  
  /**
   * The list of registered undo actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  private LinkedList<Runnable> undoActions = new LinkedList<Runnable>();
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultBigStepProofContext</code> with the specified <code>model</code>
   * and <code>node</code>.
   * 
   * @param model {@link BigStepProofModel} on which this context should operate.
   * 
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   */
  DefaultBigStepProofContext(BigStepProofModel model) {
    if (model == null) {
      throw new NullPointerException("model is null");
    }
    this.model = model;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#addProofNode(de.unisiegen.tpml.core.bigstep.BigStepProofNode, de.unisiegen.tpml.core.expressions.Expression)
   */
  public void addProofNode(BigStepProofNode node, Expression expression) {
    // default to inherit the store of the parent node
    Store store = node.getStore();
    
    // use the store of the last child node (if proven)
    if (node.getChildCount() > 0) {
      BigStepProofResult result = node.getLastChild().getResult();
      if (result != null) {
        store = result.getStore();
      }
    }
    
    // and add the new node
    addProofNode(node, expression, store);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#addProofNode(de.unisiegen.tpml.core.bigstep.BigStepProofNode, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.interpreters.Store)
   */
  public void addProofNode(BigStepProofNode node, Expression expression, Store store) {
    this.model.contextAddProofNode(this, (DefaultBigStepProofNode)node, new DefaultBigStepProofNode(expression, store));
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#isMemoryEnabled()
   */
  public boolean isMemoryEnabled() {
    return this.model.isMemoryEnabled();
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#setProofNodeResult(de.unisiegen.tpml.core.bigstep.BigStepProofNode, de.unisiegen.tpml.core.bigstep.BigStepProofResult)
   */
  public void setProofNodeResult(BigStepProofNode node, BigStepProofResult result) {
    this.model.contextSetProofNodeResult(this, (DefaultBigStepProofNode)node, result);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#setProofNodeResult(de.unisiegen.tpml.core.bigstep.BigStepProofNode, de.unisiegen.tpml.core.expressions.Expression)
   */
  public void setProofNodeResult(BigStepProofNode node, Expression value) {
    // default to inhert the store of this node
    Store store = node.getStore();
    
    // use the store of the last child node (if proven)
    if (node.getChildCount() > 0) {
      BigStepProofResult result = node.getLastChild().getResult();
      if (result != null) {
        store = result.getStore();
      }
    }
    
    // add the result
    setProofNodeResult(node, value, store);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#setProofNodeResult(de.unisiegen.tpml.core.bigstep.BigStepProofNode, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.interpreters.Store)
   */
  public void setProofNodeResult(BigStepProofNode node, Expression value, Store store) {
    setProofNodeResult(node, new BigStepProofResult(store, value));
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofContext#setProofNodeRule(de.unisiegen.tpml.core.bigstep.BigStepProofNode, de.unisiegen.tpml.core.bigstep.BigStepProofRule)
   */
  public void setProofNodeRule(BigStepProofNode node, BigStepProofRule rule) {
    this.model.contextSetProofNodeRule(this, (DefaultBigStepProofNode)node, rule);
  }
  
  
  
  //
  // Rule application
  //

  /**
   * Used to implement the {@link BigStepProofModel#apply(BigStepProofRule, DefaultBigStepProofNode)}
   * method in the {@link BigStepProofModel} class.
   * 
   * Applies the <code>rule</code> to the <code>node</code>, recording <code>rule</code> for the
   * <code>node</code> and recording all further actions that were performed, including the actions
   * required to undo the application step. 
   * 
   * @param rule the big step proof rule.
   * @param node the big step proof node.
   * 
   * @throws ProofRuleException if the application of <code>rule</code> to <code>node</code> fails.
   */
  void apply(BigStepProofRule rule, BigStepProofNode node) throws ProofRuleException {
    // record the proof step
    setProofNodeRule(node, rule);
    
    // try to apply the rule to the node
    rule.apply(this, node);
    
    // update all (unproven) super nodes
    for (;;) {
      // determine the parent node
      BigStepProofNode parentNode = node.getParent();
      if (parentNode == null) {
        break;
      }
      
      // update the parent node
      updateNode(parentNode, node);
      
      // continue with the next one
      node = parentNode;
    }
  }
  
  /**
   * Used to implement the {@link BigStepProofModel#apply(BigStepProofRule, DefaultBigStepProofNode)}
   * method in the {@link BigStepProofModel} class.
   * 
   * Reverts all previously performed actions, using the recorded undo actions. The recorded
   * undo actions will be cleared afterwards.
   */
  void revert() {
    // undo all already performed changes
    for (Runnable undoAction : this.undoActions) {
      undoAction.run();
    }
    this.undoActions.clear();
  }
  
  /**
   * Updates the <code>node</code> after a change to the <code>childNode</code>.
   * This is needed for non-axiom rules, like <b>(APP)</b>, that need to react
   * to proof steps on child nodes. If the <code>node</code> is already proven,
   * no action will be performed.
   * 
   * If the <code>childNode</code>'s value is an exception, the exception will
   * be forwarded to the <code>node</code>.
   * 
   * Otherwise the {@link BigStepProofRule#update(BigStepProofContext, BigStepProofNode)}
   * method of the rule that was applied to <code>node</code> will be executed to
   * update the state of the <code>node</code>.
   * 
   * @param node the node to update.
   * @param childNode the child node that was changed.
   */
  void updateNode(BigStepProofNode node, BigStepProofNode childNode) {
    // skip the node if its already proven
    if (node.isProven()) {
      return;
    }
    
    // determine the rule that was applied to the node
    AbstractBigStepProofRule rule = (AbstractBigStepProofRule)node.getRule();
    
    // check if child node resulted in an exception
    if (childNode.isProven() && childNode.getResult().getValue().isException()) {
      // generate an exception rule for the node
      setProofNodeRule(node, rule.toExnRule(node.getIndex(childNode)));
      
      // forward the exception value
      setProofNodeResult(node, childNode.getResult());
    }
    else {
      // use the rule's update() mechanism
      rule.update(this, node);
    }
  }
  
  
  
  //
  // Context action handling
  //
  
  /**
   * Appends the <code>redoAction</code> to the list of redoable actions and runs the <code>redoAction</code>.
   * 
   * @param redoAction the redo action to add.
   * 
   * @throws NullPointerException if <code>redoAction</code> is <code>null</code>.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  void addRedoAction(Runnable redoAction) {
    if (redoAction == null) {
      throw new NullPointerException("redoAction is null");
    }
    
    // perform the action
    redoAction.run();
    
    // record the action
    this.redoActions.add(redoAction);
  }
  
  /**
   * Prepends the <code>undoAction</code> to the list of undoable actions.
   * 
   * @param undoAction the undo action to add.
   * 
   * @throws NullPointerException if <code>undoAction</code> is <code>null</code>.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  void addUndoAction(Runnable undoAction) {
    if (undoAction == null) {
      throw new NullPointerException("undoAction is null");
    }
    
    // record the action
    this.undoActions.add(0, undoAction);
  }
  
  /**
   * Returns a single <code>Runnable</code> that runs all previously registered redo actions.
   * 
   * @return a single <code>Runnable</code> to run all redo actions.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  Runnable getRedoActions() {
    return new Runnable() {
      public void run() {
        for (Runnable redoAction : DefaultBigStepProofContext.this.redoActions) {
          redoAction.run();
        }
      }
    };
  }
  
  /**
   * Returns a single <code>Runnable</code> that runs all previously registered undo actions.
   * 
   * @return a single <code>Runnable</code> to run all undo actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  Runnable getUndoActions() {
    return new Runnable() {
      public void run() {
        for (Runnable undoAction : DefaultBigStepProofContext.this.undoActions) {
          undoAction.run();
        }
      }
    };
  }
}
