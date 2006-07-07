package bigstep.rules;

import common.ProofRuleException;
import expressions.Application;
import expressions.Expression;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class UopRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>UopRule</code> instance.
   */
  public UopRule() {
    super(true, "UOP");
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
      // the node's expression must be an application of an unary operator to a value
      Application application = (Application)node.getExpression();
      UnaryOperator e1 = (UnaryOperator)application.getE1();
      Expression e2 = application.getE2();

      // try to apply the operator
      context.setProofNodeValue(node, e1.applyTo(e2));
    }
    catch (ClassCastException e) {
      super.apply(context, node);
    }
    catch (UnaryOperatorException e) {
      super.apply(context, node);
    }
  }
}
