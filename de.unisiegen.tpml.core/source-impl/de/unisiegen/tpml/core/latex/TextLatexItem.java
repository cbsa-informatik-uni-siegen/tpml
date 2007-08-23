package de.unisiegen.tpml.core.latex ;


/**
 * This class represents a chunk of text in {@link DefaultLatexStringBuilder}s
 * item list.
 * 
 * @author Christian Fehler
 * @see AbstractLatexItem
 * @see DefaultLatexStringBuilder
 */
final class TextLatexItem extends AbstractLatexItem
{
  /**
   * The text content.
   */
  private String content ;


  /**
   * Allocates a new <code>TextItem</code>, which represents a portion of
   * text, as given by the <code>pContent</code>.
   * 
   * @param pContent The text content.
   * @throws NullPointerException if the <code>pContent</code> is
   *           <code>null</code>.
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
   * @see AbstractLatexItem#determineString(StringBuilder,int)
   */
  @ Override
  void determineString ( StringBuilder pBuffer , int pIndent )
  {
    // append the text content
    pBuffer.append ( DefaultLatexStringBuilder.getIndent ( pIndent )
        + this.content ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineStringLength()
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
