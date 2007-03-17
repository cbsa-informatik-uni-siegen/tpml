package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l1.L1TypeInferenceProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.TypeVariable;

public class L2TypeInferenceProofRuleSet extends L1TypeInferenceProofRuleSet {

	public L2TypeInferenceProofRuleSet(L1Language language) {
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
	//    registerByMethodName(L1Language.L1, "UNIFY", "applyunify");
		
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
