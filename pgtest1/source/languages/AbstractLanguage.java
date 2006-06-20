package languages;

import java.io.Reader;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class AbstractLanguage implements Language {
  //
  // Constructor (protected)
  //

  /**
   * TODO
   */
  protected AbstractLanguage() {
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newParser(java.io.Reader)
   */
  public final LanguageParser newParser(Reader reader) {
    return newParser(newScanner(reader));
  }
}
