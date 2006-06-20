package languages;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class NoSuchLanguageException extends Exception {
  //
  // Constants
  //
  
  /**
   * The unique serial version id of this class.
   */
  private static final long serialVersionUID = 8717390857258643844L;

  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>NoSuchLanguageException</code>, which
   * indicates that a lookup on a language with the specified
   * <code>id</code> (using the {@link LanguageFactory}) failed.
   * 
   * @param id the id of the language that could not be found.
   * @param e the exception leading to the error.
   */
  NoSuchLanguageException(String id, Throwable e) {
    super("No such language \"" + id + "\"", e);
  }
}
