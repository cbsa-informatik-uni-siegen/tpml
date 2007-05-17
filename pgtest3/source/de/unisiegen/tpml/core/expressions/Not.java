package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>not</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Not extends UnaryOperator
{
  /**
   * Allocates a new <code>Not</code> operator.
   */
  public Not ( )
  {
    super ( "not" ) ; //$NON-NLS-1$
  }


  /**
   * Allocates a new <code>Not</code> operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Not ( int pParserStartOffset , int pParserEndOffset )
  {
    this ( ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see UnaryOperator#applyTo(Expression)
   */
  @ Override
  public Expression applyTo ( Expression pExpression )
      throws UnaryOperatorException
  {
    try
    {
      return new BooleanConstant ( ! ( ( BooleanConstant ) pExpression )
          .booleanValue ( ) ) ;
    }
    catch ( ClassCastException cause )
    {
      throw new UnaryOperatorException ( this , pExpression , cause ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Not clone ( )
  {
    return new Not ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Not" ; //$NON-NLS-1$
  }
}
