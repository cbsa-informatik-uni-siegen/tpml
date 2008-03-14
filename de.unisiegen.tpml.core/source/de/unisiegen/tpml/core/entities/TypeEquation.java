package de.unisiegen.tpml.core.entities;


import de.unisiegen.tpml.core.interfaces.ShowBondsInput;
import de.unisiegen.tpml.core.latex.LatexCommandNames;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typeinference.TypeFormula;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * Represents a type equation. Used for the unification algorithm and in the
 * typechecker.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.entities.TypeEquationList
 */
public interface TypeEquation extends TypeFormula, ShowBondsInput,
    PrettyPrintable, LatexCommandNames
{

  /**
   * Returns the monomorphic type on the left side.
   * 
   * @return the left side type.
   */
  public MonoType getLeft ();


  /**
   * Returns the monomorphic type on the right side.
   * 
   * @return the right side type.
   */
  public MonoType getRight ();


  /**
   * Returns the seenTypes.
   * 
   * @return The seenTypes.
   */
  public SeenTypes < TypeEquation > getSeenTypes ();


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
   * Applies the {@link TypeSubstitution} to the types on both sides of the
   * equation and returns the resulting equation.
   * 
   * @param s The {@link DefaultTypeSubstitution}.
   * @return the resulting {@link DefaultTypeEquation}.
   * @see de.unisiegen.tpml.core.types.Type#substitute(TypeSubstitution)
   */
  public TypeEquation substitute ( TypeSubstitution s );
}
