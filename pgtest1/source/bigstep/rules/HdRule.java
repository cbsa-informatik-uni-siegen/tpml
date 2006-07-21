package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Application;
import expressions.EmptyList;
import expressions.Exn;
import expressions.Expression;
import expressions.Hd;
import expressions.List;
import expressions.Tuple;
import expressions.UnaryCons;

/**
 * This class represents the big step rule <code>(HD)</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class HdRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>HdRule</code> instance.
   */
  public HdRule() {
    super(true, "HD");
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
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
    // can only be applied to Applications of Hd to a list value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") Hd hd = (Hd)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new ProofRuleException(node, this);
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      context.setProofNodeResult(node, Exn.EMPTY_LIST);
      context.setProofNodeRule(node, newNoopRule("HD-EMPTY"));
      return;
    }
    
    // check if e2 is a list
    if (e2 instanceof List) {
      context.setProofNodeResult(node, ((List)e2).head());
      return;
    }
    
    // otherwise e2 must be an application of cons to a pair
    Application a1 = (Application)e2;
    Tuple tuple = (Tuple)a1.getE2();
    if (!(a1.getE1() instanceof UnaryCons) || tuple.getArity() != 2) {
      throw new ProofRuleException(node, this);
    }
    
    // jep, we can perform (HD) then
    context.setProofNodeResult(node, tuple.getExpressions(0));
  }
}
