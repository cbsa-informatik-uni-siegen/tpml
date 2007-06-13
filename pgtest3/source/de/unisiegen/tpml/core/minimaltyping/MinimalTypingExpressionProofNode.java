package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.types.MonoType;

public interface MinimalTypingExpressionProofNode extends
		MinimalTypingProofNode {
	
  
  
  /**
   * Returns the type environment for this type node, that is, the environment in which the type
   * of the expression was determined.
   * 
   * @return the type environment for this type node.
   */
  public TypeEnvironment getEnvironment();

}
