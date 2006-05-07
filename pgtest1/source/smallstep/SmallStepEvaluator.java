package smallstep;

import java.lang.reflect.Method;
import java.util.Vector;

import common.ProofStep;

import expressions.Abstraction;
import expressions.And;
import expressions.Application;
import expressions.AppliedOperator;
import expressions.BooleanConstant;
import expressions.Condition;
import expressions.Constant;
import expressions.Expression;
import expressions.InfixOperation;
import expressions.IntegerConstant;
import expressions.Let;
import expressions.LetRec;
import expressions.Operator;
import expressions.Or;
import expressions.Recursion;

/**
 * Evaluator for expression using the small step
 * semantics.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class SmallStepEvaluator {
  /**
   * The resulting {@link Expression}.
   * 
   * @see #getExpression()
   */
  private Expression expression;
  
  /**
   * The steps in the proof.
   * 
   * @see #getSteps()
   */
  private Vector<ProofStep> steps = new Vector<ProofStep>();
  

  
  //
  // Constructor
  //
  
  /**
   * Allocates a new {@link SmallStepEvaluator} used to
   * determine the next evaluation step for the given
   * <code>expression</code>, including the necessary
   * {@link SmallStepProofRule}s.
   * 
   * @param expression the {@link Expression} for which
   *                   to determine the next evaluation
   *                   step in the proof.
   */
  SmallStepEvaluator(Expression expression) {
    this.expression = evaluate(expression);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the expression as result of the
   * evaluation.
   * 
   * @return the resulting expression.
   */
  public Expression getExpression() {
    return this.expression;
  }
  
  /**
   * Returns the {@link ProofStep}s that were
   * performed during the evaluation.
   * 
   * @return the evaluation steps.
   */
  public ProofStep[] getSteps() {
    // return the steps in reversed order
    ProofStep[] steps = new ProofStep[this.steps.size()];
    for (int n = 0; n < steps.length; ++n)
      steps[n] = this.steps.elementAt(steps.length - (n + 1));
    return steps;
  }
  
  
  
  //
  // Evaluation methods
  //
  
  private Expression evaluate(Expression expression) {
    try {
      Method method = getClass().getMethod("evaluate" + expression.getClass().getSimpleName(), expression.getClass());
      return (Expression)method.invoke(this, expression);
    }
    catch (NoSuchMethodException e) {
      // no way to further evaluate the expression
      return expression;
    }
    catch (Exception e) {
      // rethrow as runtime exception, as some-
      // thing is really completely broken
      throw new RuntimeException(e);
    }
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateAnd(And and) {
    // determine the sub expressions
    Expression e0 = and.getE0();
    Expression e1 = and.getE1();
    
    // check if e0 is not already a boolean constant
    if (!(e0 instanceof BooleanConstant)) {
      // try to evaluate e0
      e0 = evaluate(e0);
      
      // check if e0 is an exception, (AND-EVAL-EXN)
      if (e0.isException()) {
        addProofStep(SmallStepProofRule.AND_EVAL_EXN, and);
        return e0;
      }
      
      // otherwise we performed (AND-EVAL)
      addProofStep(SmallStepProofRule.AND_EVAL, and);
      
      // return the new and
      return new And(e0, e1);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e0;
    if (booleanConstant.isTrue()) {
      // jep, that's (AND-TRUE) then
      addProofStep(SmallStepProofRule.AND_TRUE, and);
      return e1;
    }
    else {
      // jep, that's (AND-FALSE) then
      addProofStep(SmallStepProofRule.AND_FALSE, and);
      return BooleanConstant.FALSE;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateApplication(Application application) {
    // determine the sub expressions
    Expression e1 = application.getE1();
    Expression e2 = application.getE2();

    // check if e1 is not already a value
    if (!e1.isValue()) {
      // try to evaluate e1
      e1 = evaluate(e1);

      // check if e1 is an exception, (APP-LEFT-EXN)
      if (e1.isException()) {
        addProofStep(SmallStepProofRule.APP_LEFT_EXN, application);
        return e1;
      }
      
      // otherwise we performed (APP-LEFT)
      addProofStep(SmallStepProofRule.APP_LEFT, application);
      
      // return the new application
      return new Application(e1, e2);
    }
    
    // check if e2 is not already a value
    if (!e2.isValue()) {
      // try to evaluate e2
      e2 = evaluate(e2);
      
      // check if e2 is an exception, (APP-RIGHT-EXN)
      if (e2.isException()) {
        addProofStep(SmallStepProofRule.APP_RIGHT_EXN, application);
        return e2;
      }
      
      // otherwise we performed (APP-RIGHT)
      addProofStep(SmallStepProofRule.APP_RIGHT, application);
      
      // return the new application
      return new Application(e1, e2);
    }

    // perform the application
    return apply(application, e1, e2);
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateCondition(Condition condition) {
    // determine the sub expression
    Expression e0 = condition.getE0();
    Expression e1 = condition.getE1();
    Expression e2 = condition.getE2();
    
    // check if e0 is not already a boolean constant
    if (!(e0 instanceof BooleanConstant)) {
      // try to evaluate e0
      e0 = evaluate(e0);
      
      // check if e0 is an exception, (COND-EVAL-EXN)
      if (e0.isException()) {
        addProofStep(SmallStepProofRule.COND_EVAL_EXN, condition);
        return e0;
      }
      
      // otherwise we performed (COND-EVAL)
      addProofStep(SmallStepProofRule.COND_EVAL, condition);
      
      // return the new condition
      return new Condition(e0, e1, e2);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e0;
    if (booleanConstant.isTrue()) {
      // jep, that's (COND-TRUE) then
      addProofStep(SmallStepProofRule.COND_TRUE, condition);
      return e1;
    }
    else {
      // jep, that's (COND-FALSE) then
      addProofStep(SmallStepProofRule.COND_FALSE, condition);
      return e2;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateInfixOperation(InfixOperation infixOperation) {
    // determine the sub expressions and the operator
    Expression e1 = infixOperation.getE1();
    Expression e2 = infixOperation.getE2();
    Operator op = infixOperation.getOp();
    
    // check if e1 is not already an integer constant
    if (!(e1 instanceof IntegerConstant)) {
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // check if e1 is an exception, (APP-RIGHT-EXN) and (APP-LEFT-EXN)
      if (e1.isException()) {
        addProofStep(SmallStepProofRule.APP_RIGHT_EXN, infixOperation);
        addProofStep(SmallStepProofRule.APP_LEFT_EXN, infixOperation);
        return e1;
      }
      
      // otherwise we performed (APP-RIGHT) and (APP-LEFT)
      addProofStep(SmallStepProofRule.APP_RIGHT, infixOperation);
      addProofStep(SmallStepProofRule.APP_LEFT, infixOperation);
      
      // return the new infix operation
      return new InfixOperation(op, e1, e2);
    }
    
    // check if e2 is not already an integer constant
    if (!(e2 instanceof IntegerConstant)) {
      // try to evaluate e2
      e2 = evaluate(e2);
      
      // check if e2 is an exception, (APP-RIGHT-EXN)
      if (e2.isException()) {
        addProofStep(SmallStepProofRule.APP_RIGHT, infixOperation);
        return e2;
      }
      
      // otherwise we performed (APP-RIGHT)
      addProofStep(SmallStepProofRule.APP_RIGHT, infixOperation);
      
      // return the new infix operation
      return new InfixOperation(op, e1, e2);
    }
    
    // we can perform (OP) now
    addProofStep(SmallStepProofRule.OP, infixOperation);

    // apply the operator to the constant
    return op.applyTo((Constant)e1, (Constant)e2);
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateLet(Let let) {
    // determine the sub expressions and the identifier
    Expression e1 = let.getE1();
    Expression e2 = let.getE2();
    String id = let.getId();
    
    // check if e1 is not already a value
    if (!e1.isValue()) {
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // check if e1 is an exception, (LET-EVAL-EXN)
      if (e1.isException()) {
        addProofStep(SmallStepProofRule.LET_EVAL_EXN, let);
        return e1;
      }
      
      // otherwise we performed (LET-EVAL)
      addProofStep(SmallStepProofRule.LET_EVAL, let);
      
      // return the new let
      return new Let(id, e1, e2);
    }

    // we can perform (LET-EXEC)
    addProofStep(SmallStepProofRule.LET_EXEC, let);
    
    // and perform the substitution
    return e2.substitute(id, e1);
  }

  @SuppressWarnings("unused")
  private Expression evaluateOr(Or or) {
    // determine the sub expressions
    Expression e0 = or.getE0();
    Expression e1 = or.getE1();
    
    // check if e0 is not already a boolean constant
    if (!(e0 instanceof BooleanConstant)) {
      // try to evaluate e0
      e0 = evaluate(e0);
      
      // check if e0 is an exception, (OR-EVAL-EXN)
      if (e0.isException()) {
        addProofStep(SmallStepProofRule.OR_EVAL_EXN, or);
        return e0;
      }
      
      // otherwise we performed (OR-EVAL)
      addProofStep(SmallStepProofRule.OR_EVAL, or);
      
      // return the new or
      return new Or(e0, e1);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e0;
    if (booleanConstant.isTrue()) {
      // jep, that's (OR-TRUE) then
      addProofStep(SmallStepProofRule.OR_TRUE, or);
      return BooleanConstant.TRUE;
    }
    else {
      // jep, that's (OR-FALSE) then
      addProofStep(SmallStepProofRule.OR_FALSE, or);
      return e1;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateRecursion(Recursion recursion) {
    // determine the expression and the identifier
    Expression e = recursion.getE();
    String id = recursion.getId();
    
    // we can perform (UNFOLD)
    addProofStep(SmallStepProofRule.UNFOLD, recursion);
    
    // perform the substitution
    return e.substitute(id, recursion);
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateLetRec(LetRec letRec) {
    // determine the expressions and the identifier
    Expression e1 = letRec.getE1();
    Expression e2 = letRec.getE2();
    String id = letRec.getId();
    
    // we perform (UNFOLD), which includes a (LET-EVAL)
    addProofStep(SmallStepProofRule.UNFOLD, letRec);
    addProofStep(SmallStepProofRule.LET_EVAL, letRec);
    
    // perform the substitution on e1
    e1 = e1.substitute(id, new Recursion(id, e1));
    
    // generate the new (LET) expression
    return new Let(id, e1, e2);
  }
  
  
  
  //
  // Application methods
  //
  
  private Expression apply(Application application, Expression e1, Expression e2) {
    try {
      Method method = getClass().getMethod("apply" + e1.getClass().getSimpleName(),
                                           application.getClass(), e1.getClass(), e2.getClass());
      return (Expression)method.invoke(this, application, e1, e2);
    }
    catch (NoSuchMethodException e) {
      // no way to further evaluate the expression
      return application;
    }
    catch (Exception e) {
      // rethrow as runtime exception, as some-
      // thing is really completely broken
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unused")
  private Expression applyAbstraction(Application application, Abstraction abstr, Expression v) {
    addProofStep(SmallStepProofRule.BETA_V, application);
    return abstr.substitute(abstr.getId(), v);
  }
  
  @SuppressWarnings("unused")
  private Expression applyAppliedOperator(Application application, AppliedOperator aop, IntegerConstant c) {
    addProofStep(SmallStepProofRule.OP, application);
    return aop.getOperator().applyTo(aop.getConstant(), c);
  }

  
  
  //
  // Helper methods
  //
  
  private void addProofStep(SmallStepProofRule rule, Expression expression) {
    this.steps.add(new ProofStep(expression, rule));
  }
}
