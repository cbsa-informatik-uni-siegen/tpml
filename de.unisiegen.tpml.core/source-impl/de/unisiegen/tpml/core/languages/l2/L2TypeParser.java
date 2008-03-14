package de.unisiegen.tpml.core.languages.l2;


import java.text.MessageFormat;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner;


/**
 * The type parser class for the <code>L2</code> language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l2.L2AbstractTypeParser
 */
final class L2TypeParser extends L2AbstractTypeParser
{

  /**
   * Allocates a new <code>L2TypeParser</code> that operates on tokens from
   * the specified <code>scanner</code>.
   * 
   * @param scanner the {@link LanguageTypeScanner} to query the tokens from.
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L2TypeParser ( LanguageTypeScanner scanner )
  {
    super ( scanner );
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#report_error(java.lang.String,
   *      java.lang.Object)
   */
  @Override
  public void report_error ( String message, Object info )
  {
    Symbol symbol = ( Symbol ) info;
    if ( symbol.sym == EOF_sym () )
    {
      throw new LanguageParserException ( Messages.getString ( "Parser.0" ), //$NON-NLS-1$
          symbol.left, symbol.right );
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
  public void report_fatal_error ( String message, Object info )
      throws Exception
  {
    report_error ( message, info );
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#syntax_error(java_cup.runtime.Symbol)
   */
  @Override
  public void syntax_error ( Symbol symbol )
  {
    report_error ( MessageFormat.format (
        Messages.getString ( "Parser.1" ), symbol.value ), symbol ); //$NON-NLS-1$
  }
}
