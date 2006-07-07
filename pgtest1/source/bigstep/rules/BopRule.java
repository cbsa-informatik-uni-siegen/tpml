package bigstep.rules;

import common.ProofRuleException;
import expressions.Application;
import expressions.ArithmeticOperator;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.Expression;
import expressions.InfixOperation;
import expressions.RelationalOperator;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BopRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * TODO Add documentation here.
   */
  public BopRule() {
    super(true, "BOP");
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
      // the node's expression may either be
      // an application or an infix operation
      Expression e = node.getExpression();
      if (e instanceof Application) {
        // application: op e1 e2
        Application a1 = (Application)e;
        Application a2 = (Application)a1.getE1();
        BinaryOperator bop = (BinaryOperator)a2.getE1();
        Expression e1 = a2.getE2();
        Expression e2 = a1.getE2();
        
        // (BOP) can only handle arithmetic and relational operators
        if (!(bop instanceof ArithmeticOperator || bop instanceof RelationalOperator)) {
          throw new ProofRuleException(node, this);
        }
        
        // try to apply the operator
        context.setProofNodeValue(node, bop.applyTo(e1, e2));
      }
      else {
        // infix operation is easy
        InfixOperation infixOperation = (InfixOperation)e;
        BinaryOperator bop = infixOperation.getOp();
        Expression e1 = infixOperation.getE1();
        Expression e2 = infixOperation.getE2();
        
        // (BOP) can only handle arithmetic and relational operators
        if (!(bop instanceof ArithmeticOperator || bop instanceof RelationalOperator)) {
          throw new ProofRuleException(node, this);
        }
        
        // try to perform the infix operation
        context.setProofNodeValue(node, bop.applyTo(e1, e2));
      }
    }
    catch (BinaryOperatorException e) {
      super.apply(context, node);
    }
    catch (ClassCastException e) {
      super.apply(context, node);
    }
  }
}
