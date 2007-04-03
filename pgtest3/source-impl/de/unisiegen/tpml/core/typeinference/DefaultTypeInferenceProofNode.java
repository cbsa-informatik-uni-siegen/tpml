
package de.unisiegen.tpml.core.typeinference;

import java.util.LinkedList;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.util.Environment;

/**
 * @author benjamin
 *
 */
public class DefaultTypeInferenceProofNode extends AbstractProofNode implements TypeInferenceProofNode {

	//
	// Attributes
	//
	
	/**
	 * list of all formulas of this node
	 */
	private LinkedList<TypeFormula> formula= new LinkedList<TypeFormula>();
	
	/**
	 * list of the collected type equations of this node
	 */
	private TypeEquationList equations;
	
	/**
	 * list of the collected type substitutions of this node
	 */
	private TypeSubstitutionList substitutions; // = TypeSubstitutionList.EMPTY_LIST;
	
	/**
	 * list of proof steps of this node
	 */
	private ProofStep[] steps= new ProofStep[0];
	
	
	
	//
	// Constructors
	//
	/**
	 * Allocates a new <code>DefaultTypeEquationProofNode</code> 
	 * 
	 * @param judgement type judgement of the node which will be added to formula
	 * @param eqns type equations of the node
	 * @param subs substitutions of the node
	 * 
	 */
	public DefaultTypeInferenceProofNode(TypeJudgement judgement, TypeEquationList eqns, TypeSubstitutionList subs){
		equations=eqns;
		formula.add(judgement);
		substitutions = subs;
		
	}
	
	/**
	 * Allocates a new <code>DefaultTypeEquationProofNode</code> 
	 * 
	 * @param judgement type formulas of the node
	 * @param eqns eqns type equations of the node
	 * @param subs subs substitutions of the node
	 * 
	 */
	public DefaultTypeInferenceProofNode(LinkedList<TypeFormula> judgement, TypeEquationList eqns, TypeSubstitutionList subs){
		equations=eqns;
		formula=judgement;
		substitutions = subs;
	}

	public boolean isProven() {
		return (getSteps().length > 0);
	}

	  public boolean isFinished() {
		    if (!isProven()) {
		      return false;
		    }
		    for (int n = 0; n < getChildCount(); ++n) {
		      if (!(getChildAt(n)).isFinished()) {
		        return false;
		      }
		    }
		    return true;
		  }

	
	//
	// Accessors
	//
	
	public ProofStep[] getSteps() {
		return this.steps;
	}



	public void setSteps(ProofStep[] steps) {
		this.steps = steps;
	}

	public TypeEquationList getEquations() {
		return this.equations;
	}

	public void setEquations(TypeEquationList equations) {
		this.equations = equations;
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

	  
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getChildAt(int childIndex) {
	    return (DefaultTypeInferenceProofNode)super.getChildAt(childIndex);
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
	  
	  
	  
	  //
	  // Tree Queries
	  //
	  
	  /**
	   * {@inheritDoc}
	   *
	   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
	   */
	  @Override
	  public DefaultTypeInferenceProofNode getRoot() {
	    return (DefaultTypeInferenceProofNode)super.getRoot();
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
	  
	  
	  
	  //
	  // Leaf Queries
	  //
	  
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
	  
	  
	
	  //
	  // Base methods
	  //
	  
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
	    builder.append(substitutions);
	    builder.append("<br>");
	    if (this.parent!=null)
	    	builder.append("=    ");
	    builder.append("solve");
	    for (int i=0; i< formula.size(); i++)
	    {
	    	if (i!=0)
	    		builder.append("<br>");
	    	builder.append(formula.get(i));
	    	
	    }
	    if (getRule() != null) {
	        builder.append(" (" + getRule() + ")");
	      }
	    builder.append("</html>");
	    return builder.toString();
	  }



	public LinkedList<TypeFormula> getFormula() {
		return this.formula;
	}
	
	public void addSubstitution(DefaultTypeSubstitution s1)
	{
		substitutions.extend(s1);
	}

	public TypeSubstitutionList getSubstitutions() {
		return this.substitutions;
	}

}
