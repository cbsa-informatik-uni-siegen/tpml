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
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return a new instance of the div operator.
   */
  public static final ArithmeticOperator newDiv ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new ArithmeticOperator (
        "/" , PRIO_DIV , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic minus operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return a new instance of the minus operator.
   */
  public static final ArithmeticOperator newMinus ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new ArithmeticOperator (
        "-" , PRIO_MINUS , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic modulo operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return a new instance of the mod operator.
   */
  public static final ArithmeticOperator newMod ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new ArithmeticOperator (
        "mod" , PRIO_MOD , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic multiplication operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return a new instance of the mult operator.
   */
  public static final ArithmeticOperator newMult ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new ArithmeticOperator (
        "*" , PRIO_MULT , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the arithmetic plus operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return a new instance of the plus operator.
   */
  public static final ArithmeticOperator newPlus ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new ArithmeticOperator (
        "+" , PRIO_PLUS , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
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
  private ArithmeticOperator ( String pText , int pPrettyPriority )
  {
    super ( pText , pPrettyPriority ) ;
  }


  /**
   * Allocates a new <code>ArithmeticOperator</code> with the given
   * <code>text</code> and <code>prettyPriority</code> (for pretty printing
   * in infix operation).
   * 
   * @param pText the string representation of the operator.
   * @param pPrettyPriority the pretty print priority for infix pretty printing.
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * @see BinaryOperator#BinaryOperator(String, int)
   */
  private ArithmeticOperator ( String pText , int pPrettyPriority ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pText , pPrettyPriority ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @ Override
  public Expression applyTo ( Expression pExpression1 , Expression pExpression2 )
      throws BinaryOperatorException
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
      int n1 = ( ( IntegerConstant ) pExpression1 ).intValue ( ) ;
      int n2 = ( ( IntegerConstant ) pExpression2 ).intValue ( ) ;
      // try to perform the application
      if ( getText ( ) == "+" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 + n2 ) ;
      }
      else if ( getText ( ) == "-" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 - n2 ) ;
      }
      else if ( getText ( ) == "*" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 * n2 ) ;
      }
      else if ( getText ( ) == "/" ) //$NON-NLS-1$
      {
        return new IntegerConstant ( n1 / n2 ) ;
      }
      else if ( getText ( ) == "mod" ) //$NON-NLS-1$
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
    catch ( ClassCastException e )
    {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException ( this , pExpression1 , pExpression2 ) ;
    }
    catch ( ArithmeticException e )
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
    return new ArithmeticOperator ( getText ( ) , getPrettyPriority ( ) ) ;
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
