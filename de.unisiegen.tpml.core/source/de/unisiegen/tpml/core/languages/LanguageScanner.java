package de.unisiegen.tpml.core.languages ;


import java.io.IOException ;
import java.io.Reader ;
import java_cup.runtime.Scanner ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;


/**
 * Base interface for scanners for the various languages, which are used to
 * translate the source code for a given language to a token stream that is
 * afterwards processed by the
 * {@link de.unisiegen.tpml.core.languages.LanguageParser} to generate the
 * expression tree.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 */
public interface LanguageScanner extends Scanner
{
  /**
   * Returns the {@link PrettyStyle} for the <code>symbol</code>, for example
   * {@link PrettyStyle#CONSTANT} if the <code>symbol</code> is a parsed
   * integer, boolean or unit constant.
   * 
   * @param symbol a {@link LanguageSymbol} previously returned by this scanner.
   * @return the {@link PrettyStyle} for the <code>symbol</code>.
   * @throws NullPointerException if the <code>symbol</code> is
   *           <code>null</code>.
   */
  public PrettyStyle getStyleBySymbol ( LanguageSymbol symbol ) ;


  /**
   * Returns the next <code>LanguageSymbol</code> parsed from the source input
   * stream, or <code>null</code> on end-of-file.
   * 
   * @return the next symbol from the source input stream, or <code>null</code>
   *         on end-of-file.
   * @throws IOException if an error occurred while reading characters from the
   *           associated source input stream.
   * @throws LanguageScannerException if a syntax error occurred while trying to
   *           scan the input stream.
   */
  public LanguageSymbol nextSymbol ( ) throws IOException ,
      LanguageScannerException ;


  /**
   * Restarts the scanner with the specified <code>reader</code>, and resets
   * the complete internal state to the initial state, starting at parsing the
   * input stream of the specified <code>reader</code>.
   * 
   * @param reader the new {@link Reader}.
   * @throws NullPointerException if <code>reader</code> is <code>null</code>.
   */
  public void restart ( Reader reader ) ;
}
