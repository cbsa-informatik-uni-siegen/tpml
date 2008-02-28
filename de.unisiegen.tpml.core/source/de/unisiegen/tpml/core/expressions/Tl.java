package de.unisiegen.tpml.core.expressions;


/**
 * The <code>tl</code> operator, which, when applied to a list, returns the
 * list without the first item, or when applied to an empty list, raises the
 * <code>empty_list</code> exception.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryListOperator
 * @see Hd
 * @see IsEmpty
 */
public final class Tl extends UnaryListOperator
{

  /**
   * The keyword <code>tl</code>.
   */
  private static final String TL = "tl"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Tl.class );


  /**
   * Allocates a new <code>Tl</code> instance.
   */
  public Tl ()
  {
    super ( TL );
  }


  /**
   * Allocates a new <code>Tl</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Tl ( int pParserStartOffset, int pParserEndOffset )
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
  public Tl clone ()
  {
    return new Tl ();
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
