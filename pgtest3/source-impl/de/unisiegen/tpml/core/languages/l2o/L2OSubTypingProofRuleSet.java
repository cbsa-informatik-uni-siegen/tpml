package de.unisiegen.tpml.core.languages.l2o;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2SubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RowType;

public class L2OSubTypingProofRuleSet extends L2SubTypingProofRuleSet {

	public L2OSubTypingProofRuleSet ( Language language ) {
		super ( language );

		// register the type rules
		//	registerByMethodName ( L1Language.L1, "TRANS", "applyTrans" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "OBJECT-WIDTH", "applyObjectWidth" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "OBJECT-DEPTH", "applyObjectDepth" ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void applyObjectWidth ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		boolean goOn;
		ObjectType type = ( ObjectType ) node.getType2 ( );
		ObjectType type2 = ( ObjectType ) node.getType ( );

		RowType r1 = ( ( ObjectType ) type ).getPhi ( );
		RowType r2 = ( ( ObjectType ) type2 ).getPhi ( );
		
		Identifier[] ids1 = r1.getIdentifiers ( );
		Identifier[] ids2 = r2.getIdentifiers ( );

		MonoType[] types = r1.getTypes ( );
		MonoType[] types2 = r2.getTypes ( );
		
			for (int i = 0; i < ids1.length; i++ ) {
				goOn = false;
				for (int j = 0; j < ids2.length; j++ ) {
					if (ids1[i].equals ( ids2[j] )){
						context.addProofNode ( node, types[i], types2[j] );
						goOn = true;
					}
				}
				if (goOn)
					continue;
				throw new SubTypingException ( node );
			}
			
	}

	public void applyObjectDepth ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		boolean goOn;
		ObjectType type = ( ObjectType ) node.getType ( );
		ObjectType type2 = ( ObjectType ) node.getType2 ( );

		RowType r1 = ( ( ObjectType ) type ).getPhi ( );
		RowType r2 = ( ( ObjectType ) type2 ).getPhi ( );

		Identifier[] ids1 = r1.getIdentifiers ( );
		Identifier[] ids2 = r2.getIdentifiers ( );

		MonoType[] types = r1.getTypes ( );
		MonoType[] types2 = r2.getTypes ( );

		if ( ids1.length == ids2.length ) {

			for (int i = 0; i < ids1.length; i++ ) {
				goOn = false;
				for (int j = 0; j < ids2.length; j++ ) {
					if (ids1[i].equals ( ids2[j] )){
						context.addProofNode ( node, types[i], types2[j] );
						goOn = true;
					}
				}
				if (goOn)
					continue;
				break;
			}
		} else
			throw new SubTypingException ( node );
	}
}
