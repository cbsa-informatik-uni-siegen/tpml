package de.unisiegen.tpml.core.typeinference;

import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeSet;

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
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeUtilities;
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
 * 
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeInferenceProofContext implements TypeCheckerProofContext{

	
	  
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
   * 
   * @see typeinference.TypeEquation
   */
	private TypeEquationList equations;
	
	/**
	 * The list of type substitutions that has been collected for this context
	 * 
	 */
	private TypeSubstitutionList substitutions = TypeSubstitutionList.EMPTY_LIST;
	
	/**
	 * The type inference proof model with which this proof context is associated.
	 */
	private TypeInferenceProofModel model;
	
	/**
	 * The actual used type inference proof node
	 */
	private DefaultTypeInferenceProofNode node;
	
	/**
	 * Shows if unification is ready
	 * Means if Equationlist is empty and no other typeformula exists
	 */
	//private boolean unifyReady = false;
	
	
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
	  // Constructor
	  //
	
	/**
	 * 
	 *
	 * @param pModel the type inference proof model with which the context is associated.
	 * @param pNode the type inference proof node to apply a rule
	 * 
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   * 
	 * @see TypeInferenceProofModel#setIndex(int)
	 */
	public DefaultTypeInferenceProofContext(TypeInferenceProofModel pModel, DefaultTypeInferenceProofNode pNode) {
		if (pModel == null) {
      throw new NullPointerException("model is null");
    }
		this.model=pModel;
		this.node=pNode;
		this.equations=pNode.getEquations();
		
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
    equations=equations.extend(left, right);
	}
	
	/**
	 * 
	 * substitute the type equation list
	 *
	 * @param s type substitution for this type equation list
	 */
	public void substitute(TypeSubstitution s){
		equations=equations.substitute(s);
		}
		

	
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addProofNode(de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode, de.unisiegen.tpml.core.typechecker.TypeEnvironment, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.types.MonoType)
   */
	public void addProofNode(TypeCheckerProofNode pNode, TypeEnvironment environment, Expression expression, MonoType type) {
			    final DefaultTypeCheckerProofNode child = new DefaultTypeCheckerProofNode(environment, expression, type);
		        ((DefaultTypeCheckerProofNode)pNode).add(child);
	} 
  
  
  /**
   * {@inheritDoc}
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#getTypeForExpression(de.unisiegen.tpml.core.expressions.Expression)
   */
  public Type getTypeForExpression ( Expression expression )
  {
    if ( expression == null )
    {
      throw new NullPointerException ( "expression is null" ) ;
    }
    if ( expression instanceof BooleanConstant )
    {
      return new BooleanType ( ) ;
    }
    else if ( expression instanceof IntegerConstant )
    {
      return new IntegerType ( ) ;
    }
    else if ( expression instanceof UnitConstant )
    {
      return new UnitType ( ) ;
    }
    else if ( expression instanceof ArithmeticOperator )
    {
      return new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new IntegerType ( ) ) ) ;
    }
    else if ( expression instanceof RelationalOperator )
    {
      return new ArrowType ( new IntegerType ( ) , new ArrowType (
          new IntegerType ( ) , new BooleanType ( ) ) ) ;
    }
    else if ( expression instanceof Not )
    {
      return new ArrowType ( new BooleanType ( ) , new BooleanType ( ) ) ;
    }
    else if ( expression instanceof Assign )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new RefType ( TypeVariable.ALPHA ) , new ArrowType (
              TypeVariable.ALPHA , new UnitType ( ) ) ) ) ;
    }
    else if ( expression instanceof Deref )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new RefType ( TypeVariable.ALPHA ) ,
              TypeVariable.ALPHA ) ) ;
    }
    else if ( expression instanceof Ref )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( TypeVariable.ALPHA ,
              new RefType ( TypeVariable.ALPHA ) ) ) ;
    }
    else if ( expression instanceof Projection )
    {
      Projection projection = ( Projection ) expression ;
      TypeVariable [ ] typeVariables = new TypeVariable [ projection
          .getArity ( ) ] ;
      TreeSet < TypeVariable > quantifiedVariables = new TreeSet < TypeVariable > ( ) ;
      for ( int n = 0 ; n < typeVariables.length ; ++ n )
      {
        typeVariables [ n ] = new TypeVariable ( n , 0 ) ;
        quantifiedVariables.add ( typeVariables [ n ] ) ;
      }
      return new PolyType ( quantifiedVariables , new ArrowType (
          new TupleType ( typeVariables ) , typeVariables [ projection
              .getIndex ( ) - 1 ] ) ) ;
    }
    else if ( expression instanceof EmptyList )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ListType ( TypeVariable.ALPHA ) ) ;
    }
    else if ( expression instanceof BinaryCons )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( TypeVariable.ALPHA , new ArrowType ( new ListType (
              TypeVariable.ALPHA ) , new ListType ( TypeVariable.ALPHA ) ) ) ) ;
    }
    else if ( expression instanceof UnaryCons )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new TupleType ( new MonoType [ ]
          { TypeVariable.ALPHA , new ListType ( TypeVariable.ALPHA ) } ) ,
              new ListType ( TypeVariable.ALPHA ) ) ) ;
    }
    else if ( expression instanceof Hd )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new ListType ( TypeVariable.ALPHA ) ,
              TypeVariable.ALPHA ) ) ;
    }
    else if ( expression instanceof Tl )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new ListType ( TypeVariable.ALPHA ) , new ListType (
              TypeVariable.ALPHA ) ) ) ;
    }
    else if ( expression instanceof IsEmpty )
    {
      return new PolyType ( Collections.singleton ( TypeVariable.ALPHA ) ,
          new ArrowType ( new ListType ( TypeVariable.ALPHA ) ,
              new BooleanType ( ) ) ) ;
    }
    else
    {
      // not a simple expression
      throw new IllegalArgumentException ( "Cannot determine the type for "
          + expression ) ;
    }
  }
	
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#instantiate(de.unisiegen.tpml.core.types.Type)
   */
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
	
	
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#newTypeVariable()
   */
	public TypeVariable newTypeVariable() {
	    return new TypeVariable(this.model.getIndex(), this.offset++);

	}
	
	/**
	 * 
	 * Extend a new Substitution to the SubstitutionList
	 *
	 * @param s the new found substitution
	 */
	
	public void addSubstitution (DefaultTypeSubstitution s){
		substitutions = substitutions.extend(s);
	}
	  
	//
	// Rule application
	//
  /**
   * Applies the specified proof <code>rule</code> to the actual <code>node</code>.
   * 
   * @param rule the proof rule to apply to the <code>node</code>.
   * @param formula the TypeFormula to which to apply the <code>rule</code>.
   * @param type the type the user guessed for the <code>node</code> or <code>null</code>
   *             if the user didn't enter a type.
   * 
   * @throws ProofRuleException if the application of the <code>rule</code> to the
   *                            <code>node</code> failed for some reason.
   * @throws UnifyException if an error occurs while unifying the type equations that resulted
   *                              from the application of <code>rule</code> to <code>node</code>.
   */ 
	void apply(TypeCheckerProofRule rule, TypeFormula formula, MonoType type) throws ProofRuleException, UnifyException {
		DefaultTypeCheckerProofNode typeNode=null;
		//dirty workaround, just think about
		if (formula.getExpression()!=null)
		{
			typeNode = new DefaultTypeCheckerProofNode(formula.getEnvironment(), formula.getExpression(), formula.getType());
		}
		else if (rule.toString().equals("UNIFY"))
		{
			typeNode = new DefaultTypeEquationProofNode(formula.getEnvironment(),new IsEmpty() , formula.getType(),(TypeEquation) formula);
		}
		else
		{
			typeNode = new DefaultTypeCheckerProofNode(formula.getEnvironment(),new IsEmpty() , new UnitType());
			throw new ProofRuleException(typeNode, rule);
		}
	    // try to apply the rule to the node
		rule.apply(this, typeNode);
		
	    //  record the proof step for the node
	    //this.model.contextSetProofNodeRule(this, node, rule, formula);
	    
	    // check if the user specified a type
	    /**if (type != null) {
	    add an equation for { node.getType() = type }
	    addEquation(node.getType(), type);
	    }*/

	    // check if TypeEquationList is empty and there are no other TypeFormulas
	    // else create a new node and add it to the parent node
	/**	if (!unifyReady)*/
	    {
	    // Create a new List of formulas
	    LinkedList<TypeFormula> formulas=new LinkedList<TypeFormula>();
	    
	    // add evtl. existing formulas from the parent node
	    for (TypeFormula form : node.getFormula())
	    {
	    	if (form instanceof TypeJudgement)
	    	if ((!formula.equals(form)) )
	    	{
	    		formulas.add(form);
	    	}
	    }
	    
	    // add the nodes of the temporary existing typenode
	    for (int i=0; i< typeNode.getChildCount(); i++)
        {
	    	TypeJudgement judgement= new TypeJudgement((DefaultTypeEnvironment)typeNode.getChildAt(i).getEnvironment(), typeNode.getChildAt(i).getExpression(), typeNode.getChildAt(i).getType());
	    	formulas.add(judgement);
        }
	    
	   // if (formula instanceof TypeEquation)
	    //equations=equations.getRemaining();
	    TypeEquationList eqns = equations;
	    while (equations!= TypeEquationList.EMPTY_LIST){
	    	//System.out.println(equations.getFirst().toString());
	    	
	    	formulas.add(equations.getFirst());
	    	equations = equations.getRemaining();
	    	
	    }
	    
	    
	    // create the new node
	    this.model.contextAddProofNode(this, node, formulas, eqns, substitutions, rule, formula);
	    }
		
		
		/**
		// Create a new List of formulas
    LinkedList<TypeFormula> formulas=new LinkedList<TypeFormula>();
    // add evtl. existing Typejudgements from the parent node
    for (TypeFormula form : node.getFormula())
    {
    	if ((!formula.equals(form)) )
    	{
    		if (form instanceof TypeJudgement){
    			formulas.add(form);
    		}
    	}
    }
    
    // add the nodes of the temporary existing typenode
    for (int i=0; i< typeNode.getChildCount(); i++)
      {
    	TypeJudgement judgement= new TypeJudgement((DefaultTypeEnvironment)typeNode.getChildAt(i).getEnvironment(), typeNode.getChildAt(i).getExpression(), typeNode.getChildAt(i).getType());
    	formulas.add(judgement);
      }
    
  
    
    // add the new collected TypeEquations
    for (int i = equations.size()-1;i>-1; i--){
    	formulas.add(equations.get(i));
    }
    
    // add evtl. existing TypeEquations from the parent node
    for (TypeFormula form : node.getFormula())
    {
    	if ((!formula.equals(form)) )
    	{
    		if  (form instanceof TypeEquation)
    		{
    			formulas.add(form);
    		}
    	}
    }
//  create the new node
    this.model.contextAddProofNode(this, node, formulas, TypeEquationList.EMPTY_LIST, substitutions, rule, formula);
    */
    
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

	 
	  //
	  // Accessors
	  //

	  /**
	   * Set the TypeEquationList for this context
	   *
	   * @param equations the new list of type equations
	   */
	public void setEquations(TypeEquationList equations) {
		this.equations = equations;
	}
	/**
	 * 
	 * remove first type equation from list
	 *
	 */
	public void popEquation(){
		this.equations= this.equations.getRemaining();
	}


	/**
	 * 
	 * Set the TypeSubstitutions for this context
	 *
	 * @param substitutions the new list of type substitutions
	 */
	public void setSubstitutions(TypeSubstitutionList substitutions) {
		this.substitutions = substitutions;
	}


	/**
	 * 
	 * Signal that unification is ready
	 * means that we unified an empty list
	 *
	 
	public void setUnifyReady() {
		if (!(node.getFormula().size()>1))
		this.unifyReady = true;
	}*/
}
