package de.unisiegen.tpml.core.typeinference;

import java.util.Enumeration;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.AbstractExpressionProofModel;
import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
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


public final class TypeInferenceProofModel extends AbstractExpressionProofModel {
	 
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
	  
	  private Expression expression;
	  
	 // private DefaultTypeInferenceProofNode actualNode;
	  
	
	  
	  //
	  // Constructor
	  //
	  
	  /**
	   * Allocates a new <code>TypeCheckerProofModel</code> with the specified <code>expression</code>
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
	  public TypeInferenceProofModel(Expression pExpression, AbstractTypeCheckerProofRuleSet pRuleSet) {
	    super(new DefaultTypeInferenceProofNode(new DefaultTypeEnvironment(), pExpression, new TypeVariable(1, 0)), pRuleSet);
	      expression=pExpression;  
	      ruleSet=pRuleSet;
	      
	           
	      }
	  
	  public int getIndex() {
		    return this.index;
		  }
		  
	  
	  public void setIndex(int index) {
		    if (index < 1) {
		      throw new IllegalArgumentException("index is invalid");
		    }
		    this.index = index;
		  }
		  

	  
	  
	  /**
	 * TODO
	 *
	 * @param node
	 * @throws ProofGuessException
	 * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
	 */
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
		    
		    
		    
		    for (ProofRule rule : this.ruleSet.getRules()) { // MUST be the getRules() from the ProofRuleSet
		      
		    	boolean preUnify=true;
		    	DefaultTypeInferenceProofNode parent = node.getParent();
		    	
		    	
		    	if (parent!=null)
		    	{
		    		ProofRule tmpRule=parent.getRule();
		    		if (tmpRule!=null)
		    		{
		    			if (parent.getRule().toString().equals("UNIFY")||parent.getRule().toString().equals("P-ID"))
			    		{
		    				
			    			preUnify=false;
			    			
			    		}
		    		}
		    		
		    		parent=parent.getParent();
		    	}
		    	
		    	  //TODO
		    	 //dirty workaround, think about another way!
		    	 if (node.getParent()==null || (preUnify || rule.toString().equals("UNIFY"))) 
		    	 {
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
		        
		    }
		    
		    // unable to guess next step
		    logger.debug("Failed to find rule to apply to " + node);
		    throw new ProofGuessException(node);
		  }
		 
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
	  private void applyInternal(TypeCheckerProofRule rule, DefaultTypeInferenceProofNode node, MonoType type) throws ProofRuleException {
		//allocate a new TypeCheckerContext
		DefaultTypeInferenceProofContext context;
	  	context = new DefaultTypeInferenceProofContext(this, node);
	 
	    try {
	    	
	      // try to apply the rule to the node
	      context.apply(rule, node, type);
	      
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
	      context.revert();
	      
	      // re-throw the exception
	      throw e;
	    }
	    catch (UnificationException e) {
	      // revert the actions performed so far
	      context.revert();
	      
	      // re-throw the exception as proof rule exception
	      throw new ProofRuleException(node, rule, e);
	    }
	    catch (RuntimeException e) {
	      // revert the actions performed so far
	      context.revert();
	      
	      // re-throw the exception
	      throw e;
	    }
	  }
		

	
	 private static ProofNode nextNode(TypeCheckerProofModel model) {
		    LinkedList<ProofNode> nodes = new LinkedList<ProofNode>();
		    nodes.add(model.getRoot());
		    while (!nodes.isEmpty()) {
		      ProofNode node = nodes.poll();
		      if (node.getRules().length == 0) {
		    	  
		    	  return node;
		      }
		      for (int n = 0; n < node.getChildCount(); ++n) {
		        nodes.add(node.getChildAt(n));
		      }
		    }
		    throw new IllegalStateException("Unable to find next node");
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
	    applyInternal((TypeCheckerProofRule)rule, (DefaultTypeInferenceProofNode)node, null);
	  }
	
	 /**
	   * @param s the {@link TypeSubstitution} to apply to all nodes in the proof tree.
	   * 
	   * @throws NullPointerException if <code>s</code> is <code>null</code>.
	   */
	  void contextApplySubstitution(DefaultTypeInferenceProofContext context, TypeSubstitution s) {
	    if (s == null) {
	      throw new NullPointerException("s is null");
	    }
	    
	    // apply the substitution s to all nodes in the proof node
	    Enumeration nodes = this.root.postorderEnumeration();
	    while (nodes.hasMoreElements()) {
	      // determine the previous settings for the node
	      final DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode)nodes.nextElement();
	      final TypeEnvironment oldEnvironment = node.getEnvironment();
	      final Expression oldExpression = node.getExpression();
	      final MonoType oldType = node.getType();
	      
	      // determine the new settings for the node
	      final TypeEnvironment newEnvironment = oldEnvironment.substitute(s);
	      final Expression newExpression = oldExpression.substitute(s);
	      final MonoType newType = oldType.substitute(s);
	      
	      // check if the old and new settings differ
	      if (!oldEnvironment.equals(newEnvironment) || !oldExpression.equals(newExpression) || !oldType.equals(newType)) {
	        // add the redo action for the substitution
	        context.addRedoAction(new Runnable() {
	          public void run() {
	            node.setEnvironment(newEnvironment);
	            node.setExpression(newExpression);
	            node.setType(newType);
	            nodeChanged(node);
	          }
	        });
	        
	        // add the undo action for the substitution
	        context.addUndoAction(new Runnable() {
	          public void run() {
	            node.setEnvironment(oldEnvironment);
	            node.setExpression(oldExpression);
	            node.setType(oldType);
	            nodeChanged(node);
	          }
	        });
	      }
	    }
	  }
	  
	  /**
	   * Used to implement the {@link DefaultTypeCheckerProofContext#apply(TypeCheckerProofRule, TypeCheckerProofNode)}
	   * method of the {@link DefaultTypeCheckerProofContext} class.
	   * 
	   * @param context the type checker proof context.
	   * @param node the type checker node.
	   * @param rule the type checker rule.
	   * 
	   * @see DefaultTypeCheckerProofContext#apply(TypeCheckerProofRule, TypeCheckerProofNode)
	   */
	  void contextSetProofNodeRule(DefaultTypeInferenceProofContext context, final DefaultTypeInferenceProofNode node, final TypeCheckerProofRule rule) {
	    final ProofStep[] oldSteps = node.getSteps();
	   
	    
	    context.addRedoAction(new Runnable() {
	      public void run() {
	        node.setSteps(new ProofStep[] { new ProofStep(node.getExpression(), rule) });
	        nodeChanged(node);
	      }
	    });
	  }
	  
	  void contextAddProofNode(DefaultTypeInferenceProofContext context, final DefaultTypeInferenceProofNode node, TypeEnvironment environment, Expression expression, MonoType type, TypeEquationList eqns) {
		   
		  if (context == null) {
		     throw new NullPointerException("context is null");
		      
		    }
		    if (node == null) {
		    	throw new NullPointerException("node is null");
		    }
		    if (environment == null) {
		      throw new NullPointerException("environment is null");
		    }
		    if (expression == null) {
		      throw new NullPointerException("expression is null");
		    }
		    if (type == null) {
		      throw new NullPointerException("type is null");
		    }
		    if (!this.root.isNodeRelated(node)) {
		      throw new IllegalArgumentException("node is invalid");
		    }
		    final DefaultTypeInferenceProofNode child;
		    if (eqns==null)
		    {
		    	child = new DefaultTypeInferenceProofNode(environment, expression, type, node.getEquations());
		    }
		    else
		    {
		    	 child = new DefaultTypeInferenceProofNode(environment, expression, type, eqns);
		    }
		    
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
		    });
	  }

	public Expression getExpression() {
		return expression;
	}



	  
	
	}

