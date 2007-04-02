package de.unisiegen.tpml.core.typeinference;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * 
 * TODO
 * 
 * @author Benjamin Mies
 * 
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

	private int index = 1;

	public TypeInferenceProofModel(Expression expression,
			AbstractProofRuleSet ruleSet) {
		super(new DefaultTypeInferenceProofNode(new TypeJudgement(
				new DefaultTypeEnvironment(), expression,
				new TypeVariable(1, 0)), TypeEquationList.EMPTY_LIST,
				TypeSubstitutionList.EMPTY_LIST), ruleSet);
	}

	@Override
	public void guess(ProofNode node) throws ProofGuessException {
		guessInternal((DefaultTypeInferenceProofNode) node, null);
	}

	private void guessInternal(DefaultTypeInferenceProofNode node, MonoType type)
			throws ProofGuessException {
		if (node == null) {
			throw new NullPointerException("node is null");
		}
		if (node.getSteps().length > 0) {
			throw new IllegalArgumentException("The node is already completed");
		}

		if (!this.root.isNodeRelated(node)) {
			throw new IllegalArgumentException(
					"The node is invalid for the model");
		}
		// try to guess the next rule
		logger.debug("Trying to guess a rule for " + node);

		for (ProofRule rule : this.ruleSet.getRules()) {
			try {
				// try to apply the rule to the specified node
				applyInternal((TypeCheckerProofRule) rule, node, type);

				// remember that the user cheated
				setCheating(true);

				// yep, we did it
				logger.debug("Successfully applied (" + rule + ") to " + node);
				return;
			} catch (ProofRuleException e) {
				// rule failed to apply... so, next one, please
				logger.debug("Failed to apply (" + rule + ") to " + node, e);
				continue;
			}
		}

		// unable to guess next step
		logger.debug("Failed to find rule to apply to " + node);
		throw new ProofGuessException(node);
	}

	@Override
	public void prove(ProofRule rule, ProofNode node) throws ProofRuleException {
		if (!this.ruleSet.contains(rule)) {
			throw new IllegalArgumentException(
					"The rule is invalid for the model");
		}
		if (!this.root.isNodeRelated(node)) {
			throw new IllegalArgumentException(
					"The node is invalid for the model");
		}
		if (node.getRules().length > 0) {
			throw new IllegalArgumentException("The node is already completed");
		}
		// try to apply the rule to the specified node
		applyInternal((TypeCheckerProofRule) rule,
				(DefaultTypeInferenceProofNode) node, null);
	}

	private void applyInternal(TypeCheckerProofRule rule,
			DefaultTypeInferenceProofNode node, MonoType type)
			throws ProofRuleException {

		// allocate a new TypeCheckerContext
		DefaultTypeInferenceProofContext context = new DefaultTypeInferenceProofContext(
				this, node);
		/**
		try { context.setSubstitutions(node.getSubstitutions());
		  context.apply(rule,node.getFormula().getFirst(),type);
		   // check if we are finished  
		  final DefaultTypeInferenceProofNode root = (DefaultTypeInferenceProofNode)getRoot();
		  context.addRedoAction(new Runnable() { public void run() { setFinished(root.isFinished()); }}); 
		  context.addUndoAction(new Runnable() { public void run() { setFinished(false); } });
		   // determine the redo and undo actions from the context 
		  final Runnable redoActions = context.getRedoActions(); 
		  final Runnable undoActions = context.getUndoActions();
		   // record the undo edit action for this proof step
		  addUndoableTreeEdit(new UndoableTreeEdit() { public void redo() { redoActions.run(); } public void undo() { undoActions.run(); } });
		   } 
		  catch (ProofRuleException e) { 
			   // revert the actions performed so far
		  
		   context.revert();
		   // re-throw the exception 
		   throw e; 
		   } 
			catch (UnificationException e)
			{ 
				// revert the actions performed so far 
				context.revert(); 
				// re-throw the exception as proof rule exception 
				throw new ProofRuleException(node, rule, e); 
			} 
			catch (RuntimeException e) 
			{ // revert the actions performed so far 
				context.revert(); 
				// re-throw the exception 
				throw e;
			}
		 */
		DefaultTypeInferenceProofNode typeNode = (DefaultTypeInferenceProofNode) node;
		Exception e = null;
		// Try actual Rule with all formulas of the actual node
		for (TypeFormula formula : typeNode.getFormula()) {
			try {
				// // try to apply the rule to the specified node
				context.setSubstitutions(node.getSubstitutions());
				context.apply(rule, formula, type);

				// check if we are finished
				final DefaultTypeInferenceProofNode root = (DefaultTypeInferenceProofNode) getRoot();
				context.addRedoAction(new Runnable() {public void run() {setFinished(root.isFinished());}});
				context.addUndoAction(new Runnable() {public void run() {setFinished(false);}});

				// determine the redo and undo actions from the context
				final Runnable redoActions = context.getRedoActions();
				final Runnable undoActions = context.getUndoActions();

				// record the undo edit action for this proof step
				addUndoableTreeEdit(new UndoableTreeEdit() 
				{	public void redo() {redoActions.run();}
					public void undo() {undoActions.run();}
				});
				return;
			} catch (ProofRuleException e1) {
				// revert the actions performed so far
				context.revert();
				// rembember first exception to rethrow
				if (e==null)
				e=e1;
				continue;
			} catch (UnificationException e1) {
				// revert the actions performed so far
				context.revert();
				// rembember first exception to rethrow
				if (e==null)
				e=e1;
				continue;
			} catch (RuntimeException e1) {
				// revert the actions performed so far
				context.revert();
				// rembember first exception to rethrow
				if (e==null)
				e=e1;
				continue;
			}
		}
		if (e instanceof ProofRuleException )
		{
			// rethrow exception
			throw (ProofRuleException) e;
		}
		else if (e instanceof RuntimeException)
		{
			// rethrow exception
			throw (RuntimeException) e;
		}
		else
		{
			// re-throw the exception as proof rule exception 
			throw new ProofRuleException(node, rule, e); 
		}
	}

	void contextAddProofNode(DefaultTypeInferenceProofContext context,
			final DefaultTypeInferenceProofNode pNode,
			final LinkedList<TypeFormula> formulas,
			final TypeEquationList equations, final TypeSubstitutionList subs) {
		context.addRedoAction(new Runnable() {
			public void run() {
				pNode.add(new DefaultTypeInferenceProofNode(formulas,
						equations, subs));
				nodesWereInserted(pNode, new int[] { pNode.getIndex(pNode
						.getFirstChild()) });
			}
		});

		context.addUndoAction(new Runnable() {
			public void run() {
				DefaultTypeInferenceProofNode child = (DefaultTypeInferenceProofNode) pNode
						.getFirstChild();
				int index = pNode.getIndex(child);
				pNode.remove(index);
				nodesWereRemoved(pNode, new int[] { index },
						new Object[] { child });
			}
		});
	}

	void contextSetProofNodeRule(DefaultTypeInferenceProofContext context,
			final DefaultTypeInferenceProofNode node,
			final TypeCheckerProofRule rule, TypeFormula formula) {
		final ProofStep[] oldSteps = node.getSteps();

		context.addRedoAction(new Runnable() {
			public void run() {
				node.setSteps(new ProofStep[] { new ProofStep(new IsEmpty(),
						rule) });
				nodeChanged(node);
			}
		});

		context.addUndoAction(new Runnable() {
			public void run() {
				node.setSteps(oldSteps);
				nodeChanged(node);
			}
		});

	}

	//
	// Accessors
	//

	public int getIndex() {
		return this.index;
	}

	void setIndex(int index) {
		if (index < 1) {
			throw new IllegalArgumentException("index is invalid");
		}
		this.index = index;
	}

}
