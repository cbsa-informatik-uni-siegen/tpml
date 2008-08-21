package de.unisiegen.tpml.core.entities;


import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.UnificationException;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.entities.TypeEquation
 */
public interface TypeEquationList extends PrettyPrintable, LatexPrintable, Iterable<TypeEquation>
{
  
  /**
   * checks wheather the list is empty or not
   * 
   * @return true, if the list is empty, false otherwise
   */
  public boolean isEmpty ();

  /**
   * Returns the parserEndOffset.
   * 
   * @return The parserEndOffset.
   */
  public int getParserEndOffset ();


  /**
   * Returns the parserStartOffset.
   * 
   * @return The parserStartOffset.
   */
  public int getParserStartOffset ();


  /**
   * Sets the parser end offset.
   * 
   * @param pParserEndOffset The new parser end offset.
   */
  public void setParserEndOffset ( int pParserEndOffset );


  /**
   * Sets the parser start offset.
   * 
   * @param pParserStartOffset The new parser start offset.
   */
  public void setParserStartOffset ( int pParserStartOffset );


  /**
   * Allocates a new {@link DefaultTypeEquationList}, which extends this
   * equation list with a new {@link TypeEquation} for <code>left</code> and
   * <code>right</code>.
   * 
   * @param typeEquation The new equation.
   * @return the extended {@link DefaultTypeEquationList}.
   * @throws NullPointerException if <code>left</code> or <code>right</code>
   *           is <code>null</code>.
   */
  TypeEquationList extend ( TypeEquation typeEquation );


  /**
   * Returns the head of the type equation list.
   * 
   * @return TypeEquation first.
   */
  public TypeEquation getFirst ();


  /**
   * Returns the tail of the type equation list.
   * 
   * @return TypeEquationList remaining.
   */
  TypeEquationList getRemaining ();


  /**
   * Applies the {@link TypeSubstitution} <code>s</code> to all equations
   * contained within this list.
   * 
   * @param s the {@link TypeSubstitution} to apply.
   * @return the resulting list of equations.
   */
  TypeEquationList substitute ( TypeSubstitution s );


  /**
   * This method is the heart of the unification algorithm implementation. It
   * returns the unificator for this type equation list.
   * 
   * @return the unificator for this type equation.
   * @throws UnificationException if one of the equations contained within this
   *           list could not be unified.
   */
  TypeSubstitution unify () throws UnificationException;
  
  /**
   * returns the number of type equations
   *
   * @return number of type equations
   */
  int size();

}
