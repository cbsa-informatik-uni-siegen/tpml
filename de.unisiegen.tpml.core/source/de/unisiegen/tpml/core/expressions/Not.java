package de.unisiegen.tpml.core.expressions;

/**
 * The {@link #NOT} instance of this class represents the <code>not</code> operator in the expression
 * hierarchy. 
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Not extends UnaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Not</code> class.
   */
  public static final Not NOT = new Not();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Not</code> operator.
   * 
   * @see #NOT
   */
  private Not() {
    super("not");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e) throws UnaryOperatorException {
    if (e == BooleanConstant.TRUE) {
      return BooleanConstant.FALSE;
    }
    else if (e == BooleanConstant.FALSE) {
      return BooleanConstant.TRUE;
    }
    else {
      throw new UnaryOperatorException(this, e);
    }
  }
}
