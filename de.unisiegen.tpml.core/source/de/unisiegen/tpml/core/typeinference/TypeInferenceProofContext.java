package de.unisiegen.tpml.core.typeinference ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * The proof context for the type inference. The proof context acts as an
 * interface to the proof model for the type rules.
 * 
 * @author Benjammin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel
 */
public interface TypeInferenceProofContext extends TypeCheckerProofContext
{
  /**
   * Adds a new {@link TypeEquationTypeInference}.
   * 
   * @param pTypeEquationTypeInference The new {@link TypeEquationTypeInference}.
   */
  public void addEquation ( TypeEquationTypeInference pTypeEquationTypeInference ) ;


  /**
   * Add a Substitution to the actual node
   * 
   * @param s new TypeSubstitution
   */
  public void addSubstitution ( TypeSubstitution s ) ;


  /**
   * Get all type substitutions added to this node so far
   * 
   * @return substitutions ArrayList < DefaultTypeSubstitution > with all type
   *         substitutions
   */
  public ArrayList < TypeSubstitution > getSubstitution ( ) ;
}
