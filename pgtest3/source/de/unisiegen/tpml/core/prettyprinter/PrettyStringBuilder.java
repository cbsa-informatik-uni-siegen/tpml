package de.unisiegen.tpml.core.prettyprinter ;


/**
 * Base interface to pretty string builders, that can be generated by the
 * factory class
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory}.
 * These builders are used to generate
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyString}s in an incremental
 * fashion.
 * 
 * @author Benedikt Meurer
 * @version $Rev:835 $
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory
 */
public interface PrettyStringBuilder
{
  //
  // Primitives
  //
  /**
   * Appends a break location to the string builder. A break marks the location
   * as possible newline insertion position for the presenter.
   */
  public void addBreak ( ) ;


  /**
   * Inserts the given <code>builder</code> at the specified
   * <code>argumentPriority</code> at the end of our builder. If the return
   * priority of the <code>builder</code> is less than the specified
   * <code>argumentPriority</code>, parenthesis will be added around the text
   * generated for the <code>builder</code>.
   * 
   * @param builder the <code>PrettyStringBuilder</code> to insert.
   * @param argumentPriority the argument priority of the <code>builder</code>.
   * @throws ClassCastException if the implementation of the
   *           <code>builder</code> is different than the implementation of
   *           this builder.
   * @throws NullPointerException if <code>builder</code> is <code>null</code>.
   */
  public void addBuilder ( PrettyStringBuilder builder , int argumentPriority ) ;


  /**
   * Appends the given <code>constant</code> to the pretty string builder.
   * Constants will be highlighted when displayed to the user.
   * 
   * @param constant the constant to append.
   * @throws NullPointerException if <code>constant</code> is
   *           <code>null</code>.
   * @see #addIdentifier(String)
   * @see #addKeyword(String)
   * @see #addText(String)
   * @see PrettyStyle#CONSTANT
   */
  public void addConstant ( String constant ) ;


  /**
   * Appends the given <code>identifier</code> to the pretty string builder.
   * 
   * @param identifier the identifier to append.
   * @throws NullPointerException if <code>identifier</code> is
   *           <code>null</code>.
   * @see #addConstant(String)
   * @see #addKeyword(String)
   * @see #addText(String)
   * @see PrettyStyle#IDENTIFIER
   */
  public void addIdentifier ( String identifier ) ;


  /**
   * Appends the given <code>keyword</code> to the pretty string builder.
   * Keywords will be highlighted when displayed to the user.
   * 
   * @param keyword the keyword to append.
   * @throws NullPointerException if <code>keyword</code> is <code>null</code>.
   * @see #addConstant(String)
   * @see #addIdentifier(String)
   * @see #addText(String)
   * @see PrettyStyle#KEYWORD
   */
  public void addKeyword ( String keyword ) ;


  /**
   * Appends the given <code>text</code> to the pretty string builder. Don't
   * use this method for constants or keywords, but use the
   * {@link #addConstant(String)} and {@link #addKeyword(String)} methods
   * instead.
   * 
   * @param text the text to append.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * @see #addConstant(String)
   * @see #addKeyword(String)
   * @see PrettyStyle#NONE
   */
  public void addText ( String text ) ;


  /**
   * Appends the given <code>type</code> to the pretty string builder. Types
   * will be highlighted when displayed to the user.
   * 
   * @param type the type to append.
   * @throws NullPointerException if <code>type</code> is <code>null</code>.
   * @see #addConstant(String)
   * @see #addKeyword(String)
   * @see PrettyStyle#TYPE
   */
  public void addType ( String type ) ;


  /**
   * Generates a <code>PrettyString</code> from the current contents of the
   * builder. Note that the returned pretty string is not updated when the
   * contents of the builder change at a later time, but simply represents the
   * current content at the time when this method is called.
   * 
   * @return the <code>PrettyString</code> for the current contents of the
   *         builder.
   */
  public PrettyString toPrettyString ( ) ;
}