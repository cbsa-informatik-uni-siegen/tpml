package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent arithmetic operators in the expression
 * hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see BinaryOperator
 * @see IntegerConstant
 */
public final class ArithmeticOperator extends BinaryOperator
{
  /**
   * Returns the arithmetic division operator.
   * 
   * @return a new instance of the div operator.
   */
  public static final ArithmeticOperator newDiv ( )
  {
    return new ArithmeticOperator ( "/" , PrettyPrintPriorities.PRIO_DIV ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic minus operator.
   * 
   * @return a new instance of the minus operator.
   */
  public static final ArithmeticOperator newMinus ( )
  {
    return new ArithmeticOperator ( "-" , PrettyPrintPriorities.PRIO_MINUS ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic modulo operator.
   * 
   * @return a new instance of the mod operator.
   */
  public static final ArithmeticOperator newMod ( )
  {
    return new ArithmeticOperator ( "mod" , PrettyPrintPriorities.PRIO_MOD ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic multiplication operator.
   * 
   * @return a new instance of the mult operator.
   */
  public static final ArithmeticOperator newMult ( )
  {
    return new ArithmeticOperator ( "*" , PrettyPrintPriorities.PRIO_MULT ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic plus operator.
   * 
   * @return a new instance of the plus operator.
   */
  public static final ArithmeticOperator newPlus ( )
  {
    return new ArithmeticOperator ( "+" , PrettyPrintPriorities.PRIO_PLUS ) ; //$NON-NLS-1$
  }


  /**
   * Allocates a new <code>ArithmeticOperator</code> with the given
   * <code>text</code> and <code>prettyPriority</code> (for pretty printing
   * in infix operation).
   * 
   * @param pText the string representation of the operator.
   * @param pPrettyPriority the pretty print priority for infix pretty printing.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * @see BinaryOperator#BinaryOperator(String, int)
   */
  private ArithmeticOperator ( final String pText , final int pPrettyPriority )
  {
    super ( pText , pPrettyPriority ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @ Override
  public Expression applyTo ( final Expression pExpression1 ,
      final Expression pExpression2 ) throws BinaryOperatorException
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    try
    {
      // determine the integer values of the operands
      final int n1 = ( ( IntegerConstant ) pExpression1 ).intValue ( ) ;
      final int n2 = ( ( IntegerConstant ) pExpression2 ).intValue ( ) ;
      // try to perform the application
      if ( this.getText ( ) == "+" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 + n2 ) ;
      }
      else if ( this.getText ( ) == "-" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 - n2 ) ;
      }
      else if ( this.getText ( ) == "*" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 * n2 ) ;
      }
      else if ( this.getText ( ) == "/" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 / n2 ) ;
      }
      else if ( this.getText ( ) == "mod" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 % n2 ) ;
      }
      else
      {
        // programming error
        throw new IllegalStateException (
            "Inconsistent arithmetic operator class" ) ; //$NON-NLS-1$
      }
    }
    catch ( final ClassCastException e )
    {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException ( this , pExpression1 , pExpression2 ) ;
    }
    catch ( final ArithmeticException e )
    {
      // internal Java operators throw this on divide-by-zero
      return Exn.newDivideByZero ( ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public ArithmeticOperator clone ( )
  {
    return new ArithmeticOperator ( this.getText ( ) , this
        .getPrettyPriority ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Arithmetic-Operator" ; //$NON-NLS-1$
  }
}
