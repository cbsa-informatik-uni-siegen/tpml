package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.types.Type;

public class L3TypeInferenceProofRuleSet extends L3TypeCheckerProofRuleSet {

	public L3TypeInferenceProofRuleSet(L3Language language) {
		super(language);
		

//		unregister the type rules
	    unregister("ABSTR");
	    unregister("AND");
	    unregister("APP");
	    unregister("COND");
	    unregister("CONST");
	    unregister("ID");
	    unregister("LET");
	    unregister("OR");
	    unregister("REC");
		
//		 register the additional typeinference rule
	    registerByMethodName(L1Language.L1, "UNIFY", "applyunify");
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp","updateApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "LET", "applyLet");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	    registerByMethodName(L2Language.L2, "REC", "applyRec");
	    registerByMethodName(L3Language.L3, "LIST", "applyList");
	    registerByMethodName(L3Language.L3, "P-CONST", "applyPConst");
	    registerByMethodName(L3Language.L3, "P-ID", "applyPId", "updatePId");
	    registerByMethodName(L3Language.L3, "P-LET", "applyPLet", "updatePLet");
	    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple");
	
	}
	
	@Override
	  public void applyPId(TypeCheckerProofContext context, TypeCheckerProofNode node) {
		    Type type = node.getEnvironment().get(((Identifier)node.getExpression()).getName());
		    context.addEquation(node.getType(), context.instantiate(type));
//		    generate new child nodes
		     // context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType());
		      
		  }
	
	public void updateApp(TypeCheckerProofContext context, TypeCheckerProofNode node) {
		DefaultTypeInferenceProofNode child=(DefaultTypeInferenceProofNode)node.getFirstChild();
		boolean createchild=true;
		while (child!=null)
		{
			if (!child.isFinished())
			{
				createchild=false;
				break;
			}
		}
		if (createchild)
			context.addProofNode(node, node.getEnvironment(), node.getExpression(), node.getType());
	}
	
	  public void updatePId(TypeCheckerProofContext context, TypeCheckerProofNode pNode) {
	/**	  System.out.println("War hier");
		 TypeCheckerProofNode root =pNode.getRoot();
		  if ( root.isFinished())
		  {
			  DefaultTypeInferenceProofNode node= (DefaultTypeInferenceProofNode) pNode;
			  context.addProofNode(root, node.getEnvironment(), node.getExpression(), node.getType(), node.getEquations());
				
		  } */
	  }
    

}
