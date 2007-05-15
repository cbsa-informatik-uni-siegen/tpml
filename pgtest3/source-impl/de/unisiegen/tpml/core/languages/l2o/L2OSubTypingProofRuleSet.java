package de.unisiegen.tpml.core.languages.l2o;

import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2SubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RowType;

/**
 * The type proof rules for the <code>L2O</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L2OSubTypingProofRuleSet extends L2SubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L2OSubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L2O</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2OSubTypingProofRuleSet ( Language language ) {
		super ( language );

		unregister ( "REFL" ); //$NON-NLS-1$

		// register the type rules
		registerByMethodName ( L2OLanguage.L2O, "TRANS", "applyTrans" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2OLanguage.L2O, "OBJECT-WIDTH", "applyObjectWidth" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2OLanguage.L2O, "OBJECT-DEPTH", "applyObjectDepth" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Applies the <b>(TRANS)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyTrans ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		try {
			ObjectType type = ( ObjectType ) node.getType2 ( );
			ObjectType type2 = ( ObjectType ) node.getType ( );

			ArrayList < Identifier > newIds = new ArrayList < Identifier > ( );
			ArrayList < MonoType > newTypes = new ArrayList < MonoType > ( );

			RowType r1 = ( ( ObjectType ) type ).getPhi ( );
			RowType r2 = ( ( ObjectType ) type2 ).getPhi ( );

			Identifier[] ids1 = r1.getIdentifiers ( );
			Identifier[] ids2 = r2.getIdentifiers ( );

			MonoType[] types2 = r2.getTypes ( );

			boolean goOn;

			for ( int i = 0; i < ids1.length; i++ ) {

				goOn = false;
				for ( int j = 0; j < ids2.length; j++ ) {
					if ( ids1[i].equals ( ids2[j] ) ) {
						newIds.add ( ids2[j] );
						newTypes.add ( types2[j] );
						goOn = true;
						break;
					}
				}
				if ( goOn )
					continue;
				throw new SubTypingException ( node );
			}

			Identifier[] tmpIds = new Identifier[newIds.size ( )];
			for ( int i = 0; i < newIds.size ( ); i++ ) {
				tmpIds[i] = newIds.get ( i );
			}

			MonoType[] tmpTypes = new MonoType[newTypes.size ( )];
			for ( int i = 0; i < newTypes.size ( ); i++ ) {
				tmpTypes[i] = newTypes.get ( i );
			}

			//ObjectType newType = new ObjectType ( new RowType ( (Identifier[]) newIds.toArray ( ),(MonoType[]) newTypes.toArray ( ) ) );
			ObjectType newType = new ObjectType ( new RowType ( tmpIds, tmpTypes ) );
			context.addProofNode ( node, type2, newType );
			context.addProofNode ( node, newType, type );

		} catch ( ClassCastException e ) {
			MonoType type = node.getType ( );
			MonoType type2 = node.getType2 ( );

			context.addProofNode ( node, type, type );
			context.addProofNode ( node, type, type2 );

		}

	}

	/**
	 * Applies the <b>(OBJECT-WIDTH)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyObjectWidth ( SubTypingProofContext context,
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

		ArrayList <Identifier> newIds = new ArrayList<Identifier>();
		ArrayList<MonoType> newTypes = new ArrayList <MonoType>();
		
		for ( int i = 0; i < ids2.length; i++ ) {
			goOn = false;
			for ( int j = 0; j < ids1.length; j++ ) {
				if ( ids2[i].equals ( ids1[j] ) ) {
					if ( ! ( types2[i].equals ( types[j] ) ) ) {
						throw new SubTypingException ( node );
					}
					newIds.add (  ids1[j] );
					newTypes.add ( types[j] );
					goOn = true;
					break;
				}
			}
			if (! goOn ){
			throw new SubTypingException ( node );
			}
		}
		
		Identifier[] tmpIds = new Identifier[newIds.size ( )];
		for ( int i = 0; i < newIds.size ( ); i++ ) {
			tmpIds[i] = newIds.get ( i );
		}

		MonoType[] tmpTypes = new MonoType[newTypes.size ( )];
		for ( int i = 0; i < newTypes.size ( ); i++ ) {
			tmpTypes[i] = newTypes.get ( i );
		}

		context.addProofNode ( node, new ObjectType ( new RowType ( tmpIds, tmpTypes )), type2 );
	}

	/**
	 * Applies the <b>(OBJECT-DEPTH)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
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

			for ( int i = 0; i < ids1.length; i++ ) {
				goOn = false;
				for ( int j = 0; j < ids2.length; j++ ) {
					if ( ids1[i].equals ( ids2[j] ) ) {
						context.addProofNode ( node, types[i], types2[j] );
						goOn = true;
					}
				}
				if ( goOn )
					continue;
				break;
			}
		} else
			throw new SubTypingException ( node );
	}
}
