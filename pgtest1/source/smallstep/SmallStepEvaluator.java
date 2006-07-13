package smallstep;

import java.lang.reflect.Method;
import java.util.Vector;

import common.ProofStep;
import common.interpreters.MutableStore;
import common.interpreters.Store;

import expressions.And;
import expressions.Application;
import expressions.Assign;
import expressions.BinaryCons;
import expressions.BinaryOperator;
import expressions.BinaryOperatorException;
import expressions.BooleanConstant;
import expressions.Condition;
import expressions.Condition1;
import expressions.CurriedLet;
import expressions.CurriedLetRec;
import expressions.Deref;
import expressions.EmptyList;
import expressions.Exn;
import expressions.Expression;
import expressions.Fst;
import expressions.Hd;
import expressions.InfixOperation;
import expressions.IsEmpty;
import expressions.Lambda;
import expressions.Let;
import expressions.LetRec;
import expressions.List;
import expressions.Location;
import expressions.MultiLambda;
import expressions.MultiLet;
import expressions.Or;
import expressions.Projection;
import expressions.Recursion;
import expressions.Ref;
import expressions.Sequence;
import expressions.Snd;
import expressions.Tl;
import expressions.Tuple;
import expressions.UnaryCons;
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
   * The resulting {@link common.interpreters.Store}.
   * 
   * @see #getStore()
   */
  private Store store;
  

  
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
   * @param store the {@link common.interpreters.Store} to start with.                   
   */
  SmallStepEvaluator(Expression expression, Store store) {
    // create store, remember expression
    this.expression = expression;
    this.store = store;
    
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
    for (int n = 0; n < steps.length; ++n) {
      if (this.expression.isException()) {
        // translate meta rules to the associated EXN rules 
        SmallStepProofRule rule = (SmallStepProofRule)this.steps.elementAt(n).getRule();
        steps[n] = new ProofStep(this.steps.elementAt(n).getExpression(), rule.getExnRule());
      }
      else {
        // just use the proof step
        steps[n] = this.steps.elementAt(n);
      }
    }
    return steps;
  }
  
  /**
   * Returns the resulting {@link Store} for the evaluation.
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
    Expression e1 = and.getE1();
    Expression e2 = and.getE2();
    
    // check if e1 is not already a boolean constant
    if (!(e1 instanceof BooleanConstant)) {
      // we're about to perform (AND-EVAL)
      addProofStep("AND-EVAL", and);
      
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // exceptions need special handling
      return e1.isException() ? e1 : new And(e1, e2);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e1;
    if (booleanConstant.isTrue()) {
      // jep, that's (AND-TRUE) then
      addProofStep("AND-TRUE", and);
      return e2;
    }
    else {
      // jep, that's (AND-FALSE) then
      addProofStep("AND-FALSE", and);
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
      // we're about to perform (APP-LEFT)
      addProofStep("APP-LEFT", application);
      
      // try to evaluate e1
      e1 = evaluate(e1);

      // exceptions need special handling
      return e1.isException() ? e1 : new Application(e1, e2);
    }
    
    // check if e2 is not already a value
    if (!e2.isValue()) {
      // we're about to perform (APP-RIGHT)
      addProofStep("APP-RIGHT", application);
      
      // try to evaluate e2
      e2 = evaluate(e2);
      
      // exceptions need special handling
      return e2.isException() ? e2 : new Application(e1, e2);
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
      // we're about to perform (COND-EVAL)
      addProofStep("COND-EVAL", condition);
      
      // try to evaluate e0
      e0 = evaluate(e0);
      
      // exceptions need special handling
      return e0.isException() ? e0 : new Condition(e0, e1, e2);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e0;
    if (booleanConstant.isTrue()) {
      // jep, that's (COND-TRUE) then
      addProofStep("COND-TRUE", condition);
      return e1;
    }
    else {
      // jep, that's (COND-FALSE) then
      addProofStep("COND-FALSE", condition);
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
      // we're about to perform (COND-1-EVAL)
      addProofStep("COND-1-EVAL", condition1);
      
      // try to evaluate e0
      e0 = evaluate(e0);
      
      // exceptions need special handling
      return e0.isException() ? e0 : new Condition1(e0, e1);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e0;
    if (booleanConstant.isTrue()) {
      // jep, that's (COND-1-TRUE) then
      addProofStep("COND-1-TRUE", condition1);
      return e1;
    }
    else {
      // jep, that's (COND-1-FALSE) then
      addProofStep("COND-1-FALSE", condition1);
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
    for (int n = identifiers.length - 1; n >= 1; --n)
      e1 = new Lambda(identifiers[n], e1);
    
    // we can simply perform (LET-EXEC)
    addProofStep("LET-EXEC", curriedLet);
    
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
    for (int n = identifiers.length - 1; n >= 1; --n)
      e1 = new Lambda(identifiers[n], e1);
    
    // we can perform (UNFOLD), which includes a (LET-EVAL)
    addProofStep("LET-EVAL", curriedLetRec);
    addProofStep("UNFOLD", curriedLetRec);

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
      // we're about to perform (APP-LEFT) and (APP-RIGHT)
      addProofStep("APP-LEFT", infixOperation);
      addProofStep("APP-RIGHT", infixOperation);
      
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // exceptions need special handling
      return e1.isException() ? e1 : new InfixOperation(op, e1, e2);
    }
    
    // check if e2 is not already a value
    if (!e2.isValue()) {
      // we're about to perform (APP-RIGHT)
      addProofStep("APP-RIGHT", infixOperation);
      
      // try to evaluate e2
      e2 = evaluate(e2);
      
      // exceptions need special handling
      return e2.isException() ? e2 : new InfixOperation(op, e1, e2);
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
      // we're about to perform (LET-EVAL)
      addProofStep("LET-EVAL", let);
      
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // exceptions need special treatment
      return e1.isException() ? e1 : new Let(id, e1, e2);
    }

    // we can perform (LET-EXEC)
    addProofStep("LET-EXEC", let);
    
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
      // we're about to perform (LET-EVAL)
      addProofStep("LET-EVAL", multiLet);
      
      // try to evaluate e1
      e1 = evaluate(e1);
      
      // exceptions need special treatment
      return e1.isException() ? e1 : new MultiLet(identifiers, e1, e2);
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
      addProofStep("LET-EXEC", multiLet);
      
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
    addProofStep("LET-EVAL", letRec);
    addProofStep("UNFOLD", letRec);
    
    // perform the substitution on e1
    e1 = e1.substitute(id, new Recursion(id, e1));
    
    // generate the new (LET) expression
    return new Let(id, e1, e2);
  }
 
  @SuppressWarnings("unused")
  private Expression evaluateOr(Or or) {
    // determine the sub expressions
    Expression e1 = or.getE1();
    Expression e2 = or.getE2();
    
    // check if e1 is not already a boolean constant
    if (!(e1 instanceof BooleanConstant)) {
      // we're about to perform (OR-EVAL)
      addProofStep("OR-EVAL", or);

      // try to evaluate e1
      e1 = evaluate(e1);

      // exceptions need special treatment
      return e1.isException() ? e1 : new Or(e1, e2);
    }
    
    // determine the boolean constant value
    BooleanConstant booleanConstant = (BooleanConstant)e1;
    if (booleanConstant.isTrue()) {
      // jep, that's (OR-TRUE) then
      addProofStep("OR-TRUE", or);
      return BooleanConstant.TRUE;
    }
    else {
      // jep, that's (OR-FALSE) then
      addProofStep("OR-FALSE", or);
      return e2;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateRecursion(Recursion recursion) {
    // determine the expression and the identifier
    Expression e = recursion.getE();
    String id = recursion.getId();
    
    // we can perform (UNFOLD)
    addProofStep("UNFOLD", recursion);
    
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
      // we're about to perform (SEQ-EVAL)
      addProofStep("SEQ-EVAL", sequence);
      
      // try to evaluate e1
      e1 = evaluate(e1);

      // exceptions need special treatment
      return e1.isException() ? e1 : new Sequence(e1, e2);
    }

    // we're about to perform (SEQ-EXEC)
    addProofStep("SEQ-EXEC", sequence);
    
    // drop e1 from the sequence
    return e2;
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateList(List list) {
    // determine the sub expressions
    Expression[] expressions = list.getExpressions();
    
    // find the first sub expression that is not already a value
    for (int n = 0; n < expressions.length; ++n) {
      // check if the expression is not already a value
      if (!expressions[n].isValue()) {
        // we're about to perform (LIST)
        addProofStep("LIST", list);
        
        // try to evaluate the expression
        Expression newExpression = evaluate(expressions[n]);
        
        // check if we need to forward an exception
        if (newExpression.isException()) {
          return newExpression;
        }
        
        // otherwise generate a new list with the new expression
        Expression[] newExpressions = expressions.clone();
        newExpressions[n] = newExpression;
        return new List(newExpressions);
      }
    }
    
    // hm, can we get stuck here?
    return list;
  }
  
  @SuppressWarnings("unused")
  private Expression evaluateTuple(Tuple tuple) {
    // determine the sub expressions
    Expression[] expressions = tuple.getExpressions();
    
    // find the first sub expression that is not already a value
    for (int n = 0; n < expressions.length; ++n) {
      // check if the expression is not already a value
      if (!expressions[n].isValue()) {
        // we're about to perform (TUPLE)
        addProofStep("TUPLE", tuple);
        
        // try to evaluate the expression
        Expression newExpression = evaluate(expressions[n]);
        
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
  
  @SuppressWarnings("unused")
  private Expression evaluateWhile(While loop) {
    // determine the sub expressions
    Expression e1 = loop.getE1();
    Expression e2 = loop.getE2();
    
    // we're about to perform (WHILE)
    addProofStep("WHILE", loop);
    
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
    addProofStep("BETA-V", application);
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
      addProofStep("BETA-V", application);
      
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
      addProofStep("DEREF", application);
      
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
    addProofStep("REF", application);
    
    // allocate a new location and store the value
    MutableStore store = new MutableStore(this.store);
    Location location = store.alloc();
    store.put(location, e);
    this.store = store;
    
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
        addProofStep("FST", application);
      }
      else if (operator instanceof Snd) {
        addProofStep("SND", application);
      }
      else if (operator instanceof Projection) {
        addProofStep("PROJ", application);
      }
      else {
        addProofStep("UOP", application);
      }
      
      // and return the new expression
      return e;
    }
    catch (UnaryOperatorException exception) {
      // we're stuck
      return application;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression applyHd(Application application, Hd hd, Expression e) {
    // check if e is the empty list
    if (e == EmptyList.EMPTY_LIST) {
      addProofStep("HD-EMPTY", application);
      return Exn.EMPTY_LIST;
    }
    
    // check if e is a list
    if (e instanceof List) {
      addProofStep("HD", application);
      return ((List)e).head();
    }
    
    // otherwise try to return the first list item
    try {
      // e must be an application of cons to a pair
      Application app1 = (Application)e;
      Tuple tuple = (Tuple)app1.getE2();
      if (!(app1.getE1() instanceof UnaryCons) || tuple.getArity() != 2) {
        // we're stuck
        return application;
      }
      
      // jep, we can perform (HD) then
      addProofStep("HD", application);
      
      // return the first item
      return tuple.getExpressions(0);
    }
    catch(ClassCastException exception) {
      // we're stuck
      return application;
    }
  }

  @SuppressWarnings("unused")
  private Expression applyTl(Application application, Tl tl, Expression e) {
    // check if e is the empty list
    if (e == EmptyList.EMPTY_LIST) {
      addProofStep("TL-EMPTY", application);
      return Exn.EMPTY_LIST;
    }
    
    // check if e is a list
    if (e instanceof List) {
      addProofStep("TL", application);
      return ((List)e).tail();
    }
    
    // otherwise try to return the remaining list
    try {
      // e must be an application of cons to a pair
      Application app1 = (Application)e;
      Tuple tuple = (Tuple)app1.getE2();
      if (!(app1.getE1() instanceof UnaryCons) || tuple.getArity() != 2) {
        // we're stuck
        return application;
      }
      
      // jep, we can perform (TL) then
      addProofStep("TL", application);
      
      // return the remaining list
      return tuple.getExpressions(1);
    }
    catch(ClassCastException exception) {
      // we're stuck
      return application;
    }
  }
  
  @SuppressWarnings("unused")
  private Expression applyIsEmpty(Application application, IsEmpty isEmpty, Expression e) {
    // check if e is the empty list, or an application of cons to a value, or a list
    if (e == EmptyList.EMPTY_LIST) {
      addProofStep("IS-EMPTY-TRUE", application);
      return BooleanConstant.TRUE;
    }
    else if ((e instanceof List)
        || (e instanceof Application
         && ((Application)e).getE1() instanceof UnaryCons
         && ((Application)e).getE2().isValue())) {
      addProofStep("IS-EMPTY-FALSE", application);
      return BooleanConstant.FALSE;
    }
    else {
      // we're stuck
      return application;
    }
  }

  
  
  //
  // Helper methods
  //
  
  /**
   * Adds a new proof step with the specified <code>ruleName</code>
   * for the given <code>expression</code>.
   * 
   * @param ruleName the name of the {@link SmallStepProofRule} to add as proof step.
   * @param expression the {@link Expression} for the proof step.
   */
  private void addProofStep(String ruleName, Expression expression) {
    this.steps.add(new ProofStep(expression, SmallStepProofRule.getRule(ruleName)));
  }
  
  private Expression handleBinaryOperator(Expression applicationOrInfix, BinaryOperator op, Expression e1, Expression e2) {
    try {
      // check if we have (ASSIGN), (BOP) or (CONS)
      if (op instanceof Assign) {
        // we can perform (ASSIGN) now
        addProofStep("ASSIGN", applicationOrInfix);
        
        // change the value at the memory location
        MutableStore store = new MutableStore(this.store);
        store.put((Location)e1, e2);
        this.store = store;
        
        // return nothing
        return UnitConstant.UNIT;
      }
      else {
        // try to perform the operation
        Expression e = op.applyTo(e1, e2);
        
        // yep, that was (BOP) or (CONS) then
        addProofStep((op instanceof BinaryCons) ? "CONS" : "BOP", applicationOrInfix);
        
        // return the new expression
        return e;
      }
    }
    catch (BinaryOperatorException exception) {
      // cannot apply binary operator, we're stuck
      return applicationOrInfix;
    }
  }
  
  private Method lookupMethod(String baseName, Class clazz) throws NoSuchMethodException {
    // try for this class and all super classes up to Expression
    for (; clazz != Expression.class; clazz = clazz.getSuperclass()) {
      // try to find a suitable method
      Method[] methods = getClass().getDeclaredMethods();
      for (Method method : methods) {
        if (method.getName().equals(baseName + clazz.getSimpleName()))
          return method;
      }
    }
    throw new NoSuchMethodException(baseName);
  }
}
