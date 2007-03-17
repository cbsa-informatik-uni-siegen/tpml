/**
 * TODO
 */
package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typeinference.DefaultTypeInferenceProofNode;
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
	 //   registerByMethodName(L1Language.L1, "UNIFY", "applyunify");
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");
	    registerByMethodName(L1Language.L1, "APP", "applyApp");
	    registerByMethodName(L1Language.L1, "COND", "applyCond");
	    registerByMethodName(L1Language.L1, "CONST", "applyConst");
	    registerByMethodName(L1Language.L1, "ID", "applyId");
	    registerByMethodName(L1Language.L1, "LET", "applyLet");
	    registerByMethodName(L2Language.L2, "OR", "applyOr");
	   
		
	}
}
