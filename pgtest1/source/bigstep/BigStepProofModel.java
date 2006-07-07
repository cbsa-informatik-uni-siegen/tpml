package bigstep;

import bigstep.rules.AppRule;
import bigstep.rules.BetaValueRule;
import bigstep.rules.ValRule;
import common.AbstractProofModel;
import common.ProofNode;
import common.ProofRule;
import common.ProofRuleException;
import common.ProofStep;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BigStepProofModel extends AbstractProofModel {
  //
  // Constructor (package)
  //

  /**
   * Allocates a new <code>BigStepProofModel</code> to
   * prove the specified <code>expression</code>.
   * 
   * @param expression the {@link Expression} to prove.
   */
  BigStepProofModel(Expression expression) {
    super(new DefaultBigStepProofNode(expression));
    
    // tell the view that stores should be displayed
    // if the supplied expression contains any refe-
    // rences or memory locations
    setMemoryEnabled(expression.containsReferences());
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofModel#getRules()
   */
  @Override
  public ProofRule[] getRules() {
    return new BigStepProofRule[] {
      new AppRule(),
      new BetaValueRule(),
      new ValRule()
    };
  }

  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofModel#guess(common.ProofNode)
   */
  @Override
  public void guess(ProofNode node) {
    // TODO Auto-generated method stub
  }

  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofModel#prove(common.ProofRule, common.ProofNode)
   */
  @Override
  public void prove(ProofRule rule, ProofNode node) throws ProofRuleException {
    // verify that the rule is a big step rule
    if (!(rule instanceof BigStepProofRule)) {
      throw new IllegalArgumentException("The rule must be a big step proof rule");
    }
    
    // verify that the node is valid for the model
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("The node is invalid for the model");
    }
    
    // try to apply the rule to the specified node
    apply((BigStepProofRule)rule, (DefaultBigStepProofNode)node);
  }
  
  
  
  //
  // Rule application
  //
  
  /**
   * TODO Add documentation here.
   */
  private void apply(final BigStepProofRule rule, final DefaultBigStepProofNode node) throws ProofRuleException {
    // allocate a new big step proof context
    DefaultBigStepProofContext context = new DefaultBigStepProofContext(this);
    try {
      // try to apply the rule to the node
      context.apply(rule, node);
      
      // determine the redo and undo actions from the context
      final Runnable redoActions = context.getRedoActions();
      final Runnable undoActions = context.getUndoActions();
      
      // record the undo edit action for this proof step
      addUndoableTreeEdit(new UndoableTreeEdit() {
        public void redo() { redoActions.run(); }
        public void undo() { undoActions.run(); }
      });
    }
    catch (ProofRuleException e) {
      // revert the actions performed so far
      context.revert();
      
      // re-throw the exception
      throw e;
    }
  }
  
  
  
  //
  // Proof context support
  //

  /**
   * TODO Add documentation here.
   */
  void contextAddProofNode(DefaultBigStepProofContext context, final DefaultBigStepProofNode node, final Expression expression) {
    final DefaultBigStepProofNode child = new DefaultBigStepProofNode(expression);
    
    context.addRedoAction(new Runnable() {
      public void run() {
        node.add(child);
        nodesWereInserted(node, new int[] { node.getIndex(child) });
      }
    });
    
    context.addUndoAction(new Runnable() {
      public void run() {
        int index = node.getIndex(child);
        node.remove(index);
        nodesWereRemoved(node, new int[] { index }, new Object[] { child });
      }
    });
  }

  /**
   * TODO Add documentation here.
   */
  void contextSetProofNodeRule(DefaultBigStepProofContext context, final DefaultBigStepProofNode node, final BigStepProofRule rule) {
    final ProofStep[] oldSteps = node.getSteps();
    
    context.addRedoAction(new Runnable() {
      public void run() {
        node.setSteps(new ProofStep[] { new ProofStep(node.getExpression(), rule) });
        nodeChanged(node);
      }
    });
    
    context.addUndoAction(new Runnable() {
      public void run() {
        node.setSteps(oldSteps);
        nodeChanged(node);
      }
    });
  }
  
  /**
   * TODO Add documentation here.
   */
  void contextSetProofNodeValue(DefaultBigStepProofContext context, final DefaultBigStepProofNode node, final Expression value) {
    final Expression oldValue = node.getValue();
    
    context.addRedoAction(new Runnable() {
      public void run() {
        node.setValue(value);
        nodeChanged(node);
      }
    });
    
    context.addUndoAction(new Runnable() {
      public void run() {
        node.setValue(oldValue);
        nodeChanged(node);
      }
    });
  }
}
