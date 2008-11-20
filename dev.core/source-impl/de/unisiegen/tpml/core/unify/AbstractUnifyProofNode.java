package de.unisiegen.tpml.core.unify;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.UnifyProofStep;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * default implementation of the <code>UnifyProofNode</code> interface. The
 * class for nodes in a UnifyProofModel
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see de.unisiegen.tpml.core.unify.UnifyProofNode
 */
public abstract class AbstractUnifyProofNode extends AbstractProofNode
    implements UnifyProofNode
{

  /**
   * the list of substitutions
   */
  private TypeSubstitutionList substitutions = TypeSubstitutionList.EMPTY_LIST;


  /**
   * the list of type equations
   */
  private TypeEquationList equations;
  
  
  /**
   * is this node provable? we assume yes until we know it better
   */
  private boolean provable = true;


  //
  // Constants
  //
  /**
   * Empty {@link UnifyProofStep} array which is returned from {@link #getSteps()}
   * when no steps have been added to a proof node.
   */
  private static final UnifyProofStep [] EMPTY_ARRAY = new UnifyProofStep [ 0 ];


  /**
   * The proof steps that were already performed on this {@link ProofNode},
   * which consist of both the {@link ProofRule}.
   * 
   * @see #getSteps()
   * @see #setSteps(UnifyProofStep[])
   */
  private UnifyProofStep [] steps;


  /**
   * Creates an AbstractUnifyProofNode with the specified list of type
   * substitutions and type equations
   * 
   * @param substs the {@link TypeSubstitutionList} for this node
   * @param eqns the {@link TypeEquationList} for this node
   */
  public AbstractUnifyProofNode ( TypeSubstitutionList substs,
      TypeEquationList eqns )
  {
    this.substitutions = substs;
    this.equations = eqns;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofNode#getTypeSubstitutions()
   */
  public TypeSubstitutionList getTypeSubstitutions ()
  {
    return this.substitutions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofNode#getTypeEquationList()
   */
  public TypeEquationList getTypeEquationList ()
  {
    return this.equations;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofNode#getSteps()
   */
  public UnifyProofStep [] getSteps ()
  {
    if ( this.steps == null )
    {
      return EMPTY_ARRAY;
    }
    return this.steps;
  }


  /**
   * Sets the {@link UnifyProofStep}s which should be marked as completed for this
   * proof node. This method should only be used by proof node and model
   * implementations. Other modules, like the user interface, should never try
   * to explicitly set the steps for a node.
   * 
   * @param pSteps list of {@link UnifyProofStep}s that should be marked as
   *          completed for this node.
   * @see #getSteps()
   * @throws NullPointerException if any of the items in the <code>steps</code>
   *           array is <code>null</code>.
   */
  public void setSteps ( UnifyProofStep [] pSteps )
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
   * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode#isFinished()
   */
  public boolean isFinished ()
  {
//    if(!isProven())
//      return false;
//    for(int n = 0; n < getChildCount(); ++n)
//      if(!getChildAt(n).getRule ().getName ().equals ( "EMPTY" )) //$NON-NLS-1$
//        return false;
//    return true;
//    if ( !isProven () )
//    {
//      return false;
//    }
//    for ( int n = 0 ; n < getChildCount () ; ++n )
//    {
//      if ( !getChildAt ( n ).isFinished () )
//      {
//        return false;
//      }
//    }
//    return true;
    return getLastLeaf().getRule ().getName ().equals ( "EMPTY" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ()
  {
    return ( getSteps ().length > 0 );
  }


  /**
   * wrapper for getSteps which returns the rule that was applied to this node
   * or <code>null</code> if there was no rule applied to this node so far
   * 
   * @return the unify proof rule that was applied to this node or
   *         <code>null</code> if no rule was applied to this node
   */
  public UnifyProofRule getRule ()
  {
    UnifyProofStep [] my_steps = getSteps ();
    if ( my_steps.length > 0 )
    {
      return ( UnifyProofRule ) my_steps [ 0 ].getRule ();
    }
    return null;
  }
  
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.unify.UnifyProofNode#isProvable()
   */
  public boolean isProvable()
  {
    return this.provable;
  }
  
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.unify.UnifyProofNode#setProvable(boolean)
   */
  public void setProvable(final boolean provable)
  {
    this.provable = provable;
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getChildAt(int)
   */
  @Override
  public AbstractUnifyProofNode getChildAt ( int childIndex )
  {
    return ( AbstractUnifyProofNode ) super.getChildAt ( childIndex );
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getRoot()
   */
  @Override
  public AbstractUnifyProofNode getRoot ()
  {
    return ( AbstractUnifyProofNode ) super.getRoot ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getParent()
   */
  @Override
  public AbstractUnifyProofNode getParent ()
  {
    return ( AbstractUnifyProofNode ) super.getParent ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getFirstChild()
   */
  @Override
  public AbstractUnifyProofNode getFirstChild ()
  {
    return ( AbstractUnifyProofNode ) super.getFirstChild ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getLastChild()
   */
  @Override
  public AbstractUnifyProofNode getLastChild ()
  {
    return ( AbstractUnifyProofNode ) super.getLastChild ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public AbstractUnifyProofNode getChildAfter ( TreeNode child )
  {
    return ( AbstractUnifyProofNode ) super.getChildAfter ( child );
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getChildBefore(TreeNode)
   */
  @Override
  public AbstractUnifyProofNode getChildBefore ( TreeNode aChild )
  {
    return ( AbstractUnifyProofNode ) super.getChildBefore ( aChild );
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getFirstLeaf()
   */
  @Override
  public AbstractUnifyProofNode getFirstLeaf ()
  {
    return ( AbstractUnifyProofNode ) super.getFirstLeaf ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getLastLeaf()
   */
  @Override
  public AbstractUnifyProofNode getLastLeaf ()
  {
    return ( AbstractUnifyProofNode ) super.getLastLeaf ();
  }
}
