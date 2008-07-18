package de.unisiegen.tpml.core.bigstepclosure;


import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;


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


  /**
   * Adds a new {@link BigStepClosureProofNode} as child to the given node.
   * 
   * @param node the parent node for the new child node
   * @param closure the closure for the new child node
   * @param store the store to use for the new child node
   */
  public void addProofNode ( BigStepClosureProofNode node, Closure closure,
      Store store );


  public boolean isMemoryEnabled ();


  public BigStepClosureProofRule newNoopRule ( String name );


  public void setProofNodeResult ( BigStepClosureProofNode node,
      BigStepClosureProofResult result );


  public void setProofNodeResult ( BigStepClosureProofNode node, Closure closure );


  public void setProofNodeRule ( BigStepClosureProofNode node,
      BigStepClosureProofRule rule );
}
