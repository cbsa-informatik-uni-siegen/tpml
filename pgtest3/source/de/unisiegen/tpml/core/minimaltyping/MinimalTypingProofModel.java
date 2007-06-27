package de.unisiegen.tpml.core.minimaltyping;

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
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * The heart of the type checker. Minimal typing proof rules are supplied via an
 * {@link de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet}
 * that is passed to the constructor.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractExpressionProofModel
 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext
 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode
 */
public class MinimalTypingProofModel extends AbstractExpressionProofModel {
  //
  // Constants
  //
  
  /**
   * The {@link Logger} for this class.
   * 
   * @see Logger
   */
  private static final Logger logger = Logger.getLogger(MinimalTypingProofModel.class);

  
  
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
   * @see MinimalTypingProofContext#newTypeVariable()
   * @see types.TypeVariable
   */
  private int index = 1;
  
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>MinimalTypingProofModel</code> with the specified <code>expression</code>
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
  public MinimalTypingProofModel(Expression expression, AbstractMinimalTypingProofRuleSet ruleSet) {
    super(new DefaultMinimalTypingExpressionProofNode(new DefaultTypeEnvironment(), expression), ruleSet);
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
   * @see MinimalTypingProofContext#newTypeVariable()
   * @see types.TypeVariable
   */
  public int getIndex() {
    return this.index;
  }
  
  /**
   * Sets the current proof model index. This is a support operation,
   * called by {@link DefaultMinimalTypingProofContext} whenever a new
   * proof context is allocated.
   * 
   * @param index the new index for the proof model.
   * 
   * @see #getIndex()
   * @see DefaultMinimalTypingProofContext
   * @see DefaultMinimalTypingProofContext#DefaultTypeCheckerProofContext(MinimalTypingProofModel)
   */
  public void setIndex(int index) {
    if (index < 1) {
      throw new IllegalArgumentException("index is invalid"); //$NON-NLS-1$
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
    guessInternal((MinimalTypingProofNode)node, null);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule, de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void prove(ProofRule rule, ProofNode node) throws ProofRuleException {
    if (!this.ruleSet.contains(rule)) {
      throw new IllegalArgumentException("The rule is invalid for the model"); //$NON-NLS-1$
    }
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("The node is invalid for the model"); //$NON-NLS-1$
    }
    if (node.getRules().length > 0) {
      throw new IllegalArgumentException("The node is already completed"); //$NON-NLS-1$
    }
    
    // try to apply the rule to the specified node
    applyInternal((MinimalTypingProofRule)rule, (MinimalTypingProofNode)node, null);
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
  private void applyInternal(MinimalTypingProofRule rule, MinimalTypingProofNode node, MonoType type) throws ProofRuleException {
	//allocate a new TypeCheckerContext
	DefaultMinimalTypingProofContext context;
  	context = new DefaultMinimalTypingProofContext(this);
    try {
    	
      // try to apply the rule to the node
      context.apply(rule, node, type);
      
      // check if we are finished
      final MinimalTypingProofNode modelRoot = (MinimalTypingProofNode)getRoot();
      context.addRedoAction(new Runnable() { @SuppressWarnings("synthetic-access")
		public void run() { setFinished(modelRoot.isFinished()); } });
      context.addUndoAction(new Runnable() { @SuppressWarnings("synthetic-access")
		public void run() { setFinished(false); } });
      
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
    catch (RuntimeException e) {
      // revert the actions performed so far
      context.revert();
      
      // re-throw the exception
      throw e;
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
  private void guessInternal(MinimalTypingProofNode node, MonoType type) throws ProofGuessException {
  	
  	if (node == null) {
    	
      throw new NullPointerException("node is null"); //$NON-NLS-1$
    }
    if (node.getSteps().length > 0) {
    	
      throw new IllegalArgumentException("The node is already completed"); //$NON-NLS-1$
    }
    if (!this.root.isNodeRelated(node)) {
    	
      throw new IllegalArgumentException("The node is invalid for the model"); //$NON-NLS-1$
    }
    
    
    // try to guess the next rule
    logger.debug("Trying to guess a rule for " + node); //$NON-NLS-1$
    for (ProofRule rule : this.ruleSet.getRules()) { // MUST be the getRules() from the ProofRuleSet
      try {
        // try to apply the rule to the specified node
        applyInternal((MinimalTypingProofRule)rule, node, type);
        
        // remember that the user cheated
        setCheating(true);
        
        // yep, we did it
        logger.debug("Successfully applied (" + rule + ") to " + node); //$NON-NLS-1$ //$NON-NLS-2$
        return;
      }
      catch (ProofRuleException e) {
        // rule failed to apply... so, next one, please
        logger.debug("Failed to apply (" + rule + ") to " + node, e); //$NON-NLS-1$ //$NON-NLS-2$
        continue;
      }
      catch (RuntimeException e){
      	throw new ProofGuessException(e.getMessage ( ),node);
      }
    }
    
    // unable to guess next step
    logger.debug("Failed to find rule to apply to " + node); //$NON-NLS-1$
    throw new ProofGuessException(node);
  }
  
  
  
  //
  // Proof context support
  //

  /**
   * Adds a new child proof node below the <code>node</code> using the <code>context</code>, for the
   * <code>environment</code>, <code>expression</code> and <code>type</code>.
   * 
   * @param context the <code>MinimalTypingProofContext</code> on which the action is to be performed.
   * @param node the parent <code>DefaultMinimalTypingProofNode</code>.
   * @param environment the <code>TypeEnvironment</code> for the child node.
   * @param expression the <code>Expression</code> for the child node.
   * @param type the concrete type for the child node.
   * 
   * @throws IllegalArgumentException if <code>node</code> is invalid for this tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void contextAddProofNode(DefaultMinimalTypingProofContext context, final AbstractMinimalTypingProofNode node, TypeEnvironment environment, Expression expression) {
    if (context == null) {
      throw new NullPointerException("context is null"); //$NON-NLS-1$
    }
    if (node == null) {
      throw new NullPointerException("node is null"); //$NON-NLS-1$
    }
    if (environment == null) {
      throw new NullPointerException("environment is null"); //$NON-NLS-1$
    }
    if (expression == null) {
      throw new NullPointerException("expression is null"); //$NON-NLS-1$
    }
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("node is invalid"); //$NON-NLS-1$
    }
    
    final DefaultMinimalTypingExpressionProofNode child = new DefaultMinimalTypingExpressionProofNode(environment, expression);
     context.addRedoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        node.add(child);
        nodesWereInserted(node, new int[] { node.getIndex(child) });
      }
    });
    
    context.addUndoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        int finalIndex = node.getIndex(child);
        node.remove(finalIndex);
        nodesWereRemoved(node, new int[] { finalIndex }, new Object[] { child });
      }
    });
  }
  
  /**
   * Adds a new child proof node below the <code>node</code> using the <code>context</code>, for the
   *  <code>type</code> and <code>type2</code>.
   * 
   * @param context the <code>MinimalTypingProofContext</code> on which the action is to be performed.
   * @param node the parent <code>DefaultMinimalTypingProofNode</code>.
   * @param type the first concrete type for type Comparison.
   * @param type2 the second concrete type for type Comparison.
   * 
   * @throws IllegalArgumentException if <code>node</code> is invalid for this tree.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public void contextAddProofNode(DefaultMinimalTypingProofContext context, final AbstractMinimalTypingProofNode node, MonoType type, MonoType type2) {
    if (context == null) {
      throw new NullPointerException("context is null"); //$NON-NLS-1$
    }
    if (node == null) {
      throw new NullPointerException("node is null"); //$NON-NLS-1$
    }
    if (type == null) {
      throw new NullPointerException("type is null"); //$NON-NLS-1$
    }
    if (type2 == null) {
      throw new NullPointerException("type2 is null"); //$NON-NLS-1$
    }
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("node is invalid"); //$NON-NLS-1$
    }
    
    final DefaultMinimalTypingTypesProofNode child = new DefaultMinimalTypingTypesProofNode(type, type2);
     context.addRedoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        node.add(child);
        nodesWereInserted(node, new int[] { node.getIndex(child) });
      }
    });
    
    context.addUndoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        int finalIndex = node.getIndex(child);
        node.remove(finalIndex);
        nodesWereRemoved(node, new int[] { finalIndex }, new Object[] { child });
      }
    });
  }
  
  /**
   * Used to implement the {@link DefaultMinimalTypingProofContext#apply(MinimalTypingProofRule, MinimalTypingProofNode)}
   * method of the {@link DefaultMinimalTypingProofContext} class.
   * 
   * @param context the type checker proof context.
   * @param node the type checker node.
   * @param rule the type checker rule.
   * 
   * @see DefaultMinimalTypingProofContext#apply(MinimalTypingProofRule, MinimalTypingProofNode)
   */
  public void contextSetProofNodeRule(DefaultMinimalTypingProofContext context, final AbstractMinimalTypingProofNode node, final MinimalTypingProofRule rule) {
    final ProofStep[] oldSteps = node.getSteps();
    
    context.addRedoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        node.setSteps(new ProofStep[] { new ProofStep(node.getExpression(), rule) });
        nodeChanged(node);
      }
    });
    
    context.addUndoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        node.setSteps(oldSteps);
        nodeChanged(node);
      }
    });
  }
  
  /**
   * 
   * Set the type of the pNode
   *
   * @param context minimal typing proof context
   * @param pNode the node to set the type
   * @param type the new type
   */
  public void contextSetProofNodeType(DefaultMinimalTypingProofContext context, AbstractMinimalTypingProofNode pNode, final MonoType type) {
  	final DefaultMinimalTypingExpressionProofNode node = (DefaultMinimalTypingExpressionProofNode) pNode;
    context.addRedoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        node.setType ( type );
        nodeChanged(node);
      }
    });
    
    context.addUndoAction(new Runnable() {
      @SuppressWarnings("synthetic-access")
		public void run() {
        node.setType ( null );
        nodeChanged(node);
      }
    });
  }




}
