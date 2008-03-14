package de.unisiegen.tpml.core.languages;


import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.entities.TypeEquationList;


/**
 * Interface to unify parser for a given language, that are used in conjunction
 * with the language scanners to parse unify lists from their string
 * representations to a {@link TypeEquation} hierarchy.
 * 
 * @author Christian Fehler
 * @version $Rev: 2760 $
 */
public interface LanguageUnifyParser
{

  //
  // Primitives
  //
  /**
   * Tries to parse the token stream of the associated scanner and returns the
   * parsed {@link TypeEquationList} if successfull. Otherwise an exception is
   * thrown indicating the parsing error.
   * 
   * @return the parsed {@link TypeEquationList}.
   * @throws Exception if an error occurrs while parsing the token stream of the
   *           associated scanner.
   */
  public TypeEquationList parse () throws Exception;
}
