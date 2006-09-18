package de.unisiegen.tpml.core.typechecker;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.languages.Language;

/**
 * Abstract base class for type checker proof rule sets.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofRuleSet
 */
public abstract class AbstractTypeCheckerProofRuleSet extends AbstractProofRuleSet {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractTypeCheckerProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the {@link Language} to which the type checker proof rules in this set belong.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  protected AbstractTypeCheckerProofRuleSet(Language language) {
    super(language);
  }
  
  
  
  //
  // Rule registration
  //

  /**
   * Convenience wrapper for the {@link #register(String, Method, Method)} method, which simply passes
   * <code>null</code> for the <code>updateMethod</code> parameter.
   * 
   * The rule is prepended to the list, which is important for guessing, as
   * the last registered proof rule will be used first when guessing.
   * 
   * @param name the name of the type checker proof rule to create.
   * @param applyMethod the implementation of the apply method for the type checker.
   * 
   * @throws NullPointerException if <code>name</code> or <code>applyMethod</code> is <code>null</code>.
   * 
   * @see #register(String, Method, Method)
   */
  protected void register(String name, Method applyMethod) {
    register(name, applyMethod, null);
  }
  
  /**
   * Registers the rule with the given <code>name</code> and the <code>applyMethod</code> and
   * <code>updateMethod</code>. The <code>updateMethod</code> may be <code>null</code>.
   * 
   * The rule is prepended to the list, which is important for guessing, as
   * the last registered proof rule will be used first when guessing.
   * 
   * @param name the name of the rule.
   * @param applyMethod the <code>apply()</code> method.
   * @param updateMethod the <code>update()</code> method or <code>null</code>.
   * 
   * @throws NullPointerException if <code>name</code> or <code>applyMethod</code> is <code>null</code>.
   * 
   * @see #register(String, Method)
   * @see AbstractProofRuleSet#register(AbstractProofRule)
   */
  protected void register(String name, final Method applyMethod, final Method updateMethod) {
    if (name == null) {
      throw new NullPointerException("name is null");
    }
    if (applyMethod == null) {
      throw new NullPointerException("applyMethod is null");
    }
    
    // register a new proof rule with the name and methods
    register(new AbstractTypeCheckerProofRule(name) {
      @Override
      protected void applyInternal(TypeCheckerProofContext context, TypeCheckerProofNode node) throws Exception {
        applyMethod.invoke(AbstractTypeCheckerProofRuleSet.this, context, node);
      }
      @Override
      protected void updateInternal(TypeCheckerProofContext context, TypeCheckerProofNode node) throws Exception {
        if (updateMethod != null) {
          updateMethod.invoke(AbstractTypeCheckerProofRuleSet.this, context, node);
        }
      }
    });
  }
  
  /**
   * Convenience wrapper for the {@link #register(String, Method)} method, which determines the
   * {@link Method} for the specified <code>applyMethodName</code> and uses it for the
   * <code>applyMethod</code>.
   * 
   * The rule is prepended to the list, which is important for guessing, as
   * the last registered proof rule will be used first when guessing. 
   * 
   * @param name the name of the rule.
   * @param applyMethodName the name of the <code>apply</code> method.
   * 
   * @throws NullPointerException if either <code>name</code> or <code>applyMethodName</code>
   *                              is <code>null</code>.
   * 
   * @see #register(String, Method)
   */
  protected void registerByMethodName(String name, String applyMethodName) {
    register(name, getMethodByName(applyMethodName));
  }
  
  /**
   * Convenience wrapper for the {@link #register(String, Method, Method)} method, which determines
   * the {@link Method}s for the specified <code>applyMethodName</code> and <code>updateMethodName</code>
   * and uses them for the {@link Method} parameters.
   * 
   * @param name the name of the rule.
   * @param applyMethodName the name of the <code>apply</code> method.
   * @param updateMethodName the name of the <code>update</code> method.
   * 
   * @throws NullPointerException if either <code>name</code>, <code>applyMethodName</code> or
   *                              <code>updateMethodName</code> is <code>null</code>.
   * 
   * @see #register(String, Method, Method)
   */
  protected void registerByMethodName(String name, String applyMethodName, String updateMethodName) {
    register(name, getMethodByName(applyMethodName), getMethodByName(updateMethodName));
  }
  
  /**
   * Returns the {@link Method} with the specified <code>methodName</code>, which has two parameters,
   * a <code>BigStepProofContext</code> and a <code>BigStepProofNode</code>.
   * 
   * @param methodName the name of the method to look up.
   * 
   * @return the {@link Method} with the specified <code>methodName</code>.
   * 
   * @throws NullPointerException if <code>methodName</code> is <code>null</code>.
   * 
   * @see #registerByMethodName(String, String)
   * @see #registerByMethodName(String, String, String)
   */
  private Method getMethodByName(String methodName) {
    if (methodName == null) {
      throw new NullPointerException("methodName is null");
    }
    try {
      // lookup the method with the parameters BigStepProofContext and BigStepProofNode
      return getClass().getMethod(methodName, new Class[] { TypeCheckerProofContext.class, TypeCheckerProofNode.class });
    }
    catch (RuntimeException e) {
      // just re-throw the exception
      throw e;
    }
    catch (Exception e) {
      // translate the exception to a runtime exception
      throw new RuntimeException("Method " + methodName + " not found", e);
    }
  }
}
