package de.unisiegen.tpml.core.languages;

import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Base interface for translators that are used to translate expressions to core syntax for a
 * given language.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.languages.Language
 */
public interface LanguageTranslator {
  //
  // Primitives
  //
  
  /**
   * This is a convenience wrapper for the {@link #translateToCoreSyntax(Expression, boolean)} method,
   * where <code>false</code> is passed for the <code>recursive</code>.
   * 
   * @param expression the {@link Expression} to translate to core syntax.
   * 
   * @return <code>expression</code> translated to core syntax or the <code>expression</code> if it
   *         is already in core syntax.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   */
  public Expression translateToCoreSyntax(Expression expression);
  
  /**
   * Translates the <code>expression</code> to core syntax according to the language for which this
   * translator was created. if <code>recursive</code> is <code>true</code> the sub expressions of
   * <code>expression</code> will also be translated, otherwise only the <code>expression</code> itself
   * will be translated and the sub expression will be left untouched.
   * 
   * If the <code>expression</code> does not contain any syntactic sugar, the <code>expression</code>
   * itself will be returned.
   * 
   * @param expression the {@link Expression} to translate to core syntax.
   * @param recursive whether to recursively translate <code>expression</code> to core syntax.
   * 
   * @return <code>expression</code> translated to core syntax or the <code>expression</code> if it
   *         is already in core syntax or does not contain syntactic sugar.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   */
  public Expression translateToCoreSyntax(Expression expression, boolean recursive);
}
