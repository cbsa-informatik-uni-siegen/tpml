package de.unisiegen.tpml.core.languages.l2osubtype;

import java.text.MessageFormat;
import java.util.ArrayList;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage;
import de.unisiegen.tpml.core.languages.l2subtype.L2RecSubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.PrimitiveType;
import de.unisiegen.tpml.core.types.RowType;

/**
 * The type proof rules for the <code>L2O</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L2ORecSubTypingProofRuleSet extends L2RecSubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L2OSubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L2O</code> or a derived language.
	 * @param mode the mode chosen by the user
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2ORecSubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );

		unregister ( "REFL" ); //$NON-NLS-1$
		unregister ( "S-MU-LEFT" ); //$NON-NLS-1$
		unregister ( "S-MU-RIGHT" ); //$NON-NLS-1$
		unregister ("S-ASSUME"); //$NON-NLS-1$
		unregister ("ARROW"); //$NON-NLS-1$

		// register the type rules

		if ( mode ) {
			registerByMethodName ( L2OLanguage.L2O, "OBJECT", "applyObject" ); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			registerByMethodName ( L2OLanguage.L2O, "TRANS", "applyTrans" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L2OLanguage.L2O,
					"OBJECT-WIDTH", "applyObjectWidth" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L2OLanguage.L2O,
					"OBJECT-DEPTH", "applyObjectDepth" ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		registerByMethodName ( L1Language.L1, "ARROW", "applyArrow" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-MU-LEFT", "applyMuLeft" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-MU-RIGHT", "applyMuRight" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-ASSUME", "applyAssume" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Applies the <b>(TRANS)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 * @throws SubTypingException 
	 */
	public void applyTrans ( RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {
		try {
			ObjectType type = ( ObjectType ) node.getType2 ( );
			ObjectType type2 = ( ObjectType ) node.getType ( );

			ArrayList < Identifier > newIds = new ArrayList < Identifier > ( );
			ArrayList < MonoType > newTypes = new ArrayList < MonoType > ( );

			RowType r1 = ( RowType ) ( type ).getPhi ( );
			RowType r2 = ( RowType ) ( type2 ).getPhi ( );

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
				throw new SubTypingException (MessageFormat.format ( Messages.getString ( "SubTypingException.0" ),type,type2), node ); //$NON-NLS-1$
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
			// if both types instance of Primitive Type throw Exception
			if ( type instanceof PrimitiveType && type2 instanceof PrimitiveType ){
				throw new SubTypingException ( Messages.getString ( "SubTypingException.1" ), node ); //$NON-NLS-1$
			}
			context.addProofNode ( node, type, type );
			context.addProofNode ( node, type, type2 );

			SubTypingProofNode parent = ( SubTypingProofNode ) node.getParent ( );
			int count = 0;
			while ( parent != null ) {
				if ( parent.getRule ( ).toString ( ).equals ( "TRANS" ) ){ //$NON-NLS-1$
					count++ ;
				}
				else
					break;
				parent = (SubTypingProofNode) parent.getParent ( );
			}
			
			if (count >= 15)
				throw new SubTypingException (Messages.getString ( "SubTypingException.2" ), node ); //$NON-NLS-1$
			

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
	public void applyObjectWidth ( RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {
		boolean goOn;
		ObjectType type = ( ObjectType ) node.getType ( );
		ObjectType type2 = ( ObjectType ) node.getType2 ( );

		RowType r1 = ( RowType ) ( type ).getPhi ( );
		RowType r2 = ( RowType ) ( type2 ).getPhi ( );

		Identifier[] ids1 = r1.getIdentifiers ( );
		Identifier[] ids2 = r2.getIdentifiers ( );

		MonoType[] types = r1.getTypes ( );
		MonoType[] types2 = r2.getTypes ( );

		for ( int i = 0; i < ids2.length; i++ ) {
			goOn = false;
			for ( int j = 0; j < ids1.length; j++ ) {
				if ( ids2[i].equals ( ids1[j] ) ) {
					if ( ! ( types2[i].equals ( types[j] ) ) ) {
						throw new SubTypingException (MessageFormat.format ( Messages.getString ( "SubTypingException.3" ),type,type2), node ); //$NON-NLS-1$
						}
					goOn = true;
					break;
				}
			}
			if ( !goOn ) {
				throw new SubTypingException (MessageFormat.format ( Messages.getString ( "SubTypingException.4" ),type,type2), node ); //$NON-NLS-1$
				}
			context.addSeenType ( node.getType ( ), node.getType2 ( ) );
		}
	}

	/**
	 * Applies the <b>(OBJECT-DEPTH)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyObjectDepth ( RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {

		boolean goOn;
		ObjectType type = ( ObjectType ) node.getType ( );
		ObjectType type2 = ( ObjectType ) node.getType2 ( );

		RowType r1 = ( RowType ) ( type ).getPhi ( );
		RowType r2 = ( RowType ) ( type2 ).getPhi ( );

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
			throw new SubTypingException (MessageFormat.format ( Messages.getString ( "SubTypingException.0" ),type, type2), node ); //$NON-NLS-1$
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );
		}

	/**
	 * Applies the <b>(OBJECT)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyObject ( RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {
		boolean goOn = false;

		ObjectType type = ( ObjectType ) node.getType ( );
		ObjectType type2 = ( ObjectType ) node.getType2 ( );

		RowType r1 = ( RowType ) type.getPhi ( );
		RowType r2 = ( RowType ) type2.getPhi ( );

		Identifier[] ids1 = null;
		Identifier[] ids2 = null;
		MonoType[] types1 = null;
		MonoType[] types2 = null;

		ids1 = r1.getIdentifiers ( );
		ids2 = r2.getIdentifiers ( );

		types1 = r1.getTypes ( );
		types2 = r2.getTypes ( );

		for ( int i = 0; i < ids2.length; i++ ) {
			goOn = false;
			for ( int j = 0; j < ids1.length; j++ ) {
				if ( ids2[i].equals ( ids1[j] ) ) {
					//newIds.add (  ids1[j] );
					//newTypes.add ( types1[j] );
					context.addProofNode ( node, types1[j], types2[i] );
					goOn = true;
					break;
				}
			}
			if ( !goOn ) {
				throw new SubTypingException (MessageFormat.format ( Messages.getString ( "SubTypingException.3" ),type,type2), node ); //$NON-NLS-1$
				}
		}
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );
	}
}
