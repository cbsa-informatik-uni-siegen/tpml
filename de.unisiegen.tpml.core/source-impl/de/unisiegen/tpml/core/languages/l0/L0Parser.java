package de.unisiegen.tpml.core.languages.l0;


import java.text.MessageFormat;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;


/**
 * The parser class for the L0 language.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:415 $
 */
final class L0Parser extends L0AbstractParser
{

  /**
   * Allocates a new <code>L0Parser</code> that operates on tokens from the
   * specified <code>scanner</code>.
   * 
   * @param pLanguageScanner the {@link LanguageScanner} to query the tokens
   *          from.
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L0Parser ( LanguageScanner pLanguageScanner )
  {
    super ( pLanguageScanner );
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#report_error(java.lang.String,
   *      java.lang.Object)
   */
  @Override
  public void report_error ( String pMessage, Object pInfo )
  {
    Symbol symbol = ( Symbol ) pInfo;
    String message = pMessage;
    if ( symbol.sym == EOF_sym () )
    {
      message = Messages.getString ( "Parser.0" ); //$NON-NLS-1$
    }
    throw new LanguageParserException ( message, symbol.left, symbol.right );
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#report_fatal_error(java.lang.String,
   *      java.lang.Object)
   */
  @Override
  public void report_fatal_error ( String pMessage, Object pInfo )
      throws Exception
  {
    report_error ( pMessage, pInfo );
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#syntax_error(java_cup.runtime.Symbol)
   */
  @Override
  public void syntax_error ( Symbol pSymbol )
  {
    report_error ( MessageFormat.format (
        Messages.getString ( "Parser.1" ), pSymbol.value ), pSymbol ); //$NON-NLS-1$
  }
}
