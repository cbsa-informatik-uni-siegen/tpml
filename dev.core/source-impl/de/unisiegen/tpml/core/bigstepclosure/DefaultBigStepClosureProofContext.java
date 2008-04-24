package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.ClosureEnvironment;
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
 
  public void setProofNodeRule(BigStepClosureProofNode node, BigStepClosureProofRule rule)
  {
  }
 
  public boolean isMemoryEnabled()
  {
    return false;
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression)
  {
    
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression, Store store)
  {
    
  }
  
  public void addProofNode(BigStepClosureProofNode node, Expression expression, Store store, ClosureEnvironment environment)
  {
    
  }
  
  private BigStepClosureProofModel model;
}
