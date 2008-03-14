package de.unisiegen.tpml.core.languages.l2c;


import java.text.MessageFormat;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;


/**
 * The parser class for the <code>L2C</code> language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l2c.L2CAbstractParser
 */
final class L2CParser extends L2CAbstractParser
{

  /**
   * Allocates a new <code>L2OParser</code> that operates on tokens from the
   * specified <code>scanner</code>.
   * 
   * @param pLanguageScanner the
   *          {@link de.unisiegen.tpml.core.languages.LanguageScanner} to query
   *          tokens from.
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L2CParser ( LanguageScanner pLanguageScanner )
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
    if ( symbol.sym == EOF_sym () )
    {
      throw new LanguageParserException ( Messages.getString ( "Parser.0" ), //$NON-NLS-1$
          symbol.left, symbol.right );
    }
    throw new LanguageParserException ( pMessage, symbol.left, symbol.right );
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
