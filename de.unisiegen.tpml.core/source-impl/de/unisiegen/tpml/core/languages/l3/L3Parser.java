package de.unisiegen.tpml.core.languages.l3;


import java.text.MessageFormat;

import java_cup.runtime.Symbol;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.languages.LanguageParserException;
import de.unisiegen.tpml.core.languages.LanguageScanner;


/**
 * The parser class for the <code>L3</code> language.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l3.L3AbstractParser
 */
final class L3Parser extends L3AbstractParser
{

  /**
   * Allocates a new <code>L3Parser</code> that operates on tokens from the
   * specified <code>scanner</code>.
   * 
   * @param scanner the {@link de.unisiegen.tpml.core.languages.LanguageScanner}
   *          to query tokens from.
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L3Parser ( LanguageScanner scanner )
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
