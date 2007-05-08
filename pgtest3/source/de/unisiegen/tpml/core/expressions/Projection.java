package de.unisiegen.tpml.core.expressions ;


import java.text.MessageFormat ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;


/**
 * Instances of this class represent projections in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryOperator
 */
public class Projection extends UnaryOperator
{
  /**
   * The arity of the projection.
   * 
   * @see #getArity()
   */
  private int arity ;


  /**
   * The index of the projection.
   * 
   * @see #getIndex()
   */
  private int index ;


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected.
   * 
   * @param pArity the arity of the tuple to which this projection can be
   *          applied.
   * @param pIndex the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  public Projection ( int pArity , int pIndex )
  {
    this ( pArity , pIndex , "#" + pArity + "_" + pIndex ) ; //$NON-NLS-1$//$NON-NLS-2$
  }


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected.
   * 
   * @param pArity the arity of the tuple to which this projection can be
   *          applied.
   * @param pIndex the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @param pParserArityStartOffset TODO
   * @param pParserArityEndOffset TODO
   * @param pParserIndexStartOffset TODO
   * @param pParserIndexEndOffset TODO
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  public Projection ( int pArity , int pIndex , int pParserArityStartOffset ,
      int pParserArityEndOffset , int pParserIndexStartOffset ,
      int pParserIndexEndOffset , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pArity , pIndex ,
        "#" + pArity + "_" + pIndex , //$NON-NLS-1$ //$NON-NLS-2$
        pParserArityStartOffset , pParserArityEndOffset ,
        pParserIndexStartOffset , pParserIndexEndOffset ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected, and the
   * string representation <code>op</code>.
   * 
   * @param pArity the arity of the tuple to which this projection can be
   *          applied.
   * @param pIndex the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @param pOp the string representation of the projectin.
   * @param pParserArityStartOffset TODO
   * @param pParserArityEndOffset TODO
   * @param pParserIndexStartOffset TODO
   * @param pParserIndexEndOffset TODO
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  protected Projection ( int pArity , int pIndex , String pOp ,
      int pParserArityStartOffset , int pParserArityEndOffset ,
      int pParserIndexStartOffset , int pParserIndexEndOffset )
  {
    super ( pOp ) ;
    // validate the settings
    if ( pArity <= 0 )
    {
      throw new LanguageParserException ( MessageFormat.format ( Messages
          .getString ( "Exception.2" ) , String.valueOf ( pArity ) ) , //$NON-NLS-1$
          pParserArityStartOffset , pParserArityEndOffset ) ;
    }
    else if ( pIndex <= 0 )
    {
      throw new LanguageParserException ( MessageFormat.format ( Messages
          .getString ( "Exception.3" ) , String.valueOf ( pIndex ) ) , //$NON-NLS-1$
          pParserIndexStartOffset , pParserIndexEndOffset ) ;
    }
    else if ( pIndex > pArity )
    {
      throw new LanguageParserMultiException ( new String [ ]
      { MessageFormat.format ( Messages.getString ( "Exception.4" ) , String //$NON-NLS-1$
          .valueOf ( pArity ) , String.valueOf ( pIndex ) ) ,
          MessageFormat.format ( Messages.getString ( "Exception.5" ) , String //$NON-NLS-1$
              .valueOf ( pIndex ) , String.valueOf ( pArity ) ) } , new int [ ]
      { pParserArityStartOffset , pParserIndexStartOffset } , new int [ ]
      { pParserArityEndOffset , pParserIndexEndOffset } ) ;
    }
    this.arity = pArity ;
    this.index = pIndex ;
  }


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected, and the
   * string representation <code>op</code>.
   * 
   * @param pArity the arity of the tuple to which this projection can be
   *          applied.
   * @param pIndex the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @param pOp the string representation of the projectin.
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  protected Projection ( int pArity , int pIndex , String pOp )
  {
    super ( pOp ) ;
    // validate the settings
    if ( pArity <= 0 )
    {
      throw new IllegalArgumentException ( MessageFormat.format ( Messages
          .getString ( "Exception.2" ) , String.valueOf ( pArity ) ) ) ; //$NON-NLS-1$
    }
    else if ( pIndex <= 0 )
    {
      throw new IllegalArgumentException ( MessageFormat.format ( Messages
          .getString ( "Exception.3" ) , String.valueOf ( pIndex ) ) ) ; //$NON-NLS-1$
    }
    else if ( pIndex > pArity )
    {
      throw new IllegalArgumentException ( MessageFormat.format ( Messages
          .getString ( "Exception.4" ) , String.valueOf ( pIndex ) , String //$NON-NLS-1$
          .valueOf ( pArity ) ) ) ;
    }
    this.arity = pArity ;
    this.index = pIndex ;
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
      Expression [ ] expressions = ( ( Tuple ) pExpression ).getExpressions ( ) ;
      if ( ! pExpression.isValue ( ) )
      {
        throw new UnaryOperatorException ( this , pExpression ) ;
      }
      if ( this.arity != expressions.length )
      {
        throw new UnaryOperatorException ( this , pExpression ) ;
      }
      return expressions [ this.index - 1 ] ;
    }
    catch ( ClassCastException cause )
    {
      // cast of expression to tuple failed
      throw new UnaryOperatorException ( this , pExpression , cause ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Projection clone ( )
  {
    return new Projection ( this.arity , this.index , getText ( ) ) ;
  }


  /**
   * Returns the arity.
   * 
   * @return the arity.
   */
  public int getArity ( )
  {
    return this.arity ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Projection" ; //$NON-NLS-1$
  }


  /**
   * Returns the index.
   * 
   * @return the index.
   */
  public int getIndex ( )
  {
    return this.index ;
  }
}
