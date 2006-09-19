package de.unisiegen.tpml.core.typechecker;

import java.util.LinkedList;

import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.IntegerConstant;
import de.unisiegen.tpml.core.expressions.RelationalOperator;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.IntegerType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnitType;

/**
 * Default implementation of the <code>TypeCheckerProofContext</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext
 */
final class DefaultTypeCheckerProofContext implements TypeCheckerProofContext {
  //
  // Attributes
  //
  
  /**
   * The current offset for the <code>TypeVariable</code> allocation. The
   * offset combined with the index from the {@link #model} will be used
   * to generate a new type variable on every invocation of the method
   * {@link #newTypeVariable()}. The offset will be incremented afterwards.
   * 
   * @see #newTypeVariable()
   * @see TypeVariable
   */
  private int offset = 0;

  /**
   * The list of type equations that has been collected for this context
   * and will be used as input for the unification algorithm.
   * 
   * @see typechecker.TypeEquations
   */
  private TypeEquationList equations = TypeEquationList.EMPTY_LIST;
  
  /**
   * The type checker proof model with which this proof context is associated.
   * 
   * @see #DefaultTypeCheckerProofContext(TypeCheckerProofModel)
   */
  private TypeCheckerProofModel model;
  
  /**
   * The list of redoable actions on the proof model.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getRedoActions()
   */
  private LinkedList<Runnable> redoActions = new LinkedList<Runnable>();
  
  /**
   * The list of undoable actions on the proof model.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  private LinkedList<Runnable> undoActions = new LinkedList<Runnable>();
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultTypeCheckerProofContext</code> with the specified <code>model</code>.
   * 
   * This constructor automatically increments the index of the <code>model</code>
   * using a redo/undo pair, via the {@link TypeCheckerProofModel#setIndex(int)}
   * method.
   * 
   * @param model the type checker proof model with which the context is associated.
   * 
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   * 
   * @see TypeCheckerProofModel#setIndex(int)
   */
  DefaultTypeCheckerProofContext(final TypeCheckerProofModel model) {
    if (model == null) {
      throw new NullPointerException("model is null");
    }
    this.model = model;
    
    // increment the model index
    final int index = model.getIndex();
    addRedoAction(new Runnable() { public void run() { model.setIndex(index + 1); } });
    addUndoAction(new Runnable() { public void run() { model.setIndex(index); } });
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addEquation(de.unisiegen.tpml.core.types.MonoType, de.unisiegen.tpml.core.types.MonoType)
   */
  public void addEquation(MonoType left, MonoType right) {
    this.equations = this.equations.extend(left, right);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addProofNode(de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode, de.unisiegen.tpml.core.typechecker.TypeEnvironment, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.types.MonoType)
   */
  public void addProofNode(TypeCheckerProofNode node, TypeEnvironment environment, Expression expression, MonoType type) {
    this.model.contextAddProofNode(this, (DefaultTypeCheckerProofNode)node, environment, expression, type);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#getTypeForExpression(de.unisiegen.tpml.core.expressions.Expression)
   */
  public Type getTypeForExpression(Expression expression) {
    if (expression == null) {
      throw new NullPointerException("expression is null");
    }
    
    if (expression instanceof BooleanConstant) {
      return BooleanType.BOOL;
    }
    else if (expression instanceof IntegerConstant) {
      return IntegerType.INT;
    }
    else if (expression instanceof UnitConstant) {
      return UnitType.UNIT;
    }
    else if (expression instanceof ArithmeticOperator) {
      return ArrowType.INT_INT_INT;
    }
    else if (expression instanceof RelationalOperator) {
      return ArrowType.INT_INT_BOOL;
    }
    else {
      // not a simple expression
      throw new IllegalArgumentException("Cannot determine the type for " + expression);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#newTypeVariable()
   */
  public TypeVariable newTypeVariable() {
    return new TypeVariable(this.model.getIndex(), this.offset++);
  }
  
  
  
  //
  // Rule application
  //
  
  /**
   * Applies the specified proof <code>rule</code> to the given <code>node</code>.
   * 
   * @param rule the proof rule to apply to the <code>node</code>.
   * @param node the proof node to which to apply the <code>rule</code>.
   * 
   * @throws NullPointerException if <code>rule</code> or <code>node</code> is <code>null</code>.
   * @throws ProofRuleException if the application of the <code>rule</code> to the
   *                            <code>node</code> failed for some reason.
   * @throws UnificationException if an error occurs while unifying the type equations that resulted
   *                              from the application of <code>rule</code> to <code>node</code>.
   */
  void apply(TypeCheckerProofRule rule, TypeCheckerProofNode node) throws ProofRuleException, UnificationException {
    // record the proof step for the node
    this.model.contextSetProofNodeRule(this, (DefaultTypeCheckerProofNode)node, rule);
    
    // try to apply the rule to the node
    rule.apply(this, node);
    
    // unify the type equations and apply the substitution to the model
    this.model.contextApplySubstitution(this, this.equations.unify());
    
    // update all super nodes
    for (;;) {
      // determine the parent node
      TypeCheckerProofNode parentNode = node.getParent();
      if (parentNode == null) {
        break;
      }
      
      // update the parent node (using the previously applied rule)
      parentNode.getRule().update(this, parentNode);
      
      // continue with the next one
      node = parentNode;
    }
  }
  
  /**
   * Invokes all previously registered undo actions and clears the list of undo actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getUndoActions()
   */
  void revert() {
    // undo all already performed changes
    for (Runnable undoAction : this.undoActions) {
      undoAction.run();
    }
    this.undoActions.clear();
  }
  
  
  
  //
  // Context action handling
  //
  
  /**
   * Adds the specified <code>redoAction</code> to the internal list of redoable actions, and runs the
   * <code>redoAction</code>.
   * 
   * This method should be called before adding the matching undo action via {@link #addUndoAction(Runnable)}.
   * 
   * @param redoAction the redoable action.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   * 
   * @throws NullPointerException if <code>redoAction</code> is <code>null</code>.
   */
  void addRedoAction(Runnable redoAction) {
    if (redoAction == null) {
      throw new NullPointerException("undoAction is null");
    }
    
    // perform the action
    redoAction.run();
    
    // record the action
    this.redoActions.add(redoAction);
  }
  
  /**
   * Adds the specified <code>undoAction</code> to the internal list of undoable actions, and runs the
   * <code>undoActions</code>.
   * 
   * This method should be called after adding the matching redo action via {@link #addRedoAction(Runnable)}.
   * 
   * @param undoAction the undoable action.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   * 
   * @throws NullPointerException if <code>undoAction</code> is <code>null</code>.
   */
  void addUndoAction(Runnable undoAction) {
    if (undoAction == null) {
      throw new NullPointerException("undoAction is null");
    }
    
    // record the action
    this.undoActions.add(0, undoAction);
  }
  
  /**
   * Returns a <code>Runnable</code>, which performs all the previously
   * recorded redoable actions, added via {@link #addRedoAction(Runnable)}.
   * 
   * @return a <code>Runnable</code> for all recorded redoable actions.
   * 
   * @see #addRedoAction(Runnable)
   * @see #getUndoActions()
   */
  Runnable getRedoActions() {
    return new Runnable() {
      public void run() {
        for (Runnable redoAction : DefaultTypeCheckerProofContext.this.redoActions) {
          redoAction.run();
        }
      }
    };
  }
  
  /**
   * Returns a <code>Runnable</code>, which performs all the previously
   * recorded undoable actions, added via {@link #addUndoAction(Runnable)}.
   * 
   * @return a <code>Runnable</code> for all recorded undoable actions.
   * 
   * @see #addUndoAction(Runnable)
   * @see #getRedoActions()
   */
  Runnable getUndoActions() {
    return new Runnable() {
      public void run() {
        for (Runnable undoAction : DefaultTypeCheckerProofContext.this.undoActions) {
          undoAction.run();
        }
      }
    };
  }
}
