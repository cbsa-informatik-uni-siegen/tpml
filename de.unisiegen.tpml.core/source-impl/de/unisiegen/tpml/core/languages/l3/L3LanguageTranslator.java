package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator;
import de.unisiegen.tpml.core.types.UnitType;

/**
 * Language translator for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2Language
 * @see de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l3.L3Language
 */
public class L3LanguageTranslator extends L2LanguageTranslator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3LanguageTranslator</code>.
   */
  public L3LanguageTranslator() {
    super();
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator#translateToCoreSyntax(de.unisiegen.tpml.core.expressions.Expression, boolean)
   */
  @Override
  public Expression translateToCoreSyntax(Expression expression, boolean recursive) {
    if (expression instanceof Condition1) {
      // translate to: if e0 then e1 else ()
      Condition1 condition1 = (Condition1)expression;
      Expression e0 = condition1.getE0();
      Expression e1 = condition1.getE1();
      
      // check if we should recurse
      if (recursive) {
        e0 = translateToCoreSyntax(e0, true);
        e1 = translateToCoreSyntax(e1, true);
      }
      
      // generate the condition
      return new Condition(e0, e1, UnitConstant.UNIT);
    }
    else if (expression instanceof Sequence) {
      // translate to: let u = e1 in e2
      Sequence sequence = (Sequence)expression;
      Expression e1 = sequence.getE1();
      Expression e2 = sequence.getE2();
      
      // check if we should recurse
      if (recursive) {
        e1 = translateToCoreSyntax(e1, true);
        e2 = translateToCoreSyntax(e2, true);
      }
      
      // determine a new unique identifier
      String id = "u";
      while (e1.free().contains(id) || e2.free().contains(id)) {
        id = id + "'";
      }
      
      // generate the let expression
      return new Let(id, e1, e2);
    }
    else if (expression instanceof While) {
      // translate to: rec w:unit.if e1 then e2;w
      While loop = (While)expression;
      Expression e1 = loop.getE1();
      Expression e2 = loop.getE2();
      
      // check if we should recurse
      if (recursive) {
        e1 = translateToCoreSyntax(e1, true);
        e2 = translateToCoreSyntax(e2, true);
      }
      
      // determine a new unique identifier
      String id = "w";
      while (e1.free().contains(id) || e2.free().contains(id)) {
        id = id + "'";
      }
      
      // generate the recursion body
      Expression body = new Condition1(e1, new Sequence(e2, new Identifier(id)));
      
      // check if we should recurse
      if (recursive) {
        body = translateToCoreSyntax(body, true);
      }
      
      // generate the recursion
      return new Recursion(id, UnitType.UNIT, body);
    }
    else {
      return super.translateToCoreSyntax(expression, recursive);
    }
  }
}
