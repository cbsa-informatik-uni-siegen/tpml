package de.unisiegen.tpml.core.languages.l0;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;

/**
 * The parser class for the L0 language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 */
final class L0Parser extends L0AbstractParser {
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>L0Parser</code> that operates on tokens from the specified <code>scanner</code>.
   * 
   * @param scanner the {@link LanguageScanner} to query the tokens from.
   *                
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L0Parser(LanguageScanner scanner) {
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
  
  /**
   * {@inheritDoc}
   *
   * @see java_cup.runtime.lr_parser#syntax_error(java_cup.runtime.Symbol)
   */
  @Override
  public void syntax_error(Symbol symbol) {
    report_error("Syntax error on token \"" + symbol.value + "\"", symbol);
  }
}
