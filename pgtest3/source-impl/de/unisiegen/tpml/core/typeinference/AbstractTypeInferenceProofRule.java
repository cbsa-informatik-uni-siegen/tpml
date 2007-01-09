/**
 * 
 */
package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.AbstractProofRule;
import de.unisiegen.tpml.core.ProofRuleException;


/**
 * @author benjamin
 *
 */
public abstract class AbstractTypeInferenceProofRule extends AbstractProofRule
		implements TypeInferenceProofRule {

	AbstractTypeInferenceProofRule(int group, String name) {
		super(group, name);
		
	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofRule#apply(de.unisiegen.tpml.core.typeinference.TypeInferenceProofContext, de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode)
	 */
	public void apply(TypeInferenceProofContext context,
			TypeInferenceProofNode node) throws ProofRuleException {
		 if (node == null) {
		      throw new NullPointerException("node is null");
		    }
		    if (context == null) {
		      throw new NullPointerException("context is null");
		    }
		    try {
		      applyInternal(context, node);
		    }
		    catch (ProofRuleException e) {
		      throw e;
		    }
		    catch (Exception e) {
		      // check if e contains a usable error message
		      for (Throwable t = e; t != null; t = t.getCause()) {
		        if (t instanceof IllegalArgumentException) {
		          throw new ProofRuleException(t.getMessage(), node, this, e);
		        }
		      }
		      throw new ProofRuleException(node, this, e);
		    }

	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofRule#update(de.unisiegen.tpml.core.typeinference.TypeInferenceProofContext, de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode)
	 */
	public void update(TypeInferenceProofContext context,
			TypeInferenceProofNode node) {
		 if (node == null) {
		      throw new NullPointerException("node is null");
		    }
		    if (context == null) {
		      throw new NullPointerException("context is null");
		    }
		    try {
		      updateInternal(context, node);
		    }
		    catch (RuntimeException e) {
		      throw e;
		    }
		    catch (Exception e) {
		      throw new RuntimeException(e);
		    }

	}
	
	  //
	  // Abstract methods
	  //
	  
	
	  protected abstract void applyInternal(TypeInferenceProofContext context, TypeInferenceProofNode node) throws Exception;
	  

	  protected abstract void updateInternal(TypeInferenceProofContext context, TypeInferenceProofNode node) throws Exception;

}
