package languages;

import java.io.IOException;

import expressions.PrettyStyle;

import java_cup.runtime.Scanner;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface LanguageScanner extends Scanner {
  /**
   * Returns the {@link PrettyStyle} for the <code>symbol</code>, for
   * example {@link PrettyStyle#CONSTANT} if the <code>symbol</code>
   * is a parsed integer or boolean constant.
   * 
   * @param symbol a {@link LanguageSymbol} previously returned by
   *               this scanner.
   *
   * @return the {@link PrettyStyle} for the <code>symbol</code>.
   * 
   * @throws NullPointerException if the <code>symbol</code> is <code>null</code>.
   */
  public PrettyStyle getStyleBySymbol(LanguageSymbol symbol);
  
  /**
   * Returns the next <code>LanguageSymbol</code> parsed from the
   * source input stream, or <code>null</code> on end-of-file.
   * 
   * @return the next symbol from the source input stream, or
   *         <code>null</code> on end-of-file.
   * 
   * @throws IOException if an error occurred while reading characters
   *                     from the associated source input stream.
   */
  public LanguageSymbol nextSymbol() throws IOException;
}
