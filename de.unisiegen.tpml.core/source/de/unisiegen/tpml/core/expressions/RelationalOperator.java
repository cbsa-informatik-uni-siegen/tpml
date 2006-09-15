package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class represent relational operators in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 * @see de.unisiegen.tpml.core.expressions.BooleanConstant
 * @see de.unisiegen.tpml.core.expressions.IntegerConstant
 */
public final class RelationalOperator extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The equals operator.
   */
  public static final RelationalOperator EQUALS = new RelationalOperator("=");
  
  /**
   * The lower-than operator.
   */
  public static final RelationalOperator LOWER_THAN = new RelationalOperator("<");
  
  /**
   * The greater-than operator.
   */
  public static final RelationalOperator GREATER_THAN = new RelationalOperator(">");
  
  /**
   * The lower-equal operator.
   */
  public static final RelationalOperator LOWER_EQUAL = new RelationalOperator("<=");
  
  /**
   * The greater-equal operator.
   */
  public static final RelationalOperator GREATER_EQUAL = new RelationalOperator(">=");

  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>RelationalOperator</code> with the specified string representation
   * <code>text</code>.
   * 
   * @param text the string representation.
   * 
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  private RelationalOperator(String text) {
    super(text, PRIO_RELATIONAL_OPERATOR);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.BinaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException {
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    try {
      // determine the numeric values of the operands
      int n1 = ((IntegerConstant)e1).getNumber();
      int n2 = ((IntegerConstant)e2).getNumber();
      
      // perform the requested comparison
      if (this == EQUALS) {
        return (n1 == n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == LOWER_THAN) {
        return (n1 < n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == GREATER_THAN) {
        return (n1 > n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == LOWER_EQUAL) {
        return (n1 <= n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == GREATER_EQUAL) {
        return (n1 >= n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else {
        // programming error
        throw new IllegalStateException("inconsistent arithmetic operator class");
      }
    }
    catch (ClassCastException e) {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException(this, e1, e2);
    }
  }
}
