package de.unisiegen.tpml.core.languages;


import de.unisiegen.tpml.core.expressions.Expression;


/**
 * Base interface for translators that are used to translate expressions to core
 * syntax for a given language. They are also used to test whether a specified
 * expression is syntactic sugar for a given language.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.languages.Language
 */
public interface LanguageTranslator
{

  //
  // Primitives
  //
  /**
   * Tests whether the <code>expression</code> contains syntactic sugar
   * according to the language for which this translator was created. If
   * <code>recursive</code> is <code>true</code>, this method will return
   * <code>true</code> if either <code>expression</code> itself is syntactic
   * sugar or one of its sub expressions is syntactic sugar. Otherwise if
   * <code>recursive</code> is <code>false</code> the test is only performed
   * on the <code>expression</code> itself.
   * 
   * @param expression the {@link Expression} to test for syntactic sugar.
   * @param recursive whether to recursively check sub expressions of the
   *          <code>expression</code> as well.
   * @return <code>true</code> if the <code>expression</code> is syntactic
   *         sugar or, if <code>recursive</code> is <code>true</code>, any
   *         of its sub expressions is syntactic sugar.
   * @throws NullPointerException if <code>expression</code> is
   *           <code>null</code>.
   * @see #translateToCoreSyntax(Expression, boolean)
   */
  public boolean containsSyntacticSugar ( Expression expression,
      boolean recursive );


  /**
   * Translates the <code>expression</code> to core syntax according to the
   * language for which this translator was created. if <code>recursive</code>
   * is <code>true</code> the sub expressions of <code>expression</code>
   * will also be translated, otherwise only the <code>expression</code>
   * itself will be translated and the sub expression will be left untouched. If
   * the <code>expression</code> does not contain any syntactic sugar, the
   * <code>expression</code> itself will be returned.
   * 
   * @param expression the {@link Expression} to translate to core syntax.
   * @param recursive whether to recursively translate <code>expression</code>
   *          to core syntax.
   * @return <code>expression</code> translated to core syntax or the
   *         <code>expression</code> if it is already in core syntax or does
   *         not contain syntactic sugar.
   * @throws NullPointerException if <code>expression</code> is
   *           <code>null</code>.
   * @see #containsSyntacticSugar(Expression, boolean)
   */
  public Expression translateToCoreSyntax ( Expression expression,
      boolean recursive );
}
