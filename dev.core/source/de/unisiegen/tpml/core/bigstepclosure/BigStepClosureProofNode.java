package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.interpreters.InterpreterProofNode;


/**
 * TODO
 *
 */
public interface BigStepClosureProofNode extends InterpreterProofNode
{
  public ClosureEnvironment getEnvironment();
}
