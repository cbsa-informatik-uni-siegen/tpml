package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.Expression;
import expressions.Location;
import expressions.Ref;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;

/**
 * This class represents the big step rule <b>(REF)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class RefRule extends AbstractUnaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>RefRule</code> instance.
   */
  public RefRule() {
    super("REF");
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
    // make sure e1 is a "ref" operator
    if (e1 instanceof Ref) {
      // allocate and initialize a new location
      Location location = store.alloc();
      store.put(location, e2);
      return location;
    }
    else {
      throw new ClassCastException("Illegal operator " + e1.getClass());
    }
  }
}
