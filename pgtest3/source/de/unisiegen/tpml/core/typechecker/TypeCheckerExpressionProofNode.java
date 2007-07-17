package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.types.MonoType;

public interface TypeCheckerExpressionProofNode extends TypeCheckerProofNode {

	  /**
	   * Returns the type environment for this type node, that is, the environment in which the type
	   * of the expression was determined.
	   * 
	   * @return the type environment for this type node.
	   */
	  public TypeEnvironment getEnvironment();
	  
	 // public TypeCheckerExpressionProofNode getChildAt(int childIndex);
	  

}
