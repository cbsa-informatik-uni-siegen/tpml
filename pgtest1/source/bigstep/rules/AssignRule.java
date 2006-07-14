package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.Assign;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.Expression;
import expressions.Location;
import expressions.UnitConstant;

/**
 * This class represents the big step rule <b>(ASSIGN)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.rules.AbstractBinaryOperatorRule
 */
public final class AssignRule extends AbstractBinaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>AssignRule</code> instance.
   */
  public AssignRule() {
    super("ASSIGN");
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
    // (ASSIGN) can only be applied to Assign
    if (op instanceof Assign) {
      store.put((Location)e1, e2);
      return UnitConstant.UNIT;
    }
    else {
      throw new ClassCastException("Illegal operator " + op.getClass());
    }
  }
}
