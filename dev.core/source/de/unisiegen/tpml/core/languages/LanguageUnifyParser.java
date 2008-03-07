package de.unisiegen.tpml.core.languages;


import de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker;
import de.unisiegen.tpml.core.typechecker.TypeEquationTypeChecker;


/**
 * Interface to unify parser for a given language, that are used in conjunction
 * with the language scanners to parse unify lists from their string
 * representations to a {@link TypeEquationTypeChecker} hierarchy.
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
   * parsed {@link TypeEquationListTypeChecker} if successfull. Otherwise an
   * exception is thrown indicating the parsing error.
   * 
   * @return the parsed {@link TypeEquationListTypeChecker}.
   * @throws Exception if an error occurrs while parsing the token stream of the
   *           associated scanner.
   */
  public TypeEquationListTypeChecker parse () throws Exception;
}
