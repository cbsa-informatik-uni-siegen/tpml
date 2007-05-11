package de.unisiegen.tpml.core.languages.l2o;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2SubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;

public class L2OSubTypingProofRuleSet extends L2SubTypingProofRuleSet {

	public L2OSubTypingProofRuleSet ( Language language ) {
		super ( language );
		
		// register the type rules
	//	registerByMethodName ( L1Language.L1, "TRANS", "applyTrans" ); //$NON-NLS-1$ //$NON-NLS-2$
	//	registerByMethodName ( L1Language.L1, "OBJECT-WIDTH", "applyObjectWidth" ); //$NON-NLS-1$ //$NON-NLS-2$
	//	registerByMethodName ( L1Language.L1, "OBJECT-DEPTH", "applyObjectDepth" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	
//	public void applyTrans ( SubTypingProofContext context,
//			SubTypingProofNode node ) throws SubTypingException {
		
//	}
}
