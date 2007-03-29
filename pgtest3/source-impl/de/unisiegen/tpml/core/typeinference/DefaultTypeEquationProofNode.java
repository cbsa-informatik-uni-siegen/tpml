/**
 * TODO
 */
package de.unisiegen.tpml.core.typeinference;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeEquationProofNode extends DefaultTypeCheckerProofNode implements TypeCheckerProofNode {

	//
	// Attributes
	//
	
	private TypeSubstitutionList substitutions = TypeSubstitutionList.EMPTY_LIST;
	
	
	private TypeEquationList equations;
	
	//
	// Constructors
	//
	
	public DefaultTypeEquationProofNode(TypeEnvironment environment, Expression expression, MonoType type, TypeEquationList eqns) {
		super(environment, expression, type);
		equations = eqns;
	}

	public TypeEquationList getEquations() {
		return this.equations;
	}

	public void setEquations(TypeEquationList equations) {
		this.equations = equations;
	}
	
	public void addSubstitution(DefaultTypeSubstitution s1)
	{
		substitutions.extend(s1);
	}

	

}
