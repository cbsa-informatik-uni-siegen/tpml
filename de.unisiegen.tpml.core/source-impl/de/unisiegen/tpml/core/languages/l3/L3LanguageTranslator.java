package de.unisiegen.tpml.core.languages.l3;

import java.util.Arrays;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.EmptyList;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Fst;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.List;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Snd;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.UnaryCons;
import de.unisiegen.tpml.core.languages.l2.L2LanguageTranslator;

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
    if (expression instanceof Fst) {
      Fst fst = (Fst)expression;
      return new Projection(fst.getArity(), fst.getIndex());
    }
    else if (expression instanceof List) {
      List list = (List)expression;
      Expression[] expressions = list.getExpressions();
      expression = EmptyList.EMPTY_LIST;
      for (int n = expressions.length - 1; n >= 0; --n) {
        Expression e = expressions[n];
        if (recursive) {
          e = translateToCoreSyntax(e, true);
        }
        expression = new Application(UnaryCons.CONS, new Tuple(new Expression[] { e, expression }));
      }
      return expression;
    }
    else if (expression instanceof MultiLambda) {
      // determine the MultiLambda components
      MultiLambda multiLambda = (MultiLambda)expression;
      String[] identifiers = multiLambda.getIdentifiers();
      Expression e = multiLambda.getE();
      
      // check if we should recurse
      if (recursive) {
        e = translateToCoreSyntax(e, true);
      }
      
      // generate a new unique identifier to be used for the tuple parameter
      String id = "id";
      while (e.free().contains(id) || Arrays.asList(identifiers).contains(id)) {
        id = id + "'";
      }
      
      // generate the required let's
      for (int n = identifiers.length - 1; n >= 0; --n) {
        e = new Let(identifiers[n], null, new Application(new Projection(identifiers.length, n + 1), new Identifier(id)), e);
      }
      
      // and return the new lambda expression
      return new Lambda(id, multiLambda.getTau(), e);
    }
    else if (expression instanceof MultiLet) {
      // determine the MultiLet components
      MultiLet multiLet = (MultiLet)expression;
      String[] identifiers = multiLet.getIdentifiers();
      Expression e1 = multiLet.getE1();
      Expression e2 = multiLet.getE2();
      
      // check if we should recurse
      if (recursive) {
        e1 = translateToCoreSyntax(e1, true);
        e2 = translateToCoreSyntax(e2, true);
      }
      
      // generate a new unique identifier to be used for the tuple parameter
      String id = "id";
      while (e1.free().contains(id) || e2.free().contains(id) || Arrays.asList(identifiers).contains(id)) {
        id = id + "'";
      }
      
      // generate the required let's
      for (int n = identifiers.length - 1; n >= 0; --n) {
        e2 = new Let(identifiers[n], null, new Application(new Projection(identifiers.length, n + 1), new Identifier(id)), e2);
      }
      
      // and return the new let expression
      return new Let(id, multiLet.getTau(), e1, e2);
    }
    else if (expression instanceof Snd) {
      Snd snd = (Snd)expression;
      return new Projection(snd.getArity(), snd.getIndex());
    }
    else if (expression instanceof Tuple && recursive) {
      Expression[] oldExpressions = ((Tuple)expression).getExpressions();
      Expression[] newExpressions = new Expression[oldExpressions.length];
      for (int n = 0; n < oldExpressions.length; ++n) {
        newExpressions[n] = translateToCoreSyntax(oldExpressions[n], true);
      }
      return new Tuple(newExpressions);
    }
    else {
      return super.translateToCoreSyntax(expression, recursive);
    }
  }
}
