package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>fst</code> operator in the
 * expression hierarchy, which is syntactic sugar for <code>#2_1</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Projection
 */
public final class Fst extends Projection
{
  /**
   * The keyword <code>fst</code>.
   */
  private static final String FST = "fst" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = "First" ; //$NON-NLS-1$


  /**
   * Allocates a new instance of the <code>fst</code> operator which is a
   * special case of the projection that returns the first item of a pair.
   */
  public Fst ( )
  {
    super ( 2 , 1 , FST ) ;
  }


  /**
   * Allocates a new instance of the <code>fst</code> operator which is a
   * special case of the projection that returns the first item of a pair.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Fst ( int pParserStartOffset , int pParserEndOffset )
  {
    this ( ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Projection#clone()
   */
  @ Override
  public Fst clone ( )
  {
    return new Fst ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }
}
