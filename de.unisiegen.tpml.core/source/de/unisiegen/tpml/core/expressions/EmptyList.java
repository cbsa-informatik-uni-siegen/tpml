package de.unisiegen.tpml.core.expressions;


/**
 * The constant for the empty list, represented as <code>[]</code>, which
 * must be handled differently from the <code>List</code> expressions, which
 * are syntactic sugar.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see Constant
 */
public final class EmptyList extends Constant
{

  /**
   * The keyword <code>[]</code>.
   */
  private static final String BRACKETBRACKET = "[]"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( EmptyList.class );


  /**
   * Allocates a new <code>EmptyList</code> instance.
   */
  public EmptyList ()
  {
    super ( BRACKETBRACKET );
  }


  /**
   * Allocates a new <code>EmptyList</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public EmptyList ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public EmptyList clone ()
  {
    return new EmptyList ();
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
