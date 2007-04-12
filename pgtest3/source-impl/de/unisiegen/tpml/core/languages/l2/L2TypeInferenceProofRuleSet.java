package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.languages.l1.L1Language;

public class L2TypeInferenceProofRuleSet extends L2TypeCheckerProofRuleSet {

	public L2TypeInferenceProofRuleSet(L2Language language) {
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
	    registerByMethodName(L1Language.L1, "UNIFY", "applyUnify");
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "CONST", "applyConst");
	    registerByMethodName(L1Language.L1, "ID", "applyId");
	    registerByMethodName(L1Language.L1, "LET", "applyLet");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	    registerByMethodName(L2Language.L2, "REC", "applyRec");
	
	}

}
