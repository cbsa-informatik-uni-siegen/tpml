package de.unisiegen.tpml.core.bigstepclosure;


import de.unisiegen.tpml.core.expressions.Closure;


/**
 * TODO
 */
public interface BigStepClosureProofContext
{

  /**
   * Adds a new {@link BigStepClosureProofNode} as child to the given node. Uses
   * the default store.
   * 
   * @param node the parent node for the new child node
   * @param closure the closure for the new child node
   */
  public void addProofNode ( BigStepClosureProofNode node, Closure closure );


  public boolean isMemoryEnabled ();


  public BigStepClosureProofRule newNoopRule ( String name );


  public void setProofNodeResult ( BigStepClosureProofNode node,
      BigStepClosureProofResult result );


  public void setProofNodeResult ( BigStepClosureProofNode node, Closure closure );


  public void setProofNodeRule ( BigStepClosureProofNode node,
      BigStepClosureProofRule rule );
}
