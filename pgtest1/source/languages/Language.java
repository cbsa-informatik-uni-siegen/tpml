package languages;

import java.io.Reader;

/**
 * Base interface for all languages, which is used to
 * create scanners and parsers for a specific language.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface Language {
  /**
   * Allocates a new {@link LanguageParser} for this language,
   * using the specified <code>scanner</code> as token source
   * for the newly allocated parser.
   * 
   * @param scanner the {@link LanguageScanner} to use as token
   *                source for the newly allocated parser.
   *
   * @return the newly allocated parser for this language.
   * 
   * @throws NullPointerException if <code>scanner</code>
   *                              is <code>null</code>.
   */
  public LanguageParser newParser(LanguageScanner scanner);
  
  /**
   * Convenience wrapper method for the {@link #newParser(LanguageScanner)}
   * method, which automatically allocates a scanner for the specified
   * <code>reader</code> using the {@link #newScanner(Reader)} method.
   * 
   * @param reader the {@link Reader} for the source input stream.
   * 
   * @return the newly allocated parser for this language.
   * 
   * @throws NullPointerException if <code>reader</code> is <code>null</code>.
   */
  public LanguageParser newParser(Reader reader);

  /**
   * Allocates a new {@link LanguageScanner}, a lexer, for this
   * language, which parses tokens from the specified <code>reader</code>.
   * 
   * @param reader the {@link Reader} for the source input stream.
   * 
   * @return a newly allocated scanner for this language.
   * 
   * @throws NullPointerException if <code>reader</code>
   *                              is <code>null</code>.
   */
  public LanguageScanner newScanner(Reader reader);
}
