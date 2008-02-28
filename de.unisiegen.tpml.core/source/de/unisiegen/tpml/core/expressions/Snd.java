package de.unisiegen.tpml.core.expressions;


/**
 * Instances of this class represent the <code>snd</code> operator in the
 * expression hierarchy, which is syntactic sugar for <code>#2_2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Projection
 */
public final class Snd extends Projection
{

  /**
   * The keyword <code>snd</code>.
   */
  private static final String SND = "snd"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Snd.class );


  /**
   * Allocates a new instance of the <code>snd</code> operator which is a
   * special case of the projection that returns the second item of a pair.
   */
  public Snd ()
  {
    super ( 2, 2, SND );
  }


  /**
   * Allocates a new instance of the <code>snd</code> operator which is a
   * special case of the projection that returns the second item of a pair.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Snd ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Projection#clone()
   */
  @Override
  public Snd clone ()
  {
    return new Snd ();
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
