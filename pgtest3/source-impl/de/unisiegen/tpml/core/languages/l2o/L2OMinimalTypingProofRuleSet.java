package de.unisiegen.tpml.core.languages.l2o;

import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.CurriedMethod;
import de.unisiegen.tpml.core.expressions.Duplication;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.expressions.ObjectExpr;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.expressions.Send;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2MinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.PrimitiveType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.RowType;

/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L2OMinimalTypingProofRuleSet extends L2MinimalTypingProofRuleSet {
	/**
	 * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @param mode the actual choosen mode
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2OMinimalTypingProofRuleSet ( L1Language language, boolean mode ) {
		super ( language, mode );
		// register the type rules
		if ( !mode ) { // beginner mode
			registerByMethodName ( L2OLanguage.L2O, "TRANS", "applyTrans" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L2OLanguage.L2O,
					"OBJECT-WIDTH", "applyObjectWidth" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L2OLanguage.L2O,
					"OBJECT-DEPTH", "applyObjectDepth" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		registerByMethodName ( L2OLanguage.L2O, "EMPTY", "applyEmpty" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2OLanguage.L2O, "SEND", "applySend", "updateSend" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		registerByMethodName ( L2OLanguage.L2O,
				"OBJECT", "applyObject", "updateObject" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		registerByMethodName ( L2OLanguage.L2O,
				"DUPL-SUBSUME", "applyDuplSubsume", "updateDuplSubsume" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		registerByMethodName ( L2OLanguage.L2O,
				"METHOD-SUBSUME", "applyMethodSubsume", "updateMethodSubsume" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		registerByMethodName ( L2OLanguage.L2O, "ATTR", "applyAttr", "updateAttr" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Applies the <b>(SEND)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applySend ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Send send = ( Send ) node.getExpression ( );
		context.addProofNode ( node, node.getEnvironment ( ), send.getE ( ) );
	}

	/**
	 * Updates the <code>node</code> to which <b>(SEND)</b> was applied
	 * previously.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the node to update according to <b>(SEND)</b>.
	 */
	public void updateSend ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Send send = ( Send ) node.getExpression ( );
		if ( node.getFirstChild ( ).isFinished ( ) ) {

			Identifier m = send.getId ( );
			RowType row = ( RowType ) node.getFirstChild ( ).getType ( );

			Identifier[] ids = row.getIdentifiers ( );
			MonoType[] types = row.getTypes ( );

			Identifier id;

			// search for type of m in the rowtype and set this type for the node
			for ( int i = 0; i < ids.length; i++ ) {
				id = ids[i];
				if ( m.equals ( id ) ) {
					MonoType type = types[i];
					context.setNodeType ( node, type );
					return;
				}
			}
			throw new RuntimeException ( "type of m is not in phi" );
		}
	}

	/**
	 * Applies the <b>(OBJECT)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyObject ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		ObjectExpr object = ( ObjectExpr ) node.getExpression ( );
		MonoType tau = object.getTau ( );
		if ( tau == null ) {
			throw new RuntimeException ( "You have to enter type for self" );
		}
		if (tau instanceof RecType){
			RecType rec = (RecType) tau;
			tau = rec.getTau ( ).substitute ( rec.getTypeName ( ), rec );
		}
		TypeEnvironment environment = node.getEnvironment ( );
		environment = environment.star ( );
		context.addProofNode ( node,
				environment.extend ( object.getId ( ), tau ), object.getE ( ) );
	}

	/**
	 * Updates the <code>node</code> to which <b>(OBJECT)</b> was applied
	 * previously.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the node to update according to <b>(OBJECT)</b>.
	 */
	public void updateObject ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		if ( node.getFirstChild ( ).isFinished ( ) ) {
			MinimalTypingExpressionProofNode child = ( MinimalTypingExpressionProofNode ) node
					.getFirstChild ( );
			TypeEnvironment environment = child.getEnvironment ( );
			Identifier self = new Identifier ( "self" ); //$NON-NLS-1$
			ObjectType objectType = ( ObjectType ) environment.get ( self );
			RowType type = ( RowType ) objectType.getPhi ( );
			RowType type2 = ( RowType ) child.getType ( );

			/*
			 Identifier[] ids = type.getIdentifiers ( );
			 Identifier[] ids2 = type2.getIdentifiers ( );

			 MonoType[] types = type.getTypes ( );
			 MonoType[] types2 = type2.getTypes ( );

			 if ( ids.length != ids2.length )
			 throw new RuntimeException ( "Types not Equal" );

			 for ( int i = 0; i < ids.length; i++ ) {
			 for ( int j = 0; j < ids2.length; j++ ) {
			 if ( ids[i].equals ( ids2[j] ) ) {
			 if ( !types[i].equals ( types2[j] ) )
			 throw new RuntimeException ( "types not equal" );
			 break;
			 }
			 if ( j == ids.length - 1 )
			 throw new RuntimeException ( "Identifier not found" );
			 }*/
			try {
				subtypeInternal ( type, type2 );
				subtypeInternal ( type2, type );
			} catch ( Exception e ) {
				throw new RuntimeException ( "Types not equal" );
			}

			context.setNodeType ( node, type );

		}
	}

	/**
	 * Applies the <b>(DUPL-SUBSUME)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyDuplSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Duplication duplication = ( Duplication ) node.getExpression ( );
		Identifier self = new Identifier ( "self" ); //$NON-NLS-1$
		context.addProofNode ( node, node.getEnvironment ( ), self );

	}

	/**
	 * Updates the <code>node</code> to which <b>(DUPL-SUBSUME)</b> was applied
	 * previously.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the node to update according to <b>(DUPL-SUBSUME)</b>.
	 */
	public void updateDuplSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Duplication duplication = ( Duplication ) node.getExpression ( );

		if ( node.getChildCount ( ) == 1 && node.getLastChild ( ).isFinished ( ) ) {
			context.addProofNode ( node, node.getEnvironment ( ), duplication
					.getExpressions ( )[0] );
		}

		else if ( node.getChildCount ( ) > 1
				&& node.getLastChild ( ).isFinished ( ) ) {
			// check if a:tau und e:tau' tau'<: tau
			TypeEnvironment environment = node.getEnvironment ( );
			Identifier id = duplication.getIdentifiers ( )[node.getChildCount ( ) - 2];
			MonoType tau = ( MonoType ) environment.get ( id );
			MonoType tau2 = node.getLastChild ( ).getType ( );
			if ( tau == null || tau2 == null )
				throw new RuntimeException ( "tau and tau' not equal" );
			try {
				subtypeInternal ( tau, tau2 );
				subtypeInternal ( tau2, tau );
			} catch ( Exception e ) {
				throw new RuntimeException ( "tau and tau' not equal" );
			}
		}
		if ( node.getChildCount ( ) == duplication.getIdentifiers ( ).length +1 ) {
			context.setNodeType ( node, node.getFirstChild ( ).getType ( ) );
			// all childs added, so nothing more to do
			return;
		} else {
			// add next child
			context.addProofNode ( node, node.getEnvironment ( ), duplication
					.getExpressions ( )[node.getChildCount ( )-1] );
		}
	}

	/**
	 * Applies the <b>(METHOD-SUBSUME)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyMethodSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Row row = ( Row ) node.getExpression ( );
		if ( row.getExpressions ( ).length > 0
				&& ( ( row.getExpressions ( )[0] instanceof Method ) || ( row
						.getExpressions ( )[0] instanceof CurriedMethod ) ) ) {
			Identifier self = new Identifier ( "self" ); //$NON-NLS-1$
			context.addProofNode ( node, node.getEnvironment ( ), self );
			return;
		}
		throw new ClassCastException ( );
	}

	/**
	 * Updates the <code>node</code> to which <b>(METHOD-SUBSUME)</b> was applied
	 * previously.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the node to update according to <b>(METHOD-SUBSUME)</b>.
	 */
	public void updateMethodSubsume ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Row row = ( Row ) node.getExpression ( );

		if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) ) {
			if ( row.getExpressions ( )[0] instanceof Method ) {
				Method method = ( Method ) row.getExpressions ( )[0];
				context.addProofNode ( node, node.getEnvironment ( ), method
						.getE ( ) );
			} else {
				CurriedMethod curriedMethod = ( CurriedMethod ) row
						.getExpressions ( )[0];
				Expression curriedMethodE = curriedMethod.getE ( );
				MonoType[] types = curriedMethod.getTypes ( );
				Identifier[] identifiers = curriedMethod.getIdentifiers ( );
				for ( int n = identifiers.length - 1; n > 0; --n ) {
					curriedMethodE = new Lambda ( identifiers[n], types[n],
							curriedMethodE );
				}
				MonoType curriedMethodTau = types[0];
				if ( curriedMethodTau == null ) {
					throw new RuntimeException ( "type of this method is null " );
				}
				for ( int n = types.length - 1; n > 0; --n ) {
					if ( types[n] == null )
						throw new RuntimeException ( "type of " + identifiers[n]
								+ " is null" );
					curriedMethodTau = new ArrowType ( types[n], curriedMethodTau );
				}

				context.addProofNode ( pNode, node.getEnvironment ( ),
						curriedMethodE );

			}
		} else if ( node.getChildCount ( ) == 2
				&& node.getChildAt ( 1 ).isFinished ( ) ) {

			// Create the tailRow and add it as Expression of a new Child
			Expression[] expressions = ( ( Row ) node.getExpression ( ) )
					.getExpressions ( );
			Expression[] tailRow = new Expression[expressions.length - 1];
			for ( int i = 1; i < expressions.length; i++ ) {
				tailRow[i - 1] = expressions[i];
			}
			Row newRow = new Row ( tailRow );
			context.addProofNode ( node, node.getEnvironment ( ), newRow );
		} else if ( node.getChildCount ( ) == 3
				&& node.getChildAt ( 2 ).isFinished ( ) ) {
			Expression expression = row.getExpressions ( )[0];
			TypeEnvironment environment = node.getEnvironment ( );
			MonoType type = ( MonoType ) environment
					.get ( new Identifier ( "self" ) );
			if ( type instanceof RecType ) {
				RecType rec = ( RecType ) type;
				type = rec.getTau ( ).substitute ( rec.getTypeName ( ),
						rec.getTau ( ) );
			}
			ObjectType object = ( ObjectType ) type; //$NON-NLS-1$
			RowType rowType = ( RowType ) object.getPhi ( );
			Identifier[] identifiers = rowType.getIdentifiers ( );
			MonoType[] types = rowType.getTypes ( );
			Identifier m = ( expression instanceof Method ? ( ( Method ) expression )
					.getId ( )
					: ( ( CurriedMethod ) expression ).getIdentifiers ( )[0] );
			MonoType tau = null;

			for ( int i = 0; i < identifiers.length; i++ ) {
				if ( m.equals ( identifiers[i] ) ) {
					tau = types[i];
					break;
				}
			}
			if ( tau == null )
				throw new RuntimeException ( "Type of Method not found" );
			context.addProofNode ( node, tau, node.getChildAt ( 1 ).getType ( ) );
		} else if ( node.getChildCount ( ) == 4
				&& node.getChildAt ( 3 ).isFinished ( ) ) {

			Identifier[] ids = new Identifier[1];
			Expression expression = row.getExpressions ( )[0];
			ids[0] = ( expression instanceof Method ? ( ( Method ) expression )
					.getIdentifiers ( )[0] : ( ( CurriedMethod ) expression )
					.getIdentifiers ( )[0] );
			MonoType[] types = { node.getChildAt ( 1 ).getType ( ) };

			RowType rowType = new RowType ( ids, types );
			RowType phi = ( RowType ) node.getChildAt ( 2 ).getType ( );
			rowType = RowType.union ( rowType, phi );
			context.setNodeType ( node, rowType );
		}

	}

	/**
	 * Applies the <b>(ATTR)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyAttr ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Row row = ( Row ) node.getExpression ( );
		if ( row.getExpressions ( ).length > 0
				&& row.getExpressions ( )[0] instanceof Attribute ) {
			Attribute attr = ( Attribute ) row.getExpressions ( )[0];
			TypeEnvironment environment = node.getEnvironment ( );
			context.addProofNode ( node, environment, attr.getE ( ) );
			return;
		}
		throw new ClassCastException ( );

	}

	/**
	 * Updates the <code>node</code> to which <b>(ATTR)</b> was applied
	 * previously.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the node to update according to <b>(ATTR)</b>.
	 */
	public void updateAttr ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) ) {
			Row rowExpression = ( Row ) node.getExpression ( );
			Attribute attr = ( Attribute ) rowExpression.getExpressions ( )[0];
			TypeEnvironment environment = node.getEnvironment ( );

			// Create the tailRow and add it as Expression of a new Child
			Expression[] expressions = ( ( Row ) node.getExpression ( ) )
					.getExpressions ( );
			Expression[] tailRow = new Expression[expressions.length - 1];
			for ( int i = 1; i < expressions.length; i++ ) {
				tailRow[i - 1] = expressions[i];
			}
			Row row = new Row ( tailRow );
			context.addProofNode ( node, environment.extend ( attr.getId ( ), node
					.getFirstChild ( ).getType ( ) ), row );
		}
		if ( node.getChildCount ( ) == 2 && node.getChildAt ( 1 ).isFinished ( ) ) {
			context.setNodeType ( node, node.getChildAt ( 1 ).getType ( ) );
		}
	}

	/**
	 * Applies the <b>(EMPTY)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyEmpty ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Row row = ( Row ) node.getExpression ( );
		Expression[] expressions = row.getExpressions ( );
		if ( expressions.length == 0 ) {
			context.setNodeType ( node, new RowType ( new Identifier[0],
					new MonoType[0] ) );
			return;
		}
		throw new RuntimeException ( "type is not empty" );
	}

	/**
	 * Applies the <b>(TRANS)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 * @throws PrimitiveTypeException 
	 */
	public void applyTrans ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
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
				//throw new SubTypingException ( MessageFormat.format ( Messages
				//	.getString ( "SubTypingException.0" ), type, type2 ), node ); //$NON-NLS-1$
				throw new RuntimeException ( "types not equal" );
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
			if ( type instanceof PrimitiveType && type2 instanceof PrimitiveType ) {
				//	throw new SubTypingException ( Messages
				//		.getString ( "SubTypingException.1" ), node ); //$NON-NLS-1$
				throw new RuntimeException ( "types not equal" );
			}
			context.addProofNode ( node, type, type );
			context.addProofNode ( node, type, type2 );

			SubTypingProofNode parent = ( SubTypingProofNode ) node.getParent ( );
			int count = 0;
			while ( parent != null ) {
				if ( parent.getRule ( ).toString ( ).equals ( "TRANS" ) ) { //$NON-NLS-1$
					count++ ;
				} else
					break;
				parent = ( SubTypingProofNode ) parent.getParent ( );
			}

			if ( count >= 15 )
				//	throw new SubTypingException ( Messages
				//		.getString ( "SubTypingException.2" ), node ); //$NON-NLS-1$
				throw new RuntimeException ( "to often applied the trans rule" );

		}

	}

	/**
	 * Applies the <b>(OBJECT-WIDTH)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyObjectWidth ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
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
						/*throw new SubTypingException (
						 MessageFormat
						 .format (
						 Messages
						 .getString ( "SubTypingException.3" ), type, type2 ), node ); //$NON-NLS-1$*/
						throw new RuntimeException ( "types not equal" );
					}
					goOn = true;
					break;
				}
			}
			if ( !goOn ) {
				//	throw new SubTypingException ( MessageFormat.format ( Messages
				//		.getString ( "SubTypingException.4" ), type, type2 ), node ); //$NON-NLS-1$
				throw new RuntimeException ( "types not equal" );
			}
			context.addSeenType ( node.getType ( ), node.getType2 ( ) );
		}
	}

	/**
	 * Applies the <b>(OBJECT-DEPTH)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyObjectDepth ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;

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
			//throw new SubTypingException ( MessageFormat.format ( Messages
			//	.getString ( "SubTypingException.0" ), type, type2 ), node ); //$NON-NLS-1$
			throw new RuntimeException ( "types not equal" );
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );
	}

}
