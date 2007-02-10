package de.unisiegen.tpml.core.typeinference;

import javax.swing.tree.DefaultMutableTreeNode;

import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;


/**
 * TODO write something
 *
 * @author Benjamin Mies
 *
 */
public class TmpTree extends DefaultMutableTreeNode {
	
	public final CharSequence environment = null;
	public final CharSequence expression = null;
	public final CharSequence type = null;
	private DefaultTypeCheckerProofNode root;

	

	public TmpTree (DefaultTypeCheckerProofNode pRoot)
	{
		root = pRoot;
	}




	public DefaultTypeCheckerProofNode getRoot() {
		return root;
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
