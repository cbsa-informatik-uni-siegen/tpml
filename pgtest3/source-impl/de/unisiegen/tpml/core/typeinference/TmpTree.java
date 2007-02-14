package de.unisiegen.tpml.core.typeinference;

import javax.swing.tree.DefaultMutableTreeNode;

import de.unisiegen.tpml.core.ProofRuleSet;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.AbstractTypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;


/**
 * TODO write something
 *
 * @author Benjamin Mies
 *
 */
public class TmpTree  {
	
	DefaultTypeCheckerProofNode root;
	
	  public TmpTree(Expression expression, ProofRuleSet ruleSet) {
		    root= new DefaultTypeCheckerProofNode(new DefaultTypeEnvironment(), expression, new TypeVariable(1, 0));
		 
		  }

	public DefaultTypeCheckerProofNode getRoot() {
		return this.root;
	}
		  
	
	

	



	
}
