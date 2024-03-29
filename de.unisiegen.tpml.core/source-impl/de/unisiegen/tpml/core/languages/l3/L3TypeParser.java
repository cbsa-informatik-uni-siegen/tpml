package de.unisiegen.tpml.core.languages.l3 ;


import java.text.MessageFormat ;
import java_cup.runtime.Symbol ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.languages.LanguageParserException ;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner ;


/**
 * The type parser class for the <code>L3</code> language.
 * 
 * @author Benedikt Meurer
 * @version $Rev:415 $
 * @see de.unisiegen.tpml.core.languages.l3.L3AbstractTypeParser
 */
final class L3TypeParser extends L3AbstractTypeParser
{
  /**
   * Allocates a new <code>L3TypeParser</code> that operates on tokens from
   * the specified <code>scanner</code>.
   * 
   * @param scanner the {@link LanguageTypeScanner} to query the tokens from.
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  L3TypeParser ( LanguageTypeScanner scanner )
  {
    super ( scanner ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#report_error(java.lang.String,
   *      java.lang.Object)
   */
  @ Override
  public void report_error ( String message , Object info )
  {
    Symbol symbol = ( Symbol ) info ;
    if ( symbol.sym == EOF_sym ( ) )
    {
      throw new LanguageParserException ( Messages.getString ( "Parser.0" ) , //$NON-NLS-1$
          symbol.left , symbol.right ) ;
    }
    throw new LanguageParserException ( message , symbol.left , symbol.right ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#report_fatal_error(java.lang.String,
   *      java.lang.Object)
   */
  @ Override
  public void report_fatal_error ( String message , Object info )
      throws Exception
  {
    report_error ( message , info ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java_cup.runtime.lr_parser#syntax_error(java_cup.runtime.Symbol)
   */
  @ Override
  public void syntax_error ( Symbol symbol )
  {
    report_error ( MessageFormat.format (
        Messages.getString ( "Parser.1" ) , symbol.value ) , symbol ) ; //$NON-NLS-1$
  }
}
