package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Small step proof rules for the <code>L2</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet
 */
public class L2SmallStepProofRuleSet extends L1SmallStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L2SmallStepProofRuleSet</code> for the specified <code>language</code>,
   * which must be either <tt>L2</tt> or a derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L1SmallStepProofRuleSet#L1SmallStepProofRuleSet(L1Language)
   */
  public L2SmallStepProofRuleSet(L2Language language) {
    super(language);
    
    // register small step rules
    register(L2Language.L2, "AND-EVAL", false);
    register(L2Language.L2, "AND-FALSE", true);
    register(L2Language.L2, "AND-TRUE", true);
    register(L2Language.L2, "OR-EVAL", false);
    register(L2Language.L2, "OR-FALSE", true);
    register(L2Language.L2, "OR-TRUE", true);
    register(L2Language.L2, "UNFOLD", true);
  }
  
  
  
  //
  // The (AND-EVAL), (AND-FALSE) and (AND-TRUE) rules
  //
  
  /**
   * Evaluates the <code>and</code> expression using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param and the {@link And} expression to evaluate.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateAnd(SmallStepProofContext context, And and) {
    // determine the sub expressions
    Expression e1 = and.getE1();
    Expression e2 = and.getE2();
    
    // check if e1 is not already a boolean constant
    if (!(e1 instanceof BooleanConstant)) {
      // we're about to perform (AND-EVAL)
      context.addProofStep(getRuleByName("AND-EVAL"), and);
      
      // try to evaluate e1
      e1 = evaluate(context, e1);
      
      // exceptions need special handling
      return e1.isException() ? e1 : new And(e1, e2);
    }
    
    // determine the boolean constant value
    if (e1 == BooleanConstant.TRUE) {
      // jep, that's (AND-TRUE) then
      context.addProofStep(getRuleByName("AND-TRUE"), and);
      return e2;
    }
    else {
      // jep, that's (AND-FALSE) then
      context.addProofStep(getRuleByName("AND-FALSE"), and);
      return BooleanConstant.FALSE;
    }
  }
  
  
  
  //
  // The (OR-EVAL), (OR-FALSE) and (OR-TRUE) rules
  //
  
  /**
   * Evaluates the <code>or</code> expression using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param and the {@link And} expression to evaluate.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateOr(SmallStepProofContext context, Or or) {
    // determine the sub expressions
    Expression e1 = or.getE1();
    Expression e2 = or.getE2();
    
    // check if e1 is not already a boolean constant
    if (!(e1 instanceof BooleanConstant)) {
      // we're about to perform (OR-EVAL)
      context.addProofStep(getRuleByName("OR-EVAL"), or);

      // try to evaluate e1
      e1 = evaluate(context, e1);

      // exceptions need special treatment
      return e1.isException() ? e1 : new Or(e1, e2);
    }
    
    // determine the boolean constant value
    if (e1 == BooleanConstant.TRUE) {
      // jep, that's (OR-TRUE) then
      context.addProofStep(getRuleByName("OR-TRUE"), or);
      return BooleanConstant.TRUE;
    }
    else {
      // jep, that's (OR-FALSE) then
      context.addProofStep(getRuleByName("OR-FALSE"), or);
      return e2;
    }
  }
  
  
  
  //
  // The (UNFOLD) rule
  //

  /**
   * Evaluates the curried let expression <code>curriedLet</code> using <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param curriedLet the curried let expression.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateCurriedLet(SmallStepProofContext context, CurriedLet curriedLet) {
    // determine the sub expressions and the identifiers
    String[] identifiers = curriedLet.getIdentifiers();
    Expression e1 = curriedLet.getE1();
    Expression e2 = curriedLet.getE2();
    
    // prepend the lambda abstractions to e1
    for (int n = identifiers.length - 1; n >= 1; --n)
      e1 = new Lambda(identifiers[n], null, e1);
    
    // we can simply perform (LET-EXEC)
    context.addProofStep(getRuleByName("LET-EXEC"), curriedLet);
    
    // and perform the substitution
    return e2.substitute(identifiers[0], e1);
  }

  /**
   * Evaluates the recursive curried let expression <code>curriedLetRec</code> using <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param curriedLetRec the recursive curried let expression.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateCurriedLetRec(SmallStepProofContext context, CurriedLetRec curriedLetRec) {
    // determine the sub expressions and the identifiers
    String[] identifiers = curriedLetRec.getIdentifiers();
    MonoType[] types = curriedLetRec.getTypes();
    Expression e1 = curriedLetRec.getE1();
    Expression e2 = curriedLetRec.getE2();
    
    // prepend the lambda abstractions to e1
    for (int n = identifiers.length - 1; n >= 1; --n) {
      e1 = new Lambda(identifiers[n], types[n], e1);
    }
    
    // we can perform (UNFOLD), which includes a (LET-EVAL)
    context.addProofStep(getRuleByName("LET-EVAL"), curriedLetRec);
    context.addProofStep(getRuleByName("UNFOLD"), curriedLetRec);

    // perform the substitution on e1
    e1 = e1.substitute(identifiers[0], new Recursion(identifiers[0], types[0], e1));
    
    // generate the new (LET) expression
    return new Let(identifiers[0], types[0], e1, e2);
  }
  
  /**
   * Evaluates the recursive let expression <code>letRec</code> using <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param letRec the recursive let expression.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateLetRec(SmallStepProofContext context, LetRec letRec) {
    // determine the expressions and the identifier
    Expression e1 = letRec.getE1();
    Expression e2 = letRec.getE2();
    String id = letRec.getId();
    
    // we perform (UNFOLD), which includes a (LET-EVAL)
    context.addProofStep(getRuleByName("LET-EVAL"), letRec);
    context.addProofStep(getRuleByName("UNFOLD"), letRec);
    
    // perform the substitution on e1
    e1 = e1.substitute(id, new Recursion(id, letRec.getTau(), e1));
    
    // generate the new (LET) expression
    return new Let(id, letRec.getTau(), e1, e2);
  }
  
  /**
   * Evaluates the recursive expression <code>recursion</code> using <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param recursion the recursive expression.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateRecursion(SmallStepProofContext context, Recursion recursion) {
    // determine the expression and the identifier
    Expression e = recursion.getE();
    String id = recursion.getId();
    
    // we can perform (UNFOLD)
    context.addProofStep(getRuleByName("UNFOLD"), recursion);
    
    // perform the substitution
    return e.substitute(id, recursion);
  }
}
