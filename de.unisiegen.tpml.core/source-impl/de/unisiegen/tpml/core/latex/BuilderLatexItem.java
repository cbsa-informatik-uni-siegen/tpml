package de.unisiegen.tpml.core.latex ;


/**
 * This class represents a latex string builder that is added to another latex
 * string builder.
 * 
 * @author Christian Fehler
 * @see AbstractLatexItem
 * @see DefaultLatexStringBuilder
 */
final class BuilderLatexItem extends AbstractLatexItem
{
  /**
   * The latex string builder associated with this item.
   */
  private DefaultLatexStringBuilder builder ;


  /**
   * Allocates a new <code>BuilderLatexItem</code> for the specified
   * <code>builder</code>.
   * 
   * @param pDefaultLatexStringBuilder The latex string builder for which to
   *          allocate a builder item.
   * @throws NullPointerException If <code>builder</code> is <code>null</code>.
   */
  protected BuilderLatexItem (
      DefaultLatexStringBuilder pDefaultLatexStringBuilder )
  {
    if ( pDefaultLatexStringBuilder == null )
    {
      throw new NullPointerException ( "builder is null" ) ; //$NON-NLS-1$
    }
    this.builder = pDefaultLatexStringBuilder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineString(StringBuilder)
   */
  @ Override
  protected void determineString ( StringBuilder pBuffer )
  {
    this.builder.determineString ( pBuffer ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineStringLength()
   */
  @ Override
  protected int determineStringLength ( )
  {
    return this.builder.determineStringLength ( ) ;
  }
}
