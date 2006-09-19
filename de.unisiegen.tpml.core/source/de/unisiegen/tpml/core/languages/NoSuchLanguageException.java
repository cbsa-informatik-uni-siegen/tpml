package de.unisiegen.tpml.core.languages;

/**
 * Thrown if the {@link de.unisiegen.tpml.core.languages.LanguageFactory} cannot find the
 * {@link de.unisiegen.tpml.core.languages.Language} implementation for a given language
 * id.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageFactory
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
