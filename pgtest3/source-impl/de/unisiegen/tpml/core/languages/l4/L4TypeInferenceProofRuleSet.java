package de.unisiegen.tpml.core.languages.l4;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Condition1;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.expressions.Sequence;
import de.unisiegen.tpml.core.expressions.While;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l3.L3Language;
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
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.types.UnitType;

public class L4TypeInferenceProofRuleSet extends L4TypeCheckerProofRuleSet {

	public L4TypeInferenceProofRuleSet(L4Language language) {
		super(language);
		
		
//		unregister the type rules
	    unregister("ABSTR");
	    unregister("AND");
	    unregister("APP");
	    unregister("COND");
	    unregister("LET");
	    unregister("OR");
	    unregister("REC");
	    unregister("COND-1");
	    unregister("SEQ");
	    unregister("WHILE");
		
//		 register the additional typeinference rule
	    registerByMethodName(L1Language.L1, "UNIFY", "applyUnify");
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp", "updateApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "LET", "applyLet");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	    registerByMethodName(L2Language.L2, "REC", "applyRec");
	    registerByMethodName(L3Language.L3, "LIST", "applyList");
	    registerByMethodName(L3Language.L3, "P-CONST", "applyPConst");
	    registerByMethodName(L3Language.L3, "P-ID", "applyPId");
	    registerByMethodName(L3Language.L3, "P-LET", "applyPLet", "updatePLet");
	    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple");		
		registerByMethodName(L4Language.L4, "COND-1", "applyCond1");
	    registerByMethodName(L4Language.L4, "SEQ", "applySeq");
	    registerByMethodName(L4Language.L4, "WHILE", "applyWhile");
		
		
	}
	
	
	@Override
	  public void applyPId(TypeCheckerProofContext context, TypeCheckerProofNode node) {
			    Type type = node.getEnvironment().get(((Identifier)node.getExpression()).getName());
			    context.addEquation(node.getType(), context.instantiate(type));
//			    generate new child nodes
			    //  context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType());
			      
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
	@Override
	  public void applyApp(TypeCheckerProofContext context, TypeCheckerProofNode pNode) {
		
		DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode) pNode;
	    // split into tau1 and tau2 for the application
		TypeVariable tau2 = context.newTypeVariable();
	    ArrowType tau1 = new ArrowType(tau2, pNode.getType());
	    
	    // can be either an application or an infix operation
	    try {
	    	
	    	//TODO Benny
	    	//have to generate second child in update method
	      // generate new child nodes
	      Application application = (Application)pNode.getExpression();
	      context.addProofNode(pNode, pNode.getEnvironment(), application.getE1(), tau1);
	      node.setTmpEnvironment(pNode.getEnvironment());
	      node.setTmpExpression( application.getE2());
	      node.setTmpType(tau2);
	      
	      //context.addProofNode(pNode, pNode.getEnvironment(), application.getE2(), tau2);
	    }
	    catch (ClassCastException e) {
	      // generate new child nodes
	      InfixOperation infixOperation = (InfixOperation)pNode.getExpression();
	      Application application = new Application(infixOperation.getOp(), infixOperation.getE1());
	      context.addProofNode(pNode, pNode.getEnvironment(), application, tau1);
	      node.setTmpEnvironment(pNode.getEnvironment());
	      node.setTmpExpression( application.getE2());
	      node.setTmpType(tau2);
	      node.setTmpChild(true);
	     // context.addProofNode(pNode, pNode.getEnvironment(), infixOperation.getE2(), tau2);
	    }
	  }
	  
    
	public void updateApp(TypeCheckerProofContext context, TypeCheckerProofNode pNode) {
		
		boolean createchild=true;
		for (int i=0; i<pNode.getChildCount(); i++)
		{
			DefaultTypeInferenceProofNode child=(DefaultTypeInferenceProofNode)pNode.getChildAt(i);
			if (!child.isFinished())
			{
				createchild=false;
				break;
			}
			
		}
		
		DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode)pNode;
		if (createchild && node.hasTmpChild())
		{
			
			context.addProofNode(pNode, node.getTmpEnvironment(), node.getTmpExpression(), node.getTmpType());
			node.setTmpChild(false);
		}
		 
		
		
	}


    
    	
      

}
