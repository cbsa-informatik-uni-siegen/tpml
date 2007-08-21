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
   * Allocates a new <code>DefaultLatextringBuilder</code> for the
   * <code>printable</code>, where the priority used for the
   * <code>printable</code> is <code>returnPriority</code>.
   * 
   * @param pPrintable The printable for which to generate a latex string.
   * @param pReturnPriority The priority for the <code>printable</code>,
   *          which determines where and when to add parenthesis around
   *          {@link LatexPrintable}s added to this builder.
   * @param pName The name of the latex command.
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
   * @see LatexStringBuilder#addBuilder(LatexStringBuilder, int)
   */
  public void addBuilder ( LatexStringBuilder pLatexStringBuilder ,
      int pArgumentPriority )
  {
    if ( pLatexStringBuilder == null )
    {
      throw new NullPointerException ( "builder is null" ) ; //$NON-NLS-1$
    }
    // cast to a default latex string builder
    DefaultLatexStringBuilder defaultBuilder = ( DefaultLatexStringBuilder ) pLatexStringBuilder ;
    // check if we need parenthesis
    boolean parenthesis = ( defaultBuilder.returnPriority < pArgumentPriority ) ;
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
   * @see LatexStringBuilder#addBuilder(LatexStringBuilder, int)
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
   * @see LatexStringBuilder#addText(String)
   */
  public void addText ( String pText )
  {
    // implement the break
    if ( ( this.items.size ( ) > 0 )
        && ( this.items.get ( this.items.size ( ) - 1 ) instanceof BreakLatexItem ) )
    {
      this.items.remove ( this.items.size ( ) - 1 ) ;
      this.items.add ( new TextLatexItem ( "\\linebreak[3]" ) ) ; //$NON-NLS-1$
    }
    this.items.add ( new TextLatexItem ( pText ) ) ;
  }


  /**
   * Determines the string representation of this builders contents and adds it
   * to the <code>pBuffer</code>.
   * 
   * @param pBuffer String buffer to store the characters to.
   */
  void determineString ( StringBuilder pBuffer )
  {
    // process all items for this string builder
    for ( AbstractLatexItem item : this.items )
    {
      // determine the string representation for the item
      item.determineString ( pBuffer ) ;
    }
  }


  /**
   * Determines the number of characters required to serialize the latex string
   * builder content to a latex string.
   * 
   * @return The number of characters in the generated latex string.
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


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#toLatexString()
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
}
