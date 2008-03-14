package de.unisiegen.tpml.core.unify;


import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * default implementation of the <code>UnifyProofNode</code> interface. The
 * class for nodes in a UnifyProofModel
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see de.unisiegen.tpml.core.unify.UnifyProofNode
 */
public abstract class AbstractUnifyProofNode implements UnifyProofNode
{

  /**
   * the list of substitutions
   */
  private TypeSubstitutionList substitutions;


  /**
   * the list of type equations
   */
  private TypeEquationListTypeChecker equations;


  /**
   * Creates an AbstractUnifyProofNode with the specified list of type
   * substitutions and type equations
   * 
   * @param substs the {@link TypeSubstitutionList} for this node
   * @param eqns the {@link TypeEquationListTypeChecker} for this node
   */
  public AbstractUnifyProofNode ( TypeSubstitutionList substs,
      TypeEquationListTypeChecker eqns )
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
  public TypeEquationListTypeChecker getTypeEquationList ()
  {
    return this.equations;
  }
}
