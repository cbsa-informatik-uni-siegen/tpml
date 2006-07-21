package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.BinaryCons;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.Expression;

/**
 * This class represents the big step rule <b>(CONS)</b>, which is used
 * for the binary cons <tt>::</t> operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ConsRule extends AbstractBinaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ConsRule</code> instance.
   */
  public ConsRule() {
    super("CONS");
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
    // op must be an instanceof BinaryCons
    BinaryCons binaryCons = (BinaryCons)op;
    
    // try to apply the operator
    return binaryCons.applyTo(e1, e2);
  }
}
