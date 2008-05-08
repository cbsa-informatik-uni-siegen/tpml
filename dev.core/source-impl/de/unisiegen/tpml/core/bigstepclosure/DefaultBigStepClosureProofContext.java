package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.bigstep.DefaultBigStepProofNode;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;

/**
 * TODO
 *
 */
final class DefaultBigStepClosureProofContext implements BigStepClosureProofContext
{
  public BigStepClosureProofRule newNoopRule(String name)
  {
    return null;
  }
  
  public void setProofNodeResult(BigStepClosureProofNode node, Expression expression)
  {
    
  }
  
  public void setProofNodeResult(BigStepClosureProofNode node, BigStepClosureProofResult result)
  {
    
  }
  
  public void setProofNodeResult(BigStepClosureProofNode node, Expression expression, Store store)
  {
    
  }
 
  public void setProofNodeResult(BigStepClosureProofNode node, Closure closure)
  {
    
  }
  
  public void setProofNodeRule(BigStepClosureProofNode node, BigStepClosureProofRule rule)
  {
  }
 
  public boolean isMemoryEnabled()
  {
    return false;
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression)
  {
    addProofNode(
        node,
        expression,
        node.getChildCount() > 0
        ? node.getLastChild().getResult ().getStore()
        : node.getStore());
  }
  
  public void addProofNode(BigStepClosureProofNode node, Closure closure)
  {
    // TODO: put the default store logic in a single method
    Store store = node.getChildCount() > 0
    ? node.getLastChild ().getResult ().getStore()
    : node.getStore ();
    addProofNode(
        node,
        closure.getExpression (),
        store,
        closure.getEnvironment());
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression, Store store)
  {
    
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression, Store store, ClosureEnvironment environment)
  {
    this.model.contextAddProofNode ( this, (DefaultBigStepClosureProofNode)node,
        new DefaultBigStepClosureProofNode ( expression, store, environment ) );
  }
  
  private BigStepClosureProofModel model;
}
