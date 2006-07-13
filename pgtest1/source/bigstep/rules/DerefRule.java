package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.Deref;
import expressions.Expression;
import expressions.Location;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;

/**
 * This class represents the big step rule <b>(DEREF)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class DerefRule extends AbstractUnaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>DerefRule</code> instance.
   */
  public DerefRule() {
    super("DEREF");
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
    // e1 must be the deref operator
    if (e1 instanceof Deref) {
      // return the value at the given location
      return store.get((Location)e2);
    }
    else {
      throw new ClassCastException("Illegal operator " + e1.getClass());
    }
  }
}
