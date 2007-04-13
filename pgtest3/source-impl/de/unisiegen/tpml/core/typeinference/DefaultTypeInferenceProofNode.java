package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;

/**
 * @author benjamin
 *
 */
public class DefaultTypeInferenceProofNode extends AbstractProofNode implements
		TypeInferenceProofNode {

	//
	// Attributes
	//

	/**
	 * list of all formulas of this node
	 */
	private ArrayList<TypeFormula> formula = new ArrayList<TypeFormula>();

	/**
	 * list of the collected type substitutions of this node
	 */
	private ArrayList<TypeSubstitutionList> substitutions = new ArrayList<TypeSubstitutionList>();

	/**
	 * list of proof steps of this node
	 */
	private ProofStep[] steps = new ProofStep[0];

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
	public DefaultTypeInferenceProofNode(final TypeJudgement judgement,
			final ArrayList<TypeSubstitutionList> subs) {

		//equations = eqns;
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
	public DefaultTypeInferenceProofNode(final ArrayList<TypeFormula> judgement,
			final ArrayList<TypeSubstitutionList> subs) {

		//equations = eqns;
		formula = judgement;
		substitutions = subs;
	}



	//
	// Primitives
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.ProofNode#isProven()
	 */
	public boolean isProven() {

		return (getSteps().length > 0);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#isFinished(TypeFormula, int)
	 */
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

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
	 */
	@Override
	public DefaultTypeInferenceProofNode getChildAt(final int childIndex) {

		return (DefaultTypeInferenceProofNode) super.getChildAt(childIndex);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
	 */
	@Override
	public DefaultTypeInferenceProofNode getParent() {

		return (DefaultTypeInferenceProofNode) super.getParent();
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

		return (DefaultTypeInferenceProofNode) super.getRoot();
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

		return (DefaultTypeInferenceProofNode) super.getFirstChild();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
	 */
	@Override
	public DefaultTypeInferenceProofNode getLastChild() {

		return (DefaultTypeInferenceProofNode) super.getLastChild();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
	 */
	@Override
	public DefaultTypeInferenceProofNode getChildAfter(final TreeNode aChild) {

		return (DefaultTypeInferenceProofNode) super.getChildAfter(aChild);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
	 */
	@Override
	public DefaultTypeInferenceProofNode getChildBefore(final TreeNode aChild) {

		return (DefaultTypeInferenceProofNode) super.getChildBefore(aChild);
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

		return (DefaultTypeInferenceProofNode) super.getFirstLeaf();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
	 */
	@Override
	public DefaultTypeInferenceProofNode getLastLeaf() {

		return (DefaultTypeInferenceProofNode) super.getLastLeaf();
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

		final StringBuilder builder = new StringBuilder();
		builder.append("<html>"); //$NON-NLS-1$
		builder.append(substitutions);
		builder.append("<br>"); //$NON-NLS-1$
		for (int i = 0; i < formula.size(); i++) {
			if (i != 0) {
				builder.append("<br>"); //$NON-NLS-1$
			}
			builder.append(formula.get(i));

		}
		if (getRule() != null) {
			builder.append(" (" + getRule() + ")");  //$NON-NLS-1$ //$NON-NLS-2$
		}
		builder.append("</html>"); //$NON-NLS-1$
		return builder.toString();
	}

	//
	// Accessors
	//

	/**
	 * get the proof steps of this node
	 * @return ProofStep[] steps
	 */
	public ProofStep[] getSteps() {

		return this.steps;
	}

	/**
	 * 
	 * set the proof steps of this node
	 *
	 * @param steps new proof steps for this node
	 */
	public void setSteps(final ProofStep[] steps) {

		this.steps = steps;
	}

	/**
	 * 
	 * get the rules applied to this node
	 *
	 * @return ProofStep[] steps or null
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getRule()
	 */
	public TypeCheckerProofRule getRule() {

		final ProofStep[] steps = getSteps();
		if (steps.length > 0) {
			return (TypeCheckerProofRule) steps[0].getRule();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * get the list of type formulas of this node
	 *
	 * @return LinkedList<TypeFormula> formula
	 */
	public ArrayList<TypeFormula> getFormula() {

		return this.formula;
	}

	/**
	 * 
	 * add a new type substitution to the list of substitutions of this node
	 *
	 * @param s1 DefaultTypeSubstitution to add to list
	
	public void addSubstitution(DefaultTypeSubstitution s1) {
		
		substitutions.add(s1);
	} */

	/**
	 * 
	 * get the type substitution list of this node
	 *
	 * @return TypeSubstitutionList substitutions 
	 */
	public ArrayList<TypeSubstitutionList> getSubstitutions() {

		return this.substitutions;
	}
	
	/**
	 * get the first type formula of the type formula list of this node
	 *
	 * @return the first type formula of the list or null if list is empty
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getFirstFormula()
	 */
	public TypeFormula getFirstFormula(){
		if (!formula.isEmpty()) {
			return this.formula.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * get a list of all type formulas of this node
	 *
	 * @return ArraList containing all type formulas
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getAllFormulas()
	 */
	public ArrayList<TypeFormula> getAllFormulas(){
		return this.formula;
	}

	/**
	 * get a list of all type substitutions of this node
	 *
	 * @return ArrayList with all type substitutions of this node
	 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode#getSubstitution()
	 */
	public ArrayList<TypeSubstitutionList> getSubstitution() {
		return this.substitutions;
	}
	
	

}
