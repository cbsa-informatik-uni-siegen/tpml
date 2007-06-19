package de.unisiegen.tpml.core.languages.l2o;

import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.Duplication;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.expressions.ObjectExpr;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.expressions.Send;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2MinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
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
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2OMinimalTypingProofRuleSet ( L1Language language ) {
		super ( language );
		// register the type rules
		registerByMethodName ( L2OLanguage.L2O, "EMPTY", "applyEmpty" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2OLanguage.L2O, "SEND", "applySend", "updateSend" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		registerByMethodName ( L2OLanguage.L2O,
				"OBJECT", "applyObject", "updateObject" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//registerByMethodName ( L2OLanguage.L2O, "DUPL-SUBSUME", "applyDuplSubsume", "updateDuplSubsume" );//$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L2OLanguage.L2O,
				"METHOD-SUBSUME", "applyMethodSubsume", "updateMethodSubsume" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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
		if ( tau == null )
			throw new RuntimeException ( "You have to enter type for self" );
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
			RowType type = ( RowType ) ( ( ObjectType ) node.getType ( ) )
					.getPhi ( );
			RowType type2 = ( RowType ) node.getFirstChild ( ).getType ( );

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
				}
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
		context.addProofNode ( node, node.getEnvironment ( ), duplication
				.getExpressions ( )[0] );
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

		if ( node.getLastChild ( ).isFinished ( ) ) {
			// check if a:tau und e:tau' tau'<: tau
			TypeEnvironment environment = node.getEnvironment ( );
			Identifier id = duplication.getIdentifiers ( )[node.getChildCount ( ) - 1];
			MonoType tau = ( MonoType ) environment.get ( id );
			MonoType tau2 = node.getLastChild ( ).getType ( );
			if ( tau == null || tau2 == null || tau != tau2 )
				throw new RuntimeException ( "tau and tau' not equal" );
		}
		if ( node.getChildCount ( ) == duplication.getExpressions ( ).length ) {
			// all childs added, so nothing more to do
			return;
		} 
			// add next child
			context.addProofNode ( node, node.getEnvironment ( ), duplication
					.getExpressions ( )[node.getChildCount ( )] );
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
		Method method = ( Method ) node.getExpression ( );
		context.addProofNode ( node, node.getEnvironment ( ), method.getE ( ) );

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
		Method method = ( Method ) node.getExpression ( );
		if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) ) {
			
			// Create the tailRow and add it as Expression of a new Child
			Expression [] expressions = ((Row) node.getExpression ( )).getExpressions ( );
			Expression [] tailRow = new Expression [expressions.length-1];
			for (int i = 1; i < expressions.length; i++){
				tailRow [i-1] = expressions[i];
			}
			Row row = new Row ( tailRow );
			context.addProofNode ( node, node.getEnvironment ( ), row );
		}
		if ( node.getChildCount ( ) == 2 && node.getChildAt ( 1 ).isFinished ( ) ) {
			TypeEnvironment environment = node.getEnvironment ( );
			ObjectType type = (ObjectType) environment.get ( new Identifier("self") ); //$NON-NLS-1$
			RowType rowType = (RowType) type.getPhi ( );
			Identifier [] identifiers = rowType.getIdentifiers ( );
			MonoType [] types = rowType.getTypes ( );
			Identifier m = method.getId ( );
			MonoType tau = null;
			
			for ( int i = 0; i < identifiers.length; i++){
				if (m.equals ( identifiers[i] )){
					tau = types[i];
					break;
				}
			}
			if (tau == null)
				throw new RuntimeException("Type of Method not found");
			context.addProofNode ( node, tau, node.getFirstChild ( ).getType ( ) );
		}
		if ( node.getChildCount ( ) == 3 && node.getChildAt ( 2 ).isFinished ( ) ) {
			TypeEnvironment environment = node.getEnvironment ( );
			ObjectType type = (ObjectType) environment.get ( new Identifier("self") ); //$NON-NLS-1$
			RowType rowType = (RowType) type.getPhi ( );
			Identifier [] identifiers = rowType.getIdentifiers ( );
			MonoType [] types = rowType.getTypes ( );
			Identifier m = method.getId ( );
			MonoType tau = null;
			
			for ( int i = 0; i < identifiers.length; i++){
				if (m.equals ( identifiers[i] )){
					tau = types[i];
					break;
				}
			}
			if (tau == null)
				throw new RuntimeException("Type of Method not found");
			RowType row;
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
		Attribute attr = ( Attribute ) node.getExpression ( );
		TypeEnvironment environment = node.getEnvironment ( );
		context.addProofNode ( node, environment, attr.getE ( ) );
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
			Attribute attr = ( Attribute ) node.getExpression ( );
			TypeEnvironment environment = node.getEnvironment ( );
			
			// Create the tailRow and add it as Expression of a new Child
			Expression [] expressions = ((Row) node.getExpression ( )).getExpressions ( );
			Expression [] tailRow = new Expression [expressions.length-1];
			for (int i = 1; i < expressions.length; i++){
				tailRow [i-1] = expressions[i];
			}
			Row row = new Row ( tailRow );
			context.addProofNode ( node, environment.extend ( attr.getId ( ), node.getFirstChild ( ).getType ( ) ), row );
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
		if ( expressions.length == 0 )
			return;
		throw new RuntimeException ( "type is not empty" );
	}

}
