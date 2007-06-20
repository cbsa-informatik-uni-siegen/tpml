package de.unisiegen.tpml.core.languages.l3;

import java.text.MessageFormat;

import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l2.L2MinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TupleType;

/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L3MinimalTypingProofRuleSet extends L2MinimalTypingProofRuleSet {
	/**
	 * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @param mode the actual choosen mode
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L3MinimalTypingProofRuleSet ( L1Language language, boolean mode ) {
		super ( language, mode );
		// register the type rules
		if (!mode){ // beginner mode
			unregister ( "REFL" ); //$NON-NLS-1$
			unregister ( "S-ASSUME" ); //$NON-NLS-1$
			
			// register the type rules
			registerByMethodName ( L3Language.L3, "PODUCT", "applyProduct" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L3Language.L3, "LIST", "applyList" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
			registerByMethodName ( L1Language.L1, "S-ASSUME", "applyAssume" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		registerByMethodName ( L2Language.L2,
				"TUPLE", "applyTuple", "updateTuple" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	}

	/**
	 * Applies the <b>(TUPLE)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyTuple ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Tuple tuple = ( Tuple ) node.getExpression ( );

		context.addProofNode ( node, node.getEnvironment ( ), tuple
				.getExpressions ( )[0] );
	}

	  /**
	   * Updates the <code>node</code> to which <b>(TUPLE)</b> was applied
	   * previously.
	   * 
	   * @param context the minimal typing proof context.
	   * @param pNode the node to update according to <b>(TUPLE)</b>.
	   */
	public void updateTuple ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Tuple tuple = ( Tuple ) node.getExpression ( );

		if ( node.getLastChild ( ).isFinished ( ) ) {

			if ( node.getChildCount ( ) == tuple.getExpressions ( ).length ) {
				MonoType [] types = new MonoType[node.getChildCount ( )];
				for (int i = 0; i< node.getChildCount ( ); i++){
					types[i]= node.getChildAt ( i ).getType ( );
				}
				TupleType type = new TupleType(types);
				context.setNodeType ( node, type );
				return;
			}
				// add next child
				context.addProofNode ( node, node.getEnvironment ( ), tuple
						.getExpressions ( )[node.getChildCount ( )] );
		}
	}
	
	/**
	 * Applies the <b>(PRODUCT)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyProduct ( MinimalTypingProofContext context, MinimalTypingProofNode pNode ) {
		MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
		TupleType type;
		TupleType type2;

		type = ( TupleType ) node.getType ( );
		type2 = ( TupleType ) node.getType2 ( );

		MonoType[] types = type.getTypes ( );
		MonoType[] types2 = type2.getTypes ( );

		if ( types.length == types2.length ) {
			for ( int i = 0; i < types.length; i++ ) {
				context.addProofNode ( node, types[i], types2[i] );
				context.addSeenType ( node.getType ( ), node.getType2 ( ) );
			}
		} else
			//throw new SubTypingException (MessageFormat.format ( "SubTypingException.6", node.getType ( ), node.getType2 ( ) ), node ); //$NON-NLS-1$
			throw new RuntimeException("Types not equal");
	}

	/**
	 * Applies the <b>(LIST)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyList ( MinimalTypingProofContext context, MinimalTypingProofNode pNode ) {
		MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode;
		ListType type;
		ListType type2;

		type = ( ListType ) node.getType ( );
		type2 = ( ListType ) node.getType2 ( );

		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );

		context.addProofNode ( node, tau, tau2 );
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );
		
	}

}
