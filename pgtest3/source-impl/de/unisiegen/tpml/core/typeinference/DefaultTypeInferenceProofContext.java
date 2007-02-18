package de.unisiegen.tpml.core.typeinference;

import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator;
import de.unisiegen.tpml.core.expressions.Assign;
import de.unisiegen.tpml.core.expressions.BinaryCons;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Deref;
import de.unisiegen.tpml.core.expressions.EmptyList;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Hd;
import de.unisiegen.tpml.core.expressions.IntegerConstant;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.expressions.Not;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Ref;
import de.unisiegen.tpml.core.expressions.RelationalOperator;
import de.unisiegen.tpml.core.expressions.Tl;
import de.unisiegen.tpml.core.expressions.UnaryCons;
import de.unisiegen.tpml.core.expressions.UnitConstant;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;
import de.unisiegen.tpml.core.typechecker.TypeUtilities;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.IntegerType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.PolyType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnitType;



/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeInferenceProofContext  implements TypeInferenceProofContext,TypeCheckerProofContext{

	
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
	
	private int count =0;
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
	   * @see #DefaultTypeInferenceProofContext(TypeCheckerProofModel)
	   */
	  private TypeInferenceProofModel model;
	  
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
	  
	  private DefaultTypeCheckerProofNode tmpTree;
	  
	  private MonoType type;
	  
	  protected Vector<AbstractProofNode> children;
	  
	  private DefaultTypeInferenceProofNode node;

	
	/**
	 * TODO
	 *
	 * @param model
	 */
	  public DefaultTypeInferenceProofContext(final TypeInferenceProofModel model, DefaultTypeInferenceProofNode pNode) {
		    if (model == null) {
		      throw new NullPointerException("model is null");
		    }
		    this.model = model;
		    children=new Vector<AbstractProofNode>();
		    
		    node=pNode;
		    
		    // increment the model index
		    final int index = model.getIndex();
		    addRedoAction(new Runnable() { public void run() { model.setIndex(index + 1); } });
		    addUndoAction(new Runnable() { public void run() { model.setIndex(index); } });
	
	  }
	  
	  public void addProofNode(TypeCheckerProofNode node, TypeEnvironment environment, Expression expression, MonoType type, TypeEquationList eqns) {
		    this.model.contextAddProofNode(this, (DefaultTypeInferenceProofNode)node, environment, expression, type, eqns);
	  	}
	
	  public void addProofNode(TypeCheckerProofNode node, TypeEnvironment environment, Expression expression, MonoType type) {
		    this.model.contextAddProofNode(this, (DefaultTypeInferenceProofNode)node, environment, expression, type, null);
	  	}
	
	/**
	 * TODO
	 *
	 * @param expression
	 * @return
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
	    else if (expression instanceof Not) {
	      return ArrowType.BOOL_BOOL;
	    }
	    else if (expression instanceof Assign) {
	    	
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(new RefType(TypeVariable.ALPHA), new ArrowType(TypeVariable.ALPHA, UnitType.UNIT)));
	    }
	    else if (expression instanceof Deref) {
	    	
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(new RefType(TypeVariable.ALPHA), TypeVariable.ALPHA));
	    }
	    else if (expression instanceof Ref) {
	    	
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(TypeVariable.ALPHA, new RefType(TypeVariable.ALPHA)));
	    }
	    else if (expression instanceof Projection) {
	      Projection projection = (Projection)expression;
	      TypeVariable[] typeVariables = new TypeVariable[projection.getArity()];
	      TreeSet<TypeVariable> quantifiedVariables = new TreeSet<TypeVariable>();
	      for (int n = 0; n < typeVariables.length; ++n) {
	        typeVariables[n] = new TypeVariable(n, 0);
	        quantifiedVariables.add(typeVariables[n]);
	      }
	      return new PolyType(quantifiedVariables, new ArrowType(new TupleType(typeVariables), typeVariables[projection.getIndex() - 1]));
	    }
	    else if (expression instanceof EmptyList) {
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ListType(TypeVariable.ALPHA));
	    }
	    else if (expression instanceof BinaryCons) {
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(TypeVariable.ALPHA, new ArrowType(new ListType(TypeVariable.ALPHA), new ListType(TypeVariable.ALPHA))));
	    }
	    else if (expression instanceof UnaryCons) {
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(new TupleType(new MonoType[] { TypeVariable.ALPHA, new ListType(TypeVariable.ALPHA) }), new ListType(TypeVariable.ALPHA)));
	    }
	    else if (expression instanceof Hd) {
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(new ListType(TypeVariable.ALPHA), TypeVariable.ALPHA));
	    }
	    else if (expression instanceof Tl) {
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(new ListType(TypeVariable.ALPHA), new ListType(TypeVariable.ALPHA)));
	    }
	    else if (expression instanceof IsEmpty) {
	      return new PolyType(Collections.singleton(TypeVariable.ALPHA), new ArrowType(new ListType(TypeVariable.ALPHA), BooleanType.BOOL));
	    }
	    else {
	      // not a simple expression
	      throw new IllegalArgumentException("Cannot determine the type for " + expression);
	    }
	  }
	
	  /**
	 * TODO
	 *
	 * @param left
	 * @param right
	 */
	public void addEquation(MonoType left, MonoType right) {
			node.addEquation(left, right);
		    this.equations = this.equations.extend(left, right);
		    
		 
		  }
		  
	  
	
	 //
	  // Rule application
	  //
	  
	void apply(TypeCheckerProofRule rule, TypeInferenceProofNode pNode, MonoType type) throws ProofRuleException, UnificationException {
	    // record the proof step for the node
	    this.model.contextSetProofNodeRule(this, (DefaultTypeInferenceProofNode)pNode, rule);
	    
	    
	    //Test
	    //TmpTree tmpTree = new TmpTree(node.getExpression(), model.getRuleSet());
	    
	    DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode) pNode;
	    
	    // try to apply the rule to the node
	    //rule.apply(this, node);
	    rule.apply(this, node);
	    
	    
	    // check if the user specified a type
	    if (type != null) {
	      // add an equation for { node.getType() = type }
	      addEquation(node.getType(), type);
	    }
	
	    // unify the type equations and apply the substitution to the model
	    //this.model.contextApplySubstitution(this, this.equations.unify());
	    // update all super nodes
	    for (;;) {
	      // determine the parent node
	      DefaultTypeInferenceProofNode parentNode = (DefaultTypeInferenceProofNode) node.getParent();
	     if (parentNode == null) {
	        break;
	      }
	     if (parentNode.getRule()==null)
	     {
	    	 System.out.println("get Rule = null");
	    	 break;
	     }
	      
	      
	      // update the parent node (using the previously applied rule)
	      parentNode.getRule().update(this, parentNode);
	      
	      // continue with the next one
	      node = parentNode;
	    }
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
	        for (Runnable redoAction : DefaultTypeInferenceProofContext.this.redoActions) {
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
	        for (Runnable undoAction : DefaultTypeInferenceProofContext.this.undoActions) {
	          undoAction.run();
	        }
	      }
	    };
	  }

	  public MonoType instantiate(Type type) {
		    if (type == null) {
		      throw new NullPointerException("type is null");
		    }
		    if (type instanceof PolyType) {
		      PolyType polyType = (PolyType)type;
		      MonoType tau = polyType.getTau();
		      for (TypeVariable tvar : polyType.getQuantifiedVariables()) {
		        tau = tau.substitute(TypeUtilities.newSubstitution(tvar, newTypeVariable()));
		      }
		      return tau;
		    }
		    else {
		      return (MonoType)type;
		    }
		  }

	  public TypeVariable newTypeVariable() {
		    return new TypeVariable(this.model.getIndex(), this.offset++);
		    
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
	  




}