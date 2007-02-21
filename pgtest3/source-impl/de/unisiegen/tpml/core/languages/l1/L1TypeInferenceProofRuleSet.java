/**
 * TODO
 */
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
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.typeinference.AbstractTypeInferenceProofRuleSet;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofContext;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.BooleanType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TupleType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class L1TypeInferenceProofRuleSet extends L1TypeCheckerProofRuleSet{

	public L1TypeInferenceProofRuleSet(L1Language language) {
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
		
//		 register the additional type typeinferencerule
	    registerByMethodName(L1Language.L1, "UNIFY", "applyunify");
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr", "updateDefault");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp", "updateApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "CONST", "applyConst", "updateDefault");
	    registerByMethodName(L1Language.L1, "ID", "applyId", "updateDefault");
	    registerByMethodName(L1Language.L1, "LET", "applyLet", "updateDefault");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	   
		
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
	     // generate new child node and a TmpChild which will be generated later
	      InfixOperation infixOperation = (InfixOperation)pNode.getExpression();
	      Application application = new Application(infixOperation.getOp(), infixOperation.getE1());
	      context.addProofNode(pNode, pNode.getEnvironment(), application, tau1);
	      node.setTmpEnvironment(pNode.getEnvironment());
	      node.setTmpExpression( application.getE2());
	      node.setTmpType(tau2);
	      //Set Signal for waiting Child to be generated
	      node.setTmpChild(true);
	     // context.addProofNode(pNode, pNode.getEnvironment(),
			// infixOperation.getE2(), tau2);
	    }
	  }
	  
  
	public void updateApp(TypeCheckerProofContext context, TypeCheckerProofNode pNode) {
		
		boolean createchild=true;
		//check if first child of App is finished
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
			//generate second child wiht Equations from first child and remove Signal
			context.addProofNode(pNode, node.getTmpEnvironment(), node.getTmpExpression(), node.getTmpType(), node.getFirstChild().getEquations());
			node.setTmpChild(false);
		}
		
		
	}

}
