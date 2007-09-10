package de.unisiegen.tpml.core.latex ;


/**
 * Base interface to latex string builders, that can be generated by the factory
 * class {@link LatexStringBuilderFactory}. These builders are used to generate
 * {@link LatexString}s in an incremental fashion.
 * 
 * @author Christian Fehler
 * @see LatexString
 * @see LatexStringBuilderFactory
 */
public interface LatexStringBuilder extends LatexCommandNames
{
  /**
   * Appends a break location to the string builder. A break marks the location
   * as possible newline insertion position for the latex export.
   */
  public void addBreak ( ) ;


  /**
   * Inserts the given <code>pLatexStringBuilder</code> at the specified
   * <code>pArgumentPriority</code> at the end of our builder with the needed
   * brackets. If the return priority of the <code>pLatexStringBuilder</code>
   * is less than the specified <code>pArgumentPriority</code>, parenthesis
   * will be added around the text generated for the
   * <code>pLatexStringBuilder</code>.
   * 
   * @param pLatexStringBuilder The <code>LatexStringBuilder</code> to insert.
   * @param pArgumentPriority The argument priority of the
   *          <code>pLatexStringBuilder</code>.
   * @throws ClassCastException If the implementation of the
   *           <code>pLatexStringBuilder</code> is different than the
   *           implementation of this builder.
   * @throws NullPointerException If <code>builder</code> is <code>null</code>.
   */
  public void addBuilder ( LatexStringBuilder pLatexStringBuilder ,
      int pArgumentPriority ) ;


  /**
   * Inserts the begin of a builder.
   */
  public void addBuilderBegin ( ) ;


  /**
   * Inserts the end of a builder.
   */
  public void addBuilderEnd ( ) ;


  /**
   * Inserts the given <code>pLatexStringBuilder</code> at the specified
   * <code>pArgumentPriority</code> at the end of our builder without any
   * brackets. If the return priority of the <code>pLatexStringBuilder</code>
   * is less than the specified <code>pArgumentPriority</code>, parenthesis
   * will be added around the text generated for the
   * <code>pLatexStringBuilder</code>.
   * 
   * @param pLatexStringBuilder The <code>LatexStringBuilder</code> to insert.
   * @param pArgumentPriority The argument priority of the
   *          <code>pLatexStringBuilder</code>.
   * @throws ClassCastException If the implementation of the
   *           <code>pLatexStringBuilder</code> is different than the
   *           implementation of this builder.
   * @throws NullPointerException If <code>builder</code> is <code>null</code>.
   */
  public void addBuilderWithoutBrackets (
      LatexStringBuilder pLatexStringBuilder , int pArgumentPriority ) ;


  /**
   * Appends the given <code>pText</code> as a comment to the latex string
   * builder.
   * 
   * @param pText The text to append.
   * @throws NullPointerException If <code>pText</code> is <code>null</code>.
   */
  public void addComment ( String pText ) ;


  /**
   * Inserts an empty builder at the end of our builder. This is used, if an
   * optinal argument is not used.
   */
  public void addEmptyBuilder ( ) ;


  /**
   * Adds a break to the source code.
   * 
   * @param pIndentOffset The offset to the indent of this builder.
   */
  public void addSourceCodeBreak ( int pIndentOffset ) ;


  /**
   * Appends the given <code>pText</code> to the latex string builder.
   * 
   * @param pText The text to append.
   * @throws NullPointerException If <code>pText</code> is <code>null</code>.
   */
  public void addText ( String pText ) ;


  /**
   * Generates a <code>LatexString</code> from the current contents of the
   * builder. Note that the returned latex string is not updated when the
   * contents of the builder change at a later time, but simply represents the
   * current content at the time when this method is called.
   * 
   * @return The <code>LatexString</code> for the current contents of the
   *         builder.
   */
  public LatexString toLatexString ( ) ;
}
