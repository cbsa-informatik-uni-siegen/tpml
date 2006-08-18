package de.unisiegen.tpml.core.languages;

import java.io.Reader;

/**
 * Abstract base class for classes that implement the {@link de.unisiegen.tpml.core.languages.Language}
 * interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.languages.Language
 */
public abstract class AbstractLanguage implements Language {
  //
  // Constructor (protected)
  //

  /**
   * Allocates a new <code>AbstractLanguage</code>.
   */
  protected AbstractLanguage() {
    // nothing to do here.
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
