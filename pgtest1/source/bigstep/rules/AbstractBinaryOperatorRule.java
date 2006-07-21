package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;
import common.interpreters.MutableStore;

import expressions.Application;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.Expression;
import expressions.InfixOperation;
import expressions.UnaryOperatorException;

/**
 * Abstract base class for big step rules that can be applied to
 * binary operators, both in {@link expressions.Application}s and
 * {@link expressions.InfixOperation}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.rules.AbstractUnaryOperatorRule
 */
abstract class AbstractBinaryOperatorRule extends BigStepProofRule {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractBinaryOperatorRule</code> with
   * the specified <code>name</code>.
   * 
   * @param name the name of the rule.
   */
  protected AbstractBinaryOperatorRule(String name) {
    super(true, name);
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
  public final void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
    try {
      // depends on whether we have an Application or InfixOperation
      BinaryOperator op;
      Expression e1;
      Expression e2;
      
      // check if Application or InfixOperation
      Expression e = node.getExpression();
      if (e instanceof Application) {
        // Application: (op e1) e2
        Application a1 = (Application)e;
        Application a2 = (Application)a1.getE1();
        op = (BinaryOperator)a2.getE1();
        e1 = a2.getE2();
        e2 = a1.getE2();
      }
      else {
        // otherwise must be an InfixOperation
        InfixOperation infixOperation = (InfixOperation)e;
        op = infixOperation.getOp();
        e1 = infixOperation.getE1();
        e2 = infixOperation.getE2();
      }
      
      // verify that op, e1 and e2 are values
      if (!op.isValue() || !e1.isValue() || !e2.isValue()) {
        throw new ProofRuleException(node, this);
      }
      
      // allocate a new store and perform the application
      MutableStore store = new MutableStore(node.getStore());
      Expression value = applyTo(store, op, e1, e2);
      context.setProofNodeResult(node, value, store);
    }
    catch (BinaryOperatorException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
  
  /**
   * This method must be implemented by derived classes to handle the actual application of
   * the operator <code>op</code> to the expressions <code>e1</code> and <code>e2</code>,
   * which are generated to be values.
   * 
   * Memory operators may modify the <code>store</code> as needed.
   *         
   * @param store the {@link MutableStore} required for memory operators like <code>:=</code>.
   * @param op the binary operator to apply to <code>e1</code> and <code>e2</code>.
   * @param e1 the first operand, which is garantied to be a value.
   * @param e2 the second operand, which is garantied to be a value.
   * 
   * @return the resulting value of the operator application.
   * 
   * @throws ClassCastException if either <code>e1</code> or <code>e2</code> is invalid
   *                            for the operator.
   * @throws UnaryOperatorException if the operator <code>op</code> cannot be applied to
   *                                <code>e1</code> and <code>e2</code>.
   */
  public abstract Expression applyTo(MutableStore store, BinaryOperator op, Expression e1, Expression e2) throws ClassCastException, BinaryOperatorException;
}
