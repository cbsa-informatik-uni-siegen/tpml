package de.unisiegen.tpml.core.languages.l1;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;

/**
 * The parser class for the <code>L1</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.languages.l1.L1AbstractParser
 */
final class L1Parser extends L1AbstractParser {
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>L1Parser</code> that operates on tokens from the specified <code>scanner</code>.
   * 
   * @param scanner the {@link LanguageScanner} to query the tokens from.
   *                
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L1Parser(LanguageScanner scanner) {
    super(scanner);
  }
  
  
  
  //
  // Error reporting
  //
  
  /**
   * {@inheritDoc}
   *
   * @see java_cup.runtime.lr_parser#report_error(java.lang.String, java.lang.Object)
   */
  @Override
  public void report_error(String message, Object info) {
    Symbol symbol = (Symbol)info;
    if (symbol.sym == EOF_sym()) {
      message = "Unexpected end of file";
    }
    throw new LanguageParserException(message, symbol.left, symbol.right);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see java_cup.runtime.lr_parser#report_fatal_error(java.lang.String, java.lang.Object)
   */
  @Override
  public void report_fatal_error(String message, Object info) throws Exception {
    report_error(message, info);
  }
}
