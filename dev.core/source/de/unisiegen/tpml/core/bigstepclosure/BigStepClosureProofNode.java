package de.unisiegen.tpml.core.bigstepclosure;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.interpreters.InterpreterProofNode;


/**
 * TODO
 *
 */
public interface BigStepClosureProofNode extends InterpreterProofNode
{
  public ClosureEnvironment getEnvironment();
  
  public Closure getClosure();
  
  public BigStepClosureProofResult getResult();
  
  public BigStepClosureProofRule getRule ();
  
  public BigStepClosureProofNode getParent ();
  
  public BigStepClosureProofNode getRoot ();
  
  public BigStepClosureProofNode getFirstChild ();
  
  public BigStepClosureProofNode getLastChild ();
  
  public BigStepClosureProofNode getChildAfter ( TreeNode aChild );
  
  public BigStepClosureProofNode getChildBefore ( TreeNode aChild );
  
  public BigStepClosureProofNode getChildAt(int index);
  
  public BigStepClosureProofNode getFirstLeaf ();
  
  public BigStepClosureProofNode getLastLeaf ();
  
  public boolean isFinished ();
}
