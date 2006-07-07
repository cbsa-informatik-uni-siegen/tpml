package bigstep;

import java.util.LinkedList;

import common.ProofNode;
import common.ProofRuleException;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class DefaultBigStepProofContext implements BigStepProofContext {
  //
  // Attributes
  //

  /**
   * TODO Add documentation here.
   */
  private BigStepProofModel model;
  
  /**
   * TODO Add documentation here.
   */
  private LinkedList<Runnable> redoActions = new LinkedList<Runnable>();
  
  /**
   * TODO Add documentation here.
   */
  private LinkedList<Runnable> undoActions = new LinkedList<Runnable>();
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultBigStepProofContext</code> with the specified
   * <code>model</code> and <code>node</code>.
   * 
   * @param model the {@link BigStepProofModel} on which this context
   *              should operate.
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
  // Rule application
  //
  
  /**
   * TODO Add documentation here.
   */
  void apply(final BigStepProofRule rule, final DefaultBigStepProofNode node) throws ProofRuleException {
    // record the proof step
    setProofNodeRule(node, rule);
    
    // try to apply the rule to the node
    rule.apply(this, node);
    
    // update all super nodes
    for (ProofNode parent = node.getParent(); parent != null; parent = parent.getParent()) {
      rule.update(this, (BigStepProofNode)parent);
    }
  }
  
  /**
   * TODO Add documentation here.
   */
  void revert() {
    // undo all already performed changes
    for (Runnable undoAction : this.undoActions) {
      undoAction.run();
    }
  }
  
  
  
  //
  // Context action handling
  //
  
  /**
   * TODO Add documentation here.
   * 
   * @param redoAction
   */
  void addRedoAction(Runnable redoAction) {
    // perform the action
    redoAction.run();
    
    // record the action
    this.redoActions.add(redoAction);
  }
  
  /**
   * TODO Add documentation here.
   * 
   * @param undoAction
   */
  void addUndoAction(Runnable undoAction) {
    // record the action
    this.undoActions.add(0, undoAction);
  }
  
  /**
   * TODO Add documentation here.
   * 
   * @return
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
   * TODO Add documentation here.
   * 
   * @return
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
  
  
  
  //
  // Proof tree updates
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see BigStepProofContext#addProofNode(BigStepProofNode, Expression)
   */
  public void addProofNode(BigStepProofNode node, Expression expression) {
    this.model.contextAddProofNode(this, (DefaultBigStepProofNode)node, expression);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofContext#setProofNodeRule(bigstep.BigStepProofNode, bigstep.BigStepProofRule)
   */
  public void setProofNodeRule(BigStepProofNode node, BigStepProofRule rule) {
    this.model.contextSetProofNodeRule(this, (DefaultBigStepProofNode)node, rule);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see BigStepProofContext#setProofNodeValue(BigStepProofNode, Expression)
   */
  public void setProofNodeValue(BigStepProofNode node, Expression value) {
    this.model.contextSetProofNodeValue(this, (DefaultBigStepProofNode)node, value);
  }
}
