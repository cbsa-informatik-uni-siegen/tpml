package de.unisiegen.tpml.core.bigstep;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.languages.Language;

/**
 * Abstract base class for big step proof rule sets, that are used for the
 * {@link de.unisiegen.tpml.core.bigstep.BigStepProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofRuleSet
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofModel
 */
public abstract class AbstractBigStepProofRuleSet extends AbstractProofRuleSet {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractBigStepProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the {@link Language} to which the big step proof rules in this set belong.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  protected AbstractBigStepProofRuleSet(Language language) {
    super(language);
  }
  
  
  
  //
  // Rule registration
  //

  /**
   * Convenience wrapper for the {@link #register(String, Method, Method)} method, which simply passes
   * <code>null</code> for the <code>updateMethod</code> parameter.
   * 
   * @param name the name of the big step proof rule to create.
   * @param applyMethod the implementation of the apply method for the
   * 
   * @throws NullPointerException if <code>name</code> or <code>applyMethod</code> is <code>null</code>.
   * 
   * @see #register(String, Method, Method)
   */
  protected void register(String name, Method applyMethod) {
    register(name, applyMethod, null);
  }
  
  /**
   * 
   * @param name
   * @param applyMethod
   * @param updateMethod
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
    register(new AbstractBigStepProofRule(name) {
      @Override
      protected void applyInternal(BigStepProofContext context, BigStepProofNode node) throws Exception {
        applyMethod.invoke(AbstractBigStepProofRuleSet.this, context, node);
      }
      @Override
      protected void updateInternal(BigStepProofContext context, BigStepProofNode node) throws Exception {
        if (updateMethod != null) {
          updateMethod.invoke(AbstractBigStepProofRuleSet.this, context, node);
        }
      }
    });
  }
  
  /**
   * Convenience wrapper for the {@link #register(String, Method)} method, which determines the
   * {@link Method} for the specified <code>applyMethodName</code> and uses it for the
   * <code>applyMethod</code>.
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
      return getClass().getDeclaredMethod(methodName, new Class[] { BigStepProofContext.class, BigStepProofNode.class });
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
