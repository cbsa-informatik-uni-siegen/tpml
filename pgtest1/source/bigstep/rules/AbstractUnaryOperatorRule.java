package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;
import common.interpreters.MutableStore;

import expressions.Application;
import expressions.Expression;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;

/**
 * Abstract base class for all big step proof rules that handle
 * unary operators.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.rules.AbstractBinaryOperatorRule
 */
abstract class AbstractUnaryOperatorRule extends BigStepProofRule {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractUnaryOperatorRule</code>
   * with the specified <code>name</code>.
   * 
   * @param name the name of the rule.
   */
  protected AbstractUnaryOperatorRule(String name) {
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
      // can only be applied to Applications
      Application application = (Application)node.getExpression();
      
      // verify that e1 and e2 are values
      Expression e1 = application.getE1();
      Expression e2 = application.getE2();
      if (!e1.isValue() || !e2.isValue()) {
        throw new ProofRuleException(node, this);
      }
      
      // allocate a new store and perform the application
      MutableStore store = new MutableStore(node.getStore());
      Expression value = applyTo(store, (UnaryOperator)e1, e2);
      context.setProofNodeResult(node, value, store);
    }
    catch (UnaryOperatorException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
  
  /**
   * This method must be implemented by derived classes to handle the actual
   * application of the operator <code>e1</code> to the expression <code>e2</code>,
   * which is generated to be a value.
   * 
   * Memory operators may modify the <code>store</code> as needed.
   * 
   * @param store the {@link MutableStore} required for memory operators like <code>ref</code>.
   * @param e1 the unary operator to apply to <code>e2</code>.
   * @param e2 the operand, which is garantied to be a value.
   * 
   * @return the resulting value of the operator application.
   *         
   * @throws ClassCastException if either <code>e1</code> or <code>e2</code> is invalid
   *                            for the operator.
   * @throws UnaryOperatorException if the operator cannot be applied to <code>e2</code>.
   */
  public abstract Expression applyTo(MutableStore store, UnaryOperator e1, Expression e2) throws ClassCastException, UnaryOperatorException;
}
