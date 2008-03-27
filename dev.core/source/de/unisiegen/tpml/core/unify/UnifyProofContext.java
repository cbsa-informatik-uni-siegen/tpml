package de.unisiegen.tpml.core.unify;


import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * base interface to nodes in a UnifyProofModel
 * 
 * @author Christian Uhrhan
 * @version $Id$
 */
public interface UnifyProofContext
{

  /**
   * add a type equation to the type equation list
   * 
   * @param eqn the equation we want to add to the list
   */
  public void addEquation ( TypeEquation eqn );


  /**
   * adds a new child node below the <code>node</code>. <code>substs</code> is
   * the list of substition we got so far and <code>eqns</code> is the rest of
   * the set of equations
   * 
   * @param node the parent node
   * @param substs list of substitution we got so far
   * @param eqns rest set of equations
   */
  public void addProofNode ( UnifyProofNode node, TypeSubstitutionList substs,
      TypeEquationList eqns );
}
