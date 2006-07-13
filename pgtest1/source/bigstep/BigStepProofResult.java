package bigstep;

import common.interpreters.Store;
import expressions.Expression;

/**
 * Interface to big step results, for {@link bigstep.BigStepProofNode}s.
 * A big result consists of a value (an {@link expressions.Expression})
 * and the resulting {@link common.interpreters.Store}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.BigStepProofNode
 */
public interface BigStepProofResult {
  /**
   * Returns the {@link Store} that is part of the result of
   * a proven big step node.
   * 
   * @return the resulting store for a big step proof node.
   * 
   * @see Store
   */
  public Store getStore();
  
  /**
   * Returns the resulting value of a proven big step node.
   * 
   * @return the resulting value of a proven big step node.
   * 
   * @see Expression
   */
  public Expression getValue();
}
