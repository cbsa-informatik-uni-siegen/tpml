package languages;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class LanguageScannerException extends Exception {
  //
  // Constants
  //
  
  /**
   * The unique serialization identifier of the class.
   */
  private static final long serialVersionUID = 5806884058872318979L;

  
  
  //
  // Attributes
  //
  
  /**
   * The left character position of the error.
   * 
   * @see #getLeft()
   */
  private int left;
  
  /**
   * The right character position of the error.
   * 
   * @see #getRight()
   */
  private int right;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>LanguageScannerException</code> with the
   * specified <code>left</code> and <code>right</code> character
   * offsets, and the specified error <code>message</code>.
   * 
   * @param left the left character offset in the input source stream.
   * @param right the right character offset in the input source stream.
   * @param message the error message.
   * 
   * @see #getLeft()
   * @see #getRight()
   */
  public LanguageScannerException(int left, int right, String message) {
    super(message);
    this.left = left;
    this.right = right;
  }
  
  /**
   * Extended variant of the {@link #LanguageScannerException(int, int, String)}
   * constructor, which allows the caller to also specify an exception <code>e</code>
   * that explains the cause of the error.
   * 
   * @param left the left character offset in the input source stream.
   * @param right the right character offset in the input source stream.
   * @param message the error message.
   * @param e the cause of the exception.
   */
  public LanguageScannerException(int left, int right, String message, Exception e) {
    super(message, e);
    this.left = left;
    this.right = right;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the left character offset of the error in the source input
   * stream.
   * 
   * @return the left character offset in the source input stream.
   */
  public int getLeft() {
    return this.left;
  }
  
  /**
   * Returns the right character offset of the error in the source input
   * stream.
   * 
   * @return the right character offset in the source input stream.
   */
  public int getRight() {
    return this.right;
  }
}
