package de.unisiegen.tpml.core.languages.l4;

import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l3.L3Language;

/**
 * The type proof rules for the <code>L4</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet
 */
public class L4TypeInferenceProofRuleSet extends L4TypeCheckerProofRuleSet {

  /**
	 * Allocates a new <code>L4TypeInferenceProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L4</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L4TypeInferenceProofRuleSet(L4Language language) {
		super(language);
		
	//	unregister("P-LET");
	
	    unregister("ABSTR"); //$NON-NLS-1$
	    unregister("AND"); //$NON-NLS-1$
	    unregister("APP"); //$NON-NLS-1$
	    unregister("COND"); //$NON-NLS-1$
	    unregister ("LET"); //$NON-NLS-1$
	    unregister("OR"); //$NON-NLS-1$
	    unregister("REC"); //$NON-NLS-1$
	    unregister("LIST"); //$NON-NLS-1$
	    unregister("P-CONST"); //$NON-NLS-1$
	    unregister("P-ID"); //$NON-NLS-1$
	    unregister("P-LET"); //$NON-NLS-1$
	    unregister("TUPLE"); //$NON-NLS-1$
	    unregister("COND-1"); //$NON-NLS-1$
	    unregister("SEQ"); //$NON-NLS-1$
	    unregister("WHILE"); //$NON-NLS-1$
	    
//		 register the additional typeinference rule
	    registerByMethodName(L1Language.L1, "UNIFY", "applyUnify");  //$NON-NLS-1$ //$NON-NLS-2$
		
//		 register the type rules
	    registerByMethodName(L1Language.L1, "ABSTR", "applyAbstr");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L2Language.L2, "AND", "applyAnd");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L1Language.L1, "APP", "applyApp");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L1Language.L1, "COND", "applyCond");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L1Language.L1, "LET", "applyLet");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L2Language.L2, "OR", "applyOr");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L2Language.L2, "REC", "applyRec");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L3Language.L3, "LIST", "applyList");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L3Language.L3, "P-CONST", "applyPConst");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L3Language.L3, "P-ID", "applyPId");  //$NON-NLS-1$ //$NON-NLS-2$
	   // registerByMethodName(L3Language.L3, "P-LET", "applyPLet");
	    registerByMethodName(L3Language.L3, "TUPLE", "applyTuple");		  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L4Language.L4, "COND-1", "applyCond1");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L4Language.L4, "SEQ", "applySeq");  //$NON-NLS-1$ //$NON-NLS-2$
	    registerByMethodName(L4Language.L4, "WHILE", "applyWhile");  //$NON-NLS-1$ //$NON-NLS-2$
		
	}
}
