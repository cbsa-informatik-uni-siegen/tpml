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
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
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
	  private static final Logger logger = Logger.getLogger(TypeInferenceProofModel.class);

	  private int index = 1;
	  
	  
	  public TypeInferenceProofModel(Expression expression, AbstractProofRuleSet ruleSet) {
		  super (new DefaultTypeInferenceProofNode(new TypeJudgement(new DefaultTypeEnvironment(), expression, new TypeVariable(1, 0)), TypeEquationList.EMPTY_LIST), ruleSet);
	  }
	  
	  
	@Override
	public void guess(ProofNode node) throws ProofGuessException {
		   guessInternal((DefaultTypeInferenceProofNode)node, null);
		  }
	
 private void guessInternal(DefaultTypeInferenceProofNode node, MonoType type) throws ProofGuessException {
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
//		    			 try to apply the rule to the specified node
		 		        applyInternal((TypeCheckerProofRule)rule, node, type);
		 		        
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
	    applyInternal((TypeCheckerProofRule)rule, (DefaultTypeInferenceProofNode) node, null);
	  }
	
	 private void applyInternal(TypeCheckerProofRule rule, DefaultTypeInferenceProofNode node, MonoType type) throws ProofRuleException {
		 	
		 //allocate a new TypeCheckerContext
			DefaultTypeInferenceProofContext context = new DefaultTypeInferenceProofContext(this, node);
		
			try {
				context.apply(rule,node.getFormula().getFirst(),type);
			}  catch (ProofRuleException e) {
			      // revert the actions performed so far
			      context.revert();
			      
			      // re-throw the exception
			      throw e;
			    }
			    catch (UnificationException e) {
			      // revert the actions performed so far
			      //context.revert();
			      // re-throw the exception as proof rule exception
			      throw new ProofRuleException(node, rule, e);
			    }
			    catch (RuntimeException e) {
			      // revert the actions performed so far
			      //context.revert();
			      // re-throw the exception
			      throw e;
			    }
			
			 /**
		    try {
		    	
		      // try to apply the rule to the node
		      context.apply(rule, node, type);
		      
		      //TODO redo() and undo seem not to be important at the moment
		      
		     
		      // check if we are finished
		      final TypeInferenceProofNode root = (TypeInferenceProofNode)getRoot();
		      context.addRedoAction(new Runnable() { public void run() { setFinished(root.isFinished()); } });
		      context.addUndoAction(new Runnable() { public void run() { setFinished(false); } });
		      
		      // determine the redo and undo actions from the context
		      final Runnable redoActions = context.getRedoActions();
		      final Runnable undoActions = context.getUndoActions();
		      
		      // record the undo edit action for this proof step
		      addUndoableTreeEdit(new UndoableTreeEdit() {
		        public void redo() { redoActions.run(); }
		        public void undo() { undoActions.run(); }
		      });
		    }
		    catch (ProofRuleException e) {
		      // revert the actions performed so far
		     // context.revert();
		      
		      // re-throw the exception
		      throw e;
		    }
		    catch (UnificationException e) {
		      // revert the actions performed so far
		      //context.revert();
		      // re-throw the exception as proof rule exception
		      throw new ProofRuleException(node, rule, e);
		    }
		    catch (RuntimeException e) {
		      // revert the actions performed so far
		      //context.revert();
		      // re-throw the exception
		      throw e;
		    }*/
		  }
			

	  void contextAddProofNode(DefaultTypeInferenceProofNode pNode, LinkedList<TypeFormula> formulas, TypeEquationList equations) {
		 
		  pNode.add(new DefaultTypeInferenceProofNode(formulas, equations));
		    /**
		     context.addRedoAction(new Runnable() {
		      public void run() {
		        node.add(child);
		        
		        nodesWereInserted(node, new int[] { node.getIndex(child) });
		      }
		    });
		    
		    context.addUndoAction(new Runnable() {
		      public void run() {
		        int index = node.getIndex(child);
		        node.remove(index);
		        nodesWereRemoved(node, new int[] { index }, new Object[] { child });
		      }
		    });*/
	  }
	  
	  
	  void contextSetProofNodeRule(DefaultTypeInferenceProofContext context, final DefaultTypeInferenceProofNode node, final TypeCheckerProofRule rule, TypeFormula formula) {
		    final ProofStep[] oldSteps = node.getSteps();
		    
		    
		    node.setSteps(new ProofStep[] { new ProofStep(formula.getExpression(), rule) });
		    /**
		    context.addRedoAction(new Runnable() {
		      public void run() {
		        node.setSteps(new ProofStep[] { new ProofStep(node.getExpression(), rule) });
		        nodeChanged(node);
		      }
		    });
		    
		    context.addUndoAction(new Runnable() {
		      public void run() {
		        node.setSteps(oldSteps);
		        nodeChanged(node);
		      }
		    });
		  }*/
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

