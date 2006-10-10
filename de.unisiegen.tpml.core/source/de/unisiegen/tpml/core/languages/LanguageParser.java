package de.unisiegen.tpml.core.languages;

import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Interface to parsers for a given language, that are used in conjunction with the language scanners
 * to transform the source code for a given language to an expression tree.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.languages.Language
 */
public interface LanguageParser {
  //
  // Primitives
  //
  
  /**
   * Tries to parse the token stream of the associated scanner and returns the parsed {@link Expression}
   * if successfull. Otherwise an exception is thrown indicating the parsing error.
   * 
   * @return the parsed {@link Expression}.
   * 
   * @throws Exception if an error occurs while parsing the token stream of the associated scanner.
   */
  public Expression parse() throws Exception;
}
