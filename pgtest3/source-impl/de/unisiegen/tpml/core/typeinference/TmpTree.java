package de.unisiegen.tpml.core.typeinference;

import javax.swing.tree.DefaultMutableTreeNode;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * TODO write something
 *
 * @author Benjamin Mies
 *
 */
public class TmpTree extends DefaultTypeCheckerProofNode {
	
	public TmpTree(TypeEnvironment environment, Expression expression, MonoType type) {
		super(environment, expression, type);
		
	}
	
	

	



	
	
	  //
	  // Base methods
	  //
	  
	  /**
	   * {@inheritDoc}
	   * 
	   * Mainly useful for debugging purposes.
	   * 
	   * @see java.lang.Object#toString()
	   */
	  @Override
	  public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append(this.environment);
	    builder.append(" \u22b3 ");
	    builder.append(this.expression);
	    builder.append(" :: ");
	    builder.append(this.type);
	    //if (getRule() != null) {
	     // builder.append(" (" + getRule() + ")");
	    //}
	    return builder.toString();
	  }
	
}
