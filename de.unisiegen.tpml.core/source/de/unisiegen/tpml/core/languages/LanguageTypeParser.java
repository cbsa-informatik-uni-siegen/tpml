package de.unisiegen.tpml.core.languages;

import de.unisiegen.tpml.core.types.MonoType;

/**
 * Interface to type parser for a given language, that are used in conjunction with the language
 * scanners to parse types from their string representations to a {@link de.unisiegen.tpml.core.types.Type}
 * hierarchy, which is used in the type checker user interface to enter the type for an expression
 * directly.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 */
public interface LanguageTypeParser {
  //
  // Primitives
  //
  
  /**
   * Tries to parse the token stream of the associated scanner and returns the parsed {@link MonoType}
   * if successfull. Otherwise an exception is thrown indicating the parsing error.
   * 
   * @return the parsed {@link MonoType}.
   * 
   * @throws Exception if an error occurrs while parsing the token stream of the associated scanner.
   */
  public MonoType parse() throws Exception;
}
