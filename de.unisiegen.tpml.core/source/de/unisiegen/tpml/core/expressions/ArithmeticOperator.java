package de.unisiegen.tpml.core.expressions;


/**
 * Instances of this class represent arithmetic operators in the expression
 * hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see BinaryOperator
 * @see IntegerConstant
 */
public final class ArithmeticOperator extends BinaryOperator
{

  /**
   * The inconsistent exception string.
   */
  private static final String INCONSISTENT = "inconsistent arithmetic operator class"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( ArithmeticOperator.class );


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null"; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null"; //$NON-NLS-1$


  /**
   * The string <code>/</code>.
   */
  private static final String DIV = "/"; //$NON-NLS-1$


  /**
   * The string <code>+</code>.
   */
  private static final String PLUS = "+"; //$NON-NLS-1$


  /**
   * The string <code>-</code>.
   */
  private static final String MINUS = "-"; //$NON-NLS-1$


  /**
   * The string <code>mod</code>.
   */
  private static final String MOD = "mod"; //$NON-NLS-1$


  /**
   * The string <code>*</code>.
   */
  private static final String MULT = "*"; //$NON-NLS-1$


  /**
   * Returns the arithmetic division operator.
   * 
   * @return a new instance of the div operator.
   */
  public static final ArithmeticOperator newDiv ()
  {
    return new ArithmeticOperator ( DIV, PRIO_DIV );
  }


  /**
   * Returns the arithmetic division operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return a new instance of the div operator.
   */
  public static final ArithmeticOperator newDiv ( int pParserStartOffset,
      int pParserEndOffset )
  {
    return new ArithmeticOperator ( DIV, PRIO_DIV, pParserStartOffset,
        pParserEndOffset );
  }


  /**
   * Returns the arithmetic minus operator.
   * 
   * @return a new instance of the minus operator.
   */
  public static final ArithmeticOperator newMinus ()
  {
    return new ArithmeticOperator ( MINUS, PRIO_MINUS );
  }


  /**
   * Returns the arithmetic minus operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return a new instance of the minus operator.
   */
  public static final ArithmeticOperator newMinus ( int pParserStartOffset,
      int pParserEndOffset )
  {
    return new ArithmeticOperator ( MINUS, PRIO_MINUS, pParserStartOffset,
        pParserEndOffset );
  }


  /**
   * Returns the arithmetic modulo operator.
   * 
   * @return a new instance of the mod operator.
   */
  public static final ArithmeticOperator newMod ()
  {
    return new ArithmeticOperator ( MOD, PRIO_MOD );
  }


  /**
   * Returns the arithmetic modulo operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return a new instance of the mod operator.
   */
  public static final ArithmeticOperator newMod ( int pParserStartOffset,
      int pParserEndOffset )
  {
    return new ArithmeticOperator ( MOD, PRIO_MOD, pParserStartOffset,
        pParserEndOffset );
  }


  /**
   * Returns the arithmetic multiplication operator.
   * 
   * @return a new instance of the mult operator.
   */
  public static final ArithmeticOperator newMult ()
  {
    return new ArithmeticOperator ( MULT, PRIO_MULT );
  }


  /**
   * Returns the arithmetic multiplication operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return a new instance of the mult operator.
   */
  public static final ArithmeticOperator newMult ( int pParserStartOffset,
      int pParserEndOffset )
  {
    return new ArithmeticOperator ( MULT, PRIO_MULT, pParserStartOffset,
        pParserEndOffset );
  }


  /**
   * Returns the arithmetic plus operator.
   * 
   * @return a new instance of the plus operator.
   */
  public static final ArithmeticOperator newPlus ()
  {
    return new ArithmeticOperator ( PLUS, PRIO_PLUS );
  }


  /**
   * Returns the arithmetic plus operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return a new instance of the plus operator.
   */
  public static final ArithmeticOperator newPlus ( int pParserStartOffset,
      int pParserEndOffset )
  {
    return new ArithmeticOperator ( PLUS, PRIO_PLUS, pParserStartOffset,
        pParserEndOffset );
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
  private ArithmeticOperator ( String pText, int pPrettyPriority )
  {
    super ( pText, pPrettyPriority );
  }


  /**
   * Allocates a new <code>ArithmeticOperator</code> with the given
   * <code>text</code> and <code>prettyPriority</code> (for pretty printing
   * in infix operation).
   * 
   * @param pText the string representation of the operator.
   * @param pPrettyPriority the pretty print priority for infix pretty printing.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * @see BinaryOperator#BinaryOperator(String, int)
   */
  private ArithmeticOperator ( String pText, int pPrettyPriority,
      int pParserStartOffset, int pParserEndOffset )
  {
    this ( pText, pPrettyPriority );
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @Override
  public Expression applyTo ( Expression pExpression1, Expression pExpression2 )
      throws BinaryOperatorException
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL );
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL );
    }
    try
    {
      // determine the integer values of the operands
      int n1 = ( ( IntegerConstant ) pExpression1 ).intValue ();
      int n2 = ( ( IntegerConstant ) pExpression2 ).intValue ();
      // try to perform the application
      if ( PLUS.equals ( getText () ) )
      {
        return new IntegerConstant ( n1 + n2 );
      }
      else if ( MINUS.equals ( getText () ) )
      {
        return new IntegerConstant ( n1 - n2 );
      }
      else if ( MULT.equals ( getText () ) )
      {
        return new IntegerConstant ( n1 * n2 );
      }
      else if ( DIV.equals ( getText () ) )
      {
        return new IntegerConstant ( n1 / n2 );
      }
      else if ( MOD.equals ( getText () ) )
      {
        return new IntegerConstant ( n1 % n2 );
      }
      else
      {
        // programming error
        throw new IllegalStateException ( INCONSISTENT );
      }
    }
    catch ( ClassCastException e )
    {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException ( this, pExpression1, pExpression2 );
    }
    catch ( ArithmeticException e )
    {
      // internal Java operators throw this on divide-by-zero
      return Exn.newDivideByZero ();
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public ArithmeticOperator clone ()
  {
    return new ArithmeticOperator ( getText (), getPrettyPriority () );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }
}
