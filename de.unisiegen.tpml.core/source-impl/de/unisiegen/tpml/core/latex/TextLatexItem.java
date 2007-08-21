package de.unisiegen.tpml.core.latex ;


/**
 * This class represents a chunk of text in
 * {@link de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder}s item list,
 * which is associated with a given
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyStyle}. For example this
 * is used to implement various methods of the pretty string builder, like
 * <code>addConstant()</code>, <code>addKeyword()</code> and
 * <code>addText()</code>.
 * 
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.latex.AbstractLatexItem
 * @see de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder
 */
final class TextLatexItem extends AbstractLatexItem
{
  /**
   * The text content.
   */
  private String content ;


  /**
   * Allocates a new <code>TextItem</code>, which represents a portion of
   * text, as given by the <code>content</code>, associated with the
   * specified <code>style</code>.
   * 
   * @param pContent the text content.
   * @throws NullPointerException if either <code>content</code> or
   *           <code>style</code> is <code>null</code>.
   */
  TextLatexItem ( String pContent )
  {
    if ( pContent == null )
    {
      throw new NullPointerException ( "content is null" ) ; //$NON-NLS-1$
    }
    this.content = pContent ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineString(StringBuilder)
   */
  @ Override
  void determineString ( StringBuilder buffer )
  {
    // append the text content
    buffer.append ( this.content ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.AbstractLatexItem#determineStringLength()
   */
  @ Override
  int determineStringLength ( )
  {
    return this.content.length ( ) ;
  }


  /**
   * Returns the content.
   * 
   * @return The content.
   * @see #content
   */
  public String getContent ( )
  {
    return this.content ;
  }
}
