package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator;

/**
 * Language translator for the <code>L1</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.AbstractLanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 */
public class L1LanguageTranslator extends AbstractLanguageTranslator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L1LanguageTranslator</code>.
   */
  public L1LanguageTranslator() {
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
    if (expression instanceof InfixOperation) {
      // determine the sub expressions
      InfixOperation infixOperation = (InfixOperation)expression;
      Expression op = infixOperation.getOp();
      Expression e1 = infixOperation.getE1();
      Expression e2 = infixOperation.getE2();
      
      // check if we should recurse
      if (recursive) {
        op = translateToCoreSyntax(op, true);
        e1 = translateToCoreSyntax(e1, true);
        e2 = translateToCoreSyntax(e2, true);
      }
      
      // generate the applications
      return new Application(new Application(op, e1), e2);
    }
    else {
      // dunno, let the parent class handle it
      return super.translateToCoreSyntax(expression, recursive);
    }
  }
}
