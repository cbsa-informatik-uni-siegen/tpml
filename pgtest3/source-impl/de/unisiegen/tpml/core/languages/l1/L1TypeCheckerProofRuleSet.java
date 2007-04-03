package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.Constant;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typechecker.UnificationException;
import de.unisiegen.tpml.core.typeinference.DefaultTypeEquationProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.TypeEquation;
import de.unisiegen.tpml.core.typeinference.TypeEquationList;
import de.unisiegen.tpml.core.typeinference.UnifyException;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
/**
 * The type proof rules for the <code>L1</code> language.
 *
 * @author Benedikt Meurer
 * @version $Id: Lt1TypeCheckerProofRuleSet.java 272 2006-09-19 15:55:48Z benny $
 *
 * @see de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet
 */
public class L1TypeCheckerProofRuleSet extends AbstractTypeCheckerProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L1TypeCheckerProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  public L1TypeCheckerProofRuleSet(L1Language language) {
    super(language);
    
    // register the type rules
    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");
    registerByMethodName(L2Language.L2, "AND", "applyAnd");
    registerByMethodName(L1Language.L1, "APP", "applyApp");
    registerByMethodName(L1Language.L1, "COND", "applyCond");
    registerByMethodName(L1Language.L1, "CONST", "applyConst");
    registerByMethodName(L1Language.L1, "ID", "applyId");
    registerByMethodName(L1Language.L1, "LET", "applyLet");
    registerByMethodName(L2Language.L2, "OR", "applyOr");
  }
  
  
  
  //
  // The (AND) rule
  //
  
  /**
   * Applies the <b>(AND)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyAnd(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    And and = (And)node.getExpression();
    
    // generate new child nodes
    context.addProofNode(node, node.getEnvironment(), and.getE1(), new BooleanType());
    context.addProofNode(node, node.getEnvironment(), and.getE2(), new BooleanType());
    
    // add the {tau = bool} equation
    context.addEquation(node.getType(), new BooleanType());
  }
  
  
  
  //
  // The (ABSTR) rule
  //
  
  /**
   * Applies the <b>(ABSTR)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyAbstr(TypeCheckerProofContext context, TypeCheckerProofNode node) {
	  
    // determine the type environment
    TypeEnvironment environment = node.getEnvironment();
    
    // can be applied to both Lambda and MultiLambda
    Expression expression = node.getExpression();
    if (expression instanceof Lambda) {
      // determine the type for the parameter 
      Lambda lambda = (Lambda)expression;
      MonoType tau1 = lambda.getTau();
      if (tau1 == null) {
        // need a new type variable
        tau1 = context.newTypeVariable();
      }
      
      // generate a new type variable for the result
      TypeVariable tau2 = context.newTypeVariable();
      
      // add type equations for tau and tau1->tau2
      context.addEquation(node.getType(), new ArrowType(tau1, tau2));
      
      // generate a new child node
      context.addProofNode(node, environment.extend(lambda.getId(), tau1), lambda.getE(), tau2);
    }
    else {
      // determine the type for the parameter
      MultiLambda multiLambda = (MultiLambda)expression;
      String[] identifiers = multiLambda.getIdentifiers();

      // generate the type for identifiers (tau1)
      TypeVariable[] typeVariables = new TypeVariable[identifiers.length];
      for (int n = 0; n < identifiers.length; ++n) {
        typeVariables[n] = context.newTypeVariable();
      }
      TupleType tau1 = new TupleType(typeVariables);

      // generate the type variable for the result
      TypeVariable tau2 = context.newTypeVariable();
      
      // add type equations for tau and tau1->tau2
      context.addEquation(node.getType(), new ArrowType(tau1, tau2));
      
      // generate the environment for e
      for (int n = 0; n < identifiers.length; ++n) {
        environment = environment.extend(identifiers[n], typeVariables[n]);
      }
      
      // add the child nodes
      context.addProofNode(node, environment, multiLambda.getE(), tau2);
      
      // check if we have a type
      if (multiLambda.getTau() != null) {
        // add an equation for tau1 = multiLet.getTau()
        context.addEquation(tau1, multiLambda.getTau());
      }
    }
  }
  
  
  
  //
  // The (APP) rule
  //
  
  /**
   * Applies the <b>(APP)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyApp(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    // split into tau1 and tau2 for the application
    TypeVariable tau2 = context.newTypeVariable();
    ArrowType tau1 = new ArrowType(tau2, node.getType());
    
    // can be either an application or an infix operation
    try {
      // generate new child nodes
      Application application = (Application)node.getExpression();
      context.addProofNode(node, node.getEnvironment(), application.getE1(), tau1);
      context.addProofNode(node, node.getEnvironment(), application.getE2(), tau2);
    }
    catch (ClassCastException e) {
      // generate new child nodes
      InfixOperation infixOperation = (InfixOperation)node.getExpression();
      Application application = new Application(infixOperation.getOp(), infixOperation.getE1());
      context.addProofNode(node, node.getEnvironment(), application, tau1);
      context.addProofNode(node, node.getEnvironment(), infixOperation.getE2(), tau2);
    }
  }
  
  
  
  //
  // The (COND) rule
  //
  
  /**
   * Applies the <b>(COND)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyCond(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Condition condition = (Condition)node.getExpression();
    context.addProofNode(node, node.getEnvironment(), condition.getE0(), new BooleanType());
    context.addProofNode(node, node.getEnvironment(), condition.getE1(), node.getType());
    context.addProofNode(node, node.getEnvironment(), condition.getE2(), node.getType());
  }
  
  
  
  //
  // The (CONST) rule
  //
  
  /**
   * Applies the <b>(CONST)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyConst(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Constant constant = (Constant)node.getExpression();
    context.addEquation(node.getType(), (MonoType)context.getTypeForExpression(constant));
  }
  
  
  
  //
  // The (ID) rule
  //
  
  /**
   * Applies the <b>(ID)</b> rule to the <code>node</node> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyId(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Type type = node.getEnvironment().get(((Identifier)node.getExpression()).getName());
    context.addEquation(node.getType(), (MonoType)type);
  }
  
  
  
  //
  // The (LET) rule
  //
  
  /**
   * Applies the <b>(LET)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyLet(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    // determine the type environment
    TypeEnvironment environment = node.getEnvironment();
    
    // can be applied to LetRec, Let, MultiLet, CurriedLetRec and CurriedLet
    Expression expression = node.getExpression();
    if (expression instanceof Let) {
      // determine the first sub expression
      Let let = (Let)expression;
      Expression e1 = let.getE1();
      
      // check if a type was specified
      MonoType tau1 = let.getTau();
      if (tau1 == null) {
        tau1 = context.newTypeVariable();
      }
      
      // add the recursion for let rec
      if (expression instanceof LetRec) {
        // add the recursion for e1
        e1 = new Recursion(let.getId(), tau1, e1);
      }
      
      // add the child nodes
      context.addProofNode(node, environment, e1, tau1);
      context.addProofNode(node, environment.extend(let.getId(), tau1), let.getE2(), node.getType());
    }
    else if (expression instanceof MultiLet) {
      // determine the identifiers of the multi let
      MultiLet multiLet = (MultiLet)expression;
      String[] identifiers = multiLet.getIdentifiers();

      // generate the type for e1
      TypeVariable[] typeVariables = new TypeVariable[identifiers.length];
      for (int n = 0; n < identifiers.length; ++n) {
        typeVariables[n] = context.newTypeVariable();
      }
      TupleType tau = new TupleType(typeVariables);
      
      // generate the environment for e2
      TypeEnvironment environment2 = environment;
      for (int n = 0; n < identifiers.length; ++n) {
        environment2 = environment2.extend(identifiers[n], typeVariables[n]);
      }
      
      // add the child nodes
      context.addProofNode(node, environment, multiLet.getE1(), tau);
      context.addProofNode(node, environment2, multiLet.getE2(), node.getType());
      
      // check if we have a type
      if (multiLet.getTau() != null) {
        // add an equation for tau = multiLet.getTau()
        context.addEquation(tau, multiLet.getTau());
      }
    }
    else {
      // determine the first sub expression
      CurriedLet curriedLet = (CurriedLet)expression;
      Expression e1 = curriedLet.getE1();
      
      // generate the appropriate lambda abstractions
      MonoType[] types = curriedLet.getTypes();
      String[] identifiers = curriedLet.getIdentifiers();
      for (int n = identifiers.length - 1; n > 0; --n) {
        e1 = new Lambda(identifiers[n], types[n], e1);
      }
      
      // generate the type of the function
      MonoType tau1 = types[0];
      if (tau1 == null) {
        tau1 = context.newTypeVariable();
      }
      for (int n = types.length - 1; n > 0; --n) {
        tau1 = new ArrowType((types[n] != null) ? types[n] : context.newTypeVariable(), tau1);
      }
      
      // add the recursion for let rec
      if (expression instanceof CurriedLetRec) {
        // add the recursion
        e1 = new Recursion(identifiers[0], tau1, e1);
      }
      
      // add the child nodes
      context.addProofNode(node, environment, e1, tau1);
      context.addProofNode(node, environment.extend(identifiers[0], tau1), curriedLet.getE2(), node.getType());
    }
  }
  
  
  
  //
  // The (OR) rule
  //
  
  /**
   * Applies the <b>(OR)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the type checker proof context.
   * @param node the type checker proof node.
   */
  public void applyOr(TypeCheckerProofContext context, TypeCheckerProofNode node) {
    Or or = (Or)node.getExpression();
    
    // generate new child nodes
    context.addProofNode(node, node.getEnvironment(), or.getE1(), new BooleanType());
    context.addProofNode(node, node.getEnvironment(), or.getE2(), new BooleanType());
    
    // add the {tau = bool} equation
    context.addEquation(node.getType(), new BooleanType());
  }
  
  
  //
  // The unify rule
  //
  
  /**
   * Applies the <b>(UNIFY)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param pContext the type inference proof context.
   * @param pNode the type inference proof node.
   * @throws UnificationException 
   */
  public void applyUnify(TypeCheckerProofContext pContext, TypeCheckerProofNode pNode) throws UnifyException{
	  DefaultTypeInferenceProofContext context = (DefaultTypeInferenceProofContext) pContext;
	  DefaultTypeEquationProofNode node = (DefaultTypeEquationProofNode) pNode ;
	  TypeEquation eqn = node.getEquation();
	  /**
	  // if the TypeEquationList is an empty list, we are ready wiht unification
	  if (eqns.equals( TypeEquationList.EMPTY_LIST))
	  {
		  context.setUnifyReady();
		  return;
	  }*/
	  
	  // otherwise, we examine the first equation in the list
	  MonoType left = eqn.getLeft();
	  MonoType right = eqn.getRight();
	  
	  if (left instanceof TypeVariable || right instanceof TypeVariable)
	  {
		    // the left or right side of the equation is a type variable
	      TypeVariable tvar = (TypeVariable)(left instanceof TypeVariable ? left : right);
	      MonoType tau = (left instanceof TypeVariable ? right : left);
	      // either tvar equals tau or tvar is not present in tau
	      if (tvar.equals(tau) || !tau.free().contains(tvar)) {
	    	  
	        DefaultTypeSubstitution s = new DefaultTypeSubstitution(tvar, tau);
		  
		 
			  
			  // now have to substitude remaining eqns
			  //eqns= eqns.getRemaining();
			  //context.setEquations(eqns);
	        
	        context.substitute(s, eqn);
			  context.addSubstitution(s);
			  context.popEquation();
			  return;
		  }
	  }
	  else if (left instanceof ArrowType && right instanceof ArrowType){
		  ArrowType taul = (ArrowType)left;
	      ArrowType taur = (ArrowType)right;
	      
	      // we need to check {tau1 = tau1', tau2 = tau2'} as well
	      //eqns = eqns.getRemaining();
	      //context.setEquations(eqns);
	      context.popEquation();
	      context.addEquation(taul.getTau2(), taur.getTau2());
	      context.addEquation(taul.getTau1(), taur.getTau1());
	      return;
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
           // context.setEquations(eqns.getRemaining());
            for (int n = 0; n < typesl.length; ++n) {
              context.addEquation(typesl[n], typesr[n]);
            }
            return;
          }
//        generate new child nodes
//        context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
          //context.setEquations(eqns.getRemaining());
          return ;
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
            //context.setEquations(eqns.getRemaining());
            for (int n = 0; n < typesl.length; ++n) {
              context.addEquation(typesl[n], typesr[n]);
            }
            
            return;
          }
          
          // FALL-THROUGH: Otherwise it's a type error
        }
        else if (left instanceof RefType && right instanceof RefType) {
          // cast to RefType instances (tau and tau')
          RefType taul = (RefType)left;
          RefType taur = (RefType)right;

          // we need to check {tau = tau'} as well
         // context.setEquations(eqns.getRemaining());
          context.addEquation(taul.getTau(), taur.getTau());
          
          
          return;
        }
        else if (left instanceof ListType && right instanceof ListType) {
          // cast to ListType instances (tau and tau')
          ListType taul = (ListType)left;
          ListType taur = (ListType)right;
          
          // we need to check {tau = tau'} as well
         // context.setEquations(eqns.getRemaining());
          context.addEquation(taul.getTau(), taur.getTau());
          
          return;
        }
	  else if (left.equals(right))
	  {
		  //context.setEquations(eqns.getRemaining());
		  return;
	  }   
	  throw new UnifyException(eqn);  
  }
  
  /**
public void applyUnify(TypeCheckerProofContext context, TypeCheckerProofNode pNode) throws UnificationException {
	
	
    if (!(pNode instanceof DefaultTypeInferenceProofNode)) {
    	return ;
    }
    DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode) pNode;
    // an empty type equation list is easy to unify
    if (node.getEquations() == TypeEquationList.EMPTY_LIST ) {
    	
    	//TODO
    	// i have to implement this (just think about what to do here)
    	
    	
    return ;
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
        node.addSubstitution(s1);
       
//	      TODO
//	      i have to implement this (just think about what to do here)
        TypeEquationList eqns=node.getEquations().remaining.substitute(s1);
        node.setType(tau);
//      TODO
//      i have to implement this (just think about what to do here)
        //return s1.compose(s2);
//      generate new child nodes
        context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
        return;
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
      
      
      
//      TODO
//      i have to implement this (just think about what to do here)
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
//	      TODO
//	      i have to implement this (just think about what to do here)
//      generate new child nodes
        context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
        
        
        
        return;
      }
//    generate new child nodes
      context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
      
      return ;
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
//	      TODO
//	      i have to implement this (just think about what to do here)
//      generate new child nodes
        context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
        
        
        
        return;
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
//      TODO
//      i have to implement this (just think about what to do here)
      
//    generate new child nodes
      context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
      
      return;
    }
    else if (left instanceof ListType && right instanceof ListType) {
      // cast to ListType instances (tau and tau')
      ListType taul = (ListType)left;
      ListType taur = (ListType)right;
      
      // we need to check {tau = tau'} as well
      TypeEquationList eqns = node.getEquations().remaining;
      eqns = eqns.extend(taul.getTau(), taur.getTau());
      
      // try to unify the new list
//      TODO
//      i have to implement this (just think about what to do here)
      
//    generate new child nodes
      context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), eqns);
      
      return;
    }
    else if (left.equals(right)) {
      // the types equal, just unify the remaining equations then
//	      TODO
//	      i have to implement this (just think about what to do here)
    	
//    	 generate new child nodes
        context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType(), node.getEquations().remaining );
        
      return ;
    }
  
    // (left = right) cannot be unified
    throw new UnificationException(node.getEquations().first);
  }



*/

}
