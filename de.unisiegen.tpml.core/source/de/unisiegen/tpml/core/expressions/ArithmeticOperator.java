package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class represent arithmetic operators in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 * @see de.unisiegen.tpml.core.expressions.IntegerConstant
 */
public final class ArithmeticOperator extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The arithmetic plus operator.
   */
  public static final ArithmeticOperator PLUS = new ArithmeticOperator("+", PRIO_PLUS);
  
  /**
   * The arithmetic minus operator.
   */
  public static final ArithmeticOperator MINUS = new ArithmeticOperator("-", PRIO_MINUS);
  
  /**
   * The arithmetic multiplication operator.
   */
  public static final ArithmeticOperator MULT = new ArithmeticOperator("*", PRIO_MULT);
  
  /**
   * The arithmetic division operator.
   */
  public static final ArithmeticOperator DIV = new ArithmeticOperator("/", PRIO_DIV);
  
  /**
   * The arithmetic modulo operator.
   */
  public static final ArithmeticOperator MOD = new ArithmeticOperator("mod", PRIO_MOD);

  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>ArithmeticOperator</code> with the given <code>text</code> and
   * <code>prettyPriority</code> (for pretty printing in infix operation).
   * 
   * @param text the string representation of the operator.
   * @param prettyPriority the pretty print priority for infix pretty printing.
   * 
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * 
   * @see BinaryOperator#BinaryOperator(String, int)
   */
  private ArithmeticOperator(String text, int prettyPriority) {
    super(text, prettyPriority);
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
      // determine the integer values of the operands
      int n1 = ((IntegerConstant)e1).getNumber();
      int n2 = ((IntegerConstant)e2).getNumber();
      
      // try to perform the application
      if (this == PLUS) {
        return new IntegerConstant(n1 + n2);
      }
      else if (this == MINUS) {
        return new IntegerConstant(n1 - n2);
      }
      else if (this == MULT) {
        return new IntegerConstant(n1 * n2);
      }
      else if (this == DIV) {
        return new IntegerConstant(n1 / n2);
      }
      else if (this == MOD) {
        return new IntegerConstant(n1 % n2);
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
    catch (ArithmeticException e) {
      // internal Java operators throw this on divide-by-zero
      return Exn.DIVIDE_BY_ZERO;
    }
  }
}
