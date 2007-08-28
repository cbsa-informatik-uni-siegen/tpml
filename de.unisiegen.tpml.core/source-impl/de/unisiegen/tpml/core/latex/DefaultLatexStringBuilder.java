package de.unisiegen.tpml.core.latex ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.prettyprinter.PrettyCommandNames ;


/**
 * Default implementation of the <code>LatexStringBuilder</code> interface,
 * which is the heart of the latex printer.
 * 
 * @author Christian Fehler
 * @see LatexStringBuilder
 * @see LatexStringBuilderFactory
 */
public final class DefaultLatexStringBuilder implements LatexStringBuilder ,
    PrettyCommandNames
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
   * The array of parameter descriptions.
   */
  private String [ ] parameterDescriptions ;


  /**
   * The child parameter count.
   */
  private int count = 0 ;


  /**
   * Allocates a new <code>DefaultLatextringBuilder</code> for the
   * <code>printable</code>, where the priority used for the
   * <code>printable</code> is <code>returnPriority</code>.
   * 
   * @param pReturnPriority The priority for the <code>printable</code>,
   *          which determines where and when to add parenthesis around
   *          {@link LatexPrintable}s added to this builder.
   * @param pName The name of this latex command.
   * @param pIndent The indent of this object.
   * @param pParameterDescriptions The array of parameter descriptions.
   * @throws NullPointerException if <code>printable</code> is
   *           <code>null</code>.
   */
  public DefaultLatexStringBuilder ( int pReturnPriority , String pName ,
      int pIndent , String ... pParameterDescriptions )
  {
    this.returnPriority = pReturnPriority ;
    this.indent = pIndent ;
    this.parameterDescriptions = pParameterDescriptions ;
    if ( this.parameterDescriptions.length > 0 )
    {
      this.items.add ( new TextLatexItem ( getIndent ( this.indent )
          + "% " //$NON-NLS-1$
          + this.parameterDescriptions [ this.count ].replaceAll (
              PRETTY_LINE_BREAK , PRETTY_LINE_BREAK + "% " ) ) ) ; //$NON-NLS-1$
      this.count ++ ;
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    }
    this.items
        .add ( new TextLatexItem ( getIndent ( pIndent ) + "\\" + pName ) ) ; //$NON-NLS-1$
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
    if ( this.parameterDescriptions.length > 0 )
    {
      this.items.add ( new TextLatexItem ( getIndent ( this.indent )
          + "% " //$NON-NLS-1$
          + this.parameterDescriptions [ this.count ].replaceAll (
              PRETTY_LINE_BREAK , PRETTY_LINE_BREAK + "% " ) ) ) ; //$NON-NLS-1$
      this.count ++ ;
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    }
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
    if ( this.parameterDescriptions.length > 0 )
    {
      this.items.add ( new TextLatexItem ( getIndent ( this.indent )
          + "% " //$NON-NLS-1$
          + this.parameterDescriptions [ this.count ].replaceAll (
              PRETTY_LINE_BREAK , PRETTY_LINE_BREAK + "% " ) ) ) ; //$NON-NLS-1$
      this.count ++ ;
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    }
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "{" ) ) ; //$NON-NLS-1$
    this.indent = this.indent + LATEX_INDENT ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addBuilderEnd()
   */
  public void addBuilderEnd ( )
  {
    this.indent = this.indent - LATEX_INDENT ;
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "}" ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addBuilderWithoutBrackets(LatexStringBuilder, int)
   */
  public void addBuilderWithoutBrackets (
      LatexStringBuilder pLatexStringBuilder , int pArgumentPriority )
  {
    if ( pLatexStringBuilder == null )
    {
      throw new NullPointerException ( "builder is null" ) ; //$NON-NLS-1$
    }
    DefaultLatexStringBuilder defaultBuilder = ( DefaultLatexStringBuilder ) pLatexStringBuilder ;
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    if ( this.parameterDescriptions.length > 0 )
    {
      this.items.add ( new TextLatexItem ( getIndent ( this.indent )
          + "% " //$NON-NLS-1$
          + this.parameterDescriptions [ this.count ].replaceAll (
              PRETTY_LINE_BREAK , PRETTY_LINE_BREAK + "% " ) ) ) ; //$NON-NLS-1$
      this.count ++ ;
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    }
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
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexStringBuilder#addEmptyBuilder()
   */
  public void addEmptyBuilder ( )
  {
    this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    if ( this.parameterDescriptions.length > 0 )
    {
      this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "%" ) ) ; //$NON-NLS-1$
      this.count ++ ;
      this.items.add ( new TextLatexItem ( LATEX_LINE_BREAK_SOURCE_CODE ) ) ;
    }
    this.items.add ( new TextLatexItem ( getIndent ( this.indent ) + "{}" ) ) ; //$NON-NLS-1$
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
