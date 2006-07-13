package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.Expression;
import expressions.Not;
import expressions.UnaryMinus;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;

/**
 * This class represents the big step proof rule <b>(UOP)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class UopRule extends AbstractUnaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>UopRule</code> instance.
   */
  public UopRule() {
    super("UOP");
  }
  
  
  
  //
  // Primitives
  //

  /**
   * {@inheritDoc}
   *
   * @see bigstep.rules.AbstractUnaryOperatorRule#applyTo(common.interpreters.MutableStore, expressions.UnaryOperator, expressions.Expression)
   */
  @Override
  public Expression applyTo(MutableStore store, UnaryOperator e1, Expression e2) throws ClassCastException, UnaryOperatorException {
    // (UOP) can only handle Not and UnaryMinus 
    if (e1 instanceof Not || e1 instanceof UnaryMinus) {
      return e1.applyTo(e2);
    }
    else {
      throw new ClassCastException("Illegal operator " + e1.getClass());
    }
  }
}
