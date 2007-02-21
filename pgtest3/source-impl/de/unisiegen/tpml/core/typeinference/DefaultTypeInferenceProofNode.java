
package de.unisiegen.tpml.core.typeinference;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractExpressionProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * @author benjamin
 *
 */
public class DefaultTypeInferenceProofNode extends AbstractExpressionProofNode implements TypeCheckerProofNode, TypeInferenceProofNode {
	
	  /**
	   * The type environment for this type inference proof node.
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
	  
	  private TypeEquationList equations = TypeEquationList.EMPTY_LIST;
	  
	  private boolean tmpChild=true;
	  private TypeEnvironment tmpEnvironment;
	  private Expression tmpExpression;
	  private MonoType tmpType;
	  
	  private boolean checked = false;
	  
	  
	  
	 
	  public DefaultTypeInferenceProofNode(TypeEnvironment environment, Expression expression, MonoType type) {
		    super(expression);
		    setEnvironment(environment);
		    setType(type);
		  }
	  
	  public DefaultTypeInferenceProofNode(TypeEnvironment environment, Expression expression, MonoType type, TypeEquationList pEquations) {
		    super(expression);
		    setEnvironment(environment);
		    setType(type);
		    equations=pEquations;
		  }

	void setType(MonoType pType) {
		this.type=pType;
		
	}

	void setEnvironment(TypeEnvironment pEnvironment) {
		this.environment=pEnvironment;
		
	}

	
	public TypeEnvironment getEnvironment() {
		
		return environment;
	}

	


	
	public MonoType getType() {
		
		return type;
	}

	
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

	  public TypeCheckerProofRule getRule() {
		    ProofStep[] steps = getSteps();
		    if (steps.length > 0) {
		      return (TypeCheckerProofRule)steps[0].getRule();
		    }
		    else {
		      return null;
		    }
		  }
	  
	  public void addEquation(MonoType left, MonoType right) {
		    this.equations = this.equations.extend(left, right);
		  }
	
	  /**
	   * {@inheritDoc}
	   * 
	   * Mainly useful for debugging purposes.
	   * 
	   * @see java.lang.Object#toString()
	   */
	  @Override
	  public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("<html>");
	    
	    builder.append(this.environment);
	    builder.append(" \u22b3 ");
	    builder.append(this.expression);
	    builder.append(" :: ");
	    builder.append(this.type);
	    //builder.append("<br>");
	    builder.append(equations);
	    if (getRule() != null) {
	      builder.append(" (" + getRule() + ")");
	      builder.append("<html>");
	    }
	    return builder.toString();
	  }


	  
	public TypeEquationList getEquations() {
		return this.equations;
	}

	public TypeEnvironment getTmpEnvironment() {
		return this.tmpEnvironment;
	}

	public void setTmpEnvironment(TypeEnvironment tmpEnvironment) {
		this.tmpEnvironment = tmpEnvironment;
	}

	public Expression getTmpExpression() {
		return this.tmpExpression;
	}

	public void setTmpExpression(Expression tmpExpression) {
		this.tmpExpression = tmpExpression;
	}

	public MonoType getTmpType() {
		return this.tmpType;
	}

	public void setTmpType(MonoType tmpType) {
		this.tmpType = tmpType;
	}

	public boolean hasTmpChild() {
		return tmpChild;
	}

	public void setTmpChild(boolean tmpChild) {
		this.tmpChild = tmpChild;
	}

	public boolean isChecked() {
		return this.checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}


}
