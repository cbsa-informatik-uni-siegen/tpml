package de.unisiegen.tpml.core.languages;

import java.io.Reader;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;

/**
 * Abstract base class for classes that implement the {@link de.unisiegen.tpml.core.languages.Language}
 * interface.
 *
 * @author Benedikt Meurer
 * @version $Rev$
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
   * @see de.unisiegen.tpml.core.languages.Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public TypeCheckerProofModel newTypeCheckerProofModel(Expression expression) {
    throw new UnsupportedOperationException("The language " + getName() + " does not include a type system");
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newParser(java.io.Reader)
   */
  public final LanguageParser newParser(Reader reader) {
    return newParser(newScanner(reader));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  public LanguageTypeParser newTypeParser(LanguageTypeScanner scanner) {
    throw new UnsupportedOperationException("The language " + getName() + " does not include a type system");
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newTypeParser(java.io.Reader)
   */
  public final LanguageTypeParser newTypeParser(Reader reader) {
    return newTypeParser(newTypeScanner(reader));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newTypeScanner(java.io.Reader)
   */
  public LanguageTypeScanner newTypeScanner(Reader reader) {
    throw new UnsupportedOperationException("The language " + getName() + " does not include a type system");
  }
}
