package de.unisiegen.tpml.core.languages.l3;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;

/**
 * The parser class for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l3.L3AbstractParser
 */
final class L3Parser extends L3AbstractParser {
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>L3Parser</code> that operates on tokens from the specified <code>scanner</code>.
   * 
   * @param scanner the {@link de.unisiegen.tpml.core.languages.LanguageScanner} to query tokens from.
   * 
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L3Parser(LanguageScanner scanner) {
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
