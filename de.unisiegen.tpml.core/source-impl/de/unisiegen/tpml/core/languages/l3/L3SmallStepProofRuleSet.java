package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Fst;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Snd;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext;

/**
 * Small step proof rules for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet
 */
public class L3SmallStepProofRuleSet extends L2SmallStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3SmallStepProofRuleSet</code> for the specified <code>language</code>,
   * which must be either <tt>L3</tt> or a derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2SmallStepProofRuleSet#L2SmallStepProofRuleSet(L2Language)
   */
  public L3SmallStepProofRuleSet(L3Language language) {
    super(language);
    
    // register the additional rules
    register("FST", true);
    register("PROJ", true);
    register("SND", true);
    register("TUPLE", false);
  }
  
  
  
  //
  // The (BETA-V) rule for MultiLambdas
  //
  
  /**
   * Applies the <code>multiLambda</code> to the <code>tuple</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param multiLambda the {@link MultiLambda}.
   * @param tuple the {@link Tuple}.
   * 
   * @return the resulting expression.
   */
  public Expression applyMultiLambda(SmallStepProofContext context, Application application, MultiLambda multiLambda, Tuple tuple) {
    // determine the expressions and identifiers
    String[] identifiers = multiLambda.getIdentifiers();
    Expression[] expressions = tuple.getExpressions();
    
    // the tuple's arity must match the arity of the multiLambda
    if (expressions.length != identifiers.length) {
      return application;
    }
    
    // perform the substitutions
    Expression e = multiLambda.getE();
    for (int n = 0; n < identifiers.length; ++n) {
      e = e.substitute(identifiers[n], expressions[n]);
    }
    
    // yep, that was (BETA-V) then
    context.addProofStep(getRuleByName("BETA-V"), application);
    
    // and return the new expression
    return e;
  }
  
  
  
  //
  // The (FST) rule
  //
  
  /**
   * Applies the {@link Fst} operator <code>e1</code> to the {@link Tuple} <code>e2</code> using the
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the operator.
   * @param e2 the operand.
   * 
   * @return the resulting expression.
   */
  public Expression applyFst(SmallStepProofContext context, Application application, Fst e1, Tuple e2) {
    try {
      Expression e = e1.applyTo(e2);
      context.addProofStep(getRuleByName("FST"), application);
      return e;
    }
    catch (UnaryOperatorException e) {
      return application;
    }
  }
  
  
  
  //
  // The (LET-EVAL) and (LET-EXEC) rules for MultiLets
  //
  
  /**
   * Evaluates the <code>multiLet</code> expression using the <code>context</code> and returns
   * the resulting expression.
   * 
   * @param context the small step proof context.
   * @param multiLet the {@link MultiLet} expression.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateMultiLet(SmallStepProofContext context, MultiLet multiLet) {
    // determine the identifiers and the sub expressions
    String[] identifiers = multiLet.getIdentifiers();
    Expression e1 = multiLet.getE1();
    Expression e2 = multiLet.getE2();
    
    // check if e1 is not already a value
    if (!e1.isValue()) {
      // we're about to perform (LET-EVAL)
      context.addProofStep(getRuleByName("LET-EVAL"), multiLet);
      
      // try to evaluate e1
      e1 = evaluate(context, e1);
      
      // exceptions need special treatment
      return e1.isException() ? e1 : new MultiLet(identifiers, multiLet.getTau(), e1, e2);
    }

    // arity of the tuple must match
    Expression[] expressions = ((Tuple)e1).getExpressions();
    if (expressions.length != identifiers.length) {
      return multiLet;
    }
      
    // perform the substitutions
    for (int n = 0; n < identifiers.length; ++n) {
      e2 = e2.substitute(identifiers[n], expressions[n]);
    }
      
    // jep, that was (LET-EXEC) then
    context.addProofStep(getRuleByName("LET-EXEC"), multiLet);
      
    // return the new expression
    return e2;
  }
  
  
  
  //
  // The (PROJ) rule
  //
  
  /**
   * Applies the {@link Projection} <code>e1</code> to the {@link Tuple} <code>e2</code> using the
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the operator.
   * @param e2 the operand.
   * 
   * @return the resulting expression.
   */
  public Expression applyProjection(SmallStepProofContext context, Application application, Projection e1, Tuple e2) {
    try {
      Expression e = e1.applyTo(e2);
      context.addProofStep(getRuleByName("PROJ"), application);
      return e;
    }
    catch (UnaryOperatorException e) {
      return application;
    }
  }
  
  
  
  //
  // The (SND) rule
  //
  
  /**
   * Applies the {@link Snd} operator <code>e1</code> to the {@link Tuple} <code>e2</code> using the
   * <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the operator.
   * @param e2 the operand.
   * 
   * @return the resulting expression.
   */
  public Expression applySnd(SmallStepProofContext context, Application application, Snd e1, Tuple e2) {
    try {
      Expression e = e1.applyTo(e2);
      context.addProofStep(getRuleByName("SND"), application);
      return e;
    }
    catch (UnaryOperatorException e) {
      return application;
    }
  }
  
  
  
  //
  // The (TUPLE) rule
  //
  
  /**
   * Evaluates the <code>tuple</code> using the <code>context</code> and returns the resulting
   * expression.
   * 
   * @param context the small step proof context.
   * @param tuple the tuple to evaluate.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateTuple(SmallStepProofContext context, Tuple tuple) {
    // determine the sub expressions
    Expression[] expressions = tuple.getExpressions();
    
    // find the first sub expression that is not already a value
    for (int n = 0; n < expressions.length; ++n) {
      // check if the expression is not already a value
      if (!expressions[n].isValue()) {
        // we're about to perform (TUPLE)
        context.addProofStep(getRuleByName("TUPLE"), tuple);
        
        // try to evaluate the expression
        Expression newExpression = evaluate(context, expressions[n]);
        
        // check if we need to forward an exception
        if (newExpression.isException()) {
          return newExpression;
        }
        
        // otherwise generate a new tuple with the new expression
        Expression[] newExpressions = expressions.clone();
        newExpressions[n] = newExpression;
        return new Tuple(newExpressions);
      }
    }
    
    // hm, can we get stuck here?
    return tuple;
  }
}
