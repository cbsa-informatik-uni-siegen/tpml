package de.unisiegen.tpml.core.languages.l4;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l3.L3SubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RefType;
import de.unisiegen.tpml.core.types.TupleType;

public class L4SubTypingProofRuleSet extends L3SubTypingProofRuleSet {

	public L4SubTypingProofRuleSet ( Language language ) {
		super ( language );

    // register the type rules
		registerByMethodName ( L1Language.L1, "REF", "applyRef" ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void applyRef ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		RefType type;
		RefType type2;

		type = ( RefType ) node.getType ( );
		type2 = ( RefType ) node.getType2 ( );
		
		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );
		
		context.addProofNode ( node, tau, tau2 );
		
	}

}
