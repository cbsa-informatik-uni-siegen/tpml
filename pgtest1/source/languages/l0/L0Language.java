package languages.l0;

import java.io.Reader;

import expressions.Expression;

import java_cup.runtime.lr_parser;

import languages.AbstractLanguage;
import languages.LanguageParser;
import languages.LanguageScanner;

/**
 * This class represents the language L0.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class L0Language extends AbstractLanguage {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L0Language</code> instance.
   */
  public L0Language() {
  }
  
  
  
  //
  // Parser/Scanner allocation
  //
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newParser(languages.LanguageScanner)
   */
 public LanguageParser newParser(LanguageScanner scanner) {
   if (scanner == null) {
     throw new NullPointerException("scanner is null");
   }
   final lr_parser parser = new L0Parser(scanner);
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
    return new L0Scanner(reader);
  }
}
