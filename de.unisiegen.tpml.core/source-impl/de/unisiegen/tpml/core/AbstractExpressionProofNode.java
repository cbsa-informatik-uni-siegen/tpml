package de.unisiegen.tpml.core;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.expressions.Expression;


/**
 * Abstract base class for proof nodes that present the fundamental parts of the
 * {@link de.unisiegen.tpml.core.AbstractExpressionProofModel}s. The user
 * interface and other modules should never access methods of this class
 * directly, as they are subject to change, instead the upper layer should
 * restrict usage to the {@link de.unisiegen.tpml.core.ExpressionProofNode}
 * interface.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class AbstractExpressionProofNode extends AbstractProofNode
    implements ExpressionProofNode
{

  //
  // Constants
  //
  /**
   * Empty {@link ProofStep} array which is returned from {@link #getSteps()}
   * when no steps have been added to a proof node.
   */
  private static final ProofStep [] EMPTY_ARRAY = new ProofStep [ 0 ];


  //
  // Attributes
  //
  /**
   * The {@link Expression} associated with this proof node.
   * 
   * @see #getExpression()
   */
  protected Expression expression;


  /**
   * The proof steps that were already performed on this {@link ProofNode},
   * which consist of both the {@link ProofRule} and the {@link Expression}.
   * 
   * @see #getSteps()
   * @see #setSteps(ProofStep[])
   */
  private ProofStep [] steps;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>AbstractExpressionProofNode</code> for the given
   * <code>expression</code>. The list of {@link ProofStep}s starts as an
   * empty list.
   * 
   * @param pExpression the {@link Expression} for this proof node.
   * @throws NullPointerException if <code>expression</code> is
   *           <code>null</code>.
   */
  protected AbstractExpressionProofNode ( Expression pExpression )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( "expression is null" ); //$NON-NLS-1$
    }
    this.expression = pExpression;
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see ExpressionProofNode#getExpression()
   */
  public Expression getExpression ()
  {
    return this.expression;
  }


  /**
   * Sets the {@link Expression} which is associated with this proof node. This
   * method is used by proof model implementations, for example to implement the
   * {@link ExpressionProofModel#translateToCoreSyntax(ExpressionProofNode,boolean)}
   * method.
   * 
   * @param pExpression the new {@link Expression}.
   */
  public void setExpression ( Expression pExpression )
  {
    this.expression = pExpression;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getSteps()
   */
  public ProofStep [] getSteps ()
  {
    if ( this.steps == null )
    {
      return EMPTY_ARRAY;
    }
    return this.steps;
  }


  /**
   * Sets the {@link ProofStep}s which should be marked as completed for this
   * proof node. This method should only be used by proof node and model
   * implementations. Other modules, like the user interface, should never try
   * to explicitly set the steps for a node.
   * 
   * @param pSteps list of {@link ProofStep}s that should be marked as
   *          completed for this node.
   * @see #getSteps()
   * @throws NullPointerException if any of the items in the <code>steps</code>
   *           array is <code>null</code>.
   */
  public void setSteps ( ProofStep [] pSteps )
  {
    // check if we have new steps to set
    if ( this.steps != pSteps )
    {
      this.steps = pSteps;
      // determine the new proof rules
      ProofRule [] rules;
      if ( this.steps == null )
      {
        rules = null;
      }
      else
      {
        // determine the rules from the steps
        rules = new ProofRule [ this.steps.length ];
        for ( int n = 0 ; n < rules.length ; ++n )
        {
          rules [ n ] = this.steps [ n ].getRule ();
        }
      }
      setRules ( rules );
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getChildAt(int)
   */
  @Override
  public AbstractExpressionProofNode getChildAt ( int childIndex )
  {
    return ( AbstractExpressionProofNode ) super.getChildAt ( childIndex );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getParent()
   */
  @Override
  public AbstractExpressionProofNode getParent ()
  {
    return ( AbstractExpressionProofNode ) super.getParent ();
  }


  //
  // Tree queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getRoot()
   */
  @Override
  public AbstractExpressionProofNode getRoot ()
  {
    return ( AbstractExpressionProofNode ) super.getRoot ();
  }


  //
  // Child queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getFirstChild()
   */
  @Override
  public AbstractExpressionProofNode getFirstChild ()
  {
    return ( AbstractExpressionProofNode ) super.getFirstChild ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getLastChild()
   */
  @Override
  public AbstractExpressionProofNode getLastChild ()
  {
    return ( AbstractExpressionProofNode ) super.getLastChild ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public AbstractExpressionProofNode getChildAfter ( TreeNode aChild )
  {
    return ( AbstractExpressionProofNode ) super.getChildAfter ( aChild );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @Override
  public AbstractExpressionProofNode getChildBefore ( TreeNode aChild )
  {
    return ( AbstractExpressionProofNode ) super.getChildBefore ( aChild );
  }


  //
  // Leaf queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getFirstLeaf()
   */
  @Override
  public AbstractExpressionProofNode getFirstLeaf ()
  {
    return ( AbstractExpressionProofNode ) super.getFirstLeaf ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getLastLeaf()
   */
  @Override
  public AbstractExpressionProofNode getLastLeaf ()
  {
    return ( AbstractExpressionProofNode ) super.getLastLeaf ();
  }
}
