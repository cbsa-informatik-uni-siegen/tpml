package smallstep;

import java.lang.reflect.Field;
import java.util.Vector;

import common.ProofException;
import common.ProofModel;
import common.ProofNode;
import common.ProofRule;

import expressions.Expression;

/**
 * Small step version of the {@link ProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofModel extends ProofModel {
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof model, used to
   * prove that the <code>expression</code> evaluates
   * to a certain value.
   * 
   * @param expression the {@link Expression} for the
   *                   proof.
   */
  SmallStepProofModel(Expression expression) {
    super(new SmallStepProofNode(expression));
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see common.ProofModel#getRules()
   */
  @Override
  public ProofRule[] getRules() {
    // determine the available small step rules
    Vector<ProofRule> rules = new Vector<ProofRule>();
    Field fields[] = SmallStepProofRule.class.getDeclaredFields();
    for (int n = 0; n < fields.length; ++n) {
      try {
        Object value = fields[n].get(null);
        if (value instanceof SmallStepProofRule)
          rules.add((ProofRule)value);
      }
      catch (IllegalAccessException e) {
        // nothing to do here
      }
    }
    return rules.toArray(new ProofRule[] {});
  }
  
  
  
  //
  // Actions
  //

  /**
   * {@inheritDoc}
   * 
   * @see common.ProofModel#prove(common.ProofRule, common.ProofNode)
   */
  @Override
  public void prove(ProofRule rule, ProofNode node) throws ProofException {
    // verify that the rule is a small step rule
    if (!(rule instanceof SmallStepProofRule)) {
      throw new IllegalArgumentException("The rule must be a small step proof rule");
    }
    
    // verify that the node is valid for the model
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("The node must be valid within the model");
    }

    // apply the rule to the specified node
    apply((SmallStepProofRule)rule, (SmallStepProofNode)node);
  }
  
  
  
  //
  // Internals
  //
  
  private void apply(SmallStepProofRule rule, SmallStepProofNode node) throws ProofException {
    // apply the rule to the specified node
    SmallStepProofNode child = node.apply(rule);
    
    // check if we have a new tree node
    if (child != null) {
      // tell the view about the new tree node
      int[] indices = { node.getIndex(child) };
      nodesWereInserted(node, indices);
    }
    
    // notify the view about the changes to this node
    nodeChanged(node);
  }
}
