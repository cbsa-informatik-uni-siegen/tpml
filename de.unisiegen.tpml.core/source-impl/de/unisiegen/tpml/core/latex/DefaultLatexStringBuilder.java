package de.unisiegen.tpml.core.latex ;


import java.util.ArrayList ;


/**
 * Default implementation of the <code>PrettyStringBuilder</code> interface,
 * which is the heart of the pretty printer.
 * 
 * @author Benedikt Meurer
 * @version $Rev:835 $
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory
 */
final class DefaultLatexStringBuilder implements LatexStringBuilder
{
  /**
   * The items already added to this pretty string builder.
   * 
   * @see AbstractLatexItem
   */
  private ArrayList < AbstractLatexItem > items = new ArrayList < AbstractLatexItem > ( ) ;


  /**
   * The return priority of the printable according to the priority grammar.
   */
  private int returnPriority ;


  /**
   * Allocates a new <code>DefaultPrettyStringBuilder</code> for the
   * <code>printable</code>, where the priority used for the
   * <code>printable</code> is <code>returnPriority</code>.
   * 
   * @param pPrintable the printable for which to generate a pretty string.
   * @param pReturnPriority the priority for the <code>printable</code>,
   *          which determines where and when to add parenthesis around
   *          {@link LatexPrintable}s added to this builder.
   * @param pName TODO
   * @throws NullPointerException if <code>printable</code> is
   *           <code>null</code>.
   */
  DefaultLatexStringBuilder ( LatexPrintable pPrintable , int pReturnPriority ,
      String pName )
  {
    if ( pPrintable == null )
    {
      throw new NullPointerException ( "printable is null" ) ; //$NON-NLS-1$
    }
    this.returnPriority = pReturnPriority ;
    this.items.add ( new TextLatexItem ( "\\" + pName ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addBreak()
   */
  public void addBreak ( )
  {
    this.items.add ( BreakLatexItem.BREAK_LATEX_ITEM ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder,
   *      int)
   */
  public void addEmptyBuilder ( )
  {
    // implement the break
    if ( ( this.items.size ( ) > 0 )
        && ( this.items.get ( this.items.size ( ) - 1 ) instanceof BreakLatexItem ) )
    {
      BreakLatexItem moveBreak = ( BreakLatexItem ) this.items
          .remove ( this.items.size ( ) - 1 ) ;
      this.items.add ( new TextLatexItem ( "{}" ) ) ; //$NON-NLS-1$
      this.items.add ( moveBreak ) ;
    }
    else
    {
      this.items.add ( new TextLatexItem ( "{}" ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder,
   *      int)
   */
  public void addBuilder ( LatexStringBuilder builder , int argumentPriority )
  {
    if ( builder == null )
    {
      throw new NullPointerException ( "builder is null" ) ; //$NON-NLS-1$
    }
    // cast to a default latex string builder
    DefaultLatexStringBuilder defaultBuilder = ( DefaultLatexStringBuilder ) builder ;
    // check if we need parenthesis
    boolean parenthesis = ( defaultBuilder.returnPriority < argumentPriority ) ;
    // implement the break
    if ( ( this.items.size ( ) > 0 )
        && ( this.items.get ( this.items.size ( ) - 1 ) instanceof BreakLatexItem ) )
    {
      this.items.remove ( this.items.size ( ) - 1 ) ;
      this.items.add ( new TextLatexItem ( "{" ) ) ; //$NON-NLS-1$
      this.items.add ( new TextLatexItem ( "\\linebreak[3]" ) ) ; //$NON-NLS-1$
    }
    else
    {
      this.items.add ( new TextLatexItem ( "{" ) ) ; //$NON-NLS-1$
    }
    if ( parenthesis )
    {
      this.items.add ( new TextLatexItem ( "(" ) ) ; //$NON-NLS-1$
    }
    this.items.add ( new BuilderLatexItem ( defaultBuilder ) ) ;
    if ( parenthesis )
    {
      this.items.add ( new TextLatexItem ( ")" ) ) ; //$NON-NLS-1$
    }
    this.items.add ( new TextLatexItem ( "}" ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addText(java.lang.String)
   */
  public void addText ( String text )
  {
    // implement the break
    if ( ( this.items.size ( ) > 0 )
        && ( this.items.get ( this.items.size ( ) - 1 ) instanceof BreakLatexItem ) )
    {
      this.items.remove ( this.items.size ( ) - 1 ) ;
      this.items.add ( new TextLatexItem ( "\\linebreak[3]" ) ) ; //$NON-NLS-1$
    }
    this.items.add ( new TextLatexItem ( text ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#toPrettyString()
   */
  public LatexString toLatexString ( )
  {
    // determine the final string length for the builder contents
    int length = determineStringLength ( ) ;
    // allocate the string buffer
    StringBuilder buffer = new StringBuilder ( length ) ;
    // determine the string representation and place it into the string buffer
    determineString ( buffer ) ;
    return new DefaultLatexString ( buffer.toString ( ) ) ;
  }


  //
  // Pretty string construction
  //
  /**
   * Determines the string representation of this builders contents and adds it
   * to the <code>buffer</code>.
   * 
   * @param buffer string buffer to store the characters to.
   */
  void determineString ( StringBuilder buffer )
  {
    // process all items for this string builder
    for ( AbstractLatexItem item : this.items )
    {
      // determine the string representation for the item
      item.determineString ( buffer ) ;
    }
  }


  /**
   * Determines the number of characters required to serialize the pretty string
   * builder content to a pretty string.
   * 
   * @return the number of characters in the generated pretty string.
   */
  int determineStringLength ( )
  {
    int length = 0 ;
    for ( AbstractLatexItem item : this.items )
    {
      length += item.determineStringLength ( ) ;
    }
    return length ;
  }
}
