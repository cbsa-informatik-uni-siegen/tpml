package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Application;
import expressions.BooleanConstant;
import expressions.EmptyList;
import expressions.Expression;
import expressions.IsEmpty;
import expressions.List;
import expressions.Tuple;
import expressions.UnaryCons;

/**
 * This class represents the big step rule <b>(IS-EMPTY-FALSE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class IsEmptyFalseRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>IsEmptyFalseRule</code> instance.
   */
  public IsEmptyFalseRule() {
    super(true, "IS-EMPTY-FALSE");
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
    // node's expression must be an Application of IsEmpty to a value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") IsEmpty isEmpty = (IsEmpty)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new ProofRuleException(node, this);
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      // let (IS-EMPTY-TRUE) handle the node
      BigStepProofRule rule = new IsEmptyTrueRule();
      context.setProofNodeRule(node, rule);
      rule.apply(context, node);
    }
    else if (e2 instanceof List) {
      // Lists aren't empty
      context.setProofNodeResult(node, BooleanConstant.TRUE);
    }
    else {
      // otherwise e2 must be an application of cons to a pair
      Application a1 = (Application)e2;
      Tuple tuple = (Tuple)a1.getE2();
      if (a1.getE1() instanceof UnaryCons && tuple.getArity() == 2) {
        context.setProofNodeResult(node, BooleanConstant.TRUE);
      }
      else {
        throw new ProofRuleException(node, this);
      }
    }
  }
}
