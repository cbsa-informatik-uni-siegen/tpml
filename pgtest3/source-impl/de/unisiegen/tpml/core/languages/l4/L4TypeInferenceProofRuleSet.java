package de.unisiegen.tpml.core.languages.l4;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.MultiLet;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l3.L3Language;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.TypeVariable;

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
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr", "updateDefault");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp", "updateApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "LET", "applyLet", "updateDefault");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	    registerByMethodName(L2Language.L2, "REC", "applyRec", "updateDefault");
	    registerByMethodName(L3Language.L3, "LIST", "applyList");
	    registerByMethodName(L3Language.L3, "P-CONST", "applyPConst");
	    registerByMethodName(L3Language.L3, "P-ID", "applyPId");
	    registerByMethodName(L3Language.L3, "P-LET", "applyPLet", "updatePLet");
	    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple");		
		registerByMethodName(L4Language.L4, "COND-1", "applyCond1");
	    registerByMethodName(L4Language.L4, "SEQ", "applySeq");
	    registerByMethodName(L4Language.L4, "WHILE", "applyWhile");
		
		
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
		TypeVariable tau2 = context.newTypeVariable();
	    ArrowType tau1 = new ArrowType(tau2, node.getType());
		 Expression e = node.getExpression ( ) ;
	    
	    // can be either an application or an infix operation
		try
		{
			// otherwise it must be an infix operation
			Application application = (Application)e;
			context.addProofNode(pNode, pNode.getEnvironment(), application.getE1(), tau1);
	    }
		catch (ClassCastException e1)
		{
	     // generate new child node and a TmpChild which will be generated later
	      InfixOperation infixOperation = (InfixOperation)e;
	      Application application = new Application(infixOperation.getOp(), infixOperation.getE1());
	      context.addProofNode(pNode, pNode.getEnvironment(), application, tau1);
	    }
	  }
	
    
	public void applyLet(TypeCheckerProofContext context, TypeCheckerProofNode pNode) {
		DefaultTypeInferenceProofNode node = (DefaultTypeInferenceProofNode) pNode;
		TypeVariable tau = context.newTypeVariable();
		Expression expression=node.getExpression();
		
//			 generate new child node, second Child will be generated in update
		      Let let = (Let)expression;
		      context.addProofNode(pNode, pNode.getEnvironment(), let.getE1(), tau);
	} 
	
}
