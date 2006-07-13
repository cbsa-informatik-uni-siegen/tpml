package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Application;
import expressions.Expression;
import expressions.Lambda;
import expressions.MultiLambda;
import expressions.Projection;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BetaValueRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>BetaValueRule</code> instance,
   * which represents the <b>(BETA-V)</b> big step proof
   * rule.
   */
  public BetaValueRule() {
    super(true, "BETA-V");
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
      // the expression must an application to a value...
      Application application = (Application)node.getExpression();
      Expression e2 = application.getE2();
      if (!e2.isValue()) {
        throw new ProofRuleException(node, this);
      }
      
      // ...with a lambda or multi lambda expression
      Expression e1 = application.getE1();
      if (e1 instanceof MultiLambda) {
        // multi lambda is special
        MultiLambda multiLambda = (MultiLambda)e1;
        Expression e = multiLambda.getE();
        
        // perform the required substitutions
        String[] identifiers = multiLambda.getIdentifiers();
        for (int n = 0; n < identifiers.length; ++n) {
          // substitute: (#l_n e2) for id
          e = e.substitute(identifiers[n], new Application(new Projection(identifiers.length, n + 1), e2));
        }
        
        // add the proof node for e
        context.addProofNode(node, e);
      }
      else {
        // must be lambda then
        Lambda lambda = (Lambda)e1;
        context.addProofNode(node, lambda.getE().substitute(lambda.getId(), e2));
      }
    }
    catch (ClassCastException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // forward the result of the first child node to this node (may be null)
    context.setProofNodeResult(node, node.getChildAt(0).getResult());
  }
}
