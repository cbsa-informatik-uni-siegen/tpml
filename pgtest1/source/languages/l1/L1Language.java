package languages.l1;

import java.io.Reader;

import java_cup.runtime.lr_parser;
import languages.AbstractLanguage;
import languages.LanguageParser;
import languages.LanguageScanner;
import expressions.Expression;

/**
 * This class represents the language L1.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class L1Language extends AbstractLanguage {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L1Language</code> instance.
   */
  public L1Language() {
  }
  
  
  
  //
  // Parser/Scanner allocation
  //
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newParser(languages.LanguageScanner)
   */
  public final LanguageParser newParser(LanguageScanner scanner) {
    if (scanner == null) {
      throw new NullPointerException("scanner is null");
    }
    final lr_parser parser = new L1Parser(scanner);
    return new LanguageParser() {
      public Expression parse() throws Exception {
        return (Expression)parser.parse().value;
      }
    };
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newScanner(java.io.Reader)
   */
  public LanguageScanner newScanner(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("reader is null");
    }
    return new L1Scanner(reader);
  }
}
