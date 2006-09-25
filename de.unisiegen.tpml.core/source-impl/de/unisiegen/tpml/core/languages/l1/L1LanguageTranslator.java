package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.languages.l0.L0LanguageTranslator;

/**
 * Language translator for the <code>L1</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.AbstractLanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 * @see de.unisiegen.tpml.core.languages.l1.L1LanguageTranslator
 */
public class L1LanguageTranslator extends L0LanguageTranslator {
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
    else if (expression instanceof Condition && recursive) {
      // determine the sub expressions
      Condition condition = (Condition)expression;
      Expression e0 = condition.getE0();
      Expression e1 = condition.getE1();
      Expression e2 = condition.getE2();
      
      // translate the sub expressions
      e0 = translateToCoreSyntax(e0, true);
      e1 = translateToCoreSyntax(e1, true);
      e1 = translateToCoreSyntax(e2, true);
      
      // generate the condition
      return new Condition(e0, e1, e2);
    }
    else if (expression instanceof CurriedLet) {
      // translate to: let id1 = lambda id2...lambda idn.e1 in e2
      CurriedLet curriedLet = (CurriedLet)expression;
      Expression e1 = curriedLet.getE1();
      
      // check if we should recurse
      if (recursive) {
        e1 = translateToCoreSyntax(e1, true);
      }
      
      // add the lambdas
      for (int n = curriedLet.getIdentifiers().length - 1; n > 0; --n) {
        e1 = new Lambda(curriedLet.getIdentifiers(n), null, e1);
      }
      
      // generate the let expression
      return new Let(curriedLet.getIdentifiers(0), e1, curriedLet.getE2());
    }
    else {
      // dunno, let the parent class handle it
      return super.translateToCoreSyntax(expression, recursive);
    }
  }
}
