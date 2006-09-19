package de.unisiegen.tpml.core.languages.l0;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.smallstep.AbstractSmallStepProofRuleSet;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext;

/**
 * Small step proof rules for the <b>L0</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.smallstep.AbstractSmallStepProofRuleSet
 */
public class L0SmallStepProofRuleSet extends AbstractSmallStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L0SmallStepProofRuleSet</code> for the specified <code>language</code>,
   * which must be either <tt>L0</tt> or a derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  public L0SmallStepProofRuleSet(L0Language language) {
    super(language);
    
    // register the small step rules
    register("APP-LEFT", false);
    register("APP-RIGHT", false);
    register("BETA-V", true);
  }
  
  
  
  //
  // Application
  //
  
  /**
   * Similar in its nature to the {@link AbstractSmallStepProofRuleSet#evaluate(SmallStepProofContext, Expression)}
   * method, but is used to evaluate applications.
   * 
   * This method determines the class of the <code>e1</code> (i.e. <tt>Lambda</tt> for lambda
   * abstractions) and then looks for a method named <tt>applyClass</tt> (i.e. <tt>applyLambda</tt>
   * for lambda abstractions). The method is passed the <code>context</code>, the <code>application</code>,
   * <code>e1</code> and <code>e2</code>. If no such method exists, a method for the parent class
   * of the <code>application</code> will be search and so on.
   * 
   * If no method is found, the <code>application</code> will be returned and <code>context</code>
   * will not be modified.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param e1 the first expression of the application.
   * @param e2 the second expression of the application.
   * 
   * @return the resulting expression.
   * 
   * @see #evaluateApplication(SmallStepProofContext, Application)
   */
  protected Expression apply(SmallStepProofContext context, Application application, Expression e1, Expression e2) {
    try {
      // determine the specific apply method
      Method method = lookupMethod("apply", e1.getClass());
      
      // invoke the specific apply method
      return (Expression)method.invoke(this, context, application, e1, e2);
    }
    catch (NoSuchMethodException e) {
      // no way to further evaluate the application
      return application;
    }
    catch (RuntimeException e) {
      // rethrow as something is really completely broken
      throw e;
    }
    catch (Exception e) {
      // rethrow as runtime exception, sombody b0rked it
      throw new RuntimeException(e);
    }
  }
  
  
  
  //
  // The (APP-LEFT) and (APP-RIGHT) rules
  //
  
  /**
   * Evaluates the <code>application</code> using the <code>context</code>.
   * 
   * @param context the small step proof context.
   * @param application the application to evaluate.
   * 
   * @return the resulting expression.
   */
  public Expression evaluateApplication(SmallStepProofContext context, Application application) {
    // determine the sub expressions
    Expression e1 = application.getE1();
    Expression e2 = application.getE2();

    // check if e1 is not already a value
    if (!e1.isValue()) {
      // we're about to perform (APP-LEFT)
      context.addProofStep(getRuleByName("APP-LEFT"), application);
      
      // try to evaluate e1
      e1 = evaluate(context, e1);

      // exceptions need special handling
      return e1.isException() ? e1 : new Application(e1, e2);
    }
    
    // check if e2 is not already a value
    if (!e2.isValue()) {
      // we're about to perform (APP-RIGHT)
      context.addProofStep(getRuleByName("APP-RIGHT"), application);
      
      // try to evaluate e2
      e2 = evaluate(context, e2);
      
      // exceptions need special handling
      return e2.isException() ? e2 : new Application(e1, e2);
    }

    // perform the application
    return apply(context, application, e1, e2);
  }
  
  
  
  //
  // The (BETA-V) rule
  //
  
  /**
   * Applies the <b>(BETA-V)</b> rule to the <code>application</code>.
   * 
   * @param context the small step proof context.
   * @param application the application.
   * @param lambda the first operand of the <code>application</code>.
   * @param v the second operand of the <code>application</code>.
   * 
   * @return the resulting expression.
   */
  public Expression applyLambda(SmallStepProofContext context, Application application, Lambda lambda, Expression v) {
    context.addProofStep(getRuleByName("BETA-V"), application);
    return lambda.getE().substitute(lambda.getId(), v);
  }
}
