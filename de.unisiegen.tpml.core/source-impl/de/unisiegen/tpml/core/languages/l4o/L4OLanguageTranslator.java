package de.unisiegen.tpml.core.languages.l4o;


import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator;
import de.unisiegen.tpml.core.languages.l3.L3Language;
import de.unisiegen.tpml.core.languages.l3.L3LanguageTranslator;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.languages.l4.L4LanguageTranslator;


/**
 * Language translator for the <code>L4</code> language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see L3Language
 * @see L3LanguageTranslator
 * @see L4Language
 */
public class L4OLanguageTranslator extends L4LanguageTranslator
{

  /**
   * Allocates a new <code>L4OLanguageTranslator</code>.
   */
  public L4OLanguageTranslator ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see L2LanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax ( final Expression expression,
      final boolean recursive )
  {
    return super.translateToCoreSyntax ( expression, recursive );
  }
}
