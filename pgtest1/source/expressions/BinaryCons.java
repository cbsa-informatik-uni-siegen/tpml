package expressions;

import expressions.annotation.SyntacticSugar;

/**
 * The binary version of the <code>cons</code> operator, written
 * as <code>::</code> (two colons), which is syntactic sugar
 * for the unary <code>cons</code> operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
public final class BinaryCons extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>BinaryCons</code> class.
   */
  public static final BinaryCons CONS = new BinaryCons();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Constructs a new <code>BinaryCons</code> instance.
   * 
   * @see #CONS
   */
  private BinaryCons() {
    super("::", 1);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.BinaryOperator#applyTo(expressions.Expression, expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException {
    try {
      // try to create a new list from e1 and e2
      return new List(e1, e2);
    }
    catch (ClassCastException exception) {
      // we're stuck
      throw new BinaryOperatorException(this, e1, e2);
    }
  }
}
