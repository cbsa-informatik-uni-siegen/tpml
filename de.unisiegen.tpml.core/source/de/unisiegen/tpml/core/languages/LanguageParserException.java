package de.unisiegen.tpml.core.languages;

/**
 * This exception is thrown whenever a parse error occurs in the
 * {@link languages.LanguageParser}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 */
public final class LanguageParserException extends RuntimeException {
  //
  // Constants
  //
  
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = -5267613216858473920L;

  
  
  //
  // Attributes
  //
  
  /**
   * The left character offset of the parse error.
   * 
   * @see #getLeft()
   */
  private int left;
  
  /**
   * The right character offset of the parse error.
   * 
   * @see #getRight()
   */
  private int right;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>LanguageParserException</code> with the specified
   * parameters.
   * 
   * @param message the error message.
   * @param left the left character offset.
   * @param right the right character offset.
   * 
   * @see #getLeft()
   * @see #getRight()
   */
  public LanguageParserException(String message, int left, int right) {
    super(message);
    this.left = left;
    this.right = right;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the left character offset of the error.
   * 
   * @return the left character offset of the error.
   */
  public int getLeft() {
    return this.left;
  }
  
  /**
   * Returns the right character offset of the error.
   * 
   * @return the right character offset of the error.
   */
  public int getRight() {
    return this.right;
  }
}
