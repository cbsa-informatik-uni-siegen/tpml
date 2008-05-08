package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;

/**
 * TODO
 *
 */
public interface BigStepClosureProofContext
{
  public void addProofNode (BigStepClosureProofNode node, Expression expression);
  
  public void addProofNode (BigStepClosureProofNode node, Closure closure);
  
  public void addProofNode (BigStepClosureProofNode node, Expression expression, Store store);
  
  public void addProofNode (BigStepClosureProofNode node, Expression expression, Store store, ClosureEnvironment environment);
  
  public boolean isMemoryEnabled();
  
  public BigStepClosureProofRule newNoopRule( String name );
  
  public void setProofNodeResult (BigStepClosureProofNode node, BigStepClosureProofResult result);
  
  public void setProofNodeResult(BigStepClosureProofNode node, Expression expression);
  
  public void setProofNodeResult ( BigStepClosureProofNode node, Expression value,
      Store store );
  
  public void setProofNodeRule ( BigStepClosureProofNode node, BigStepClosureProofRule rule );
}
