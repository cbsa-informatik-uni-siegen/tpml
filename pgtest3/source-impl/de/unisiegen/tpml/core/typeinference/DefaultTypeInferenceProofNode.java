
package de.unisiegen.tpml.core.typeinference;

import java.util.LinkedList;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
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
	// Constants
	//
	
	private LinkedList<TypeFormula> formula= new LinkedList<TypeFormula>();
	
	private TypeEquationList equations;
	
	private ProofStep[] steps= new ProofStep[0];
	
	private TypeSubstitutionList substitutions; // = TypeSubstitutionList.EMPTY_LIST;
	
	
	
	//
	// Constructors
	//
	public DefaultTypeInferenceProofNode(TypeJudgement judgement, TypeEquationList eqns, TypeSubstitutionList subs){
		equations=eqns;
		formula.add(judgement);
		//formula.add(list);
		substitutions = subs;
		
	}
	
	public DefaultTypeInferenceProofNode(LinkedList<TypeFormula> judgement, TypeEquationList eqns, TypeSubstitutionList subs){
		equations=eqns;
		formula=judgement;
		//formula.add(list);
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
		      if (!((DefaultTypeInferenceProofNode)getChildAt(n)).isFinished()) {
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
