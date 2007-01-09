/**
 * 
 */
package de.unisiegen.tpml.core.typeinference;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractExpressionProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * @author benjamin
 *
 */
public final class DefaultTypeInferenceProofNode extends
		AbstractExpressionProofNode implements TypeInferenceProofNode {
	
	  /**
	   * The type environment for this type checker proof node.
	   * 
	   * @see #getEnvironment()
	   * @see #setEnvironment(TypeEnvironment)
	   */
	private TypeEnvironment environment;
	  
	  /**
	   * The type for this type node, which is either a type variable or a monorphic type.
	   * 
	   * @see #getType()
	   * @see #setType(MonoType)
	   */
	  private MonoType type;

	protected DefaultTypeInferenceProofNode(Expression expression) {
		super(expression);
	    setEnvironment(environment);
	    setType(type);
	}

	void setType(MonoType pType) {
		this.type=pType;
		
	}

	void setEnvironment(TypeEnvironment pEnvironment) {
		this.environment=pEnvironment;
		
	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getEnvironment()
	 */
	public TypeEnvironment getEnvironment() {
		
		return environment;
	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getRule()
	 */
	public TypeInferenceProofRule getRule() {
		 ProofStep[] steps = getSteps();
		    if (steps.length > 0) {
		      return (TypeInferenceProofRule)steps[0].getRule();
		    }
		    else {
		      return null;
		    }
	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getType()
	 */
	public MonoType getType() {
		
		return type;
	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#isFinished()
	 */
	public boolean isFinished() {
		if (!isProven()) {
		      return false;
		    }
		    for (int n = 0; n < getChildCount(); ++n) {
		      if (!getChildAt(n).isFinished()) {
		        return false;
		      }
		    }
		    return true;
	}

	/* (non-Javadoc)
	 * @see de.unisiegen.tpml.core.ProofNode#isProven()
	 */
	public boolean isProven() {
		return (getSteps().length > 0);
	}
	
	 /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getRoot() {
	    return (DefaultTypeInferenceProofNode)super.getRoot();
	  }
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstLeaf()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getFirstLeaf() {
	    return (DefaultTypeInferenceProofNode)super.getFirstLeaf();
	  }
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getLastLeaf() {
	    return (DefaultTypeInferenceProofNode)super.getLastLeaf();
	  }
	
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getParent() {
	    return (DefaultTypeInferenceProofNode)super.getParent();
	  }
	  
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getChildAt(int childIndex) {
	    return (DefaultTypeInferenceProofNode)super.getChildAt(childIndex);
	  }
	  
	  //
	  // Child Queries
	  //
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstChild()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getFirstChild() {
	    return (DefaultTypeInferenceProofNode)super.getFirstChild();
	  }
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getLastChild() {
	    return (DefaultTypeInferenceProofNode)super.getLastChild();
	  }
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getChildAfter(TreeNode aChild) {
	    return (DefaultTypeInferenceProofNode)super.getChildAfter(aChild);
	  }
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getChildBefore(TreeNode aChild) {
	    return (DefaultTypeInferenceProofNode)super.getChildBefore(aChild);
	  }
}
