package de.unisiegen.tpml.core.interpreters ;


import de.unisiegen.tpml.core.AbstractExpressionProofNode ;
import de.unisiegen.tpml.core.AbstractProofNode ;
import de.unisiegen.tpml.core.expressions.Expression ;


/**
 * Abstract base class for big and small step interpreter proof nodes, used in
 * the {@link de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofModel}s.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode
 * @see de.unisiegen.tpml.core.AbstractProofNode
 */
public abstract class AbstractInterpreterProofNode extends
    AbstractExpressionProofNode implements InterpreterProofNode
{
  //
  // Attributes
  //
  /**
   * The store used to prove the expression associated with this proof node.
   * 
   * @see #getStore()
   * @see #setStore(Store)
   */
  private Store store ;


  //
  // Constructor (protected)
  //
  /**
   * Allocates a new <code>AbstractInterpreterProofNode</code> for the
   * specified <code>expression</code> and <code>store</code>.
   * 
   * @param pExpression the {@link Expression} to prove.
   * @param pStore the {@link Store} used to prove the <code>expression</code>.
   * @throws NullPointerException if either <code>expression</code> or
   *           <code>store</code> is <code>null</code>.
   * @see AbstractProofNode#AbstractProofNode()
   */
  protected AbstractInterpreterProofNode ( Expression pExpression , Store pStore )
  {
    super ( pExpression ) ;
    setStore ( pStore ) ;
  }


  //
  // Accessors
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode#getStore()
   */
  public Store getStore ( )
  {
    return new DefaultStore ( ( DefaultStore ) this.store ) ;
  }


  /**
   * Sets the new {@link Store} for this proof node.
   * 
   * @param pStore the new {@link Store} for this node.
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   * @see #getStore()
   */
  public void setStore ( Store pStore )
  {
    if ( pStore == null )
    {
      throw new NullPointerException ( "store is null" ) ; //$NON-NLS-1$
    }
    this.store = pStore ;
  }
}
