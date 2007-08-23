package de.unisiegen.tpml.core.latex ;


import java.util.ArrayList ;


/**
 * Default implementation of the <code>LatexStringBuilder</code> interface,
 * which is the heart of the latex printer.
 * 
 * @author Christian Fehler
 * @see LatexStringBuilder
 * @see LatexStringBuilderFactory
 */
final class DefaultLatexStringBuilder implements LatexStringBuilder
{
  /**
   * Returns a string only with spaces. The count of the returned spaces is
   * <code>pIdent</code>.
   * 
   * @param pIndent The number of spaces.
   * @return A string only with spaces. The count of the returned spaces is
   *         <code>pIdent</code>.
   */
  public static String getIndent ( int pIndent )
  {
    StringBuilder result = new StringBuilder ( ) ;
    for ( int i = 0 ; i < pIndent ; i ++ )
    {
      result.append ( " " ) ; //$NON-NLS-1$
    }
    return result.toString ( ) ;
  }


  /**
   * The items already added to this latex string builder.
   * 
   * @see AbstractLatexItem
   */
  private ArrayList < AbstractLatexItem > items = new ArrayList < AbstractLatexItem > ( ) ;


  /**
   * The return priority of the printable according to the priority grammar.
   */
  private int returnPriority ;


  /**
   * The indent of this object.
   */
  private int indent ;


  /**
   * Allocates a new <code>DefaultLatextringBuilder</code> for the
   * <code>printable</code>, where the priority used for the
   * <code>printable</code> is <code>returnPriority</code>.
   * 
   * @param pPrintable The printable for which to generate a latex string.
   * @param pReturnPriority The priority for the <code>printable</code>,
   *          which determines where and when to add parenthesis around
   *          {@link LatexPrintable}s added to this builder.
   * @param pName The name of the latex command.
   * @param pIndent The indent of this object.
   * @throws NullPointerException if <code>printable</code> is
   *           <code>null</code>.
   */
  DefaultLatexStringBuilder ( LatexPrintable pPrintable , int pReturnPriority ,
      String pName , int pIndent )
  {
    if ( pPrintable == null )
    {
      throw new NullPointerException ( "printable is null" ) ; //$NON-NLS-1$
    }
    this.returnPriority = pReturnPriority ;
    this.items
        .add ( new TextLatexItem ( getIndent ( pIndent ) + "\\" + pName ) ) ; //$NON-NLS-1$
    this.indent = pIndent ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addBuilder(LatexStringBuilder, int)
   */
  public void addBuilder ( LatexStringBuilder pLatexStringBuilder ,
      int pArgumentPriority )
  {
    if ( pLatexStringBuilder == null )
    {
      throw new NullPointerException ( "builder is null" ) ; //$NON-NLS-1$
    }
    DefaultLatexStringBuilder defaultBuilder = ( DefaultLatexStringBuilder ) pLatexStringBuilder ;
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "{" ) ) ; //$NON-NLS-1$
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    boolean parenthesis = ( defaultBuilder.returnPriority < pArgumentPriority ) ;
    if ( parenthesis )
    {
      this.items.add ( new TextLatexItem ( getIndent ( this.indent
          + LATEX_INDENT )
          + "\\" + LATEX_PARENTHESIS ) ) ; //$NON-NLS-1$
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
      this.items.add ( new TextLatexItem ( getIndent ( this.indent
          + LATEX_INDENT )
          + "{" ) ) ; //$NON-NLS-1$
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
      this.items.add ( new BuilderLatexItem ( defaultBuilder ) ) ;
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
      this.items.add ( new TextLatexItem ( getIndent ( this.indent
          + LATEX_INDENT )
          + "}" ) ) ; //$NON-NLS-1$
    }
    else
    {
      this.items.add ( new BuilderLatexItem ( defaultBuilder ) ) ;
    }
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "}" ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addBuilderBegin()
   */
  public void addBuilderBegin ( )
  {
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "{" ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addBuilderEnd()
   */
  public void addBuilderEnd ( )
  {
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "}" ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addEmptyBuilder()
   */
  public void addEmptyBuilder ( )
  {
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "{" ) ) ; //$NON-NLS-1$
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "}" ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addText(String)
   */
  public void addText ( String pText )
  {
    this.items.add ( new TextLatexItem ( pText ) ) ;
  }


  /**
   * Determines the string representation of this builders contents and adds it
   * to the <code>pBuffer</code>.
   * 
   * @param pBuffer String buffer to store the characters to.
   * @param pIndent The indent of this object.
   */
  public void determineString ( StringBuilder pBuffer , int pIndent )
  {
    for ( AbstractLatexItem item : this.items )
    {
      item.determineString ( pBuffer , pIndent ) ;
    }
  }


  /**
   * Determines the number of characters required to serialize the latex string
   * builder content to a latex string.
   * 
   * @return The number of characters in the generated latex string.
   */
  public int determineStringLength ( )
  {
    int length = 0 ;
    for ( AbstractLatexItem item : this.items )
    {
      length += item.determineStringLength ( ) ;
    }
    return length ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#toLatexString()
   */
  public LatexString toLatexString ( )
  {
    StringBuilder buffer = new StringBuilder ( ) ;
    determineString ( buffer , this.indent ) ;
    return new DefaultLatexString ( buffer.toString ( ) ) ;
  }
}
