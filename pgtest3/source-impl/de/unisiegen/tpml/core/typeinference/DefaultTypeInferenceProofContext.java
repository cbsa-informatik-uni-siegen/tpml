package de.unisiegen.tpml.core.typeinference;

import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeSet;

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
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
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
 * 
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeInferenceProofContext  implements TypeInferenceProofContext, TypeCheckerProofContext{

	
	  
	//
	// Attributes
	//
	  

	
	private int offset = 0;
	
	private TypeEquationList equations=TypeEquationList.EMPTY_LIST;
	
	private TypeInferenceProofModel model;
	
	private DefaultTypeInferenceProofNode node;
	
	
	/**
	 * 
	 *
	 * @param model
	 * @param node
	 */
	public DefaultTypeInferenceProofContext(TypeInferenceProofModel model, DefaultTypeInferenceProofNode pNode) {
		this.model=model;
		this.node=pNode;
		this.equations=pNode.getEquations();
	}


	
	/**
	 * 
	 *
	 * @param node
	 * @param environment
	 * @param expression
	 * @param type
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addProofNode(de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode, de.unisiegen.tpml.core.typechecker.TypeEnvironment, de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.types.MonoType)
	 */
	public void addProofNode(TypeCheckerProofNode pNode, TypeEnvironment environment, Expression expression, MonoType type) {
		// TODO Think about what to do here
		
		  
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
			    
			    final DefaultTypeCheckerProofNode child = new DefaultTypeCheckerProofNode(environment, expression, type);
		        ((DefaultTypeCheckerProofNode)pNode).add(child);
		//this.model.contextAddProofNode(this, node, environment, expression, type, equations);
	} 
	
	/**
	 * 
	 *
	 * @param expression
	 * @return
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
		 * 
		 *
		 * @param type
		 * @return
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
	 * 
	 *
	 * @return
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#newTypeVariable()
	 */
	public TypeVariable newTypeVariable() {
	    return new TypeVariable(this.model.getIndex(), this.offset++);

	}
	
	/**
	 * 
	 *
	 * @param left
	 * @param right
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext#addEquation(de.unisiegen.tpml.core.types.MonoType, de.unisiegen.tpml.core.types.MonoType)
	 */
	public void addEquation(MonoType left, MonoType right) {
	    equations=equations.extend(left, right);
	 
	  }
	  
	 //
	  // Rule application
	  //
	  
	void apply(TypeCheckerProofRule rule, TypeFormula formula, MonoType type) throws ProofRuleException, UnificationException {
	    
	
		
		DefaultTypeCheckerProofNode typeNode = new DefaultTypeCheckerProofNode(formula.getEnvironment(), formula.getExpression(), formula.getType());
		
	    // try to apply the rule to the node
		rule.apply(this, typeNode);
	    //  record the proof step for the node
	    this.model.contextSetProofNodeRule(this, node, rule, formula);
	    
	    // check if the user specified a type
	    if (type != null) {
	      // add an equation for { node.getType() = type }
	     // addEquation(node.getType(), type);
	    }
	
	    // unify the type equations and apply the substitution to the model
	    //this.model.contextApplySubstitution(this, this.equations.unify());
	    
	    /** think about if we have any update function
	    // update all super nodes
	    for (;;) {
	      // determine the parent node
	      DefaultTypeCheckerProofNode parentNode = (DefaultTypeCheckerProofNode) node.getParent();
	     if (parentNode == null) {
	        break;
	      }
	        
	      // update the parent node (using the previously applied rule)
	      parentNode.getRule().update(this, parentNode);
	      
	      
	      // continue with the next one
	      node = parentNode;
	    }*/
	    
	    
	    LinkedList<TypeFormula> formulas=new LinkedList<TypeFormula>();
	    for (int i=0; i< typeNode.getChildCount(); i++)
        {
	    	TypeJudgement judgement= new TypeJudgement((DefaultTypeEnvironment)typeNode.getChildAt(i).getEnvironment(), typeNode.getChildAt(i).getExpression(), typeNode.getChildAt(i).getType());
	    	formulas.add(judgement);
        }
	    this.model.contextAddProofNode(node, formulas, equations);
	  }

}
