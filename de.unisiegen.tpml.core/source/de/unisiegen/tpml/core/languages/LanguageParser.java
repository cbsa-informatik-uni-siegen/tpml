package de.unisiegen.tpml.core.languages;

import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Interface to parsers for a given language, that are used in conjunction with the language scanners
 * to transform the source code for a given language to an expression tree.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.languages.Language
 */
public interface LanguageParser {
  /**
   * Tries to parse the token stream of the associated scanner and returns the parse {@link Expression}
   * if successfull. Otherwise an exception is thrown indicating the parsing error.
   * 
   * @return the parse {@link Expression}.
   * 
   * @throws Exception if an error occurs while parsing the token stream of the associated scanner.
   */
  public Expression parse() throws Exception;
}
