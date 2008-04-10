package de.unisiegen.tpml.core.unify;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.entities.DefaultTypeEquationList;
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
  private TypeSubstitutionList substitutions;


  /**
   * the list of type equations
   */
  private TypeEquationList equations;


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
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven ()
  {
    /*
     * FIXME:
     * last rule have to be (EMPTY) but for this rule
     * equations is already empty so here we're actual
     * are finished one rule to earlier
     */
    return this.equations.isEmpty ();
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
