package de.unisiegen.tpml.core.languages.l4;

import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l3.L3TypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnitType;

public class L4TypeInferenceProofRuleSet extends L4TypeCheckerProofRuleSet {

	public L4TypeInferenceProofRuleSet(L4Language language) {
		super(language);
		
		// register the additional type rules
	    registerByMethodName(L4Language.L4, "UNIFY", "applyunify");
	}
	
	

    
    
    
    
    	 //
    	  // Unification
    	  //
    	  
    	  /**
    	   * This method is the heart of the unification algorithm implementation. It returns the unificator for
    	   * this type equation list.
    	 * @return 
    	   * 
    	   * @return the unificator for this type equation. 
    	   * 
    	   * @throws UnificationException if one of the equations contained within this list could not be unified.
    	   */
	public DefaultTypeSubstitution applyunify(TypeCheckerProofContext context, TypeCheckerProofNode pNode) throws UnificationException {
		
		    if (!(pNode instanceof DefaultTypeInferenceProofNode)) {
		    	return DefaultTypeSubstitution.EMPTY_SUBSTITUTION;
		    }
		    DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode) pNode;
    	    // an empty type equation list is easy to unify
    	    if (node.getEquations() == TypeEquationList.EMPTY_LIST ) {
    	    	
    	    	//TODO
    	    	// i have to implement this (just think about what to do here)
    	    return DefaultTypeSubstitution.EMPTY_SUBSTITUTION;
    	    }
    	    
    	    // otherwise, we examine the first equation in the list
    	    MonoType left = node.getEquations().first.getLeft();
    	    MonoType right = node.getEquations().first.getRight();
    	    
    	    // different actions, depending on the exact types
    	    if (left instanceof TypeVariable || right instanceof TypeVariable) {
    	      // the left or right side of the equation is a type variable
    	      TypeVariable tvar = (TypeVariable)(left instanceof TypeVariable ? left : right);
    	      MonoType tau = (left instanceof TypeVariable ? right : left);
    	      
    	      // either tvar equals tau or tvar is not present in tau
    	      if (tvar.equals(tau) || !tau.free().contains(tvar)) {
    	        DefaultTypeSubstitution s1 = new DefaultTypeSubstitution(tvar, tau);
//      	      TODO
//      	      i have to implement this (just think about what to do here)
    	        DefaultTypeSubstitution s2 = node.getEquations().remaining.substitute(s1).unify();
//    	      TODO
//    	      i have to implement this (just think about what to do here)
    	        return s1.compose(s2);
    	      }
    	      
    	      // FALL-THROUGH: Otherwise it's a type error
    	    }
    	    else if (left instanceof ArrowType && right instanceof ArrowType) {
    	      // cast to ArrowType instances (tau and tau')
    	      ArrowType taul = (ArrowType)left;
    	      ArrowType taur = (ArrowType)right;
    	      
    	      // we need to check {tau1 = tau1', tau2 = tau2'} as well
    	      TypeEquationList eqns = node.getEquations().remaining;
    	      eqns = eqns.extend(taul.getTau2(), taur.getTau2());
    	      eqns = eqns.extend(taul.getTau1(), taur.getTau1());
    	      
    	      // try to unify the new list
//    	      TODO
//    	      i have to implement this (just think about what to do here)
    	      return eqns.unify();
    	    }
    	    else if (left instanceof TupleType && right instanceof TupleType) {
    	      // cast to TupleType instances (tau and tau')
    	      TupleType taul = (TupleType)left;
    	      TupleType taur = (TupleType)right;
    	      
    	      // determine the sub types
    	      MonoType[] typesl = taul.getTypes();
    	      MonoType[] typesr = taur.getTypes();
    	      
    	      // check if the arities match
    	      if (typesl.length == typesr.length) {
    	        // check all sub types
    	        TypeEquationList eqns = node.getEquations().remaining;
    	        for (int n = 0; n < typesl.length; ++n) {
    	          eqns = eqns.extend(typesl[n], typesr[n]);
    	        }
    	        
    	        // try to unify the new list
//      	      TODO
//      	      i have to implement this (just think about what to do here)
    	        
    	        return eqns.unify();
    	      }
    	      
    	      // FALL-THROUGH: Otherwise it's a type error
    	    }
    	    else if (left instanceof RefType && right instanceof RefType) {
    	      // cast to RefType instances (tau and tau')
    	      RefType taul = (RefType)left;
    	      RefType taur = (RefType)right;

    	      // we need to check {tau = tau'} as well
    	      TypeEquationList eqns = node.getEquations().remaining;
    	      eqns = eqns.extend(taul.getTau(), taur.getTau());
    	      
    	      // try to unify the new list
//    	      TODO
//    	      i have to implement this (just think about what to do here)
    	      return eqns.unify();
    	    }
    	    else if (left instanceof ListType && right instanceof ListType) {
    	      // cast to ListType instances (tau and tau')
    	      ListType taul = (ListType)left;
    	      ListType taur = (ListType)right;
    	      
    	      // we need to check {tau = tau'} as well
    	      TypeEquationList eqns = node.getEquations().remaining;
    	      eqns = eqns.extend(taul.getTau(), taur.getTau());
    	      
    	      // try to unify the new list
//    	      TODO
//    	      i have to implement this (just think about what to do here)
    	      return eqns.unify();
    	    }
    	    else if (left.equals(right)) {
    	      // the types equal, just unify the remaining equations then
//      	      TODO
//      	      i have to implement this (just think about what to do here)
    	      return node.getEquations().remaining.unify();
    	    }
    	  
    	    // (left = right) cannot be unified
    	    throw new UnificationException(node.getEquations().first);
    	  }
      

}
