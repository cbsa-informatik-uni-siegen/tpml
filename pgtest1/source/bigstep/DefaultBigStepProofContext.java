package bigstep;

import java.util.LinkedList;

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
  void apply(BigStepProofRule rule, BigStepProofNode node) throws ProofRuleException {
    // record the proof step
    setProofNodeRule(node, rule);
    
    // try to apply the rule to the node
    rule.apply(this, node);
    
    // update all (unproven) super nodes
    for (;;) {
      // determine the parent node
      BigStepProofNode parentNode = (BigStepProofNode)node.getParent();
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
   * TODO Add documentation here.
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
    BigStepProofRule rule = node.getRule();
    
    // check if child node resulted in an exception
    if (childNode.isProven() && childNode.getValue().isException()) {
      // generate an exception rule for the node
      setProofNodeRule(node, rule.toExnRule(node.getIndex(childNode)));
      
      // forward the exception value
      setProofNodeValue(node, childNode.getValue());
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
