package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Application;
import expressions.EmptyList;
import expressions.Exn;
import expressions.Expression;
import expressions.List;
import expressions.Tl;
import expressions.Tuple;
import expressions.UnaryCons;

/**
 * This class represents the big step rule <b>(TL)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class TlRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>TlRule</code> instance.
   */
  public TlRule() {
    super(true, "TL");
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
    // can only be applied to Applications of Tl to a list value
    Application application = (Application)node.getExpression();
    @SuppressWarnings("unused") Tl tl = (Tl)application.getE1();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new ProofRuleException(node, this);
    }
    
    // check if e2 is the empty list
    if (e2 == EmptyList.EMPTY_LIST) {
      context.setProofNodeResult(node, Exn.EMPTY_LIST);
      context.setProofNodeRule(node, newNoopRule("TL-EMPTY"));
      return;
    }
    
    // check if e2 is a list
    if (e2 instanceof List) {
      context.setProofNodeResult(node, ((List)e2).tail());
      return;
    }
    
    // otherwise e2 must be an application of cons to a pair
    Application a1 = (Application)e2;
    Tuple tuple = (Tuple)a1.getE2();
    if (!(a1.getE1() instanceof UnaryCons) || tuple.getArity() != 2) {
      throw new ProofRuleException(node, this);
    }
    
    // jep, we can perform (TL) then
    context.setProofNodeResult(node, tuple.getExpressions(1));
  }
}
