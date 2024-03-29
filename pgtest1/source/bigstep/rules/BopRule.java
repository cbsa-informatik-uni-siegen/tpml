package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.ArithmeticOperator;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.Expression;
import expressions.RelationalOperator;

/**
 * This class represents the big step rule <b>(BOP)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BopRule extends AbstractBinaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>BopRule</code> instance.
   */
  public BopRule() {
    super("BOP");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.rules.AbstractBinaryOperatorRule#applyTo(common.interpreters.MutableStore, expressions.BinaryOperator, expressions.Expression, expressions.Expression)
   */
  @Override
  public Expression applyTo(MutableStore store, BinaryOperator op, Expression e1, Expression e2) throws ClassCastException, BinaryOperatorException {
    // (BOP) can only handle arithmetic and relational operators
    if (op instanceof ArithmeticOperator || op instanceof RelationalOperator) {
      return op.applyTo(e1, e2);
    }
    else {
      throw new ClassCastException("Illegal operator " + op.getClass());
    }
  }
}
