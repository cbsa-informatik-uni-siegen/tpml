package languages;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface LanguageParser {
  /**
   * Tries to parse the token stream of the associated scanner and
   * returns the parse {@link Expression} if successfull. Otherwise
   * an exception is thrown indicating the parsing error.
   * 
   * @return the parse {@link Expression}.
   * 
   * @throws Exception if an error occurs while parsing the token
   *                   stream of the associated scanner.
   */
  public Expression parse() throws Exception;
}
