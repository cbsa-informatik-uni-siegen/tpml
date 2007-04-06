package de.unisiegen.tpml.core.typeinference;

import java.util.LinkedList;
import org.apache.log4j.Logger;
import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * The heart of the type inference. Type inference proof rules are supplied via an
 * {@link de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet}
 * that is passed to the constructor.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofContext
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode
 */
public final class TypeInferenceProofModel extends AbstractProofModel {

	//
	// Constants
	//
	/**
	 * The {@link Logger} for this class.
	 * 
	 * @see Logger
	 */
	private static final Logger logger = Logger
			.getLogger(TypeInferenceProofModel.class);

	//
	// Attributes
	//

	/**
	 * The current proof index, which indicates the number of steps that
	 * have been performed on the proof model so far (starting with one),
	 * and is used to generate new unique type variables in the associated
	 * contexts.
	 * 
	 * @see #getIndex()
	 * @see TypeCheckerProofContext#newTypeVariable()
	 * @see types.TypeVariable
	 */
	private int index = 1;

	//
	// Constructor
	//

	/**
	 * Allocates a new <code>TypeInferenceProofModel</code> with the specified <code>expression</code>
	 * as its root node.
	 * 
	 * @param expression the {@link Expression} for the root node.
	 * @param ruleSet the available type rules for the model.
	 * 
	 * @throws NullPointerException if either <code>expression</code> or <code>ruleSet</code> is
	 *                              <code>null</code>.
	 *
	 * @see AbstractProofModel#AbstractProofModel(AbstractProofNode, AbstractProofRuleSet)
	 */
	public TypeInferenceProofModel(Expression expression,
			AbstractProofRuleSet ruleSet) {

		super(new DefaultTypeInferenceProofNode(new TypeJudgement(
				new DefaultTypeEnvironment(), expression, new TypeVariable(1, 0)),
				TypeSubstitutionList.EMPTY_LIST), ruleSet);
	}

	//
	// Accessors
	//

	/**
	 * Returns the current proof model index, which is the number of
	 * steps already performed on the model (starting with one) and
	 * used to allocate new, unique {@link types.TypeVariable}s. It
	 * is incremented with every proof step performed on the model.
	 * 
	 * @return the current index of the proof model.
	 * 
	 * @see TypeCheckerProofContext#newTypeVariable()
	 * @see types.TypeVariable
	 */
	public int getIndex() {

		return this.index;
	}

	/**
	 * Sets the current proof model index. This is a support operation,
	 * called by {@link DefaultTypeInferenceProofContext} whenever a new
	 * proof context is allocated.
	 * 
	 * @param index the new index for the proof model.
	 * 
	 * @see #getIndex()
	 * @see DefaultTypeInferenceProofContext
	 * @see DefaultTypeInferenceProofContext#DefaultTypeInferenceProofContext(TypeInferenceProofModel)
	 */
	void setIndex(int index) {

		if (index < 1) {
			throw new IllegalArgumentException("index is invalid");
		}
		this.index = index;
	}

	//
	// Actions
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see #guessWithType(ProofNode, MonoType)
	 * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void guess(ProofNode node) throws ProofGuessException {

		guessInternal((DefaultTypeInferenceProofNode) node, null);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule, de.unisiegen.tpml.core.ProofNode)
	 */
	@Override
	public void prove(ProofRule rule, ProofNode node) throws ProofRuleException {

		if (!this.ruleSet.contains(rule)) {
			throw new IllegalArgumentException("The rule is invalid for the model");
		}
		if (!this.root.isNodeRelated(node)) {
			throw new IllegalArgumentException("The node is invalid for the model");
		}
		if (node.getRules().length > 0) {
			throw new IllegalArgumentException("The node is already completed");
		}
		// try to apply the rule to the specified node
		applyInternal((TypeCheckerProofRule) rule,
				(DefaultTypeInferenceProofNode) node, null, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * added an the formula to apply the rule
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule, de.unisiegen.tpml.core.ProofNode)
	 */
	public void prove(ProofRule rule, ProofNode node, TypeFormula formula)
			throws ProofRuleException {

		if (!this.ruleSet.contains(rule)) {
			throw new IllegalArgumentException("The rule is invalid for the model");
		}
		if (!this.root.isNodeRelated(node)) {
			throw new IllegalArgumentException("The node is invalid for the model");
		}
		if (node.getRules().length > 0) {
			throw new IllegalArgumentException("The node is already completed");
		}
		// try to apply the rule to the specified node
		applyInternal((TypeCheckerProofRule) rule,
				(DefaultTypeInferenceProofNode) node, null, null);
	}

	//
	// Rule application
	//

	/**
	 * Applies the specified proof <code>rule</code> to the given <code>node</code>
	 * in this proof model.
	 * 
	 * @param rule the type proof rule to apply.
	 * @param node the type proof node to which to apply the <code>rule</code>.
	 * @param type the type the user guessed for the <code>node</code> or <code>null</code>
	 *             if the user didn't enter a type.
	 * 
	 * @throws ProofRuleException if the application of the <code>rule</code> to
	 *                            the <code>node</code> failed.
	 * 
	 * @see #guess(ProofNode)
	 * @see #prove(ProofRule, ProofNode)
	 */
	private void applyInternal(TypeCheckerProofRule rule,
			DefaultTypeInferenceProofNode node, MonoType type, TypeFormula form)
			throws ProofRuleException {

		// allocate a new TypeCheckerContext
		DefaultTypeInferenceProofContext context = new DefaultTypeInferenceProofContext(
				this, node);

		DefaultTypeInferenceProofNode typeNode = (DefaultTypeInferenceProofNode) node;
		Exception e = null;

		if (form != null) {
			try {
				// try to apply the rule to the specified node
				context.setSubstitutions(node.getSubstitutions());
				context.apply(rule, form, type);
				return;
			}
			catch (UnifyException e1) {
				// revert the actions performed so far
				context.revert();
				// re-throw the exception as proof rule exception 
				throw new ProofRuleException(node, rule, e);
			}

		}
		else {

			// Try actual Rule with all formulas of the actual node
			for (TypeFormula formula : typeNode.getFormula()) {
				try {
					// try to apply the rule to the specified node
					context.setSubstitutions(node.getSubstitutions());
					context.apply(rule, formula, type);

					return;
				}
				catch (ProofRuleException e1) {
					// revert the actions performed so far
					context.revert();
					// rembember first exception to rethrow
					if (e == null)
						e = e1;
					continue;
				}
				catch (UnifyException e1) {
					// revert the actions performed so far
					context.revert();
					// rembember first exception to rethrow
					if (e == null)
						e = e1;
					continue;
				}
				catch (RuntimeException e1) {
					// revert the actions performed so far
					context.revert();
					// rembember first exception to rethrow
					if (e == null)
						e = e1;
					continue;
				}
			}
			if (e instanceof ProofRuleException) {
				// rethrow exception
				throw (ProofRuleException) e;
			}
			else if (e instanceof RuntimeException) {
				// rethrow exception
				throw (RuntimeException) e;
			}
			else {
				// re-throw the exception as proof rule exception 
				throw new ProofRuleException(node, rule, e);
			}
		}
	}

	/**
	 * Implementation of the {@link #guess(ProofNode)} and {@link #guessWithType(ProofNode, MonoType)}
	 * methods.
	 * 
	 * @param node the proof node for which to guess the next step.
	 * @param type the type that the user entered for this <code>node</code> or <code>null</code> to
	 *             let the type inference algorithm guess the type.
	 * 
	 * @throws IllegalArgumentException if the <code>node</code> is invalid for this model.             
	 * @throws IllegalStateException if for some reason <code>node</code> cannot be proven.
	 * @throws NullPointerException if <code>node</code> is <code>null</code>.
	 * @throws ProofGuessException if the next proof step could not be guessed.
	 *
	 * @see #guess(ProofNode)
	 * @see #guessWithType(ProofNode, MonoType)
	 */
	private void guessInternal(DefaultTypeInferenceProofNode node, MonoType type)
			throws ProofGuessException {

		if (node == null) {
			throw new NullPointerException("node is null");
		}
		if (node.getSteps().length > 0) {
			throw new IllegalArgumentException("The node is already completed");
		}

		if (!this.root.isNodeRelated(node)) {
			throw new IllegalArgumentException("The node is invalid for the model");
		}
		// try to guess the next rule
		logger.debug("Trying to guess a rule for " + node);
		for (ProofRule rule : this.ruleSet.getRules()) {
			try {
				// try to apply the rule to the specified node
				applyInternal((TypeCheckerProofRule) rule, node, type, null);
				// remember that the user cheated
				setCheating(true);

				// yep, we did it
				logger.debug("Successfully applied (" + rule + ") to " + node);
				return;
			}
			catch (ProofRuleException e) {
				// rule failed to apply... so, next one, please
				logger.debug("Failed to apply (" + rule + ") to " + node, e);
				continue;
			}
		}

		// unable to guess next step
		logger.debug("Failed to find rule to apply to " + node);
		throw new ProofGuessException(node);
	}

	//
	// Proof context support
	//

	/**
	 * Adds a new child proof node below the <code>node</code> using the <code>context</code>, for the
	 * <code>environment</code>, <code>expression</code> and <code>type</code>.
	 * 
	 * @param context the <code>TypeCheckerProofContext</code> on which the action is to be performed.
	 * @param node the parent <code>DefaultTypeInferenceProofNode</code>.
	 * @param environment the <code>TypeEnvironment</code> for the child node.
	 * @param expression the <code>Expression</code> for the child node.
	 * @param type the type variable or the concrete type for the child node, used for the unification.
	 * 
	 * @throws IllegalArgumentException if <code>node</code> is invalid for this tree.
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	void contextAddProofNode(final DefaultTypeInferenceProofContext context,
			final DefaultTypeInferenceProofNode pNode,
			final LinkedList<TypeFormula> formulas, final TypeSubstitutionList subs,
			final TypeCheckerProofRule rule, final TypeFormula formula) {

		final DefaultTypeInferenceProofNode child = new DefaultTypeInferenceProofNode(
				formulas, subs);
		final ProofStep[] oldSteps = pNode.getSteps();

		// add redo and undo options
		addUndoableTreeEdit(new UndoableTreeEdit() {

			public void redo() {

				setFinished(((DefaultTypeInferenceProofNode) root).isFinished());

				pNode.add(child);
				contextSetProofNodeRule(context, pNode, rule, formula);
				nodesWereInserted(pNode, new int[] { pNode.getIndex(child) });
				nodeChanged(pNode);
			}

			public void undo() {

				// update the "finished" state
				setFinished(false);

				// remove the child and revert the steps
				int[] indices = { pNode.getIndex(child) };
				pNode.removeAllChildren();
				nodesWereRemoved(pNode, indices, new Object[] { child });
				pNode.setSteps(oldSteps);
				nodeChanged(pNode);

			}
		});

	}

	/**
	 * Used to implement the {@link DefaultTypeInferenceProofContext#apply(TypeCheckerProofRule, TypeInferenceProofNode)}
	 * method of the {@link DefaultTypeInferenceProofContext} class.
	 * 
	 * @param context the type inference proof context.
	 * @param node the type inference node.
	 * @param rule the type checker rule.
	 * 
	 * @see DefaultTypeCheckerProofContext#apply(TypeCheckerProofRule, TypeCheckerProofNode)
	 */
	void contextSetProofNodeRule(DefaultTypeInferenceProofContext context,
			final DefaultTypeInferenceProofNode node,
			final TypeCheckerProofRule rule, TypeFormula formula) {

		node.setSteps(new ProofStep[] { new ProofStep(new IsEmpty(), rule) });
		nodeChanged(node);
	}

	//
	// Undo/Redo
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofModel#addUndoableTreeEdit(de.unisiegen.tpml.core.AbstractProofModel.UndoableTreeEdit)
	 */
	@Override
	public void addUndoableTreeEdit(UndoableTreeEdit edit) {

		// perform the redo of the edit
		edit.redo();

		// add to the undo history
		super.addUndoableTreeEdit(edit);
	}
	
	// Just for testing
	public void setRoot(AbstractProofNode node){
		this.root=node;
	}

}
