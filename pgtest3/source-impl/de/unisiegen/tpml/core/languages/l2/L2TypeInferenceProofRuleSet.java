package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.languages.l1.L1Language;

/**
 * The type proof rules for the <code>L2</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet
 */
public class L2TypeInferenceProofRuleSet extends L2TypeCheckerProofRuleSet {

	/**
	 * Allocates a new <code>L2TypeInferenceProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L2</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2TypeInferenceProofRuleSet ( L2Language language ) {
		super ( language );

		//		unregister the type rules
		unregister ( "ABSTR" ); //$NON-NLS-1$
		unregister ( "AND" ); //$NON-NLS-1$
		unregister ( "APP" ); //$NON-NLS-1$
		unregister ( "COND" ); //$NON-NLS-1$
		unregister ( "CONST" ); //$NON-NLS-1$
		unregister ( "ID" ); //$NON-NLS-1$
		unregister ( "LET" ); //$NON-NLS-1$
		unregister ( "OR" ); //$NON-NLS-1$
		unregister ( "REC" ); //$NON-NLS-1$

		//		 register the additional typeinference rule
		registerByMethodName ( L1Language.L1, "UNIFY", "applyUnify" ); //$NON-NLS-1$ //$NON-NLS-2$

		//		 register the type rules
		registerByMethodName ( L1Language.L1, "ABSTR", "applyAbstr" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2Language.L2, "AND", "applyAnd" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "APP", "applyApp" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "COND", "applyCond" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "CONST", "applyConst" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "ID", "applyId" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "LET", "applyLet" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2Language.L2, "OR", "applyOr" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2Language.L2, "REC", "applyRec" ); //$NON-NLS-1$ //$NON-NLS-2$

	}

}
