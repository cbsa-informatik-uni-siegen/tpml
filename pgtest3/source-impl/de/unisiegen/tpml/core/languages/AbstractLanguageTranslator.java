package de.unisiegen.tpml.core.languages;

import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Abstract base class for implementations of the {@link de.unisiegen.tpml.core.languages.LanguageTranslator} interface,
 * which provides default implementations of the required methods, that may need to be overwritten by derived classes
 * with concrete implementations.
 *
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * 
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 */
public abstract class AbstractLanguageTranslator implements LanguageTranslator {
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * Note that the default implementation provided by <code>AbstractLanguageTranslator</code> translates the
   * <code>expression</code> via the {@link #translateToCoreSyntax(Expression, boolean)} method and compares the
   * result to the <code>expression</code> using the {@link Object#equals(java.lang.Object)} method. If the two
   * expressions are different, <code>true</code> will be returned, otherwise <code>false</code> will be returned.
   * 
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#containsSyntacticSugar(de.unisiegen.tpml.core.expressions.Expression, boolean)
   */
  public boolean containsSyntacticSugar(Expression expression, boolean recursive) {
    return !translateToCoreSyntax(expression, recursive).equals(expression);
  }

  /**
   * {@inheritDoc}
   *
   * Note that the default implementation provided by the <code>AbstractLanguageTranslator</code> simply returns
   * the <code>expression</code> passed to it, after testing <code>expression</code> for <code>null</code>. So
   * derived classes will likely need to overwrite atleast this method to provide a usable implementation of the
   * translator interface.
   *  
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#translateToCoreSyntax(de.unisiegen.tpml.core.expressions.Expression, boolean)
   */
  public Expression translateToCoreSyntax(Expression expression, boolean recursive) {
    if (expression == null) {
      throw new NullPointerException("expression is null");
    }
    return expression;
  }
}
