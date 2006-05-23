package smallstep;

import java.lang.reflect.Method;
import java.util.Vector;

import common.MutableStore;
import common.ProofStep;
import common.Store;

import expressions.Lambda;
import expressions.And;
import expressions.Application;
import expressions.Assign;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.BooleanConstant;
import expressions.Condition;
import expressions.Condition1;
import expressions.CurriedLet;
import expressions.CurriedLetRec;
import expressions.Deref;
import expressions.Expression;
import expressions.Fst;
import expressions.InfixOperation;
import expressions.Let;
import expressions.LetRec;
import expressions.Location;
import expressions.MultiLambda;
import expressions.MultiLet;
import expressions.Or;
import expressions.Projection;
import expressions.Recursion;
import expressions.Ref;
import expressions.Sequence;
import expressions.Snd;
import expressions.Tuple;
import expressions.UnaryOperator;
import expressions.UnaryOperatorException;
import expressions.UnitConstant;
import expressions.While;

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
  
  /**
   * The resulting {@link common.Store}.
   * 
   * @see #getStore()
   */
  private MutableStore store;
  

  
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
   * @param store the {@link common.Store} to start with.                   
   */
  SmallStepEvaluator(Expression expression, Store store) {
    // create store, remember expression
    this.expression = expression;
    this.store = new MutableStore(store);
    
    // evaluate expression
    this.expression = evaluate(this.expression);
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
  
  /**
   * Returns the resulting {@link Store} for
   * the evaluation.
   * 
   * @return the resulting store.
   */
  public Store getStore() {
    return this.store;
  }
  
  
  
  //
  // Evaluation methods
  //
  
  private Expression evaluate(Expression expression) {
    try {
      // determine the specific evaluate method
      Method method = lookupMethod("evaluate", expression.getClass());
      
      // try to invoke the method
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
  private Expression evaluateCondition1(Condition1 condition1) {
    // determine the sub expression
    Expression e0 = condition1.getE0();
    Expression e1 = condition1.getE1();
    
    // check if e0 is not already a boolean constant
    if (!(e0 instanceof BooleanConstant)) {
      // try to evaluate e0
      e0 = evaluate(e0);
      
      // check if e0 is an exception, (COND-1-EVAL-EXN)
      if (e0.isException()) {
        addProofStep(SmallStepProofRule.COND_1_EVAL_EXN, condition1);
        return e0;
      }
      
      // otherwise we performed (COND-1-EVAL)
      addProofStep(SmallStepProofRule.COND_1_EVAL, condition1);
      
      // return the new condition1
      return new Condition1(e0, e1);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e0;
    if (booleanConstant.isTrue()) {
      // jep, that's (COND-1-TRUE) then
      addProofStep(SmallStepProofRule.COND_1_TRUE, condition1);
      return e1;
    }
    else {
      // jep, that's (COND-1-FALSE) then
      addProofStep(SmallStepProofRule.COND_1_FALSE, condition1);
      return UnitConstant.UNIT;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateCurriedLet(CurriedLet curriedLet) {
    // determine the sub expressions and the identifiers
    String[] identifiers = curriedLet.getIdentifiers();
    Expression e1 = curriedLet.getE1();
    Expression e2 = curriedLet.getE2();
    
    // prepend the lambda abstractions to e1
    for (int n = 1; n < identifiers.length; ++n)
      e1 = new Lambda(identifiers[n], e1);
    
    // we can simply perform (LET-EXEC)
    addProofStep(SmallStepProofRule.LET_EXEC, curriedLet);
    
    // and perform the substitution
    return e2.substitute(identifiers[0], e1);
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateCurriedLetRec(CurriedLetRec curriedLetRec) {
    // determine the sub expressions and the identifiers
    String[] identifiers = curriedLetRec.getIdentifiers();
    Expression e1 = curriedLetRec.getE1();
    Expression e2 = curriedLetRec.getE2();
    
    // prepend the lambda abstractions to e1
    for (int n = 1; n < identifiers.length; ++n)
      e1 = new Lambda(identifiers[n], e1);
    
    // we can perform (UNFOLD), which includes a (LET-EVAL)
    addProofStep(SmallStepProofRule.UNFOLD, curriedLetRec);
    addProofStep(SmallStepProofRule.LET_EVAL, curriedLetRec);

    // perform the substitution on e1
    e1 = e1.substitute(identifiers[0], new Recursion(identifiers[0], e1));
    
    // generate the new (LET) expression
    return new Let(identifiers[0], e1, e2);
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateInfixOperation(InfixOperation infixOperation) {
    // determine the sub expressions and the operator
    Expression e1 = infixOperation.getE1();
    Expression e2 = infixOperation.getE2();
    BinaryOperator op = infixOperation.getOp();
    
    // check if e1 is not already an integer constant
    if (!e1.isValue()) {
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
    
    // check if e2 is not already a value
    if (!e2.isValue()) {
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
    
    // try to perform the application
    return handleBinaryOperator(infixOperation, op, e1, e2);
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
  private Expression evaluateMultiLet(MultiLet multiLet) {
    // determine the identifiers and the sub expressions
    String[] identifiers = multiLet.getIdentifiers();
    Expression e1 = multiLet.getE1();
    Expression e2 = multiLet.getE2();
    
    // check if e1 is not already a value
    if (!e1.isValue()) {
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // check if e1 is an exception, (LET-EVAL-EXN)
      if (e1.isException()) {
        addProofStep(SmallStepProofRule.LET_EVAL_EXN, multiLet);
        return e1;
      }
      
      // otherwise we performed (LET-EVAL)
      addProofStep(SmallStepProofRule.LET_EVAL, multiLet);
      
      // return the new multi let
      return new MultiLet(identifiers, e1, e2);
    }

    // try to perform the (LET-EXEC)
    try {
      // arity of the tuple must match
      Expression[] expressions = ((Tuple)e1).getExpressions();
      if (expressions.length != identifiers.length)
        return multiLet;
      
      // perform the substitutions
      for (int n = 0; n < identifiers.length; ++n) {
        e2 = e2.substitute(identifiers[n], expressions[n]);
      }
      
      // jep, that was (LET-EXEC) then
      addProofStep(SmallStepProofRule.LET_EXEC, multiLet);
      
      // return the new expression
      return e2;
    }
    catch (ClassCastException exception) {
      // e1 is not a tuple
      return multiLet;
    }
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
  private Expression evaluateSequence(Sequence sequence) {
    // determine the sub expressions
    Expression e1 = sequence.getE1();
    Expression e2 = sequence.getE2();

    // check if e1 is not already a value
    if (!e1.isValue()) {
      // try to evaluate e1
      e1 = evaluate(e1);

      // check if e1 is an exception, (SEQ-EVAL-EXN)
      if (e1.isException()) {
        addProofStep(SmallStepProofRule.SEQ_EVAL_EXN, sequence);
        return e1;
      }
      
      // otherwise we performed (SEQ-EVAL)
      addProofStep(SmallStepProofRule.SEQ_EVAL, sequence);
      
      // return the new sequence
      return new Sequence(e1, e2);
    }

    // we're about to perform (SEQ-EXEC)
    addProofStep(SmallStepProofRule.SEQ_EXEC, sequence);
    
    // drop e1 from the sequence
    return e2;
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateTuple(Tuple tuple) {
    // determine the sub expressions
    Expression[] expressions = tuple.getExpressions();
    
    // find the first sub expression that is not already a value
    for (int n = 0; n < expressions.length; ++n) {
      // check if the expression is not already a value
      if (!expressions[n].isValue()) {
        // try to evaluate the expression
        Expression newExpression = evaluate(expressions[n]);
        
        // check if we need to forward an exception
        if (newExpression.isException()) {
          addProofStep(SmallStepProofRule.TUPLE_EXN, tuple);
          return newExpression;
        }
        
        // we performed (TUPLE) then
        addProofStep(SmallStepProofRule.TUPLE, tuple);
        
        // otherwise generate a new tuple with the new expression
        Expression[] newExpressions = tuple.getExpressions().clone();
        newExpressions[n] = newExpression;
        return new Tuple(newExpressions);
      }
    }
    
    // hm, can we get stuck here?
    return tuple;
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateWhile(While loop) {
    // determine the sub expressions
    Expression e1 = loop.getE1();
    Expression e2 = loop.getE2();
    
    // we're about to perform (WHILE)
    addProofStep(SmallStepProofRule.WHILE, loop);
    
    // translate to: if e1 then (e2; while e1 do e2)
    return new Condition1(e1, new Sequence(e2, loop));
  }
  
  
  
  //
  // Application methods
  //
  
  private Expression apply(Application application, Expression e1, Expression e2) {
    try {
      // determine the specific apply method
      Method method = lookupMethod("apply", e1.getClass());
      
      // invoke the specific apply method
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
  private Expression applyLambda(Application application, Lambda lambda, Expression v) {
    addProofStep(SmallStepProofRule.BETA_V, application);
    return lambda.getE().substitute(lambda.getId(), v);
  }
  
  @SuppressWarnings("unused")
  private Expression applyMultiLambda(Application application, MultiLambda multiLambda, Expression v) {
    try {
      // v must be a tuple with the appropriate arity
      Tuple tuple = (Tuple)v;
      if (tuple.getArity() != multiLambda.getArity()) {
        return application;
      }
      
      // perform the substitutions
      String[] identifiers = multiLambda.getIdentifiers();
      Expression[] expressions = tuple.getExpressions();
      Expression e = multiLambda.getE();
      for (int n = 0; n < identifiers.length; ++n) {
        e = e.substitute(identifiers[n], expressions[n]);
      }
      
      // yep, that was (BETA-V) then
      addProofStep(SmallStepProofRule.BETA_V, application);
      
      // and return the new expression
      return e;
    }
    catch (ClassCastException exception) {
      // v is not a tuple
      return application;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression applyApplication(Application application, Application app1, Expression e2) {
    try {
      // try to perform the application
      return handleBinaryOperator(application, (BinaryOperator)app1.getE1(), app1.getE2(), e2);
    }
    catch (ClassCastException exception) {
      // the first application is not what we need
      return application;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression applyDeref(Application application, Deref deref, Location location) {
    try {
      // lookup the expression for the location
      Expression e = this.store.get(location);
      
      // we performed (DEREF)
      addProofStep(SmallStepProofRule.DEREF, application);
      
      // and return the expression
      return e;
    }
    catch (IllegalArgumentException e) {
      // the location is invalid
      return application;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression applyRef(Application application, Ref ref, Expression e) {
    // we're about to perform (REF)
    addProofStep(SmallStepProofRule.REF, application);
    
    // allocate a new location and store the value
    Location location = this.store.alloc();
    this.store.put(location, e);
    
    // return the new location
    return location;
  }
  
  @SuppressWarnings("unused")
  private Expression applyUnaryOperator(Application application, UnaryOperator operator, Expression e) {
    try {
      // try to perform the application
      e = operator.applyTo(e);
      
      // determine the appropriate rule
      if (operator instanceof Fst) {
        addProofStep(SmallStepProofRule.FST, application);
      }
      else if (operator instanceof Snd) {
        addProofStep(SmallStepProofRule.SND, application);
      }
      else if (operator instanceof Projection) {
        addProofStep(SmallStepProofRule.PROJ, application);
      }
      else {
        addProofStep(SmallStepProofRule.UOP, application);
      }
      
      // and return the new expression
      return e;
    }
    catch (UnaryOperatorException exception) {
      // we're stuck
      return application;
    }
  }

  
  
  //
  // Helper methods
  //
  
  private void addProofStep(SmallStepProofRule rule, Expression expression) {
    this.steps.add(new ProofStep(expression, rule));
  }
  
  private Expression handleBinaryOperator(Expression applicationOrInfix, BinaryOperator op, Expression e1, Expression e2) {
    try {
      // check if we have (ASSIGN) or (OP)
      if (op instanceof Assign) {
        // we can perform (ASSIGN) now
        addProofStep(SmallStepProofRule.ASSIGN, applicationOrInfix);
        
        // change the value at the memory location
        this.store.put((Location)e1, e2);
        
        // return nothing
        return UnitConstant.UNIT;
      }
      else {
        // try to perform the operation
        Expression e = op.applyTo(e1, e2);
        
        // yep, that was (BOP) then
        addProofStep(SmallStepProofRule.BOP, applicationOrInfix);
        
        // return the new expression
        return e;
      }
    }
    catch (BinaryOperatorException exception) {
      // cannot apply binary operator, we're stuck
      return applicationOrInfix;
    }
  }
  
  private Method lookupMethod(String baseName, Class klass) throws NoSuchMethodException {
    // try for this class and all super classes up to Expression
    for (; klass != Expression.class; klass = klass.getSuperclass()) {
      // try to find a suitable method
      Method[] methods = getClass().getDeclaredMethods();
      for (Method method : methods) {
        if (method.getName().equals(baseName + klass.getSimpleName()))
          return method;
      }
    }
    throw new NoSuchMethodException(baseName);
  }
}
