package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1LanguageTranslator;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Language translator for the <code>L2</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 * @see de.unisiegen.tpml.core.languages.l1.L1LanguageTranslator
 */
public class L2LanguageTranslator extends L1LanguageTranslator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L2LanguageTranslator</code>.
   */
  public L2LanguageTranslator() {
    super();
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1LanguageTranslator#translateToCoreSyntax(de.unisiegen.tpml.core.expressions.Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax(Expression expression, boolean recursive) {
    if (expression instanceof CurriedLetRec) {
      // translate to: let id1 = rec id1.lambda id2...lambda idn.e1 in e2
      CurriedLetRec curriedLetRec = (CurriedLetRec)expression;
      MonoType[] types = curriedLetRec.getTypes();
      Expression e1 = curriedLetRec.getE1();
      
      // check if we should recurse
      if (recursive) {
        e1 = translateToCoreSyntax(e1, true);
      }
      
      // add the lambdas
      for (int n = curriedLetRec.getIdentifiers().length - 1; n > 0; --n) {
        e1 = new Lambda(curriedLetRec.getIdentifiers(n), types[n], e1);
      }
      
      // try to generate a recursive type
      MonoType tau = types[0];
      try {
        for (int n = 1; n < types.length; ++n) {
          tau = new ArrowType(types[n], tau);
        }
      }
      catch (NullPointerException e) {
        // no type for the recursion
        tau = null;
      }
      
      // generate the let expression
      return new Let(curriedLetRec.getIdentifiers(0), curriedLetRec.getTypes(0), new Recursion(curriedLetRec.getIdentifiers(0), tau, e1), curriedLetRec.getE2());
    }
    else if (expression instanceof LetRec) {
      // determine the sub expressions
      LetRec letRec = (LetRec)expression;
      Expression e1 = letRec.getE1();
      Expression e2 = letRec.getE2();
      
      // check if we should recurse
      if (recursive) {
        e1 = translateToCoreSyntax(e1, true);
        e2 = translateToCoreSyntax(e2, true);
      }
      
      // generate the let expression
      return new Let(letRec.getId(), letRec.getTau(), new Recursion(letRec.getId(), letRec.getTau(), e1), e2);
    }
    else if (expression instanceof Recursion && recursive) {
      // determine the sub expressions
      Recursion recursion = (Recursion)expression;
      Expression e = recursion.getE();
      
      // translate the sub expression
      e = translateToCoreSyntax(e, true);
      
      // generate the recursion
      return new Recursion(recursion.getId(), recursion.getTau(), e);
    }
    else {
      return super.translateToCoreSyntax(expression, recursive);
    }
  }
}
