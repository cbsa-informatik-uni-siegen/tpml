package de.unisiegen.tpml.core.smallstep;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 */
public abstract class AbstractSmallStepProofRuleSet extends AbstractProofRuleSet {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractSmallStepProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the {@link Language} to which the small step proof rules in this set belong.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  protected AbstractSmallStepProofRuleSet(Language language) {
    super(language);
  }
  
  
  
  //
  // Rule registration
  //
  
  /**
   * Convenience wrapper for the {@link AbstractProofRuleSet#register(AbstractProofRule)} method,
   * which registers a new small step rule with the given <code>name</code>.
   * 
   * @param name the name of the new small step rule.
   * @param axiom whether the new small step rule is an axiom.
   * 
   * @see SmallStepProofRule#isAxiom()
   */
  protected void register(String name, boolean axiom) {
    register(new DefaultSmallStepProofRule(name, axiom));
  }
  
  
  
  //
  // Evaluation
  //

  /**
   * Evaluates the next small steps for the <code>expression</code> and returns the resulting
   * {@link Expression}. The resulting {@link Store} is available from the <code>context</code>
   * and the {@link de.unisiegen.tpml.core.ProofStep}s are added to the <code>context</code>
   * as well.
   * 
   * This method determines the class of the <code>expression</code> (i.e. <tt>Application</tt>
   * for applications) and then looks for a method named <tt>evaluateClass</tt> (i.e.
   * <tt>evaluateApplication</tt> for applications). The method is passed the <code>context</code>
   * and the <code>expression</code>. If no such method exists, a method for the parent class
   * of the <code>expression</code> will be search and so on.
   * 
   * If no method is found, the <code>expression</code> will be returned and <code>context</code>
   * will not be modified.
   * 
   * @param context the small step proof context.
   * @param expression the expression for which to evaluate the next small step.
   * 
   * @return the resulting expression.
   * 
   * @throws NullPointerException if <code>context</code> or <code>expression</code> is <code>null</code>.
   * 
   * @see #lookupMethod(String, Class)
   */
  public Expression evaluate(SmallStepProofContext context, Expression expression) {
    if (context == null) {
      throw new NullPointerException("context is null");
    }
    if (expression == null) {
      throw new NullPointerException("expression is null");
    }
    
    try {
      // determine the specific evaluate method
      Method method = lookupMethod("evaluate", expression.getClass());
      return (Expression)method.invoke(this, context, expression);
    }
    catch (NoSuchMethodException e) {
      // no way to further evaluate the expression
      return expression;
    }
    catch (RuntimeException e) {
      // rethrow the exception, as something is really completely broken
      throw e;
    }
    catch (Exception e) {
      // rethrow as runtime exception, something is really completely broken
      throw new RuntimeException(e);
    }
  }
  
  /**
   * TODO Add documentation here.
   * 
   * @param baseName
   * @param clazz
   * 
   * @return
   * 
   * @throws NoSuchMethodException
   */
  protected Method lookupMethod(String baseName, Class clazz) throws NoSuchMethodException {
    // try for this class and all super classes up to Expression
    for (; clazz != Expression.class; clazz = clazz.getSuperclass()) {
      // try to find a suitable method
      for (Method method : getClass().getDeclaredMethods()) {
        if (method.getName().equals(baseName + clazz.getSimpleName())) {
          return method;
        }
      }
    }
    throw new NoSuchMethodException(baseName);
  }
}
