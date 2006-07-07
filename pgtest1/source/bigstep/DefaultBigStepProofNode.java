package bigstep;

import smallstep.Store;
import common.AbstractProofNode;

import expressions.Expression;

/**
 * The class for nodes in a {@link bigstep.BigStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class DefaultBigStepProofNode extends AbstractProofNode implements BigStepProofNode {
  //
  // Attributes
  //
  
  /**
   * The resulting value of the expression at this node.
   * May be either a value (see {@link Expression#isValue()})
   * or an exception (see {@link {@link Expression#isException()}),
   * or <code>null</code> if the node is not yet proven.
   * 
   * @see #getValue()
   */
  private Expression value;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>DefaultBigStepProofNode</code> with the
   * specified <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   */
  DefaultBigStepProofNode(Expression expression) {
    super(expression);
  }
  
  /**
   * Allocates a new <code>DefaultBigStepProofNode</code> with the
   * specified <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   */
  DefaultBigStepProofNode(Expression expression, Store store) {
    super(expression/*FIXME:, store*/);
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see BigStepProofNode#getValue()
   */
  public Expression getValue() {
    return this.value;
  }
  
  /**
   * Sets the resulting value for this expression.
   * The <code>value</code> must be either a value
   * or an exception (according to the semantics of
   * the big step interpreter), otherwise an
   * {@link IllegalArgumentException} will be thrown.
   * If <code>value</code> is <code>null</code> a
   * {@link NullPointerException} will be thrown.
   * 
   * @param value
   *
   * @throws IllegalArgumentException if <code>value</code> is invalid.
   * @throws NullPointerException if <code>value</code> is <code>null</code>.
   * 
   * @see #getValue()
   */
  void setValue(Expression value) {
    if (!value.isException() && !value.isValue()) {
      throw new IllegalArgumentException("value is invalid");
    }
    this.value = value;
  }
}
