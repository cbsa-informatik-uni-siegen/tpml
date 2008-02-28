package de.unisiegen.tpml.core.expressions;


/**
 * The <code>is_empty</code> operator, which, when applied to a list, returns
 * <code>true</code> if the list is empty, or <code>false</code> if the list
 * contains atleast one item.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryListOperator
 * @see Hd
 * @see Tl
 */
public final class IsEmpty extends UnaryListOperator
{

  /**
   * The string for the is empty.
   */
  private static final String IS_EMPTY = "is_empty"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( IsEmpty.class );


  /**
   * Allocates a new <code>IsEmpty</code> instance.
   */
  public IsEmpty ()
  {
    super ( IS_EMPTY );
  }


  /**
   * Allocates a new <code>IsEmpty</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public IsEmpty ( int pParserStartOffset, int pParserEndOffset )
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
  public IsEmpty clone ()
  {
    return new IsEmpty ();
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
