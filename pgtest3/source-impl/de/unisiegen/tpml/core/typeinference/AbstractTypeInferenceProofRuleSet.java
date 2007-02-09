/**
 * 
 */
package de.unisiegen.tpml.core.typeinference;

import java.lang.reflect.Method;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.languages.Language;


/**
 * @author benjamin
 *
 */
public abstract class AbstractTypeInferenceProofRuleSet extends
		AbstractProofRuleSet {

	protected AbstractTypeInferenceProofRuleSet(Language language) {
		super(language);
		
	}
	
	  protected void register(int group, String name, Method applyMethod) {
		    register(group, name, applyMethod, null);
		  }
		  

	  
	  protected void register(int group, String name, final Method applyMethod, final Method updateMethod) {
		    if (name == null) {
		      throw new NullPointerException("name is null");
		    }
		    if (applyMethod == null) {
		      throw new NullPointerException("applyMethod is null");
		    }
		    
		    // register a new proof rule with the name and methods
		    register(new AbstractTypeInferenceProofRule(group, name) {
		      @Override
		      protected void applyInternal(TypeInferenceProofContext context, TypeInferenceProofNode node) throws Exception {
		        applyMethod.invoke(AbstractTypeInferenceProofRuleSet.this, context, node);
		      }
		      @Override
		      protected void updateInternal(TypeInferenceProofContext context, TypeInferenceProofNode node) throws Exception {
		        if (updateMethod != null) {
		          updateMethod.invoke(AbstractTypeInferenceProofRuleSet.this, context, node);
		        }
		      }
		    });
		  }
	  
	  
	  protected void registerByMethodName(int group, String name, String applyMethodName) {
		    register(group, name, getMethodByName(applyMethodName));
		  }
	  
	  
	  protected void registerByMethodName(int group, String name, String applyMethodName, String updateMethodName) {
		    register(group, name, getMethodByName(applyMethodName), getMethodByName(updateMethodName));
		  }
		  
	  
	
	  
	  private Method getMethodByName(String methodName) {
		    if (methodName == null) {
		      throw new NullPointerException("methodName is null");
		    }
		    try {
		      // lookup the method with the parameters BigStepProofContext and BigStepProofNode
		      return getClass().getMethod(methodName, new Class[] { TypeInferenceProofContext.class, TypeInferenceProofNode.class });
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
	  
	  protected void unregister(String name) {
		    unregister(getRuleByName(name));
		  }
		  
	  
}
