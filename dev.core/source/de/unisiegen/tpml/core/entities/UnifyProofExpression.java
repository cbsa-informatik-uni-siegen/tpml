package de.unisiegen.tpml.core.entities;


import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * @author Christian Uhrhan
 * @version $Id: UnifyProofStep.java 2825 2008-04-17 17:30:28Z uhrhan $
 */
public interface UnifyProofExpression extends PrettyPrintable, LatexPrintable
{

  /**
   * returns the currently set type equation list
   * 
   * @return type equation list
   */
  TypeEquationList getTypeEquationList ();


  /**
   * sets the type equations
   * 
   * @param equations the new type equation list
   */
  void setTypeEquationList ( final TypeEquationList equations );


  /**
   * returns the currently set type substitution list
   * 
   * @return type substitution lists
   */
  TypeSubstitutionList getTypeSubstitutionList ();


  /**
   * sets the type substitutions
   * 
   * @param substitutions the new type substitution list
   */
  void setSubstitutionList ( final TypeSubstitutionList substitutions );
}
