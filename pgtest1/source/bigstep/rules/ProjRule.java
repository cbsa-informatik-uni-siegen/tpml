package bigstep.rules;

import common.ProofRuleException;
import expressions.Application;
import expressions.Projection;
import expressions.Tuple;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * This class represents the big step rule <b>(PROJ)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProjRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ProjRule</code> instance.
   */
  public ProjRule() {
    super(true, "PROJ");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#apply(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException {
    try {
      // can only be applied to Applications of Projections to Tuples...
      Application application = (Application)node.getExpression();
      Projection projection = (Projection)application.getE1();
      Tuple tuple = (Tuple)application.getE2();
      
      // ...and the tuple must be a value and the arity must match
      if (!tuple.isValue() || projection.getArity() != tuple.getArity()) {
        throw new ProofRuleException(node, this);
      }
      
      // well, this is easy then
      context.setProofNodeResult(node, tuple.getExpressions(projection.getIndex() - 1));
    }
    catch (ClassCastException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
}
