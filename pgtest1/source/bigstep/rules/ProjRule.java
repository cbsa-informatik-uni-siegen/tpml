package bigstep.rules;

import common.interpreters.MutableStore;

import expressions.Expression;
import expressions.Projection;
import expressions.Tuple;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;

/**
 * This class represents the big step rule <b>(PROJ)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProjRule extends AbstractUnaryOperatorRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ProjRule</code> instance.
   */
  public ProjRule() {
    super("PROJ");
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
    // cast to the appropriate types
    Projection projection = (Projection)e1;
    Tuple tuple = (Tuple)e2;
    
    // return the projected item
    return projection.applyTo(tuple);
  }
}
