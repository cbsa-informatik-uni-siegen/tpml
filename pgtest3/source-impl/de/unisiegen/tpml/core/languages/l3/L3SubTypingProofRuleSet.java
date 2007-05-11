package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2SubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TupleType;

public class L3SubTypingProofRuleSet extends L2SubTypingProofRuleSet {

	public L3SubTypingProofRuleSet ( Language language ) {
		super ( language );
    
		// register the type rules
		registerByMethodName ( L1Language.L1, "PODUCT", "applyProduct" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "LIST", "applyList" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void applyProduct ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		TupleType type;
		TupleType type2;

		type = ( TupleType ) node.getType ( );
		type2 = ( TupleType ) node.getType2 ( );

		MonoType[] types = type.getTypes ( );
		MonoType[] types2 = type2.getTypes ( );

		if ( types.length == types2.length ) {
			for ( int i = 0; i < types.length; i++ ) {
				context.addProofNode ( node, types[i], types2[i] );
			}
		} else
			throw new SubTypingException ( node );
	}

	public void applyList ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		ListType type;
		ListType type2;

		type = ( ListType ) node.getType ( );
		type2 = ( ListType ) node.getType2 ( );
		
		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );
		
		context.addProofNode ( node, tau, tau2 );
	}
}
