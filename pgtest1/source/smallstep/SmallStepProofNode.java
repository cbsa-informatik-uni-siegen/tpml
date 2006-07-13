package smallstep;

import common.interpreters.AbstractInterpreterProofNode;
import common.interpreters.Store;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofNode extends AbstractInterpreterProofNode {
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * 
   * @see common.ProofNode#getExpression()
   */
  SmallStepProofNode(Expression expression) {
    this(expression, Store.EMPTY_STORE);
  }
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   * 
   * @throws NullPointerException if <code>expression</code> or
   *                              <code>store</code> is <code>null</code>.
   *
   * @see common.ProofNode#getExpression()
   * @see common.interpreters.InterpreterProofNode#getStore()                              
   */
  SmallStepProofNode(Expression expression, Store store) {
    super(expression, store);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> if either the node is
   * already proven or the node is a value or an
   * exception, which means there are also no more
   * possible steps to perform.
   * 
   * @return <code>false</code> if more steps can be
   *         performed in the proof.
   *         
   * @see common.ProofNode#isProven()
   */
  @Override
  public boolean isProven() {
    return (super.isProven()
         || getExpression().isValue()
         || getExpression().isException());
  }
}
