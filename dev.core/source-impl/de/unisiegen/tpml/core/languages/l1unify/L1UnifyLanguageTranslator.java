package de.unisiegen.tpml.core.languages.l1unify;


import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator;
import de.unisiegen.tpml.core.languages.l0.L0LanguageTranslator;
import de.unisiegen.tpml.core.languages.l1.L1Language;


/**
 * Language translator for the <code>L1</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see AbstractLanguageTranslator
 * @see L1Language
 * @see L1UnifyLanguageTranslator
 */
public class L1UnifyLanguageTranslator extends L0LanguageTranslator
{

  /**
   * Allocates a new <code>L1LanguageTranslator</code>.
   */
  public L1UnifyLanguageTranslator ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax ( final Expression expression,
      @SuppressWarnings ( "unused" )
      final boolean recursive )
  {
    return expression;
  }
}
