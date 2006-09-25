package de.unisiegen.tpml.core.languages.l0;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator;

/**
 * Language translator for the <code>L0</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.AbstractLanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 */
public class L0LanguageTranslator extends AbstractLanguageTranslator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L0LanguageTranslator</code> instance.
   */
  public L0LanguageTranslator() {
    super();
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.AbstractLanguageTranslator#translateToCoreSyntax(de.unisiegen.tpml.core.expressions.Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax(Expression expression, boolean recursive) {
    if (expression instanceof Application && recursive) {
      Application application = (Application)expression;
      return new Application(translateToCoreSyntax(application.getE1(), true), translateToCoreSyntax(application.getE2(), true));
    }
    else if (expression instanceof Lambda && recursive) {
      Lambda lambda = (Lambda)expression;
      return new Lambda(lambda.getId(), lambda.getTau(), translateToCoreSyntax(lambda.getE(), true));
    }
    else {
      return super.translateToCoreSyntax(expression, recursive);
    }
  }
}
