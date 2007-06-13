package de.unisiegen.tpml.core.languages.l2o;

import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.ObjectExpr;
import de.unisiegen.tpml.core.expressions.Send;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2MinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RowType;

/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L2OMinimalTypingProofRuleSet extends
L2MinimalTypingProofRuleSet {
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
		registerByMethodName ( L2OLanguage.L2O,"REC", "applyEmpty" );//$NON-NLS-1$ //$NON-NLS-2$

	}
	
	/**
	 * Applies the <b>(SEND)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applySend ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
		Send send = (Send) node.getExpression ( );
		context.addProofNode ( node, node.getEnvironment ( ), send.getE ( ) );
	}
	
	public void updateSend ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
		Send send = (Send) node.getExpression ( );
		if (node.getFirstChild ( ).isFinished ( )){
			
			Identifier m = send.getId ( );
			RowType row = (RowType) node.getFirstChild ( ).getType ( );
			
			Identifier [] ids = row.getIdentifiers();
			MonoType [] types = row.getTypes();
			
			Identifier id;
			
			// search for type of m in the rowtype and set this type for the node
			for ( int i = 0; i< ids.length; i++){
				id = ids[i];
				if (m.equals(id)){
					MonoType type = types[i];
					context.setNodeType(node, type);
					return;
				}
			}
			throw new RuntimeException("type of m is not in phi");
		}
	}
	
	/**
	 * Applies the <b>(OBJECT)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyObject ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
		ObjectExpr object = (ObjectExpr) node.getExpression();
		TypeEnvironment environment = node.getEnvironment();
		context.addProofNode(node, environment.extend(, type), expression)
	}

	/**
	 * Applies the <b>(EMPTY)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param node the minimal typing proof node.
	 */
	public void applyEmpty ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
		RowType type = (RowType) node.getType ( );
		if (type == null)
			return;
		throw new RuntimeException("type is not empty");
	}


}
