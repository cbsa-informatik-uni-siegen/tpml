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
	 //   registerByMethodName(L1Language.L1, "UNIFY", "applyUnify");
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "LET", "applyLet");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	    registerByMethodName(L2Language.L2, "REC", "applyRec");
	    registerByMethodName(L3Language.L3, "LIST", "applyList");
	    registerByMethodName(L3Language.L3, "P-CONST", "applyPConst");
	    registerByMethodName(L3Language.L3, "P-ID", "applyPId");
	    registerByMethodName(L3Language.L3, "P-LET", "applyPLet");
	    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple");		
		registerByMethodName(L4Language.L4, "COND-1", "applyCond1");
	    registerByMethodName(L4Language.L4, "SEQ", "applySeq");
	    registerByMethodName(L4Language.L4, "WHILE", "applyWhile");
		
		
	}
}
